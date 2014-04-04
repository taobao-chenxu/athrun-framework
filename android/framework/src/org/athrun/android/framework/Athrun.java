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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.athrun.android.framework.utils.RClassUtils;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

/**
 * Android Test Framework Class for TmtsTestCase to include.
 * 
 * @author shidun Added in 2011-05-16.
 * @author bingyang.djj.
 */
final class Athrun {

	private final Logger logger = LogConfigure.getLogger(getClass());

	/**
	 * Parameter of Athrun.
	 */
	private Instrumentation inst;
	private AthrunDevice athrunDevice;
	private ElementFinder elementFinder;

	/**
	 * Constructor of {@link Athrun}.
	 * 
	 * @param inst
	 *            {@link Instrumentation}.
	 * @param activity
	 *            {@link Activity}.
	 */
	Athrun(Instrumentation inst, Activity activity, int timeout) {
		this.inst = inst;
		this.athrunDevice = new AthrunDevice(inst, activity);
		this.elementFinder = new ElementFinder(timeout, inst);
		logger.info("Construct instance of Athrun finished.");
	}

	AthrunDevice getDevice() {
		return athrunDevice;
	}
	
	ElementFinder getElementFinder() {
		return elementFinder;
	}
	
	ActivityUtils getActivityUtils() {
		return athrunDevice.getActivityUtils();
	}
	
	String getPackageName() {
		return inst.getTargetContext().getPackageName();
	}

	private Resources getResource() {
		return inst.getTargetContext().getResources();
	}

	String getStringById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "string", name);
		return getResource().getString(id);
	}

	XmlResourceParser getLayoutById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "layout", name);
		return getResource().getLayout(id);
	}
	
	/**
	 * get drawable by resource id 
	 * added by huangqin 2013-4-24
	 * @param name the name of resource id
	 * @return
	 * @throws Exception
	 */
	Drawable getDrawableById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "drawable", name);
		return getResource().getDrawable(id);
	}
	
	XmlResourceParser getAnimationById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "anim", name);
		return getResource().getAnimation(id);
	}
}