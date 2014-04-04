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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.TextView;

/**
 * Base element for Android {@code AbsListView}. Especially for {@code ListView}
 * and {@code GridView}.
 * 
 * @author bingyang.djj
 * 
 */
public class AbsListViewElement extends ViewGroupElement {
	private AbsListView absListView;

	protected AbsListViewElement(Instrumentation inst, AbsListView absListView) {
		super(inst, absListView);
		this.absListView = absListView;
	}

	@Override
	public <T extends ViewElement> T getChildByIndex(int index,
			Class<T> returnType) {
		Constructor<?>[] constructors = returnType.getDeclaredConstructors();
		Object obj = null;
		View view = getChildViewByIndex(index);
		if (null == view) {
			return null;
		}

		try {
			constructors[0].setAccessible(true);
			obj = constructors[0].newInstance(inst, view);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnType.cast(obj);
	}

	public ViewElement getChildByIndex(int index) {
		return getChildByIndex(index, ViewElement.class);
	}

	/**
	 * Scroll the view to the specified line.
	 * 
	 * @param line
	 *            The
	 */
	public void scrollToLine(final int line) {
		inst.waitForIdleSync();

		final int max = absListView.getAdapter().getCount() - 1;
		final int realLine;

		if (line <= max) {
			realLine = line;

		} else {
			realLine = max;
		}

		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				logger.info("scrollToLine(" + realLine + ")");
				absListView.setSelection(realLine);
			}
		});
		inst.waitForIdleSync();
	}

	private View getChildViewByIndex(int index) {
		scrollToLine(index);

		final int max = absListView.getAdapter().getCount() - 1;
		final int realIndex;

		if (index <= max) {
			realIndex = index;

		} else {
			realIndex = max;
		}

		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + getMaxTimeToFindView()) {
			if (null == absListView.getChildAt(realIndex
					- absListView.getFirstVisiblePosition())) {
				logger.info("getChildByIndex(" + realIndex
						+ ") return null, sleep");
				sleep(RETRY_TIME);
				continue;

			} else {
				logger.info("getChildByIndex(" + realIndex + ") succeed and " +
						"first visible position:" + absListView.getFirstVisiblePosition());
				inst.waitForIdleSync();
				return absListView.getChildAt(realIndex
						- absListView.getFirstVisiblePosition());
			}
		}
		logger.error("getChildByIndex(" + realIndex + ") return null.");

		return null;
	}

	private View getChildViewByIndexInScreen(int index) {
		final long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() < startTime + getMaxTimeToFindView()) {

			if (null == absListView.getChildAt(index)) {
				logger.info("getChildByIndexInScreen(" + index
						+ ") return null, sleep");
				sleep(RETRY_TIME);
				continue;

			} else {
				return absListView.getChildAt(index);
			}
		}

		logger.error("getChildByIndexInScreen(" + index + ") return null.");
		return null;
	}

	public <T extends ViewElement> T getChildByIndexInScreen(int index,
			Class<T> returnType) {
		Constructor<?>[] constructors = returnType.getDeclaredConstructors();
		Object obj = null;
		View view = getChildViewByIndexInScreen(index);
		if (null == view) {
			return null;
		}

		try {
			constructors[0].setAccessible(true);
			obj = constructors[0].newInstance(inst, view);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnType.cast(obj);
	}

	public ViewElement getChildByIndexInScreen(int index) {
		return getChildByIndexInScreen(index, ViewElement.class);
	}

	/**
	 * Scroll the AbsListView to next screen.
	 */
	public void scrollToNextScreen() {
		inst.waitForIdleSync();

		if (isTheLastCompletelyShown()) {
			scrollToLine(absListView.getLastVisiblePosition() + 1);

		} else {
			scrollToLine(absListView.getLastVisiblePosition());
		}
	}

	private boolean isTheLastCompletelyShown() {
		int lastIndex = absListView.getLastVisiblePosition()
				- absListView.getFirstVisiblePosition();
		View lastItem = absListView.getChildAt(lastIndex);

		return !ViewUtils.isViewCenterOutOfScreen(lastItem);
	}

	/**
	 * Returns the position within the adapter's data set for the first item
	 * displayed on screen.
	 * 
	 * @return
	 */
	public int getFirstVisiblePosition() {
		inst.waitForIdleSync();
		return absListView.getFirstVisiblePosition();
	}

	/**
	 * Returns the position within the adapter's data set for the last item
	 * displayed on screen.
	 * 
	 * @return
	 */
	public int getLastVisiblePosition() {
		inst.waitForIdleSync();
		return absListView.getLastVisiblePosition();
	}

	private List<TextView> getAllTextViewOfListItem(View listItem) {
		List<TextView> allTextViews = new ArrayList<TextView>();

		if (listItem instanceof TextView) {
			allTextViews.add((TextView) listItem);

		} else if (listItem instanceof ViewGroup) {
			ArrayList<View> allViews = new ArrayList<View>();
			ViewUtils.addChildren(allViews, (ViewGroup) listItem, false);
			allTextViews = ViewUtils.filterViews(TextView.class, allViews);
		}

		return allTextViews;
	}

	public String getTextOfLine(int index) {
		List<TextView> allTextViews = getAllTextViewOfListItem(getChildViewByIndex(index));
		StringBuilder allText = new StringBuilder();

		for (TextView textView : allTextViews) {
			allText.append(textView.getText());
		}

		return allText.toString();
	}

	public String getTextOfLineInScreen(int index) {
		List<TextView> allTextViews = getAllTextViewOfListItem(getChildViewByIndexInScreen(index));
		StringBuilder allText = new StringBuilder();

		for (TextView textView : allTextViews) {
			allText.append(textView.getText());
		}

		return allText.toString();
	}
	
	public Adapter getAdapter(){
		return absListView.getAdapter(); 
	}	
}
