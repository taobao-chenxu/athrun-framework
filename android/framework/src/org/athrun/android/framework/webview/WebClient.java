package org.athrun.android.framework.webview;

import java.util.List;
import android.app.Instrumentation;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * WebChromeClient used to get information on web elements by injections of JavaScript. 
 * 
 * @author xiaoliang.chenxl
 * 
 */

class WebClient extends WebChromeClient {
    WebElementCreator       webElementCreator;
    private Instrumentation inst;
    private WebChromeClient athrunWebClient;
    private WebUtils        webUtils;
    /**
     * Constructs this object.
     *
     * @param instrumentation the Instrumentation instance
     * @param webElementCreator the WebElementCreator instance
     * @param webUtils 
     * @param webUtils 
     */

    public WebClient(Instrumentation inst, WebElementCreator webElementCreator, WebUtils webUtils) {
        this.webUtils = webUtils;
        this.inst = inst;
        this.webElementCreator = webElementCreator;
        athrunWebClient = this;
    }

    /**
     * Enables JavaScript in the given WebViews objects.
     * 
     * @param webViews the WebView objects to enable JavaScript in
     */

    public void enableJavascriptAndSetWebClient(List<WebView> webViews) {
        for (final WebView webView : webViews) {

            if (webView != null) {

                inst.runOnMainSync(new Runnable() {
                    public void run() {
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebChromeClient(athrunWebClient);

                    }
                });
            }
        }
    }

    /**
     * Overrides onJsPrompt in order to create WebElement objects based on the web elements attributes prompted by the injections of JavaScript
     */

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                              JsPromptResult r) {

        if (message != null) {

            if (message.equals("athrun-finished")) {
                webElementCreator.setFinished(true);
            } else {
                webElementCreator.createWebElementAndAddInList(message, view);
                
            }
        }
        r.confirm();
        return true;
    }
    /**
     * Overrides onProgressChanged in order to inject java script for webview record and playback
     */
//    @Override
//    public void onProgressChanged(WebView view, int newProgress) {
//        super.onProgressChanged(view, newProgress);        
//        if (newProgress == 100) {
//            final WebView webview = view;
//           // final String javascriptString = "var e ; var last; window.onmouseup = function(a){ e=a.toElement;e.onchange = function(){prompt(last.value);};last = e;prompt(e.tagName);}";
//            final String javascriptString = webUtils.prepareForStartOfJavascriptExecution();
//
//            webUtils.activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable() {
//                public void run() {
//                    if (webview != null) {
//                        webview.loadUrl("javascript:" + javascriptString);
//                    }
//                }
//            });
//
//        }
//
//    }
}
