package org.athrun.ios.instrumentdriver.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.athrun.ios.instrumentdriver.UIAElement;
import org.athrun.ios.instrumentdriver.UIAElementHelp;

import net.sf.json.JSONObject;


/**
 * @author dylan.zhang
 * 
 */

public class JSObjectLibrary {

	/**
	 * get Element By Script
	 * @param scriptPropName
	 * @param elementType
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public <T extends UIAElement> T getElementByScript(String elementType,
			String scriptPropName) throws Exception {

		Class<T> element = (Class<T>) Class
				.forName("org.athrun.ios.instrumentdriver." + elementType);
		T scriptClass = (T) UIAElementHelp.getElement(element, scriptPropName);
		
		return scriptClass;
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public JSONObject returnScriptJSONObjectType(String resource) {
		JSONObject script = JSONObject.fromObject(resource);
		return script;
	}

	/**
	 * 设置 资源文件的位置
	 * 
	 * @param path
	 * @return
	 */
	public String perScriptSource() {
		String path = System.getProperty("user.dir")
				+ "/src/PluginApp.js";
		String resource = readFile(path);
		return resource;
	}

	/**
	 * load and read file
	 * 
	 * @param path
	 * @return
	 */
	public String readFile(String path) {
		boolean flag = assertIsFile(path);
		String str = null;
		
		if (!flag) {
			return "There is no JSON resource file";
		}
		
		try {
			InputStream is = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			
			String line = br.readLine(); 
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			str = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 *
	 * @param path
	 * @return String type
	 */
	public boolean assertIsFile(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}
}
