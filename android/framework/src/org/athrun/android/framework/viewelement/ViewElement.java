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
package org.athrun.android.framework.viewelement;

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;
import org.athrun.android.framework.ViewOperation;
import org.athrun.android.framework.utils.ScreenUtils;

import android.app.Instrumentation;
import android.view.View;
import android.widget.TextView;

/**
 * Base class for all view elements.
 * 
 * @author bingyang.djj
 * 
 */
public class ViewElement implements IViewElement {

	protected Logger logger = LogConfigure.getLogger(getClass());

	protected Instrumentation inst;
	protected ViewOperation viewOperation;
	private View view;

	private static int maxTimeToFindView = ANR_TIME;

	protected ViewElement(Instrumentation inst, View view) {
		this.inst = inst;
		this.viewOperation = ViewOperation.getInstance(inst);
		this.view = view;
		logViewInfo();
	}

	protected void logViewInfo() {
		logger.info("Construct an instance of "
				+ this.getClass().getSimpleName() + " finished.");
	}

	private void beforeClick() {
		// we need to wait for a view to be shown
		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + getMaxTimeToFindView()) {
			if (view.getVisibility() == View.VISIBLE) {
				break;
			} else {
				sleep(RETRY_TIME);
			}
		}

		if (null == view) {
			logger.error("View is null, doClick() failed.");
		}
		ViewUtils.scrollInToScreenIfNeeded(inst, view);
		ViewUtils.hideSoftInputFromWindow(inst, view);
		sleep(RETRY_TIME);
		inst.waitForIdleSync();
	}

	@Override
	public void doClick() {
		logger.info("doClick().");
		beforeClick();
		viewOperation.clickOnScreen(getViewCenter().getX(), getViewCenter()
				.getY());
	}

	/**
	 * Perform click on this view by send command to monkey.
	 */
	public void doClickByMonkey() {
		beforeClick();
		ViewOperation.monkeyClick(getViewCenter().getX(), getViewCenter()
				.getY());
	}

	@Override
	public void doLongClick(int time) {
		logger.info("doLongClick(" + time + ").");

		if (null == view) {
			logger.error("View is null, doLongClick() failed.");
		}

		ViewUtils.scrollInToScreenIfNeeded(inst, view);

		try {
			int x = getViewCenter().getX();
			int y = getViewCenter().getY();
			ViewUtils.hideSoftInputFromWindow(inst, view);
			sleep(RETRY_TIME);
			inst.waitForIdleSync();
			viewOperation.clickLongOnScreen(x, y, time);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doLongClick() {
		this.doLongClick(0);
	}

	@Override
	public void doClick(float x, float y) {

		if (null == view) {
			logger.error("View is null, doClick(float x,float y) failed.");
		}
		ViewUtils.hideSoftInputFromWindow(inst, view);
		sleep(RETRY_TIME);
		viewOperation.clickOnScreen(x, y);
	}

	@Override
	public void requestFocus() {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				view.requestFocus();
			}
		});
		inst.waitForIdleSync();

	}

	@Override
	public boolean isEnabled() {
		return view.isEnabled();
	}

	@Override
	public boolean isSelected() {
		return view.isSelected();
	}

	@Override
	public boolean isDisplayed() {
		return view.hasWindowFocus() && view.isEnabled() && view.isShown()
				&& (getWidth() > 0) && (getHeight() > 0);
	}

	@Override
	public boolean isFocused() {
		return view.isFocused();
	}

	@Override
	public int getId() {
		return view.getId();
	}

	@Override
	public int getWidth() {
		return view.getWidth();
	}

	@Override
	public int getHeight() {
		return view.getHeight();
	}

	@Override
	public ViewCoordinate getLocation() {
		return ViewUtils.getViewCoordinate(view);
	}

	@Override
	public ViewCoordinate getViewCenter() {
		return ViewUtils.getViewCenter(view);
	}

	@Override
	public void scrollTo(final int x, final int y) {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				view.scrollTo(x, y);
				view.invalidate();
			}
		});
		inst.waitForIdleSync();
	}

	@Override
	public void scrollBy(final int x, final int y) {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				view.scrollBy(x, y);
				view.invalidate();
			}
		});
		inst.waitForIdleSync();
	}

	protected int getScreenHeight() {
		return ScreenUtils.getScreenHeight(view.getContext());
	}

	protected int getScreenWidth() {
		return ScreenUtils.getScreenWidth(view.getContext());
	}

	protected void sleep(int time) {
		try {
			Thread.sleep(time);
			this.inst.waitForIdleSync();

		} catch (InterruptedException ignored) {
			ignored.printStackTrace();
		}
	}

	public static final void setMaxTimeToFindView(int maxTime) {
		maxTimeToFindView = maxTime;
	}

	protected int getMaxTimeToFindView() {
		return maxTimeToFindView;
	}

	// added by suying
	public View getView() {
		return view;
	}
	
	/**
	 * get a view's tag
	 * added by huangqin 2013-6-4
	 * @return
	 */
	public String getTag() {
		return view.getTag().toString();
	}
	
	/**
	 * get a view's tag by key
	 * added by huangqin 2013-6-4
	 * @param key
	 * @return
	 */
	public String getTagByKey(int key) {		
		return view.getTag(key).toString();
	}	
}
