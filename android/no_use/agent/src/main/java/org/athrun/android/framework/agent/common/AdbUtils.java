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

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * 
 * @author bingyang.djj
 *
 */
public final class AdbUtils {
	
	private AdbUtils() {
		throw new AssertionError();
	}

	public static String getSdkPath() {
		String sdkPath = System.getenv("ANDROID_SDK");
		if (null != sdkPath) {
			return sdkPath;
		}
		
		throw new RuntimeException(
				"Can not find sdk path in your computer's environment, please add it, with the Key: ANDROID_SDK, and the Value: [SDK PATH]");
	}

	public static String getAdbPath(String sdkPath) {
		String[] possibleLocations = new String[] { "platform-tools", "tools" };
		List<String> triedLocations = Lists.newArrayList();

		for (String subDirectory : possibleLocations) {
			String fullPath = joinPath(sdkPath, new String[] { subDirectory,
					"adb.exe" });

			if (fileExists(fullPath)) {
				return fullPath;
			}

			triedLocations.add(fullPath);
		}

		throw new RuntimeException(
				"Could not find adb in any of the expected locations: "
						+ triedLocations);
	}

	private static String joinPath(String root, String[] path) {
		if (root.endsWith(File.separator)) {
			root = root.substring(0, root.length() - 1);
		}

		return Joiner.on(File.separator).join(Lists.asList(root, path));
	}

	private static boolean fileExists(String path) {
		return new File(path).exists();
	}

	public static Process runCommand(String... arguments) {
		List<String> commandLine = Lists.asList(getAdbPath(getSdkPath()),
				arguments);
		ProcessBuilder processBuilder = new ProcessBuilder(commandLine);

		try {
			return processBuilder.start();

		} catch (IOException e) {
			throw new RuntimeException(
					"An IOException occurred when starting ADB.", e);
		}
	}

	private static void confirmExitValueIs(int expected, Process process) {
		// Consider if we need to add timeout logic here.
		while (true) {
			try {
				process.waitFor();
				break;
			} catch (InterruptedException exception) {
				// do nothing, try to wait again
			}
		}

		int actual = process.exitValue();

		if (expected != actual) {
			throw new RuntimeException("Exit value of process was " + actual
					+ " but expected " + expected);
		}
	}

	public static void sendText(String text) {
		Process adbProcess = runCommand("shell", "input", "text", text);
		confirmExitValueIs(0, adbProcess);
	}

	public static void sendKeyEvent(int keyCode) {
		Process adbProcess = runCommand("shell", "input", "keyevent",
				String.valueOf(keyCode));
		confirmExitValueIs(0, adbProcess);
	}
}
