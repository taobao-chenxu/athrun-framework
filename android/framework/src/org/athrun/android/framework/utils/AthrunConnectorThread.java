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

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;

/**
 * 
 * @author bingyang.djj
 *
 */
public final class AthrunConnectorThread {
	private static Logger logger = LogConfigure.getLogger(AthrunConnectorThread.class);
	private static AthrunConnector athrunServer = AthrunConnector.getInstance();
	
	private static Thread createThread() {
		Thread serverThread = new Thread(athrunServer);
		return serverThread;
	}
	
	public static void start() {
		logger.info("start().");
		createThread().start();
	}
	
	public static void stop() {
		if (null != athrunServer) {
			logger.info("stop().");
			athrunServer.stop();
		}
	}
	
	public static String execute(String command) {
		logger.info("execute(" + command +").");
		return athrunServer.setCommand(command);
	}
	
	/**
	 * @deprecated The main() is used for test only.
	 * @param args
	 */
	public static void main(String[] args) {
		execute("DB: auto:auto@mysql://10.232.27.2/test?characterEncoding=utf8");
		execute("DB: select id from tb1 limit 1");
		execute("DB: select name from tb1 ");
	}
}
