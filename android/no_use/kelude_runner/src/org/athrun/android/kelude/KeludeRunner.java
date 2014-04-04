package org.athrun.android.kelude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.athrun.android.result.JunitKeludeLogConverter;

public final class KeludeRunner {
	private static final String RUNNER = "pl.polidea.instrumentation.PolideaInstrumentationTestRunner";


	static final String REPORT_FILE_NAME = "junitReport.xml";

	private String[] commands;
	private Map<String, String> commandMap = new HashMap<String, String>();

	private String testClassName;
	private String testMethodName;

	private String device;
	private String testPackageName;
	private String packageName;
	private String resultPath;

	KeludeRunner(String[] commands) {
		this.commands = commands;
		init();
		getTestInfo();
	}

	private void init() {
		for (int i = 0; i < commands.length; i = i + 2) {
			commandMap.put(commands[i].replace("-", ""), commands[i + 1]);
		}
	}
	
	protected String getReportFileDir(){
		return String.format("/data/data/%s/files", getPackageName());
	}

	private String getInstCommand() {
		StringBuilder instCommand = new StringBuilder();

		instCommand.append("adb -s ").append(getDeviceName())
				.append(" shell am instrument -w")

				.append(" -e junitOutputDirectory ").append(getReportFileDir())
				.append(" -e junitSplitLevel ").append("none")

				.append(" -e junitSingleFileName ").append(REPORT_FILE_NAME)
				.append(" -e class ").append(getTestClassName()).append("#")
				.append(getTestMethodName()).append(" ")
				.append(getTestPackageName()).append("/")
				.append(getTestRunnerName());

		return instCommand.toString();
	}

	private String getDeviceName() {
		this.device = this.commandMap.get("deviceName");
		return this.device;
	}

	private void getTestInfo() {
		String testInfo = this.commandMap.get("method");
		String[] args = testInfo.split("\\.");

		this.testMethodName = args[args.length - 1];
		this.testClassName = testInfo.replace(("." + testMethodName), "");
	}

	private String getTestClassName() {
		return this.testClassName;
	}

	private String getTestMethodName() {
		return this.testMethodName;
	}

	private String getTestPackageName() {
		this.testPackageName = this.commandMap.get("testPackageName");
		return this.testPackageName;
	}

	private String getTestRunnerName() {
		return RUNNER;
	}
	
	protected String getPackageName()
	{
		this.packageName = this.commandMap.get("packageName");
		return this.packageName;
	}

	private String getLocalReportPath() {
		this.resultPath = this.commandMap.get("results_file");
		return this.resultPath;
	}

	private String getFileRoot(String path) {
		String[] dirStrings = path.split("/");

		String linuxPath = path.replace(dirStrings[dirStrings.length - 1], "");
		String windowsPath = linuxPath.replace("/", "\\");
		return windowsPath;
	}

	private String clean() throws Exception {
		return ShellCommandRunner.run("adb -s " + getDeviceName() + " shell rm " + getReportFileDir() + "/"
				+ REPORT_FILE_NAME);
	}
	
	/**
	 * @param localReportPath
	 * @throws IOException
	 */
	private static void convertJunitToKeludeReport(String localReportPath)
			throws IOException {
		// TODO Auto-generated method stub
		File tmpFile = new File("c:/AthrunLog/tmp.xml");
		File local = new File(localReportPath);
		InputStream is = new FileInputStream(local);
		FileOutputStream fo = new FileOutputStream(tmpFile);
		JunitKeludeLogConverter.convert(is, fo, "no report for junit");
		is.close();
		fo.close();
		FileUtils.copyFile(tmpFile, local);
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2 || args.length % 2 != 0) {
			System.out.println("Incorrect args.");
			System.out.println("Usage:");
			System.out
					.println("- device [device] -method [testMethodName] -testPackageName [testPackageName] -results_file [localpath]");

		} else {

			KeludeRunner runner = new KeludeRunner(args);
			System.out.println("Start runner...");

			File resultFile = new File(runner.getFileRoot(runner
					.getLocalReportPath()));
			if (!resultFile.exists()) {
				resultFile.mkdirs();
			}

			System.out.println("Delete old result files on device...");
			System.out.println(runner.clean());
			System.out.println("Delete old result files on device succeed.");

			System.out.println("Run command: " + runner.getInstCommand());
			String testInfo = ShellCommandRunner.run(runner.getInstCommand());
			System.out.println("Test run finished.");
			System.out.println(testInfo);

			if (!testInfo.contains("Time:")) {
				// String logPath = runner.resultPath.replace("xml", "log");
				// System.out
				// .println("Test run fininshed with exceptions, save exceptions info to "
				// + logPath);
				// FileUtils.writeStringToFile(new File(logPath), testInfo,
				// "UTF-8");
				throw new RuntimeException(testInfo);

			} else {
				TestResultCollector resultCollector = new TestResultCollector(
						runner.device, runner.resultPath, runner);
				String result = resultCollector.getJunitReport(runner.device);

				System.out.println("Pull file result: ");
				System.out.println(result);

				if (!result.contains("KB/s")) {
					System.out
							.println("Exception occurs while pull the remote result file to local.");
					// FileUtils.copyFile(new File(DEFAULT_RESULT_FILE), new
					// File(
					// runner.resultPath));
					throw new RuntimeException(result);

				} else {
					System.out.println("Convert result to kelude format...");
					convertJunitToKeludeReport(runner.getLocalReportPath());
					System.out
							.println("Convert result to kelude format finished.");

					System.out.println("Finish runner, system exit...");
					System.exit(0);
				}
			}
		}
	}
}
