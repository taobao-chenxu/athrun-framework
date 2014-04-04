package org.athrun.ios.instrumentdriver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

/**
 * @author taichan
 */
public class ResourceManager {

	private static final String FILERUNSHELL = "/runTests.sh";
	private static final String FILETCPSHELL = "/TcpSocket.sh";
	private static final String FILERUNNERJS = "/CSRunner.js";
	private static final String FILECOMMONJS = "/common.js";

	private static final String home = System.getProperty("user.home");
	private static final String instrumentFolder = "InstrumentDriver";
	
	public static File instrumentRoot = new File(home, instrumentFolder);

	public static void updateResource() {
		try {
			FileUtils.forceMkdir(instrumentRoot);
			FileUtils.forceMkdir(new File(instrumentRoot, "log"));
			copyResource(FILERUNSHELL);
			copyResource(FILETCPSHELL);
			copyResource(FILERUNNERJS);
			copyResource(FILECOMMONJS);
			replace("%InstrumentRoot%", getInstrumentRoot(), new File(
					instrumentRoot, FILERUNNERJS));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void replace(String oldStr, String newStr, File file)
			throws IOException {
		File bakFile = new File(file.getPath() + ".bak");
		if (bakFile.exists()) {
			FileUtils.forceDelete(bakFile);
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		PrintWriter writer = new PrintWriter(new FileWriter(bakFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			writer.println(line.replaceAll(oldStr, newStr));
		}
		reader.close();
		writer.close();
		FileUtils.forceDelete(file);
		FileUtils.copyFile(bakFile, file);
	}

	private static void copyResource(String pathStr) throws URISyntaxException,
			IOException {

		InputStream is = ResourceManager.class.getResourceAsStream(pathStr);
		byte[] buffer = new byte[1024];

		File newfile = new File(instrumentRoot, pathStr);
		int read = 0;
		FileOutputStream fos = new FileOutputStream(newfile);
		while ((read = is.read(buffer)) != -1) {
			fos.write(buffer, 0, read);
		}
		is.close();
		fos.close();
		
//		String path = ResourceManager.class.getResource(pathStr).toURI()
//				.getPath();
//		FileUtils.copyFile(new File(path), new File(instrumentRoot, pathStr));
	}

	public static String getRunShell() {
		return new File(instrumentRoot, FILERUNSHELL).getPath();
	};

	public static String getTcpShell() {
		return new File(instrumentRoot, FILETCPSHELL).getPath();
	}

	public static String getInstrumentRoot() {
		return instrumentRoot.getPath();
	}
}