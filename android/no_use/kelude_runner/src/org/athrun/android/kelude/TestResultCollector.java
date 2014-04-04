package org.athrun.android.kelude;

import java.io.IOException;

public class TestResultCollector {
	private String device;
	private String resultPath;
	private KeludeRunner runner;

	private String getDeviceFilePath()
	{
		return runner.getReportFileDir()
				+ "/" + KeludeRunner.REPORT_FILE_NAME;
	}

	public TestResultCollector(String device, String resultPath, KeludeRunner runner) {
		this.device = device;
		this.resultPath = resultPath;
		this.runner = runner;
	}

	String getJunitReport(String device) {

		try {
			String result = ShellCommandRunner
					.run(getReportCommand(this.resultPath));

			return result;

		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getReportPath() {
		StringBuilder filePath = new StringBuilder();
		filePath.append("adb");
		if (null == this.device) {
			filePath.append(" pull ").append(getDeviceFilePath());

		} else {
			filePath.append(" -s ").append(device).append(" pull ")
					.append(getDeviceFilePath());
		}

		return filePath.toString();
	}

	private String getReportCommand(String localPath) {
		StringBuilder command = new StringBuilder();
		command.append(getReportPath()).append(" ").append(localPath);

		return command.toString();
	}
}
