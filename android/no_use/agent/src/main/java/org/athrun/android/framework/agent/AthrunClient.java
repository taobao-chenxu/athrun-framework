/* Athrun - Android automation testing Framework.
 Copyright (C) 2010-2012 TaoBao UI AutoMan Team

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., HuaXing road, Hangzhou,China. 
 Email:taichan@taobao.com,shidun@taobao.com,bingyang.djj@taobao.com
*/
package org.athrun.android.framework.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.athrun.android.framework.agent.common.DBCommandRunner;
import org.athrun.android.framework.agent.common.MonkeyCommandRunner;
import org.athrun.android.framework.agent.common.ShellCommandRunner;

/**
 * 
 * @author bingyang.djj
 * 
 */
class AthrunClient implements Runnable {
	private final Logger logger = Logger.getLogger(getClass());

	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;

	private static final String WHETHER_CONNECTED = "FIN";
	private static final String CONFIRM_CONNECTED = "SYN_RCVD";
	private static final int SLEEP_BEFORE_RETRY = 1000;
	// private static final int REQUEST_TIME_OUT = 10000;
	private static final String LOCALHOST = "127.0.0.1";
	private static final int SERVER_PORT = 54321;
	private static final String FORWARD = " forward tcp:" + SERVER_PORT
			+ " tcp:" + SERVER_PORT;

	private static CountDownLatch countDownLatch;
	private static ExecutorService exec;

	private DBCommandRunner dbc;
	private MonkeyCommandRunner monkeyCommandRunner;

	private static boolean running;

	private static AthrunClient instance;

	private AthrunClient(String device) throws IOException,
			InterruptedException {
		String result = ShellCommandRunner.run(createForwardCommand(device));
		if (!result.equalsIgnoreCase("")) {
			throw new RuntimeException(result);
		}

		dbc = new DBCommandRunner();
		monkeyCommandRunner = MonkeyCommandRunner.getInstance(device,
				MonkeyCommandRunner.MONKEY_PORT);

		exec = Executors.newSingleThreadExecutor(new HandlerThreadFactory());
	}

	private String createForwardCommand(String device) {
		StringBuilder forward = new StringBuilder();
		forward.append("adb");
		if (null == device) {
			forward.append(FORWARD);

		} else {
			forward.append(" -s ").append(device).append(FORWARD);
		}

		return forward.toString();
	}

	public static AthrunClient getInstance(String device) {
		if (null == instance) {
			try {
				instance = new AthrunClient(device);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public void run() {
		start();
	}

	private void start() {
		running = true;

		while (running) {
			logger.debug("AthrunClient: Start while loop.");

			synchronized (AthrunClient.class) {

				try {
					logger.debug("AthrunClient: Try to conncet to ADB Server.");
					socket = new Socket(LOCALHOST, SERVER_PORT);
					// socket.setSoTimeout(REQUEST_TIME_OUT);

				} catch (IOException e) {
					// If we got here ,that means we failed to connect to the
					// ADB server.
					logger.debug("Connect to ADB Server failed, retry after "
							+ SLEEP_BEFORE_RETRY + "ms.");
					waitForRetry(SLEEP_BEFORE_RETRY);
					continue;
				}

				// If we got here, that means we connect to the ADB server
				// succeed.
				logger.debug("AthrunServer: Connect to ADB Server succeed.");
				logger.debug("AthrunServer: Try to connect to AthrunServer.");
				if (!isConnectToAthrunServer()) {

					try {
						out.close();
						in.close();
						socket.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.debug("AthrunClient: Finalize finished.");
					logger.debug("AthrunClient: Connect to AthrunServer failed, retry after "
							+ SLEEP_BEFORE_RETRY + "ms.");
					waitForRetry(SLEEP_BEFORE_RETRY);
					continue;
				}

				// If we got here, that means we connect to the AthrunServer
				// succeed.
				logger.info("AthrunClient: Connect to AthrunServer succeed.");
				exec.execute(runnable);

				blcokCurrentThread();

				waitForRetry(500);
			}
		}
	}

	public void stop() {
		if (null != monkeyCommandRunner) {
			monkeyCommandRunner.stop();
		}
		
		running = false;
		if (null != out) {
			logger.info("AthrunClient: close outputstream.");
			out.close();
		}

		if (null != in) {
			try {
				logger.info("AthrunClient: close inputstream.");
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (null != socket) {
			try {
				logger.info("AthrunClient: close socket.");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (null != exec) {
			logger.info("AthrunClient: shutdown exec.");
			exec.shutdown();
		}
	}

	private class HandlerThreadFactory implements ThreadFactory {

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
			return thread;
		}
	}

	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {

		public void uncaughtException(Thread t, Throwable e) {
			// notifyThreadFinished();
			// t.interrupt();
			e.printStackTrace();
		}
	}

	private void waitForRetry(int timeout) {
		try {
			TimeUnit.MILLISECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean isConnectToAthrunServer() {
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			logger.debug("AthrunClient: Send data: " + WHETHER_CONNECTED);
			sendDataToServer(WHETHER_CONNECTED);

			if (isConnected()) {
				logger.info("AthrunClient: isConnectToAthrunServer() return true.");
				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("AthrunClient: isConnectToAthrunServer() return false.");
		return false;
	}

	private boolean isConnected() {
		String inFromServer = getCommandFromServer();

		if (null != inFromServer) {
			if (inFromServer.equalsIgnoreCase(CONFIRM_CONNECTED)) {
				logger.debug("AthrunClient: Receive <" + inFromServer + ">.");
				logger.debug("AthrunClient: isConnected() return true.");
				logger.debug("AthrunClient: Receive <" + inFromServer + ">.");
				return true;
			}
		}

		logger.debug("AthrunClient: isConnected() return false.");
		return false;
	}

	private Runnable runnable = new Runnable() {

		public void run() {
			try {
				String inFromServer = getCommandFromServer();

				if (null == inFromServer) {
					logger.debug("AthrunClient: inFromServer is null, finish current thread.");
					notifyThreadFinished();
					return;
				}

				logger.info("AthrunClient: Receive <" + inFromServer
						+ "> from server.");

				String result = executeCommand(inFromServer);
				logger.info("AthrunClent: executeCommand(" + inFromServer
						+ ") finished.");
				sendDataToServer(result);
				notifyThreadFinished();

			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void blcokCurrentThread() {
		if (null == countDownLatch || 0 == countDownLatch.getCount()) {
			countDownLatch = new CountDownLatch(1);
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void notifyThreadFinished() {
		if (null == countDownLatch) {
			return;
		}
		countDownLatch.countDown();
		logger.info("AthrunClient: notifyThreadFinished()");
	}

	private String getCommandFromServer() {
		logger.debug("AthrunClient: getCommandFromServer()");
		String inFromServer = null;
		try {
			inFromServer = in.readLine();

		} catch (IOException e) {
			logger.debug("AthrunClient: getCommandFromServer() encounter an IOException.");
		}

		logger.info("AthrunClient: getCommandFromServer() return "
				+ String.valueOf(inFromServer) + ".");
		return inFromServer;
	}

	private void sendDataToServer(String data) throws IOException {
		if (null == data || data.equalsIgnoreCase("")) {
			data = "SUCCESS";
		}
		out.println(data);
		logger.info("AthrunClient: Send data: " + data + " succeed.");
	}

	private String executeCommand(String in) throws Exception {

		/**
		 * Current rule： DB_MYSQL: select name from table where condition return
		 * name queried from the table DB_oracle: insert into table (column)
		 * values ('value') return null
		 */
		logger.info(">>> " + in + " >>>");
		String type = in.split(":")[0];
		String command = in.substring(type.length() + 1);
		command = command.trim();
		String out = null;
		if (type.equalsIgnoreCase("DB")) {
			Thread.sleep(1000);
			out = dbc.run(command);

		} else if (type.equalsIgnoreCase("SHELL_COMMAND")) {
			Thread.sleep(1000);
			out = ShellCommandRunner.run(command);

		} else if (type.equalsIgnoreCase("CALL")) {
			int duration = Integer.valueOf(in.split(":")[1]);
			interruptByCall(duration);
			out = "INTERRUPTED BY CALL SUCCESS in " + duration + "s.";

		} else if (type.equalsIgnoreCase("SMS")) {
			incomingSms();
			out = "MOCK INCOMING SMS SUCCESS";

		} else if (type.equalsIgnoreCase("MONKEY")) {
			out = monkeyCommandRunner.execCommand(command);
		}

		logger.info("<<< " + out + " <<<");
		return out;
	}

//	private void printLog(String log) {
//		long id = Thread.currentThread().getId();
//		logger.debug("#" + id + "# " + log);
//	}

	private void interruptByCall(int duration) throws Exception {
		logger.info("AthrunClient: callAndCancel() started.");
		Socket emulator = new Socket(LOCALHOST, getDeviceConsolePort());
		BufferedReader in = new BufferedReader(new InputStreamReader(
				emulator.getInputStream()));
		in.readLine();
		PrintWriter out = new PrintWriter(emulator.getOutputStream(), true);

		out.println("gsm call 10086");
		TimeUnit.SECONDS.sleep(duration);
		out.println("gsm cancel 10086");
		out.close();
		in.close();
		emulator.close();
		logger.info("AthrunClient: callAndCancel() fininshed.");
	}

	private void incomingSms() throws Exception {
		Socket emulator = new Socket(LOCALHOST, getDeviceConsolePort());
		BufferedReader in = new BufferedReader(new InputStreamReader(
				emulator.getInputStream()));
		in.readLine();
		PrintWriter out = new PrintWriter(emulator.getOutputStream(), true);

		out.println("sms send 10086 hello");
		out.close();
		in.close();
		emulator.close();
	}

	/**
	 * Return console port of the device, we know the emulator's only now.
	 * 
	 * @return
	 */
	private int getDeviceConsolePort() {
		return 5554;
	}
}