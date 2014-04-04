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

import org.athrun.android.framework.ViewOperation;

import android.app.Instrumentation;
import android.view.View;

/**
 * Use this class, when you want to slide a view.
 * @author bingyang.djj
 *
 */
public class SlideableElement extends ViewElement implements ISlideableElement {
	private int x;
	private int y;
	private int h;
	private int w;

	protected SlideableElement(Instrumentation inst, View view) {
		super(inst, view);
		this.x = getViewCenter().getX();
		this.y = getViewCenter().getY();
		this.h = getHeight();
		this.w = getWidth();
	}

	@Override
	public void slide(ViewOperation.Direction direction) {
		slide(direction, 20);
	}

	private void slide(ViewOperation.Direction direction, int speed) {

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
//		int x = getViewCenter().getX();
//		int y = getViewCenter().getY();
//		int h = getHeight();

		viewOperation.drag(x, x, y, y - h / 2, speed);
		inst.waitForIdleSync();
	}

	private void dragDown(int speed) {
		inst.waitForIdleSync();
//		int x = getViewCenter().getX();
//		int y = getViewCenter().getY();
//		int h = getHeight();

		viewOperation.drag(x, x, y, y + h / 2, speed);
		inst.waitForIdleSync();
	}

	private void dragLeft(int speed) {
		inst.waitForIdleSync();
//		int x = getViewCenter().getX();
//		int y = getViewCenter().getY();
//		int w = getWidth();

		viewOperation.drag(x, x - w / 2, y, y, speed);
		inst.waitForIdleSync();
	}

	private void dragRight(int speed) {
		inst.waitForIdleSync();
//		int x = getViewCenter().getX();
//		int y = getViewCenter().getY();
//		int w = getWidth();

		viewOperation.drag(x, x + w / 2, y, y, speed);
		inst.waitForIdleSync();
	}
}
