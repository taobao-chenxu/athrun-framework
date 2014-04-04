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

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.athrun.android.framework.utils.AthrunConnectorThread;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * This class contains MotionEvent methods. Examples are drag() & clickOnScreen().
 * @author bingyang.djj
 * 
 */
public class ViewOperation {
	
	private final Logger logger = LogConfigure.getLogger(getClass());
	private Instrumentation inst;
	private static ViewOperation instance;
    
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private ViewOperation(Instrumentation inst) {
		this.inst = inst;
	}

	public static ViewOperation getInstance(Instrumentation inst) {
		if (null == instance) {
			instance = new ViewOperation(inst);
		}
		return instance;
	}

	/**
	 * Clicks on a given coordinate on the screen.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void clickOnScreen(float x, float y) {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		MotionEvent event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_DOWN, x, y, 0);
		MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_UP, x, y, 0);

		try {
			inst.sendPointerSync(event);
			inst.sendPointerSync(event2);
			logger.info("sent motionevent to click, x is " + x + " y is "
					+ y);

		} catch (SecurityException e) {
			Assert.assertTrue("Click can not be completed!", false);
		}
	}

	/**
	 * Long clicks a given coordinate on the screen
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @throws InterruptedException
	 */
	public void clickLongOnScreen(float x, float y, int time) throws InterruptedException {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		MotionEvent event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_DOWN, x, y, 0);
		try {
			inst.sendPointerSync(event);
			logger.info("longclick start, x is " + x + " y is "
					+ y);
		} catch (SecurityException e) {
			Assert.assertTrue(
					"Click can not be completed! Something is in the way e.g. the keyboard.",
					false);
		}
		inst.waitForIdleSync();
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_MOVE, x + ViewConfiguration.getTouchSlop()
						/ 2, y + ViewConfiguration.getTouchSlop() / 2, 0);
		inst.sendPointerSync(event);
		inst.waitForIdleSync();
		
		if (time > 0) {
			Thread.sleep(time);
			
		} else {
			Thread.sleep((int)(ViewConfiguration.getLongPressTimeout() * 2.5F));
		}
		
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP,
				x, y, 0);
		inst.sendPointerSync(event);
		inst.waitForIdleSync();
		logger.info("longclick finished, x is " + x + " y is "
				+ y);
	}
	
	/**
	 * Simulate touching a specific location and dragging to a new location.</p>
	 * 
	 * This method was copied from {@code TouchUtils.java} in the Android Open
	 * Source Project, and modified here.
	 * 
	 * @param fromX
	 *            X coordinate of the initial touch, in screen coordinates
	 * @param toX
	 *            Xcoordinate of the drag destination, in screen coordinates
	 * @param fromY
	 *            X coordinate of the initial touch, in screen coordinates
	 * @param toY
	 *            Y coordinate of the drag destination, in screen coordinates
	 * @param stepCount
	 *            How many move steps to include in the drag. If you want to
	 *            perform quick velocity, make this value smaller.
	 * 
	 */
	public void drag(float fromX, float toX, float fromY, float toY,
			int stepCount) {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		float y = fromY;
		float x = fromX;
		float yStep = (toY - fromY) / stepCount;
		float xStep = (toX - fromX) / stepCount;
		MotionEvent event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_DOWN, x, y, 0);
		try {
			inst.sendPointerSync(event);
		} catch (SecurityException ignored) {
		}
		inst.waitForIdleSync();

		for (int i = 0; i < stepCount; ++i) {
			y += yStep;
			x += xStep;
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_MOVE, x, y, 0);
			try {
				inst.sendPointerSync(event);
			} catch (SecurityException ignored) {
			}

			inst.waitForIdleSync();
		}

		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP,
				x, y, 0);
		try {
			inst.sendPointerSync(event);
		} catch (SecurityException ignored) {
		}

		inst.waitForIdleSync();
	}
	
	/**
	 * Clicks on a given coordinate on the screen.
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public static void monkeyClick(int x, int y) {
		AthrunConnectorThread.execute("MONKEY:" + "tap " + x + " " + y);
	}
	
	/**
	 * Simulate touching a specific location and dragging to a new location.</p>
	 * 
	 * This method was copied from {@code TouchUtils.java} in the Android Open
	 * Source Project, and modified here.
	 * 
	 * @param fromX
	 *            X coordinate of the initial touch, in screen coordinates
	 * @param toX
	 *            Xcoordinate of the drag destination, in screen coordinates
	 * @param fromY
	 *            X coordinate of the initial touch, in screen coordinates
	 * @param toY
	 *            Y coordinate of the drag destination, in screen coordinates
	 * @param stepCount
	 *            How many move steps to include in the drag. If you want to
	 *            perform quick velocity, make this value smaller.
	 * @throws  
	 * 
	 */
	public void longClickAndDrag(float fromX, float toX, float fromY, float toY,
			int stepCount) {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		float y = fromY;
		float x = fromX;
		float yStep = (toY - fromY) / stepCount;
		float xStep = (toX - fromX) / stepCount;
		MotionEvent event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_DOWN, x, y, 0);
		try {
			inst.sendPointerSync(event);
		} catch (SecurityException ignored) {
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < stepCount; ++i) {
			y += yStep;
			x += xStep;
			eventTime = SystemClock.uptimeMillis();
			event = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_MOVE, x, y, 0);
			try {
				inst.sendPointerSync(event);
			} catch (SecurityException ignored) {
			}

			inst.waitForIdleSync();
		}

		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP,
				x, y, 0);
		try {
			inst.sendPointerSync(event);
		} catch (SecurityException ignored) {
		}

		inst.waitForIdleSync();
	}
}
