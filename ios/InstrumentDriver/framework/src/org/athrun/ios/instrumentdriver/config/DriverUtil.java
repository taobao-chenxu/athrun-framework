package org.athrun.ios.instrumentdriver.config;

/**
 * 
 * @author bingxin
 *
 */
import java.io.File;

import org.apache.log4j.Logger;

public class DriverUtil {

	private static Logger logger=Logger.getLogger(DriverUtil.class.getName());

	public static String getApp(){
		String app = Config.get("target_app");
		if (app == null) {
			throw new Error("athrun.properties中没有设置target_app属性");
		}
		if(app.startsWith("~")){
			app = app.replaceAll("~", System.getProperty("user.home"));
		}
		if (!new File(app).exists()) {
				throw new Error("配置的app不存在");
		}
		logger.debug("target_app:"+app);
		return app;
	}

	public static Boolean isDebug(){
		String isDebug = Config.get("isDebug");
		if(isDebug.equalsIgnoreCase("true")){
			logger.debug("debug模式开启");
			return true;
		}else{
			logger.debug("debug模式关闭");
			return false;
		}
	}

	public static Boolean isSimulator() {
		String device = Config.get("isRunSimulator");
		String udid = Config.get("udid");
		if (device == null || !device.equalsIgnoreCase("false") || udid == null) {
			 logger.debug("使用模拟器运行");
			return true;
		} else {
			logger.debug("使用真机运行");
			return false;
		}
	}
	
	public static String getUDID(){
		String udid = Config.get("udid");
		logger.debug("udid:"+udid);
		return udid;
	}
	
	public static int getTimeOut(){
		int timeout = 0;
		try {
			timeout = Integer.parseInt(Config.get("timeout"));
		} catch (Exception e) {
			timeout = 60;// default timeout=60s
			logger.debug("未设置超时时间，默认超时时间为1分钟");
		}
		if (timeout < 30){
			timeout = 60;
			logger.debug("超时时间设置过小，超时时间自动设为1分钟");
		}
		timeout *= 1000; // ms
		return timeout;
	}
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
