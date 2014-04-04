package net.srcz.android.screencast.api.injector;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.srcz.android.screencast.api.AndroidDevice;
import net.srcz.android.screencast.api.StreamUtils;

import com.android.ddmlib.IDevice;

public class Injector {
	private static final int PORT = 1324;
	private static final String LOCAL_AGENT_JAR_LOCATION = "/MyInjectEventApp.jar";
	private static final String REMOTE_AGENT_JAR_LOCATION = "/data/local/tmp/InjectAgent.jar";
	private static final String AGENT_MAIN_CLASS = "net.srcz.android.screencast.client.Main";
	IDevice device;

	public static Socket s;
	OutputStream os;
	Thread t = new Thread("Agent Init") {
		public void run() {
			try {
				init();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	};

	public ScreenCaptureThread screencapture;

	public Injector(IDevice d) throws IOException {
		this.device = d;
		this.screencapture = new ScreenCaptureThread(d);
	}

	public void start() {
		t.start();
	}

	private void uploadAgent() throws IOException {
		try {
			File tempFile = File.createTempFile("agent", ".jar");
			StreamUtils.transfertResource(getClass(), LOCAL_AGENT_JAR_LOCATION,
					tempFile);
			new AndroidDevice(device).pushFile(tempFile,
					REMOTE_AGENT_JAR_LOCATION);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @return true if there was a client running
	 */
	private static boolean killRunningAgent() {
		try {
			Socket s = new Socket("127.0.0.1", PORT);
			OutputStream os = s.getOutputStream();
			os.write("quit\n".getBytes());
			os.flush();
			os.close();
			s.close();
			return true;
		} catch (Exception ex) {
			// ignor�
		}
		return false;
	}

	public void close() {
		try {
			if (os != null) {
				os.write("quit\n".getBytes());
				os.flush();
				os.close();
			}
			s.close();
		} catch (Exception ex) {
			// ignored
		}
		screencapture.interrupt();
		try {
			s.close();
		} catch (Exception ex) {
			// ignored
		}
		try {
			synchronized (device) {
				/*
				 * if(device != null) device.removeForward(PORT, PORT);
				 */
			}
		} catch (Exception ex) {
			// ignored
		}
	}

	public void injectMouse(int action, float x, float y) throws IOException {
		long downTime = 10;
		long eventTime = 10;

		int metaState = -1;

		String cmdList1 = "pointer/" + downTime + "/" + eventTime + "/"
				+ action + "/" + x + "/" + y + "/" + metaState;
		injectData(cmdList1);
	}

	public void injectTrackball(float amount) throws IOException {
		long downTime = 0;
		long eventTime = 0;
		float x = 0;
		float y = amount;
		int metaState = -1;

		String cmdList1 = "trackball/" + downTime + "/" + eventTime + "/"
				+ ConstEvtMotion.ACTION_MOVE + "/" + x + "/" + y + "/"
				+ metaState;
		injectData(cmdList1);
		String cmdList2 = "trackball/" + downTime + "/" + eventTime + "/"
				+ ConstEvtMotion.ACTION_CANCEL + "/" + x + "/" + y + "/"
				+ metaState;
		injectData(cmdList2);
	}

	public void injectKeycode(int type, int keyCode) {
		String cmdList = "key/" + type + "/" + keyCode;
		injectData(cmdList);
	}

	private void injectData(String data) {
		try {
			if (os == null) {
				System.out.println("Injector is not running yet...");
				return;
			}
			os.write((data + "\n").getBytes());
			os.flush();
		} catch (Exception sex) {
			try {
				s = new Socket("127.0.0.1", PORT);
				os = s.getOutputStream();
				os.write((data + "\n").getBytes());
				os.flush();
			} catch(Exception ex) {
				// ignored
			}
		}
	}

	private void init() throws UnknownHostException, IOException,
			InterruptedException {
		device.createForward(PORT, PORT);

		if (killRunningAgent())
			System.out.println("Old client closed");

		uploadAgent();

		Thread threadRunningAgent = new Thread("Running Agent") {
			public void run() {
				try {
					launchProg("" + PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		threadRunningAgent.start();
		Thread.sleep(4000);
		connectToAgent();
		System.out.println("succes !");
	}

	private void connectToAgent() {
		for (int i = 0; i < 10; i++) {
			try {
				s = new Socket("127.0.0.1", PORT);
				break;
			} catch (Exception s) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		System.out.println("Desktop => device socket connected");
		screencapture.start();
		try {
			os = s.getOutputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void launchProg(String cmdList) throws IOException {
		String fullCmd = "export CLASSPATH=" + REMOTE_AGENT_JAR_LOCATION;
		fullCmd += "; exec app_process /system/bin " + AGENT_MAIN_CLASS + " "
				+ cmdList;
		System.out.println(fullCmd);
		device.executeShellCommand(fullCmd,
				new OutputStreamShellOutputReceiver(System.out));
		System.out.println("Prog ended");
		device.executeShellCommand("rm " + REMOTE_AGENT_JAR_LOCATION,
				new OutputStreamShellOutputReceiver(System.out));
	}
}
