import java.io.IOException;

import javax.xml.stream.events.StartDocument;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.Client;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

/**
 * 
 */

/**
 * @author taichan
 * 
 */
public class MyDeviceChangeListener implements IDeviceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceConnected
	 * (com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceConnected(IDevice device) {
		// TODO Auto-generated method stub
		System.out.println(getTime() + "deviceConnected "
				+ device.getSerialNumber());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#
	 * deviceDisconnected(com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceDisconnected(IDevice device) {
		// TODO Auto-generated method stub
		System.out.println(getTime() + "deviceDisconnected "
				+ device.getSerialNumber());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceChanged
	 * (com.android.ddmlib.IDevice, int)
	 */
	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		// TODO Auto-generated method stub
		System.out.println(getTime() + "deviceChanged(" + changeMask + ")"
				+ device.getSerialNumber() + "-" + Client.CHANGE_PORT
				+ "表示可以用了");
		if (changeMask == Client.CHANGE_PORT) {

			new Thread(new OneParameterRunnable(device) {
				public void run() {

					try {
						IDevice device = (IDevice) getParameter();
						device.executeShellCommand(
								"logcat",
								new MultiLineReceiver(device.getSerialNumber()) {

									@Override
									public boolean isCancelled() {
										// TODO Auto-generated method stub
										return false;
									}

									@Override
									public void processNewLines(String[] lines) {
										System.out.println(getDevicename()
												+ " - " + lines);
									}
								}, 0);
						System.out.println(device.getSerialNumber()
								+ " + exit.");
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AdbCommandRejectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ShellCommandUnresponsiveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();

		}
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private String getTime() {
		return new java.util.Date().toLocaleString() + " ";
	}
}
