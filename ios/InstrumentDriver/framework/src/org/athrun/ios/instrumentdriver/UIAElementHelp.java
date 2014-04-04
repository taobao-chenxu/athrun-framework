/**
 * 
 */
package org.athrun.ios.instrumentdriver;

import static org.athrun.ios.instrumentdriver.RunType.ArrayLength;
import static org.athrun.ios.instrumentdriver.RunType.DEBUG;

import java.lang.reflect.Constructor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ziyu.hch
 * 
 */
public class UIAElementHelp {

	/**
	 * 返回UIAElement元素数组
	 * 
	 * @param type
	 *            元素类型
	 * @param guid
	 *            代表但前元素的js对象
	 * @return The UIAElement Array
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UIAElement> T[] elementArray(Class<T> type,
			String guid) throws Exception {
		T[] elements;
		if (DEBUG) {
			elements = (T[]) elementsJSONArray(guid, type);
		} else {
			String[] guids = guidArray(guid);
			elements = (T[]) java.lang.reflect.Array.newInstance(type,
					guids.length);

			for (int i = 0; i < guids.length; i++) {
				Constructor<T> constructor = type.getConstructor(String.class);

				elements[i] = constructor.newInstance(guids[i]);
			}
		}
		return elements;
	}

	/**
	 * 根据类型，代表该js对象的字符串，返回该元素对象
	 * 
	 * @param type
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UIAElement> T getElement(Class<T> type, String guid)
			throws Exception {

		T element;
		if (DEBUG) {
			element = (T) getJSONObject(guid, type);
		} else {
			Constructor<T> constructor = type.getConstructor(String.class);

			element = constructor.newInstance(guid);
		}
		return element;
	}

	private static <T> Object elementsJSONArray(String guid, Class<T> classType)
			throws Exception {

		String elementsJSON = MySocket.getJSONArray(guid);
		JSONArray jsonElementArray = JSONArray.fromObject(elementsJSON);

		return JSONArray.toArray(jsonElementArray, classType);
	}

	private static <T> Object getJSONObject(String guid, Class<T> classType)
			throws Exception {

		String elementJSON = MySocket.getJSONObject(guid);
		JSONObject element = JSONObject.fromObject(elementJSON);

		return JSONObject.toBean(element, classType);
	}

	private static String[] guidArray(String guid) {

		String[] guids = new String[ArrayLength];
		for (int i = 0; i < ArrayLength; i++) {
			String _guid = guid + "[" + i + "]";
			guids[i] = _guid;
		}
		return guids;
	}
}
