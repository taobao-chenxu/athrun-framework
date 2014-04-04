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

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.athrun.android.framework.dragon.ScreenShot;
import org.athrun.android.framework.special.taobaoview.SkuOptionElement;
import org.athrun.android.framework.utils.AthrunConnectorThread;
import org.athrun.android.framework.utils.RClassUtils;
import org.athrun.android.framework.viewelement.AbsListViewElement;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.viewelement.ScrollViewElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewElement;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.Time;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * Extends this class to use Athrun. If your app overrides onDestroy() method of
 * the main Activity, and adds code like this:
 * <p>
 * android.os.Process.killProcess(android.os.Process.myPid()); </br>
 * <p>
 * You will encounter a process crash error when you run your test, to resolve
 * this, override the tearDown() method in your test class which extends form
 * TmtsTestCase, just make it to be an empty method and run test methods one by
 * one.
 * 
 * @author taichan
 * @author bingyang.djj
 * 
 */
@SuppressWarnings("rawtypes")
public class AthrunTestCase extends ActivityInstrumentationTestCase2 {
	
	private final Logger logger = Logger.getLogger(AthrunTestCase.class);

	/**
	 * {@link Athrun}.
	 */
	private Athrun athrun;
	
	/**
	 * {@link Instrumentation}.
	 */
	private Instrumentation inst;

	private static int maxTimeToFindView = 4 * IViewElement.ANR_TIME;
	
	// andsun add
	private Activity ac ;
	private String activityName;
	private ActivityManager activityManager ;
	/**
	 * Constructor of {@link AthrunTestCase}.
	 * 
	 * @param pkg
	 *            Package name of the app under test.
	 * @param activityClass
	 *            First {@link Activity} to start.
	 * @throws Exception
	 *             Exception.
	 */
	@SuppressWarnings("unchecked")
	public AthrunTestCase(String pkg, String activityClass) throws Exception {
		super(pkg, Class.forName(activityClass));
		LogConfigure.setLogger(pkg);
	}

	/**
	 * Return an instance of {@link Athrun}.
	 * 
	 * @return An instance of {@link Athrun}.
	 */
	private Athrun getAthrun() {
		return new Athrun(getInstrumentation(), getActivity(), maxTimeToFindView);
	}

	@Override
	protected void runTest() throws Throwable{
		String testMethodName = getClass().getName() + "." + getName();
		logger.info("Begin to run " + testMethodName + ".");
		
		//Add Retry when some tests may easily failed.
		Method method = getClass().getMethod(getName(), (Class[])null);
		int retryTimes = 0;
		boolean firstTime = true;
		Failover failover = method.getAnnotation(Failover.class);
		if(failover != null && failover.retryTimes() >= 1){
			retryTimes = failover.retryTimes();
		}
		
		while(retryTimes >= 0){
			try{
				if(!firstTime){
					//finish current activity, avoid interrupting retry result.
					tearDown();
					setUp();
				}
				firstTime = false;
				super.runTest();
				break;
			}catch (Throwable e){
				if(retryTimes >= 1){
					retryTimes--;
					logger.error("fail...retrying...", e);
					continue;
				}else{
					logger.error("runTest() throws an exception: ", e);
					// add by zhuangfei(jiand.zhaojd@alibaba-inc.com)
					 captureScreenShot();
					throw e;
				}
			}
		}
		logger.info(testMethodName + " run finished.");
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		logger.info("setUp()...");
		this.inst = getInstrumentation();
		this.athrun = getAthrun();
		this.athrun.getElementFinder().setTimeout(maxTimeToFindView);
		ViewElement.setMaxTimeToFindView(maxTimeToFindView);
		AthrunConnectorThread.start();
		logger.info("setUp() finished.");
	}

	@Override
	protected void tearDown() throws Exception {
		logger.info("tearDown()...");

		try {
			this.athrun.getActivityUtils().finishOpenedActivities();
			AthrunConnectorThread.stop();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.tearDown();
		logger.info("tearDown() finished.");
	}

	/**
	 * Return an instance of {@code AthrunDevice}.
	 * 
	 * @return An instance of {@code AthrunDevice}.
	 */
	public AthrunDevice getDevice() {
		return athrun.getDevice();
	}
	
	/**
	 * Return an instance of {@code ViewElement} or its subclass by the given id and return type.
	 * @param id
	 * @param index
	 * @param returnType
	 * @return
	 * @throws Exception
	 */
	public <T> T findElementById(int id, int index, Class<T> returnType)
			throws Exception {
		return athrun.getElementFinder().findElementById(id, index, returnType);
	}
	
	public <T> T findWebElementById(int id, int index, Class<T> returnType, ActivityUtils activityUtils)
            throws Exception {
        return athrun.getElementFinder().findWebElementById(id, index, returnType, activityUtils);
    }

	/**
	 * Return an instance of {@code ViewElement} or its subclass by the given id
	 * and return type. It is strongly recommended you to use this API in such
	 * way:
	 * <p>
	 * 1.Add a new package in test project's src folder, this package must has
	 * the same package name as the app's R.java has.</br>
	 * <p>
	 * 2.Copy the R.java from app's gen folder into the package founded in step
	 * 1.</br>
	 * <p>
	 * 3.When the App has been changed, do not forget to update R.java in test
	 * project.</br>
	 * 
	 * <p>
	 * Now you can use this API in this way: findElementById(R.id.yourId,
	 * ViewElement.class).</br>
	 * 
	 * @param <T>
	 *            The {@code ViewElement} or its subclass.
	 * @param id
	 *            Int value of view's id.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return The {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T> T findElementById(int id, Class<T> returnType)
			throws Exception {
		return this.findElementById(id, 0, returnType);
	}
	
	public ViewElement findElementById(int id, int index)
			throws Exception {
		return this.findElementById(id, index, ViewElement.class);
	}

	/**
	 * Return an instance of {@code ViewElement} by the given id.
	 * 
	 * @param id
	 *            Int value of view's id.
	 * @return The {@code ViewElement}.
	 * @throws Exception
	 */
	public ViewElement findElementById(int id) throws Exception {
		return this.findElementById(id, ViewElement.class);
	}
	
	public <T> T findElementById(String literalId, int index, Class<T> returnType) throws Exception {
		int intId = RClassUtils.getIdByName(this.inst.getTargetContext().getPackageName(), literalId);
		return this.findElementById(intId, index, returnType);
	}
	public <T> T findWebElementById(String literalId, int index, Class<T> returnType, ActivityUtils activityUtils) throws Exception {
        int intId = RClassUtils.getIdByName(this.inst.getTargetContext().getPackageName(), literalId);
        return this.findWebElementById(intId, index, returnType, activityUtils);
    }
	/**
	 * Return an instance of {@code ViewElement} or its subclass by the given name
	 * and return type.
	 * 
	 * @param <T>
	 *            The {@code ViewElement} or its subclass.
	 * @param literalId
	 *            String name of view id, the string after @+id/ defined in
	 *            layout files.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return The {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T> T findElementById(String literalId,
			Class<T> returnType) throws Exception {
		return this.findElementById(literalId, 0, returnType);
	}
	
	public <T> T findWebElementById(String literalId,
            Class<T> returnType) throws Exception {
         return this.findWebElementById(literalId, 0, returnType, getDevice().getActivityUtils());
     }
	
	public ViewElement findElementById(String literalId, int index) throws Exception {
		return this.findElementById(literalId, index, ViewElement.class);
	}
	
	/**
	 * Return an instance of {@code ViewElement} by the given name.
	 * 
	 * @param literalId
	 *            String name of view id, the string after @+id/ defined in
	 *            layout files.
	 * @return The {@code ViewElement}.
	 * @throws Exception
	 */
	public ViewElement findElementById(String literalId) throws Exception {
		return this.findElementById(literalId, 0);
	}

//	/**
//	 * Return an instance of {@code ViewElement} or its subclass by the id tree and
//	 * return type. This method is used to find a view that is contained in
//	 * another view.
//	 * 
//	 * @param <T>
//	 *            The {@code ViewElement} or its subclass.
//	 * @param idTree
//	 *            Parent id plus child id with ">", but without whitespace.
//	 *            Example: parentId>childId.
//	 * @param returnType
//	 *            The {@code ViewElement} or its subclass, this parameter
//	 *            determines the return type of this method.
//	 * @return The {@code ViewElement} or its subclass.
//	 * @throws Exception
//	 */
//	public <T extends ViewElement> T findElementInTree(String idTree,
//			Class<T> returnType) throws Exception {
//		return athrun.findElementInTree(idTree, returnType);
//	}

	/**
	 * Return an instance of {@code TextViewElement} by the given text.
	 * 
	 * @param text
	 *            Text to be found.
	 * @return The {@code TextViewElement}.
	 * @throws Exception
	 */
	public TextViewElement findElementByText(String text) throws Exception {
		return this.findElementByText(text, 0, false);
	}
	
	public TextViewElement findElementByText(String text, int index) throws Exception {
		return this.findElementByText(text, index, false);
	}
	
	/**
	 * 
	 * @param text
	 * @param index
	 * @param isEqual
	 * @return
	 * @throws Exception
	 */
	public TextViewElement findElementByText(String text, int index, boolean isEqual) throws Exception {
		return athrun.getElementFinder().findElementByText(text, index, isEqual, TextViewElement.class);
	}

	/**
	 * Return an instance of {@code ViewElement} or its subclass by index.
	 * 
	 * @param <T>
	 * @param index
	 * @param view
	 *            Original type of view you want to get, it should be
	 *            {@code View} or its subclass, this param must be exact.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return Instance of {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T extends ViewElement> T findElementByIndex(int index,
			Class<? extends View> view, Class<T> returnType) throws Exception {
		return athrun.getElementFinder().findElementByIndex(index, view, returnType);
	}
	
	public ScrollViewElement findScrollElementByIndex(int index) throws Exception {
		return findElementByIndex(index, ScrollView.class, ScrollViewElement.class);
	}
	
	public AbsListViewElement findListElementByIndex(int index) throws Exception {
		return findElementByIndex(index, ListView.class, AbsListViewElement.class);
	}
	
	public AbsListViewElement findGridElementByIndex(int index) throws Exception {
		return findElementByIndex(index, GridView.class, AbsListViewElement.class);
	}
	
	/**
	 * This method is used for taobao android client only.
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public SkuOptionElement findSkuOptionByText(String text) throws Exception {
		return athrun.getElementFinder().findSkuOptionByText(text);
	}
	
	public boolean waitForText(String text, int timeout) {
		return athrun.getElementFinder().waitForText(text, timeout);
	}

	/**
	 * Set max time to find view, default time is 5000ms. If a view can not be
	 * found in this time, the test will fail.
	 * 
	 * @param maxTime
	 *            Time in milliseconds.
	 */
	public static void setMaxTimeToFindView(int maxTime) {
		maxTimeToFindView = maxTime;
	}

	/**
	 * Get string value by id from res/values/strings.xml of the app under test.
	 * 
	 * @param name
	 *            Id of the string resource, the name attribute of string node
	 *            defined in android xml files.
	 * @return String value of the string resource.
	 * @throws Exception
	 */
	public String getStringById(String name) throws Exception {
		return athrun.getStringById(name);
	}

	/**
	 * get the activity name that display in the front screen. 
	 * resolve the problem activity name is wrong when crossing application.
	 * note: maybe need adding android.permission.GET_TASKS in AndroidManifest.xml of application under test
	 * 2011/12/08
	 * @author andsun sunzhaoliang31@163.com
	 * @return activityName   see as this type "com.android.browser.BrowserActivity"
	 */
	public String getCurrentActivityNameAcrossApp() throws Exception{
		ac = getActivity();
		activityManager = (ActivityManager)ac.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE); 
		List<RunningTaskInfo> forGroundActivity = activityManager.getRunningTasks(1); 
		RunningTaskInfo currentActivity; 
		currentActivity = forGroundActivity.get(0); 
		activityName = currentActivity.topActivity.getClassName(); 
		
		return activityName;
	}
	
	public void closeCurrentActivity(String ActivityName) throws Exception{
		Intent mintent= new Intent(Intent.ACTION_MAIN);
		mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		mintent.addCategory(Intent.CATEGORY_HOME);
		ac.setIntent(mintent);
		ac.startActivityIfNeeded(mintent, 0);
		activityManager.killBackgroundProcesses(ActivityName); 
	}
	/**
	 * if the activity is ActivityGroup, return current active sub_activity name.
	 * else return active activity name
	 * 2013/04/1
	 * @author xiaoliang.chenxl
	 * @return activityName   
	 */
	public String getCurrentActivityName() throws Exception{
		
		Activity tabAC= getDevice().getActivityUtils().getCurrentActivity();
		if (tabAC instanceof ActivityGroup) {
			return ((ActivityGroup) tabAC).getCurrentActivity().getClass().getName();
		} else {
			return tabAC.getClass().getName();
		}
	}
	
	/**
     * screen shot and save it to sdcard. file name is based on the activity and capture time
     * Please add permission: android.permission.WRITE_EXTERNAL_STORAGE
     * in test apk's AndroidManifest.xml
     * @author 
     * @return void   
	 * @throws Exception 
     */
	public void captureScreenShot() throws Exception{
	    Time time = new Time("GMT+8");    
        time.setToNow();   
        String testMethodName = getClass().getSimpleName() + "." + getName();
        String savedName = testMethodName + "_" + time.year + "_" + time.month + "_" + 
                    time.monthDay + "_" + time.hour + "_" + time.minute + "_" + time.second;
        
	    Activity curActivity = getDevice().getCurrentActivity();
	    new ScreenShot().takeScreenShot(curActivity);
	    //ScreenShot.shoot(curActivity,savedName);
	}
	/**
     * screen shot and save it as given name to sdcard. 
     * Please add permission: android.permission.WRITE_EXTERNAL_STORAGE
     * in test apk's AndroidManifest.xml
     * @author 
     * @return void  
     *  
     */
    public void captureScreenShot(String pictureName){
        Activity curActivity = getDevice().getCurrentActivity();
        new ScreenShot().takeScreenShot(curActivity);
       // ScreenShot.shoot(curActivity, pictureName);
    }
    
	
	/**
	 * get drawable value by id from res/drawable* of the app under test
	 * added by huangqin 2013-4-24
	 * @param name Id of the drawable resource
	 * @return drawable value of the drawable resource.
	 * @throws Exception
	 */
	public Drawable getDrawableById(String name) throws Exception {
		return athrun.getDrawableById(name);
	}	
	
	public XmlResourceParser getAnimationById(String name) throws Exception {
		return athrun.getAnimationById(name);
	}
}
