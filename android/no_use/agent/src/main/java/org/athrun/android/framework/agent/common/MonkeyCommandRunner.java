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
package org.athrun.android.framework.agent.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MonkeyCommandRunner {
	public static final int MONKEY_PORT = 65432;
	private static final String FORWARD = " forward tcp:" + MONKEY_PORT
			+ " tcp:" + MONKEY_PORT;
	private static final String MONKEY_PORT_COMMAND = " shell monkey --port ";
	private static MonkeyCommandRunner instance;
	private Socket monkeySocket;
	private BufferedReader in;
	private PrintWriter out;

	private MonkeyCommandRunner(String device, int port) {
		try {
			ShellCommandRunner.run(createForwardCommand(device));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		openMonkeyPort(device, port);
		init(port);
	}

	public static MonkeyCommandRunner getInstance(String device, int port) {
		if (null == instance) {
			instance = new MonkeyCommandRunner(device, port);
		}
		return instance;
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

	private Process openMonkeyPort(String device, int port) {
		try {
			Process process = Runtime.getRuntime().exec(getMonkeyCommand(device) + port);
			return process;

		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("Can not open monkey port.");
	}
	
	private String getMonkeyCommand(String device) {
		StringBuilder monkey = new StringBuilder();
		monkey.append("adb");
		
		if (null != device) {
			monkey.append(" -s ").append(device).append(MONKEY_PORT_COMMAND);
			
		} else {
			monkey.append(MONKEY_PORT_COMMAND);
		}
		
		return monkey.toString();
	}

	private void init(int port) {

		try {
			monkeySocket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(
					monkeySocket.getInputStream()));
			out = new PrintWriter(monkeySocket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String execCommand(String command) {
		out.println(command);
		try {
			return in.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void stop() {
		if (null != out) {
			out.close();
		}
		
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (null != monkeySocket) {
			try {
				monkeySocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}