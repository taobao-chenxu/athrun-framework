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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

/**
 * 
 * @author bingyang.djj
 *
 */
public final class IpUtils {
	private static final String LOG_TAG = "IpUtils";
	
	
	private IpUtils() {
		throw new AssertionError();
	}
	
	public static String getLocalIpAddress() {  
	    try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement();  
	                if (!inetAddress.isLoopbackAddress()) {  
	                    return inetAddress.getHostAddress().toString();  
	                }  
	            }  
	        }  
	        
	    } catch (SocketException ex) {  
	        Log.e(LOG_TAG, ex.toString());
	    }  
	    
	    return null;  
	}  

}
