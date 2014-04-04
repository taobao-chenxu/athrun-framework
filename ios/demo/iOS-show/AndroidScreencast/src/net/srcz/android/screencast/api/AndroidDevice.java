package net.srcz.android.screencast.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import net.srcz.android.screencast.api.file.FileInfo;
import net.srcz.android.screencast.api.injector.NullSyncProgressMonitor;
import net.srcz.android.screencast.api.injector.OutputStreamShellOutputReceiver;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.SyncService.SyncResult;

public class AndroidDevice {

	IDevice device;

	public AndroidDevice(IDevice device) {
		this.device = device;
	}

	public void openUrl(String url) {
		executeCommand("am start "+url);
	}
	
	public String executeCommand(String cmd) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			device.executeShellCommand(cmd,
					new OutputStreamShellOutputReceiver(bos));
			return new String(bos.toByteArray(), "UTF-8");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void pushFile(File localFrom, String remoteTo) {
		try {
			if (device.getSyncService() == null)
				throw new RuntimeException("SyncService is null, ADB crashed ?");

			SyncResult result = device.getSyncService().pushFile(localFrom.getAbsolutePath(),
					remoteTo, new NullSyncProgressMonitor());
			if (result.getCode() != 0)
				throw new RuntimeException("code = " + result.getCode()
						+ " message= " + result.getMessage());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void pullFile(String removeFrom, File localTo) {
		// ugly hack to call the method without FileEntry
		try {
			if (device.getSyncService() == null)
				throw new RuntimeException("SyncService is null, ADB crashed ?");

			Method m = device.getSyncService().getClass().getDeclaredMethod(
					"doPullFile", String.class, String.class,
					ISyncProgressMonitor.class);
			m.setAccessible(true);
			m.invoke(device.getSyncService(), removeFrom, localTo.getAbsolutePath(), device
					.getSyncService().getNullProgressMonitor());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<FileInfo> list(String path) {
		try {
			String s = executeCommand("ls -l "+path);
			String[] entries = s.split("\r\n");
			Vector<FileInfo> liste = new Vector<FileInfo>();
			for (int i = 0; i < entries.length; i++) {
				String[] data = entries[i].split(" ");
				if (data.length < 4)
					continue;
				/*
				 * for(int j=0; j<data.length; j++) {
				 * System.out.println(j+" = "+data[j]); }
				 */
				String attribs = data[0];
				boolean directory = attribs.startsWith("d");
				String name = data[data.length - 1];

				FileInfo fi = new FileInfo();
				fi.attribs = attribs;
				fi.directory = directory;
				fi.name = name;
				fi.path = path;
				fi.device = this;

				liste.add(fi);
			}

			return liste;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
