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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogcatCollector {
	private Thread thread;
	private StringBuilder stringBuilder;
	private Process process;
	
	private final static String LOGCAT_COMMAND = "adb logcat -v threadtime";

	public void start() {
		thread = new Thread(new Runnable() {
			
			public void run() {
				try {
					process = Runtime.getRuntime().exec(LOGCAT_COMMAND);
					InputStream is = process.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					stringBuilder = new StringBuilder();
					stringBuilder.append("====adb logcat start====").append("\r\n");
					String line = null;
					
					while ((line = br.readLine()) != null) {
						stringBuilder.append(line).append("\r\n");
					}	
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/*
	 * If path is null, then no file will be saved
	 */
	public void stop(String path) {
		process.destroy();
		thread.interrupt();
		
		if (path != null) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(path);
				stringBuilder.append("====adb logcat end====");
				fw.append(stringBuilder.toString());
				
			} catch (IOException e) {
				e.printStackTrace();
				
			} finally {
				if (null != fw) {
					try {
						System.out.println("Close logcat FileWriter.");
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} else {
			//do nothing now.
		}
	}
}
