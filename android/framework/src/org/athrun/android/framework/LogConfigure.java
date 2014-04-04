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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Configure log4j
 * 
 * @author taichan
 * @author bingyang.djj
 * 
 */
public final class LogConfigure {
	private static Logger logger;

	private static boolean configured = false;

	private LogConfigure() {
		throw new AssertionError();
	}

	public static void setLogger(String pkg) {
		final LogConfigurator logConfigurator = new LogConfigurator();

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			logConfigurator.setFileName(Environment
					.getExternalStorageDirectory() + "/Athrun/athrun.log");
		} else {
			String path = "/data/data/" + pkg + "/files/Athrun";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			logConfigurator.setFileName(path + "/athrun.log");
		}
		logConfigurator.setRootLevel(Level.INFO);
		logConfigurator.configure();
		configured = true;
	}

	public static Logger getLogger(Class<?> clazz) {
		if (!configured) {
			throw new AssertionError("not configured");
		}
		logger = Logger.getLogger(clazz);
		return logger;
	}
}
