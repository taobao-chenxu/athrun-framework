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

/**
 * @author bingyang.djj
 *
 */
public final class ViewCoordinate {
	private int coordinateX;
	private int coordinateY;
	
	/**
	 * Consructor of ViewLocation
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
    public ViewCoordinate(int x, int y) {
    	this.coordinateX = x;
    	this.coordinateY = y;
    }
    
    /**
     * Return the x coordinate.
     * @return The x coordinate.
     */
    public int getX() {
    	return this.coordinateX;
    }
    
    /**
     * Return the y coordinate.
     * @return The y coordinate.
     */
    public int getY() {
    	return this.coordinateY;
    }
}
