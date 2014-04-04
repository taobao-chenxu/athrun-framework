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

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;
/**
 * This class is used to check the element type. 
 * 
 * @author bichai@taobao.com
 */
public class ActivityElementType {

	enum Type {
		TextView, ListView, ScrollView, ViewFlipper, ViewAnimator, ViewGroup, ViewParent
	}

	public static Type checkElementType(View view) {
		Type type = null;
		if (view instanceof TextView) {
			type = Type.TextView;
		} else if (view instanceof ListView) {
			type = Type.ListView;
		} else if (view instanceof ScrollView) {
			type = Type.ScrollView;
		} else if (view instanceof ViewFlipper) {
			type = Type.ViewFlipper;
		} else if (view instanceof ViewAnimator) {
			type = Type.ViewAnimator;
		} else if (view instanceof ViewGroup) {
			type = Type.ViewGroup;
		} else if (view instanceof ViewParent) {
			type = Type.ViewParent;
		} else {
			System.out.println("Unknown view type, please check the element type");
		}
		return type;

	}

	public static <T> String TypeComparison(View view, Class<T> returnType) {	
	    String returnStr = null;
		Type type = checkElementType(view);

		String returnTypeName = returnType.getSimpleName();

		type.name();
		if (returnTypeName.contains(type.name())) {
		    returnStr = "Element type is right! Elements type is: "
                    + type.name();
		} else {
		    returnStr = "The element type is " + type.name()
                    + ", but it is defined as " + returnTypeName;
		}
		return returnStr;
	}

}
