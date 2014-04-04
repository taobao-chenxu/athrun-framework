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
package org.athrun.android.framework.utils;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;

/**
 * Tool class to get int fields of R.java innerClasses.
 * 
 * @author bingyang.djj Added in 2011-08-10
 */
public final class RClassUtils {
	private static Logger logger = LogConfigure.getLogger(RClassUtils.class);

	private RClassUtils() {
		throw new AssertionError();
	}

	/**
	 * Get int field of R.java innerClasse.
	 * 
	 * @param packageName
	 * @param innerClassName
	 * @param fieldName
	 * @return Int field of R.java innerClasse.
	 * @throws Exception
	 */
	public static int getRFieldByName(String packageName,
			String innerClassName, String fieldName) throws Exception {
		String className = packageName + ".R$" + innerClassName;
		Class<?> innerClass = null;
		innerClass = Class.forName(className);
		Field innerClassField = null;
		innerClassField = innerClass.getField(fieldName);

		if (null != innerClassField) {
			int intId = innerClassField.getInt(null);
			logger.info("Get id " + fieldName + " from inner class "
					+ innerClassName + " of R.java succeed, its int value is "
					+ intId + ".");
			return intId;

		} else {
			logger.error("getRFieldByName() return -1.");
			return -1;
		}
	}

	/**
	 * Get id from R.java.
	 * 
	 * @param packageName
	 * @param idName
	 * @return Id from R.java.
	 * @throws Exception
	 */
	public static int getIdByName(String packageName, String idName)
			throws Exception {
		return getRFieldByName(packageName, "id", idName);
	}

}
