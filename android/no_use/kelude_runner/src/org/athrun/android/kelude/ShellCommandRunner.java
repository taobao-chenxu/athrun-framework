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
package org.athrun.android.kelude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class ShellCommandRunner {

	public static String run(String command) throws IOException,
			InterruptedException {
		StringBuilder output = new StringBuilder();
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader er = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		
		String line = null;
		
		while ((line = br.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}
		while ((line = er.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}
		p.waitFor();
		
		er.close();
		br.close();
		String result = output.toString();
		return result;
	}
}
