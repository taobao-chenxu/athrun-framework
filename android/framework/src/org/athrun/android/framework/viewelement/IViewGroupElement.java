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
 Email:taichan@taobao.com, bingyang.djj@taobao.com
*/
package org.athrun.android.framework.viewelement;

/**
 * Interface for Android {@code ViewGroup}.
 * @author bingyang.djj
 *
 */
public interface IViewGroupElement extends IViewElement {
	
	/**
	 * Return child view by the given index and return type in {@code ViewGroup}.
	 * @param <T> The {@code ViewElement} subclass to return.
	 * @param index Index of the child.
	 * @param caster The {@code ViewElement} subclass to return.
	 * @return Child view by the given index and return type in {@code ViewGroup}.
	 */
	<T extends ViewElement> T getChildByIndex(int index, Class<T> caster);
	
	/**
     * Return an instance of {@code ViewElement} or its subclass by the given name.
     * @param <T> {@code ViewElement} or its subclass.
     * @param name String name of view id, the string after @+id/ defined in
	 *            layout files.
     * @param caster {@code ViewElement} or its subclass.
     * @return {@code ViewElement} or its subclass according to the name.
     */
    <T extends ViewElement> T findElementById(String name, Class<T> caster);
    
    /**
     * Return an instance of {@code ViewGroupElement} or its subclass by the given name.
     * @param <T> {@code ViewGroupElement} or its subclass.
     * @param name tag of a view
     * @param caster {@code ViewGroupElement} or its subclass.
     * @return {@code ViewGroupElement} or its subclass according to the name.
     * added by huangqin 2013-6-4
     */
    <T extends ViewElement> T findElementByTag(String name, Class<T> caster);
}
