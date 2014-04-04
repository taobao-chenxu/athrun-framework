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
 Email:taichan@taobao.com,bingyang.djj@taobao.com
*/
package org.athrun.android.framework.viewelement;

import android.app.Instrumentation;
import android.widget.CompoundButton;

/**
 * Base class for CheckBox, RadioButton, ToggleButton.
 * @author bingyang.djj
 *
 */
public class CheckableElement extends TextViewElement {
	private CompoundButton compoundButton;

	protected CheckableElement(Instrumentation inst, CompoundButton compoundButton) {
		super(inst, compoundButton);
		this.compoundButton = compoundButton;
	}
	
	/**
	 * The current checked state of the view.
	 * @return The current checked state of the view.
	 */
	public boolean isChecked() {
		inst.waitForIdleSync();
		return compoundButton.isChecked();
	}
}
