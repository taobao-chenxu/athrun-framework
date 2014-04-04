package org.athrun.android.test;

import java.io.IOException;

import org.athrun.android.framework.AthrunDevice;
import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;

import android.view.KeyEvent;



public class VirtualDeviceTest extends AthrunTestCase {

	public VirtualDeviceTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}
	
//	public void testDeviceSetScreen() throws Exception {
//		getTmtsDevice().setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		assertEquals("landscape", getTmtsDevice().getScreenOrientation());
//		findTmtsTextViewByText("ListView").doClick();
//		Thread.sleep(2000);
//		getTmtsDevice().setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		assertEquals("portrait", getTmtsDevice().getScreenOrientation());
//	}
//	
//	public void testGetScreenHeightAndWidth() {
//		assertEquals(480, getTmtsDevice().getScreenHeight());
//		assertEquals(320, getTmtsDevice().getScreenWidth());
//	}
//	
	@Test
	public void testDeviceType() {
		assertEquals(true, getDevice().isEmulator());
	}
//	
//	public void testGetIp() throws Exception {
//		findTmtsTextViewByText("ListView").doClick();
//		log(IpUtils.getLocalIpAddress());
//	}
	
	@Test
	public void testCall() throws IOException, InterruptedException {
		AthrunDevice.interruptByCall(10);
	}
	
	@Test
	public void testSms() {
		AthrunDevice.incomingMessage();
	}
	
	@Test
	public void testPressKeyAcrossApp() throws Exception {
		findElementByText("ListView").doClick();
		AthrunDevice.pressBackAcrossApp();
		Thread.sleep(5000);
	}
	
	@Test
	public void testPressKeyAcrossApp2() throws Exception {
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_DOWN);
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_CENTER);
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_DOWN);
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_DOWN);
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_DOWN);
		AthrunDevice.pressKeyAcrossApp(KeyEvent.KEYCODE_BACK);
		
		Thread.sleep(5000);
	}
}
