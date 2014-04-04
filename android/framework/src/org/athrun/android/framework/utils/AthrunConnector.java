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
package org.athrun.android.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;



/**
 * 
 * @author bingyang.djj
 * 
 */
final class AthrunConnector implements Runnable {
	private Logger logger = LogConfigure.getLogger(getClass());

	public static final int SERVER_PORT = 54321;
	private static final int TIME_FOR_RETRY = 100;
	private static final String WHETHER_CONNECTED = "FIN";
	private static final String CONFIRM_CONNECTED = "SYN_RCVD";

	private static ExecutorService mExecutorService;
	private static ServerSocket mServerSocket;

	private static CountDownLatch countDownLatch;
	private static String result = null;
	private static String command;
	private static boolean running;

	private static AthrunConnector instance;

	private AthrunConnector() {
		logger.info("Construct AthrunConnector.");
	}

	public static AthrunConnector getInstance() {
		if (null == instance) {
			instance = new AthrunConnector();
		}
		return instance;
	}

	@Override
	public void run() {

		try {
			running = true;
			mServerSocket = new ServerSocket(SERVER_PORT);
			mExecutorService = Executors
					.newSingleThreadExecutor(new HandlerThreadFactory());
			logger.info("Start server...");

			while (running) {

				while (null == command) {

					if (false == running) {
						return;
					}

					logger.debug("Command is null, sleep and wait for command.");
					WaitForRetry(TIME_FOR_RETRY);
				}

				Socket client = mServerSocket.accept();
				mExecutorService.execute(new ThreadServer(client, command));

				blockCurrentThread();

				if (false == running) {
					return;
				}

				WaitForRetry(500);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class HandlerThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
			return thread;
		}
	}

	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
		}
	}

	private class ThreadServer implements Runnable {
		private BufferedReader in;
		private PrintWriter out;
		private String command;

		public ThreadServer(Socket socket, String command) throws IOException {
			this.command = command;
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);
		}

		@Override
		public void run() {
			synchronized (AthrunConnector.class) {

				try {
					String inFromClient = getDataFromClient();
					if (null == inFromClient) {
						notifyThreadFinished();
						return;

					} else if (inFromClient.equalsIgnoreCase(WHETHER_CONNECTED)) {
						logger.info("The data from client is <"
								+ inFromClient + ">.");
						sendCommand(CONFIRM_CONNECTED);
					}

					logger.info("Ready to send command <" + command
							+ ">.");
					sendCommand(command);

				} catch (IOException e) {
					e.printStackTrace();
				}

				logger.info("Ready to get result.");
				result = getDataFromClient();
				logger.info("Get result: <" + result + ">.");

				AthrunConnector.command = null;
				logger.info("Clear command finished, notify.");

				notifyThreadFinished();
			}
		}

		private String getDataFromClient() {
			String inFromClient = null;

			try {
				inFromClient = in.readLine();

			} catch (IOException e) {
				logger.info("getDataFromClient() encounter an IOException.");
			}

			logger.info("getDataFromClient() return "
					+ String.valueOf(inFromClient));
			return inFromClient;
		}

		private void sendCommand(String command) throws IOException {
			out.println(command);
			logger.info("<" + command + "> send succeed.");
		}
	}

	private static void WaitForRetry(int timeout) {
		try {
			TimeUnit.MILLISECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String setCommand(String command) {
		AthrunConnector.command = command;
		blockCurrentThread();
		logger.info("The result is: " + result);
		return result;
	}

	private void blockCurrentThread() {
		if (null == countDownLatch || 0 == countDownLatch.getCount()) {
			countDownLatch = new CountDownLatch(1);
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void notifyThreadFinished() {
		if (null == countDownLatch) {
			return;
		}
		countDownLatch.countDown();
	}

	public void stop() {
		try {
			logger.info("Ready to shutdown.");
			running = false;
			mServerSocket.close();
			mExecutorService.shutdown();
			logger.info("Shutdown succeed.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
