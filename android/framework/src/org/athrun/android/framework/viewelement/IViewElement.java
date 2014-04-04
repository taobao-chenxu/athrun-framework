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

/**
 * Interface for all view elements.
 * 
 * @author bingyang.djj
 * 
 */
public interface IViewElement {

	/**
	 * Time for retry in milliseconds.
	 */
	static final int RETRY_TIME = 500;

	/**
	 * ANR time in milliseconds.
	 */
	static final int ANR_TIME = 5000;

	/**
	 * Log tag.
	 */
	static final String LOG_TAG = "ITmtsView";

	/**
	 * Set the scrolled position of your view. This will cause a call to
	 * onScrollChanged(int, int, int, int) and the view will be invalidated.
	 * This version also clamps the scrolling to the bounds of our child.
	 * 
	 * @param x
	 *            the x position to scroll to
	 * @param y
	 *            the y position to scroll to
	 */
	void scrollTo(final int x, final int y);

	/**
	 * Move the scrolled position of your view. This will cause a call to
	 * onScrollChanged(int, int, int, int) and the view will be invalidated.
	 * 
	 * @param x
	 *            the amount of pixels to scroll by horizontally
	 * @param y
	 *            the amount of pixels to scroll by vertically
	 */
	void scrollBy(final int x, final int y);

	/**
	 * Perform click on this view.
	 */
	void doClick();

	/**
	 * Perform click on one point this view.
	 */
	void doClick(float x, float y);

	/**
	 * Perform long click on this view.
	 */
	void doLongClick();
	
	/**
	 * Perform long click on this view by the given time.
	 * @param time time in milliseconds.
	 */
	void doLongClick(int time);

	/**
	 * Call this to try to give focus to a specific view or to one of its
	 * descendants.
	 */
	void requestFocus();

	/**
	 * Returns if the AccessibilityManager is enabled.
	 * 
	 * @return True if this AccessibilityManager is enabled, false otherwise.
	 */
	boolean isEnabled();

	/**
	 * Indicates the selection state of this view.
	 * 
	 * @return True if the view is selected, false otherwise.
	 */
	boolean isSelected();

	/**
	 * Returns if the view is displayed.
	 * 
	 * @return True if the view is displayed, false otherwise.
	 */
	boolean isDisplayed();
	
	/**
	 * Returns true if this view has focus.
	 * @return
	 */
	boolean isFocused();

	/**
	 * Returns this view's identifier.
	 * 
	 * @return This view's identifier.
	 */
	int getId();

	/**
	 * Return the width of the your view.
	 * 
	 * @return The width of your view, in pixels.
	 */
	int getWidth();

	/**
	 * Return the height of your view.
	 * 
	 * @return The height of your view, in pixels.
	 */
	int getHeight();

	/**
	 * Return the location of this view in screen.
	 * 
	 * @return The location of this view in screen.
	 */
	ViewCoordinate getLocation();

	/**
	 * Return the center coordinate of this view in screen.
	 * 
	 * @return The center coordinate of this view in screen.
	 */
	ViewCoordinate getViewCenter();
}
