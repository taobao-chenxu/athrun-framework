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

public class TestResultCollector {
	private String device;
	private String testPackage;
	private String appPackage;

	private static final String JUNIT_RESULT_PATH = "c:\\AthrunLog\\junit-report.xml";

	public TestResultCollector(String device, String testPackage,
			String appPackage) {
		this.device = device;
		this.testPackage = testPackage;
		this.appPackage = appPackage;
	}

	public File getJunitReport(String device) {
		try {
			String result = ShellCommandRunner.run(getReportCommand(
					this.appPackage, JUNIT_RESULT_PATH));
			if (result.contains("does not exists")) {
				result = ShellCommandRunner.run(getReportCommand(
						this.testPackage, JUNIT_RESULT_PATH));
			}

			return new File(JUNIT_RESULT_PATH);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("Can not get the report file from device.");
	}

	private String getReportPath(String mobilePath) {
		StringBuilder filePath = new StringBuilder();
		filePath.append("adb");
		if (null == this.device) {
			filePath.append(" pull /data/data/");
			filePath.append(mobilePath);
			filePath.append("/files/junit-report.xml");

		} else {
			filePath.append("-s ").append(device).append(" pull /data/data/")
					.append(mobilePath).append("/files/junit-report.xml");
		}

		return filePath.toString();
	}

	private String getReportCommand(String mobilePath, String localPath) {
		StringBuilder command = new StringBuilder();
		command.append(getReportPath(mobilePath)).append(" ").append(localPath);

		return command.toString();
	}

	public static void main(String[] args) {
		TestResultCollector collector = new TestResultCollector(null,
				"org.athrun.android.test", "org.athrun.android.app");
		collector.getJunitReport(null);
	}
}
