import com.android.ddmlib.AndroidDebugBridge;

/**
 * @author taichan
 * 
 */
public class Main {

	public static MyDeviceChangeListener deviceChangedInstance = new MyDeviceChangeListener();

	public static void main(String[] argv) throws InterruptedException {

		System.out.println("123");
		AndroidDebugBridge.init(false);

		AndroidDebugBridge adb = AndroidDebugBridge.createBridge("adb.exe",
				false);
		AndroidDebugBridge.addDeviceChangeListener(deviceChangedInstance);

		while (true) {
			Thread.sleep(1000);
		}

	}
}
