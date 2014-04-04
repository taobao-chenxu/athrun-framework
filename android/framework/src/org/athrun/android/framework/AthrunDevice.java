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
package org.athrun.android.framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.athrun.android.framework.utils.AthrunConnectorThread;
import org.athrun.android.framework.utils.ScreenUtils;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.viewelement.ViewCoordinate;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.KeyEvent;

/**
 * This class represents the device, and contains methods to operate device,
 * such as press a key.
 * 
 * @author bingyang.djj
 * 
 */
public final class AthrunDevice {

	private final Logger logger = LogConfigure.getLogger(AthrunDevice.class);

	private Instrumentation inst;
	private ActivityUtils activityUtils;

	private ViewOperation viewOperation;

	private int screenWidth;
	private int screenHeight;

	private static final String LANDSCAPE = "landscape";
	private static final String PORTRAIT = "portrait";
	private static final String EMULATOR_IMEI = "000000000000000";

	private static final String KEY_EVENT_COMMAND = "SHELL_COMMAND: adb shell input keyevent ";

	private static final String ENTER_TEXT_COMMAND = "SHELL_COMMAND: adb shell input text ";

	private static final int offsetX = 10;
	private static final int offsetY = 50;

	AthrunDevice(Instrumentation inst, Activity activity) {
		this.inst = inst;
		this.activityUtils = new ActivityUtils(inst, activity);
		this.viewOperation = ViewOperation.getInstance(inst);

		this.screenWidth = ScreenUtils.getScreenWidth(inst.getTargetContext());
		this.screenHeight = ScreenUtils
				.getScreenHeight(inst.getTargetContext());
	}

	ActivityUtils getActivityUtils() {
		return this.activityUtils;
	}

	/**
	 * Return the height of current screen in pixels.
	 * 
	 * @return Height of current screen.
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Return the width of current screen in pixels.
	 * 
	 * @return Width of current screen
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Press a series of key by codes example:(KEYCODE_DPAD_LEFT,
	 * KEYCODE_DPAD_CENTER).
	 * 
	 * @param keys
	 *            Keycode.
	 */
	public void pressKeys(int... keys) {
		inst.waitForIdleSync();
		for (int i = 0; i < keys.length; i++) {
			logger.info("Send KeyCode: " + String.valueOf(keys[i]));
			inst.sendCharacterSync(keys[i]);
			inst.waitForIdleSync();
		}
	}

	/**
	 * Perform press the Back key.
	 */
	public void pressBack() {
		try {
			Thread.sleep(IViewElement.ANR_TIME);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pressKeys(KeyEvent.KEYCODE_BACK);
	}

	/**
	 * Perform press the Menu key.
	 */
	public void pressMenu() {
		pressKeys(KeyEvent.KEYCODE_MENU);
	}

	/**
	 * Perform press the Home key.
	 */
	public void pressHome() {
		pressKeys(KeyEvent.KEYCODE_HOME);
	}

	/**
	 * Perform press the Up key.
	 */
	public void pressUp() {
		pressKeys(KeyEvent.KEYCODE_DPAD_UP);
	}

	/**
	 * Perform press the Down key.
	 */
	public void pressDown() {
		pressKeys(KeyEvent.KEYCODE_DPAD_DOWN);
	}

	/**
	 * Perform press the Left key.
	 */
	public void pressLeft() {
		pressKeys(KeyEvent.KEYCODE_DPAD_LEFT);
	}

	/**
	 * Perform press the Right key.
	 */
	public void pressRight() {
		pressKeys(KeyEvent.KEYCODE_DPAD_RIGHT);
	}

	/**
	 * Perform press the Enter key.
	 */
	public void pressEnter() {
		pressKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	}

	/**
	 * Perform press the Volume Down key.
	 */
	public void pressVolumeDown() {
		pressKeys(KeyEvent.KEYCODE_VOLUME_DOWN);
	}

	/**
	 * Perform press the Volume Up key.
	 */
	public void pressVolumeUp() {
		pressKeys(KeyEvent.KEYCODE_VOLUME_UP);
	}

	/**
	 * Start athrun client first to use this method.
	 */
	public static void pressBackAcrossApp() {
		pressKeyAcrossApp(KeyEvent.KEYCODE_BACK);
	}

	/**
	 * Start athrun client first to use this method.
	 */
	public static void pressEnterAcrossApp() {
		pressKeyAcrossApp(KeyEvent.KEYCODE_DPAD_CENTER);
	}

	/**
	 * Start athrun client first to use this method.
	 * 
	 * @param keyCode
	 *            keycode to send.
	 */
	public static void pressKeyAcrossApp(int keyCode) {
		AthrunConnectorThread.execute(KEY_EVENT_COMMAND
				+ String.valueOf(keyCode));
	}

	/**
	 * Start athrun client first to use this method.
	 * 
	 * @param text
	 *            text to input
	 */
	public static void enterText(String text) {
		AthrunConnectorThread.execute(ENTER_TEXT_COMMAND + text);
	}

	public Activity getCurrentActivity() {
		return activityUtils.getCurrentActivity();
	}

	/**
	 * Sets the Orientation (Landscape/Portrait) for the current activity.
	 * 
	 * @param orientation
	 *            An orientation constant such as
	 *            {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
	 *            or
	 *            {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
	 *            .
	 */
	public void setScreenOrientation(int orientation) {
		activityUtils.setActivityOrientation(orientation);
		inst.waitForIdleSync();
	}

	/**
	 * Get the Orientation (Landscape/Portrait) for the current activity.
	 * 
	 * @return landscape or portrait.
	 */
	public String getScreenOrientation() {
		inst.waitForIdleSync();
		int orientation = getCurrentActivity().getRequestedOrientation();
		return (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == orientation) ? LANDSCAPE
				: PORTRAIT;
	}

	/**
	 * Waits for the given {@link Activity}.
	 * 
	 * @param name
	 *            the name of the {@code Activity} to wait for e.g.
	 *            {@code "MyActivity"}
	 * @param timeout
	 *            the amount of time in milliseconds to wait
	 * @return {@code true} if {@code Activity} appears before the timeout and
	 *         {@code false} if it does not
	 * 
	 */
	public boolean waitForActivity(String name, int timeout) {
		return activityUtils.waitForActivity(name, timeout);
	}
	
	/**
	 * Waits for the given {@link Activity}.
	 * 
	 * @param name
	 *            the name of the {@code Activity} to wait for e.g.
	 *            {@code "MyActivity"}
	 *            
	 * @return {@code true} if {@code Activity} appears before the timeout and
	 *         {@code false} if it does not
	 * 
	 */
	public boolean waitForActivity(String name) {
		return activityUtils.waitForActivity(name, 5000);
	}

	/**
	 * Returns to the given {@link Activity}.
	 * 
	 * @param name
	 *            the name of the {@code Activity} to return to, e.g.
	 *            {@code "MyActivity"}
	 * 
	 */
	public void goBackToActivity(String name) {
		activityUtils.goBackToActivity(name);
	}

	/**
	 * Return true if current device is an emulator, false otherwise. In order
	 * to use this API, your APP but not your test project must has the
	 * permission to READ_PHONE_STATE.
	 * 
	 * @return If current device is an emulator, false otherwise.
	 */
	public boolean isEmulator() {
		Context context = inst.getTargetContext();
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String imei = tm.getDeviceId();
		if (null == imei || imei.equalsIgnoreCase(EMULATOR_IMEI)) {
			return true;
		}

		return false;
	}

	/**
	 * Simulate an incoming call and hang up. Only work on emulator now.</p> In
	 * order to use this method, you should run athrun client on PC first.
	 * 
	 * @param duration
	 *            How long the call will last.
	 */
	public static void interruptByCall(int duration) {
		AthrunConnectorThread.execute("CALL:" + duration);
	}

	/**
	 * Simulate an incoming sms. Only work on emulator now.</p> In order to use
	 * this method, you should run athrun client on PC first.
	 */
	public static void incomingMessage() {
		AthrunConnectorThread.execute("SMS:54321");
	}

	private ActivityManager getActivityManager() {
		return (ActivityManager) inst.getTargetContext().getSystemService(
				Context.ACTIVITY_SERVICE);
	}

	private WifiManager getWifiManager() {
		return (WifiManager) inst.getTargetContext().getSystemService(
				Context.WIFI_SERVICE);

	}

	/**
	 * disconnected wifi
	 * 
	 * @param
	 * @return true if succeed
	 * @throws Exception
	 */
	public boolean disconnectWifi() {
		boolean s = true;
		getWifiManager().disconnect();
		return s;
	}
	
	/**
	 * reconnected wifi 
	 * @param
	 * @return true if succeed
	 * @throws Exception
	 */
	public boolean reconnectWifi() {
		boolean s = true;
		getWifiManager().reconnect();
		return s;
	}

	private int getCurrentProcessUid() {
		int uid = 0;
		ActivityManager mActivityManager = getActivityManager();
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			if (appProcessInfo.processName.contains(inst.getTargetContext()
					.getPackageName())) {
				uid = appProcessInfo.uid;
			}
		}
		return uid;
	}

	private int getCurrentProcessPid() {
		int pid = 0;
		ActivityManager mActivityManager = getActivityManager();
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			if (appProcessInfo.processName.contains(inst.getTargetContext()
					.getPackageName())) {
				pid = appProcessInfo.pid;
			}
		}
		return pid;
	}

	/**
	 * get Current Process traffic
	 * 
	 * @param
	 * @return rcv & snd by array
	 * @throws Exception
	 */
	String[] getTraffic() throws Exception {
		String result[] = new String[2];
		int uid = getCurrentProcessUid();
		String str1 = "/proc/uid_stat/" + uid + "/tcp_rcv";
		String str2 = "/proc/uid_stat/" + uid + "/tcp_snd";
		try {
			FileReader fileRcv = new FileReader(str1);
			FileReader fileSnd = new FileReader(str2);
			BufferedReader localBufferedReader1 = new BufferedReader(fileRcv);
			BufferedReader localBufferedReader2 = new BufferedReader(fileSnd);
			result[0] = localBufferedReader1.readLine();
			result[1] = localBufferedReader2.readLine();
			localBufferedReader1.close();
			localBufferedReader2.close();
			return result;
		} catch (IOException e) {

		}
		return result;
	}

	/**
	 * get Current Process memory
	 * 
	 * @param
	 * @return memory value
	 * @throws Exception
	 */
	String getPidMemory() throws Exception {
		int pid = getCurrentProcessPid();
		String str1 = "/proc/" + pid + "/status";
		String str2 = "";
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				if (str2.indexOf("VmRSS") >= 0) {
					return "" + str2;
				}
			}
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return str2;
	}

	/**
	 * Return available memory of the device.
	 * 
	 * @return Available memory of the device.
	 */
	public String getAvailMemoryInfo() {
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		getActivityManager().getMemoryInfo(outInfo);
		return Formatter.formatFileSize(inst.getTargetContext(),
				outInfo.availMem);
	}

	/**
	 * Return total memory of the device in MB.
	 * 
	 * @return Total memory of the device in MB.
	 */
	public String getTotalMemoryInfo() {
		String sysFilePath = "/proc/meminfo";
		String memoryInfo;
		String[] arrayOfmemoryInfo;
		long totalMemory = 0;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					sysFilePath));

			memoryInfo = bufferedReader.readLine();
			arrayOfmemoryInfo = memoryInfo.split("\\s+");

			totalMemory = Integer.valueOf(arrayOfmemoryInfo[1]).intValue() * 1024;

			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Formatter.formatFileSize(inst.getTargetContext(), totalMemory);
	}

	/**
	 * Return memory usage of the device in % format.
	 * 
	 * @return Memory usage of the device in % format.
	 */
	public String getMemoryUsage() {
		double avail = Double.valueOf(getAvailMemoryInfo().replace("MB", ""));
		double total = Double.valueOf(getTotalMemoryInfo().replace("MB", ""));
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("##.##%");
		return df.format(avail / total);
	}

	/**
	 * 
	 * @param direction
	 *            directions defined in {@code OperationDirection}
	 */
	public void slide(ViewOperation.Direction direction) {
		slide(direction, 20);
	}

	public void slide(ViewOperation.Direction direction, int speed) {

		switch (direction) {
		case UP:
			dragUp(speed);
			break;

		case DOWN:
			dragDown(speed);
			break;

		case LEFT:
			dragLeft(speed);
			break;

		case RIGHT:
			dragRight(speed);
			break;

		default:
			break;
		}
	}

	private void dragUp(int speed) {
		inst.waitForIdleSync();

		viewOperation.drag(getMiddleBottom().getX(), getMiddleTop().getX(),
				getMiddleBottom().getY() - offsetY, getMiddleTop().getY()
						+ offsetY, speed);
		inst.waitForIdleSync();
	}

	private void dragDown(int speed) {
		inst.waitForIdleSync();

		viewOperation.drag(getMiddleTop().getX(), getMiddleBottom().getX(),
				getMiddleTop().getY() + offsetY, getMiddleBottom().getY()
						- offsetY, speed);
		inst.waitForIdleSync();
	}

	private void dragLeft(int speed) {
		inst.waitForIdleSync();

		viewOperation.drag(getMiddleRight().getX() - offsetX, getMiddleLeft()
				.getX() + offsetX, getMiddleRight().getY(), getMiddleLeft()
				.getY(), speed);
		inst.waitForIdleSync();
	}

	private void dragRight(int speed) {
		inst.waitForIdleSync();

		viewOperation.drag(getMiddleLeft().getX() + offsetX, getMiddleRight()
				.getX() - offsetX, getMiddleLeft().getY(), getMiddleRight()
				.getY(), speed);
		inst.waitForIdleSync();
	}

	private ViewCoordinate getMiddleTop() {
		return new ViewCoordinate(getScreenWidth() / 2, 0);
	}

	private ViewCoordinate getMiddleBottom() {
		return new ViewCoordinate(getScreenWidth() / 2, getScreenHeight());
	}

	private ViewCoordinate getMiddleLeft() {
		return new ViewCoordinate(0, getScreenHeight() / 2);
	}

	private ViewCoordinate getMiddleRight() {
		return new ViewCoordinate(getScreenWidth(), getScreenHeight() / 2);
	}

	public static void waitMoment(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void monkeyPress(String key) {
		AthrunConnectorThread.execute("MONKEY: press " + key);
	}

	public void tap(int x, int y) {
		this.viewOperation.clickOnScreen(x, y);
	}
	/**
	 * 
	 * @param direction
	 * @param speed
	 */
	public void longClickAndSlide(ViewOperation.Direction direction) {
		longClickAndSlide(direction, 20);
	}

	public void longClickAndSlide(ViewOperation.Direction direction, int speed) {

		switch (direction) {
		case UP:
			longClickAndDragUp(speed);
			break;

		case DOWN:
			longClickAndDragDown(speed);
			break;

		case LEFT:
			longClickAndDragLeft(speed);
			break;

		case RIGHT:
			longClickAndDragRight(speed);
			break;

		default:
			break;
		}
	}
	private void longClickAndDragUp(int speed) {
		inst.waitForIdleSync();

		viewOperation.longClickAndDrag(getMiddleBottom().getX(), getMiddleTop().getX(),
				getMiddleBottom().getY() - offsetY, getMiddleTop().getY()
						+ offsetY, speed);
		inst.waitForIdleSync();
	}

	private void longClickAndDragDown(int speed) {
		inst.waitForIdleSync();

		viewOperation.longClickAndDrag(getMiddleTop().getX(), getMiddleBottom().getX(),
				getMiddleTop().getY() + offsetY, getMiddleBottom().getY()
						- offsetY, speed);
		inst.waitForIdleSync();
	}

	private void longClickAndDragLeft(int speed) {
		inst.waitForIdleSync();

		viewOperation.longClickAndDrag(getMiddleRight().getX() - offsetX, getMiddleLeft()
				.getX() + offsetX, getMiddleRight().getY(), getMiddleLeft()
				.getY(), speed);
		inst.waitForIdleSync();
	}

	private void longClickAndDragRight(int speed) {
		inst.waitForIdleSync();

		viewOperation.longClickAndDrag(getMiddleLeft().getX() + offsetX, getMiddleRight()
				.getX() - offsetX, getMiddleLeft().getY(), getMiddleRight()
				.getY(), speed);
		inst.waitForIdleSync();
	}
}
