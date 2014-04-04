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
import android.text.InputType;
import android.widget.TextView;

/**
 * Class for Android {@code TextView} and its subclass.
 * 
 * @author bingyang.djj
 * 
 */
public class TextViewElement extends ViewElement {
	private TextView textView;

	/**
	 * Constructor of {@code TmtsTextView}.
	 * 
	 * @param inst
	 *            {@code Instrumentation}.
	 * @param textView
	 *            {@code TextView}.
	 */
	protected TextViewElement(Instrumentation inst, TextView textView) {
		super(inst, textView);
		this.textView = textView;
	}

	/**
	 * Return the text the TextView is displaying.
	 * 
	 * @return The text the TextView is displaying.
	 */
	public String getText() {
		inst.waitForIdleSync();
		ViewUtils.scrollInToScreenIfNeeded(inst, textView);
		inst.waitForIdleSync();
		// sleep(ANR_TIME);
		return textView.getText().toString();
	}

	/**
	 * Sets the string value of the TextView.
	 * 
	 * @param text
	 *            Text to set to.
	 */
	public void setText(final String text) {
		// Do not use this method, because inst.sendStringSync(text) does not
		// support Chinese.
		// inst.waitForIdleSync();
		// if (textView instanceof EditText) {
		// setEditText(text);
		//
		// } else {
		// inst.runOnMainSync(new Runnable() {
		//
		// @Override
		// public void run() {
		// textView.setText(text);
		// }
		// });
		// }
		//
		// inst.waitForIdleSync();

		inst.waitForIdleSync();
		ViewUtils.scrollInToScreenIfNeeded(inst, textView);
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				textView.setText(text);
			}
		});
		inst.waitForIdleSync();
	}

	/**
	 * Clear the text the TextView is displaying.
	 */
	public void clearText() {
		// inst.waitForIdleSync();
		// inst.runOnMainSync(new Runnable() {
		//
		// @Override
		// public void run() {
		// textView.setText("");
		// }
		// });
		// inst.waitForIdleSync();
		setText("");
	}
	
	/**
	 * Different from setText() , this method simulates input the character one by one.</p>
	 * However, it does not support Chinese now.
	 * @param text the text to input to.
	 */
	public void inputText(String text) {
		inst.waitForIdleSync();
		
			inst.runOnMainSync(new Runnable() {
				
				@Override
				public void run() {
					textView.setInputType(InputType.TYPE_NULL);
				}
			});
		ViewUtils.scrollInToScreenIfNeeded(inst, textView);
		doClick();
		inst.sendStringSync(text);
	}
}
