package net.srcz.android.screencast;

import java.io.IOException;

import net.srcz.android.screencast.api.injector.Injector;
import net.srcz.android.screencast.app.SwingApplication;
import net.srcz.android.screencast.ui.JDialogDeviceList;
import net.srcz.android.screencast.ui.JFrameMain;
import net.srcz.android.screencast.ui.JSplashScreen;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

public class Main extends SwingApplication {

	JFrameMain jf;
	Injector injector;
	IDevice device;
	
	public Main(boolean nativeLook) throws IOException {
		super(nativeLook);
		JSplashScreen jw = new JSplashScreen("");
		
		try {
			initialize(jw);
		} finally {
			jw.setVisible(false);
			jw = null;
		}
	}
	
	private void initialize(JSplashScreen jw) throws IOException {
		jw.setText("Getting devices list...");
		jw.setVisible(true);
		
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();
		waitDeviceList(bridge);

		IDevice devices[] = bridge.getDevices();
		
		jw.setVisible(false);

		// Let the user choose the device
		if(devices.length == 1) {
			device = devices[0];
		} else {
			JDialogDeviceList jd = new JDialogDeviceList(devices);
			jd.setVisible(true);
			
			device = jd.getDevice();
		}
		if(device == null) {
			System.exit(0);
			return;
		}
		
		// Start showing the device screen
		jf = new JFrameMain(device);
		jf.setTitle(""+device);
		
		
		// Show window
		jf.setVisible(true);
		
		// Starting injector
		jw.setText("Starting input injector...");
		jw.setVisible(true);

		injector = new Injector(device);
		injector.start();
		jf.setInjector(injector);	
	}

	
	private void waitDeviceList(AndroidDebugBridge bridge) {
		int count = 0;
		while (bridge.hasInitialDeviceList() == false) {
			try {
				Thread.sleep(100);
				count++;
			} catch (InterruptedException e) {
				// pass
			}
			// let's not wait > 10 sec.
			if (count > 300) {
				throw new RuntimeException("Timeout getting device list!");
			}
		}
	}
	
	protected void close() {
		System.out.println("cleaning up...");
		if(injector != null)
			injector.close();
	
		if(device != null) {
			synchronized (device) {
				AndroidDebugBridge.terminate();
			}
		}
		System.out.println("cleanup done, exiting...");
		super.close();
	}

	public static void main(String args[]) throws IOException {
		boolean nativeLook = args.length == 0 || !args[0].equalsIgnoreCase("nonativelook");
		new Main(nativeLook);
	}

}
