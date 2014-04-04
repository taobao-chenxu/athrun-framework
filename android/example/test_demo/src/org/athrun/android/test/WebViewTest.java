package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.webview.By;
import org.athrun.android.framework.webview.WebElement;
import org.athrun.android.framework.webview.WebViewElement;

public class WebViewTest extends AthrunTestCase {
	private static final String LOG_TAG = "WebViewTest";

	public WebViewTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testZoomInOut() throws Exception {
		findElementByText("WebView").doClick();
		WebViewElement webview = findWebElementById("mywebview", WebViewElement.class);
		webview.zoomIn();
		webview.zoomIn();
		webview.zoomIn();
		webview.zoomOut();
		webview.zoomOut();
	}

	@Test
	public void testSearch() throws Exception {
        findElementByText("WebView").doClick();
        WebViewElement webview = findWebElementById("mywebview", WebViewElement.class);
        // test search by xpath
        webview.clickOnWebElement(By.xpath("/html/body/div[2]/div/div/div/div/div/div/div/a[3]/strong"), 0);
        // test getWebUrl()
        assertTrue(webview.getWebUrl(), webview.getWebUrl().contains("wap.etao.com"));
        // test clear the text 
        webview.clearTextInWebElement(By.name("q"));
        // test search by name
        webview.typeTextInWebElement(By.name("q"), "iphone4s", 0);
        //test changing the attribute of web element
        webview.enterTextInWebElement(By.className("btn-bg"), "hahaha");
        // test click web element
        webview.clickOnWebElement(By.className("btn-bg"), 0);
        Thread.sleep(5000);
	}

	@Test
	public void testGetText() throws Exception {
		findElementByText("WebView").doClick();
		Thread.sleep(2000);
		WebViewElement webview = findWebElementById("mywebview", WebViewElement.class);
		WebElement webEle =  webview.getWebElement(By.className("btn-bg"),0);
		assertTrue(webEle.getText(), webEle.getText().equals("搜索"));
		Thread.sleep(5000);
	}
}
