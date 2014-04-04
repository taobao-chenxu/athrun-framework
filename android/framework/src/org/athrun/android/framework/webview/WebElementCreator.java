package org.athrun.android.framework.webview;

import java.util.ArrayList;
import android.os.SystemClock;
import android.webkit.WebView;

/**
 * Contains TextView related methods. Examples are:
 * getTextViewsFromWebViews(), createTextViewAndAddInList().
 * 
 * @author xiaoliang.chenxl
 * 
 */

class WebElementCreator {

	private ArrayList<WebElement> webElements;
	private boolean isFinished = false;

	/**
	 * Constructs this object
	 */

	public WebElementCreator(){
		webElements = new ArrayList<WebElement>();
	}

	/**
	 * Prepares for start of creating TextView objects based on web elements 
	 */

	public void prepareForStart(){
		setFinished(false);
		webElements.clear();
	}

	/**
	 * Returns an ArrayList of TextView objects based on the web elements shown
	 * 
	 * @return an ArrayList of TextView objects based on the web elements shown
	 */

	public ArrayList<WebElement> getWebElementsFromWebViews(){
		waitForWebElementsToBeCreted();
		return webElements;
	}

	/**
	 * Returns true if all TextView objects based on web elements have been created
	 * 
	 * @return true if all TextView objects based on web elements have been created
	 */

	public boolean isFinished(){
		return isFinished;
	}


	/**
	 * Set to true if all {@code TextView} objects have been created
	 * 
	 * @param isFinished true if all {@code TextView} objects have been created
	 */

	public void setFinished(boolean isFinished){
		this.isFinished = isFinished;
	}

	/**
	 * Creates a {@ WebElement} object from the given text and {@code WebView}
	 * 
	 * @param webData the data of the web element 
	 * @param webView the {@code WebView} the text is shown in
	 */

	public void createWebElementAndAddInList(String webData, WebView webView){

		WebElement webElement = createWebElementAndSetLocation(webData, webView);

		if((webElement!=null))
			webElements.add(webElement);
	}

	/**
	 * Sets the location of a WebElement 
	 * 
	 * @param webElement the TextView object to set location 
	 * @param webView the WebView the text is shown in
	 * @param x the x location to set
	 * @param y the y location to set
	 * @param width the width to set
	 * @param height the height to set
	 */

	private void setLocation(WebElement webElement, WebView webView, int x, int y, int width, int height ){
		float scale = webView.getScale();
		int[] locationOfWebViewXY = new int[2];
		webView.getLocationOnScreen(locationOfWebViewXY);

		int locationX = (int) (locationOfWebViewXY[0] + (x + (Math.floor(width / 2))) * scale);
		int locationY = (int) (locationOfWebViewXY[1] + (y + (Math.floor(height / 2))) * scale);

		webElement.setLocationX(locationX);
		webElement.setLocationY(locationY);
	}

	/**
	 * Creates a WebView object 
	 * 
	 * @param information the data of the web element
	 * @param webView the web view the text is shown in
	 * 
	 * @return a WebElement object with a given text and location
	 */

	private WebElement createWebElementAndSetLocation(String information, WebView webView){
		String[] data = information.split(";,");
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		try{
		    // fix a float conversion bug
			x = (int)(Float.valueOf(data[5]) + 0.5);
			y = (int)(Float.valueOf(data[6]) + 0.5);
			width = (int)(Float.valueOf(data[7]) + 0.5);
			height = (int)(Float.valueOf(data[8]) + 0.5);
		}catch(Exception ignored){}

		WebElement webElement = null;
		
		try{
			webElement = new WebElement(data[0], data[1], data[2], data[3], data[4]);
			setLocation(webElement, webView, x, y, width, height);
		}catch(Exception ignored) {}
		
		return webElement;
	}

	/**
	 * Waits for WebElement objects to be created
	 * 
	 * @return true if successfully created before timeout
	 */

	private boolean waitForWebElementsToBeCreted(){
		final long endTime = SystemClock.uptimeMillis() + 5000;

		while(SystemClock.uptimeMillis() < endTime){

			if(isFinished){
				return true;
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}