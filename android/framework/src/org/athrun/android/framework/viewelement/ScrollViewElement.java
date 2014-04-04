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

import android.app.Instrumentation;
import android.widget.ScrollView;

/**
 * Class for Android {@code ScrollView}.
 * @author bingyang.djj
 *
 */
public class ScrollViewElement extends ViewElement {
	private ScrollView scrollView;
    
	protected ScrollViewElement(Instrumentation inst, ScrollView scrollView) {
		super(inst, scrollView);
 		this.scrollView = scrollView;
	}
	
	/**
	 * Handles scrolling in response to a "home/end" shortcut press. 
	 * This method will scroll the view to the top or bottom and give the focus to the topmost/bottommost component in the new visible area. 
	 * If no component is a good candidate for focus, this scrollview reclaims the focus.
	 * @param direction the scroll direction: android.view.View.FOCUS_UP to go the top of the view or android.view.View.FOCUS_DOWN to go the bottom
	 */
	public void fullScroll(final int direction) {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				scrollView.fullScroll(direction);
			}
		});
		inst.waitForIdleSync();
	}
	
	/**
	 * Perform scroll to the next screen.
	 */
	public void scrollToNextScreen() {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				scrollView.scrollBy(scrollView.getWidth(), scrollView.getHeight());
			}
		});
		inst.waitForIdleSync();
	}
}
