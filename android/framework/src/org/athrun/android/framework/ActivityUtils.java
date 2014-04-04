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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.Assert;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.view.KeyEvent;

/**
 * This class contains activity related methods. Examples are:
 * getCurrentActivity(), getActivityList(), getAllOpenedActivities().
 * 
 * @author bingyang.djj
 */
public class ActivityUtils {

	private final Logger logger = LogConfigure.getLogger(getClass());

	private static final int PAUSE = 500;
	private static final int MINIPAUSE = 300;
	private static final int ACTIVITYSYNCTIME = 50;

	private final Instrumentation inst;
	private ActivityMonitor activityMonitor;
	private Activity activity;

	private Stack<Activity> activityStack;
	private Timer activitySyncTimer;

	/**
	 * Constructor that takes in the instrumentation and the start activity.
	 * 
	 * @param inst
	 *            the {@code Instrumentation} instance.
	 * @param activity
	 *            the start {@code Activity}
	 * @param sleeper
	 *            the {@code Sleeper} instance
	 * 
	 */
	ActivityUtils(Instrumentation inst, Activity activity) {
		this.inst = inst;
		this.activity = activity;
		createStackAndPushStartActivity();
		activitySyncTimer = new Timer();
		setupActivityMonitor();
		setupActivityStackListener();
		logger.info("Construct instance of ActivityUtils finished.");
	}

	private void createStackAndPushStartActivity() {
		activityStack = new Stack<Activity>();
		if (activity != null) {
			activityStack.push(this.activity);
		}
	}

	private void setupActivityStackListener() {
		TimerTask activitySyncTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (ActivityUtils.this.activityMonitor != null) {
					Activity activity = ActivityUtils.this.activityMonitor
							.getLastActivity();
					if (activity != null) {
						if ((!ActivityUtils.this.activityStack.isEmpty())
								&& ActivityUtils.this.activityStack.peek()
										.equals(activity)) {
							return;
						}

						if (!activity.isFinishing()) {
							ActivityUtils.this.activityStack.remove(activity);
							ActivityUtils.this.activityStack.push(activity);
						}
					}
				}
			}
		};
		activitySyncTimer.schedule(activitySyncTimerTask, 0, ACTIVITYSYNCTIME);
	}

	/**
	 * Returns a {@code List} of all the opened/active activities.
	 * 
	 * @return a {@code List} of all the opened/active activities
	 * 
	 */
	ArrayList<Activity> getAllOpenedActivities() {
		return new ArrayList<Activity>(this.activityStack);
	}

	/**
	 * This is were the activityMonitor is set up. The monitor will keep check
	 * for the currently active activity.
	 * 
	 */
	private void setupActivityMonitor() {

		try {
			IntentFilter filter = null;
			activityMonitor = inst.addMonitor(filter, null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * 
	 */
	void setActivityOrientation(int orientation) {
		Activity activity = getCurrentActivity();
		activity.setRequestedOrientation(orientation);
	}

	/**
	 * Returns the current {@code Activity}, after sleeping a default pause
	 * length.
	 * 
	 * @return the current {@code Activity}
	 * 
	 */
	Activity getCurrentActivity() {
		Activity currentActivity = getCurrentActivity(true);
		logger.info("Current activity is "
				+ currentActivity.getClass().getName() + ".");
		return currentActivity;
	}

	/**
	 * Waits for an activity to be started if one is not provided by the
	 * constructor.
	 * 
	 */
	private final void waitForActivityIfNotAvailable() {
		if (activity == null) {

			if (activityMonitor != null) {

				while (activityMonitor.getLastActivity() == null) {
					sleep(MINIPAUSE);
				}

			} else {
				sleep(MINIPAUSE);
				setupActivityMonitor();
				waitForActivityIfNotAvailable();
			}
		}
	}

	public Activity getCurrentActivity(boolean shouldSleepFirst) {
		if (shouldSleepFirst) {
			sleep(PAUSE);
			inst.waitForIdleSync();
		}
		waitForActivityIfNotAvailable();
		if (!activityStack.isEmpty()) {
			activity = activityStack.peek();
		}

		return activity;
	}

	boolean waitForActivity(String name, int timeout) {
		long now = SystemClock.uptimeMillis();
		long endTime = now + timeout;
		while ((!getCurrentActivity().getClass().getSimpleName().equals(name))
				&& (now < endTime)) {
			now = SystemClock.uptimeMillis();
		}

		return now < endTime;
	}

	void goBackToActivity(String name) {
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();
		boolean found = false;

		for (int i = 0; i < activitiesOpened.size(); i++) {
			if (((Activity) activitiesOpened.get(i)).getClass().getSimpleName()
					.equals(name)) {
				found = true;
				break;
			}
		}

		if (found) {
			while (!getCurrentActivity().getClass().getSimpleName()
					.equals(name)) {
				try {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
				} catch (SecurityException ignored) {
				}
			}

		} else {

			Assert.assertTrue("No Activity named " + name
					+ " has been priorly opened", false);
		}
	}

	public void finalize() throws Throwable {
		try {
			if (this.activityMonitor != null) {
				this.inst.removeMonitor(this.activityMonitor);
			}

		} catch (Exception ignored) {
		}
		super.finalize();
	}

	void finishInactiveActivities() {
		logger.info("There are " + this.activityStack.size()
				+ " inactive Activities.");

		for (Iterator<?> iter = this.activityStack.iterator(); iter.hasNext();) {
			Activity activity = (Activity) iter.next();
			if (activity != getCurrentActivity()) {
				finishActivity(activity);
				iter.remove();
			}
		}
	}

	private void finishActivity(Activity activity) {
		String name = activity.getClass().getName();

		try {
			logger.info("Finish Activity: " + name + ".");
			activity.finish();

		} catch (Throwable e) {
			e.printStackTrace();
			logger.error(
					"Finish activity " + name + " encounter an exception.", e);
		}
	}

	void finishOpenedActivities() {
		this.activitySyncTimer.cancel();
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();

		int size = activitiesOpened.size();

		logger.info("There are " + size + " opened Activities.");

		for (int i = size - 1; i >= 0; i--) {
			sleep(100);
			finishActivity((Activity) activitiesOpened.get(i));
		}

		finishActivity(getCurrentActivity());
		sleep(MINIPAUSE);

		try {
			this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			sleep(100);
			this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

		} catch (Throwable ignored) {
		}

		this.activityStack.clear();
		logger.info("finishOpenedActivities()");
	}

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignored) {
		}
	}
}
