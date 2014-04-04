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
 Email:taichan@taobao.com,xiaoliang.chenxl@alibaba-inc.com
*/
package org.athrun.android.framework.webview;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import junit.framework.Assert;

import org.athrun.android.framework.ActivityUtils;
import org.athrun.android.framework.LogConfigure;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.webview.By;
import org.athrun.android.framework.webview.WebElement;
import org.athrun.android.framework.utils.SleepUtils;


import android.app.Instrumentation;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Class for Android.
 * @author xiaoliang.chenxl
 * 
 */
public class WebViewElement {
    protected Logger logger = LogConfigure.getLogger(getClass());
	private static final String LOG_TAG = "WebViewElement";
	
	private WebView mWebView;
	
    private final Instrumentation inst;
	public WebUtils webUtils;
	private ViewFetcher viewFetcher;
	private final int TIMEOUT = 4 * IViewElement.ANR_TIME;
	protected String webUrl = null;
	
	public static final int DOWN = 0;
    public static final int UP = 1;
    private boolean canScroll = false;
	
	List<WebElement> webElements;
	

	public WebViewElement(Instrumentation inst, WebView webview, ActivityUtils activityUtils) {
		this.mWebView = webview;
		this.inst = inst;
		this.viewFetcher = new ViewFetcher(activityUtils);
		this.webUtils = new WebUtils(inst, activityUtils, viewFetcher);
		webElements = new ArrayList<WebElement>();
	}
	/**
	 * getter of mWebView
	 * @return the instance of WebView
	 */
	
	public WebView getmWebView() {
        return mWebView;
    }
	/**
     * Types text in a WebElement matching the specified By object.
     * 
     * @param by the By object.
     * @param text the text to enter in the field
     */
    
    public void typeTextInWebElement(By by, String text){
        typeTextInWebElement(by, text, 0);
    }
	/**
     * Types text in a WebElement matching the specified By object.
     * 
     * @param by the By object.
     * @param text the text to enter in the field
     * @param match if multiple objects match, this determines which one will be typed in
     * 
     */
    
    public void typeTextInWebElement(By by, String text, int match){
        clickOnWebElement(by, match);
        webUtils.hideSoftKeyboard(true);
        inst.sendStringSync(text);
    }
    
    /**
     * Clicks a WebElement matching the specified By object.
     * 
     * @param by the By object.
     * @param match if multiple objects match, this determines which one to click
     * 
     */
    
    public void clickOnWebElement(By by, int match){
        clickOnWebElement(by, match, true);
    }
    
    
    /**
     * Clicks the specified WebElement.
     * 
     * @param webElement the WebElement to click
     * 
     */
    
    public void clickOnWebElement(WebElement webElement){
        if(webElement == null)
            Assert.assertTrue("WebElement is null and can therefore not be clicked!", false);

        clickOnScreen(webElement.getLocationX(), webElement.getLocationY());
    }
    
    /**
     * Waits for a WebElement matching the specified By object. Default timeout is 20 seconds. 
     * 
     * @param by the By object. 
     * @return true if the WebElement is displayed and false if it is not displayed before the timeout
     * 
     */
    
    public boolean waitForWebElement(By by){
        return (waitForWebElement(by, 0, TIMEOUT, true) != null);
    }
    
    /**
     * Clicks on a web element using the given By method
     * 
     * @param by the By object.
     * @param match if multiple objects match, this determines which one will be clicked
     * @param scroll true if scrolling should be performed
     */

    public void clickOnWebElement(By by, int match, boolean scroll){    
        WebElement webElementToClick = waitForWebElement(by, match, TIMEOUT, scroll);

        if(webElementToClick == null){
            if(match > 1) {
                Assert.assertTrue(match + " WebElements with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' are not found!", false);
            }
            else {
                Assert.assertTrue("WebElement with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' is not found!", false);
            }
        }
        clickOnScreen(webElementToClick.getLocationX(), webElementToClick.getLocationY());
    }
    /**
     * Waits for a web element.
     * 
     * @param by the By object. 
     * @param minimumNumberOfMatches the minimum number of matches that are expected to be shown. 0 means any number of matches
     * @param timeout the the amount of time in milliseconds to wait 
     * @param scroll true if scrolling should be performed 
     */
    private WebElement waitForWebElement(final By by, int minimumNumberOfMatches, int timeout, boolean scroll){
        final long endTime = SystemClock.uptimeMillis() + timeout;

        while (true) {  

            final boolean timedOut = SystemClock.uptimeMillis() > endTime;

            if (timedOut){
                logger.info("time out and can not find the web element");
                return null;
            }
            SleepUtils.sleep(500);

            WebElement webElementToReturn = searchForWebElement(by, minimumNumberOfMatches, timeout, scroll); 

            if(webElementToReturn != null)
                return webElementToReturn;

            if(scroll) 
                scrollWebView(DOWN);
        }
    }
    /**
     * Scrolls a WebView.
     * 
     * @param webView the WebView to scroll
     * @param direction the direction to scroll
     * @param allTheWay {@code true} to scroll the view all the way up or down, false to scroll one page up or down                          or down.
     * @return true if more scrolling can be done
     */
    
    public boolean scrollWebView(int direction){
        if (direction == DOWN) {
            inst.runOnMainSync(new Runnable(){
                public void run(){
                    canScroll =  mWebView.pageDown(false);
                }
            });
        }
        if(direction == UP){
            inst.runOnMainSync(new Runnable(){
                public void run(){
                    canScroll =  mWebView.pageUp(false);
                }
            });
        }
        return canScroll;
    }
    /**
     * Searches for a web element.
     * 
     * @param by the By object.
     * @param minimumNumberOfMatches the minimum number of matches that are expected to be shown. 0 means any number of matches
     * @param timeout the amount of time in milliseconds to wait
     * @param scroll true if scrolling should be performed
     * @return true if the web element is found
     */

    private WebElement searchForWebElement(final By by, int minimumNumberOfMatches, int timeout, boolean scroll){

        if(minimumNumberOfMatches < 1){
            minimumNumberOfMatches = 1;
        }

        List<WebElement> viewsFromScreen = webUtils.getCurrentWebElements(by);
        addViewsToList (webElements, viewsFromScreen);

        return getViewFromList(webElements, minimumNumberOfMatches);
    }
    
    /**
     * Returns a text view with a given match.
     * 
     * @param webElements the list of views
     * @param match the match of the view to return
     * @return the view with a given match
     */

    private WebElement getViewFromList(List<WebElement> webElements, int match){

        WebElement webElementToReturn = null;

        if(webElements.size() >= match){

            try{
                webElementToReturn = webElements.get(--match);
            }catch(Exception ignored){}
        }
        if(webElementToReturn != null)
            webElements.clear();

        return webElementToReturn;
    }
    /**
     * Adds views to a given list.
     * 
     * @param allWebElements the list of all views
     * @param webTextViewsOnScreen the list of views shown on screen
     */

    private void addViewsToList(List<WebElement> allWebElements, List<WebElement> webElementsOnScreen){

        int[] xyViewFromSet = new int[2];
        int[] xyViewFromScreen = new int[2];

        for(WebElement textFromScreen : webElementsOnScreen){
            boolean foundView = false;
            textFromScreen.getLocationOnScreen(xyViewFromScreen);

            for(WebElement textFromList : allWebElements){
                textFromList.getLocationOnScreen(xyViewFromSet);

                if(textFromScreen.getText().equals(textFromList.getText()) && xyViewFromScreen[0] == xyViewFromSet[0] && xyViewFromScreen[1] == xyViewFromSet[1]) {
                    foundView = true;
                }
            }

            if(!foundView){
                allWebElements.add(textFromScreen);
            }
        }

    }
    /**
     * Clicks on a given coordinate on the screen.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public void clickOnScreen(float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x, y, 0);
        try{
            inst.sendPointerSync(event);
            inst.sendPointerSync(event2);
        }catch(SecurityException e){
            Assert.assertTrue("Click can not be completed!", false);
        }
    }
    
    /**
     * Enters text in a WebElement matching the specified By object.
     * 
     * @param by the By object.
     * @param text the text to enter in the WebElement field
     * 
     */

    public void enterTextInWebElement(By by, String text){
        if(waitForWebElement(by, 0, TIMEOUT, false) == null) {
            Assert.assertTrue("WebElement with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' is not found!", false);
        }
        webUtils.enterTextIntoWebElement(by, text);
    }
    
    /**
     * Clears text in a WebElement matching the specified By object.
     * 
     * @param by the By object.
     * 
     */
    
    public void clearTextInWebElement(By by){
        webUtils.enterTextIntoWebElement(by, "");
    }
    /**
     * Returns a WebElement matching the specified By object and index.
     * 
     * @param by the By object. 
     * @param index the index of the WebElement. 0 if only one is available
     * @return a WebElement matching the specified index
     */

    public WebElement getWebElement(By by, int index){
        int match = index + 1;
        WebElement webElement = waitForWebElement(by, match, TIMEOUT, true);

        if(webElement == null) {
            if(match > 1){
                Assert.assertTrue(match + " WebElements with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' are not found!", false);
            }
            else {
                Assert.assertTrue("WebElement with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' is not found!", false);
            }
        }
        return webElement;
    }
    
    /**
     * Returns the current web page URL.
     * 
     * @return the current web page URL
     * 
     */

    public String getWebUrl() {

        if(mWebView == null)
            Assert.assertTrue("WebView is not found!", false);

        inst.runOnMainSync(new Runnable() {
            public void run() {
                webUrl = mWebView.getUrl();
            }
        });
        return webUrl;
    }
    
    public void zoomIn() throws Exception {
        waitForWebViewLoaded();
        inst.waitForIdleSync();
        inst.runOnMainSync(new Runnable() {

            @Override
            public void run() {
                mWebView.zoomIn();
            }
        });
        inst.waitForIdleSync();
    }

    public void zoomOut() throws Exception {
        waitForWebViewLoaded();
        inst.waitForIdleSync();
        inst.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mWebView.zoomOut();
            }
        });
        inst.waitForIdleSync();
    }
    /**
     * @return boolean
     */
    private boolean waitForWebViewLoaded() {
        Boolean isOK = false;
        final long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + TIMEOUT) {
            try {
                if (mWebView.getProgress() == 100) {
                    isOK = true;
                    break;
                } else {
                    Thread.sleep(500);
                    Log.d("Mywebview", "sleep:" + mWebView.getProgress());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isOK;
    }
    /**
     * Returns an ArrayList of WebElements displayed in the active WebView.
     * 
     * @return an ArrayList of the WebElement objects currently displayed in the active WebView
     */
    
    public ArrayList<WebElement> getCurrentWebElements(){
        return webUtils.getCurrentWebElements();
    }
    /**
     * Returns an ArrayList of TextViews displayed in the active WebView.
     * 
     * @return an ArrayList of the TextView objects currently displayed in the active WebView
     */
    
    public ArrayList<TextView> getTextViewsFromWebView(){
        return webUtils.getTextViewsFromWebView();
    }
}
