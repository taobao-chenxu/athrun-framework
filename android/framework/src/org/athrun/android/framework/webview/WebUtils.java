package org.athrun.android.framework.webview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.athrun.android.framework.ActivityUtils;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Contains web related methods. Examples are:
 * enterTextIntoWebElement(), getWebTexts(), getWebElements().
 * 
 * @author xiaoliang.chenxl
 * 
 */

public class WebUtils {

    private ViewFetcher     viewFetcher;
    private Instrumentation inst;
    public ActivityUtils   activityUtils;
    WebClient               mWebCLient;
    WebElementCreator       webElementCreator;

    /**
     * Constructs this object
     * 
     * @param instrumentation the Instrumentation instance
     * @param activityUtils the ActivityUtils instance
     * @param viewFetcher the ViewFetcher instance
     * 
     */

    public WebUtils(Instrumentation instrumentation, ActivityUtils activityUtils,
                    ViewFetcher viewFetcher) {
        this.inst = instrumentation;
        this.activityUtils = activityUtils;
        this.viewFetcher = viewFetcher;
        webElementCreator = new WebElementCreator();
        mWebCLient = new WebClient(instrumentation, webElementCreator, this);
    }

    /**
     * Returns TextView} objects based on web elements shown in the present WebViews
     * 
     * @param onlyFromVisibleWebViews true if only from visible WebViews
     * @return an ArrayList of TextViews created from the present WebViews 
     */

    public ArrayList<TextView> getTextViewsFromWebView() {
        boolean javaScriptWasExecuted = executeJavaScriptFunction("allTexts();");

        return createAndReturnTextViewsFromWebElements(javaScriptWasExecuted);
    }

    /**
     * Creates and returns TextView objects based on WebElements
     * 
     * @return an ArrayList with TextViews
     */

    private ArrayList<TextView> createAndReturnTextViewsFromWebElements(boolean javaScriptWasExecuted) {
        ArrayList<TextView> webElementsAsTextViews = new ArrayList<TextView>();

        if (javaScriptWasExecuted) {
            for (WebElement webElement : webElementCreator.getWebElementsFromWebViews()) {

                WebTextView textView = new WebTextView(inst.getContext(), webElement.getText(),
                    webElement.getLocationX(), webElement.getLocationY());
                if (viewFetcher.isViewSufficientlyShown(textView))
                    webElementsAsTextViews.add(textView);
            }
        }
        return webElementsAsTextViews;
    }

    /**
     * Returns an ArrayList of WebElements currently shown in the active WebView.
     * 
     * @return an ArrayList of the WebElement objects currently shown in the active WebView
     */

    public ArrayList<WebElement> getCurrentWebElements() {
        boolean javaScriptWasExecuted = executeJavaScriptFunction("allWebElements();");

        return getSufficientlyShownWebElements(javaScriptWasExecuted);
    }

    /**
     * Returns an ArrayList of WebElements of the specified By object currently shown in the active WebView.
     * 
     * @param by the By object.
     * @return an ArrayList of the WebElement objects currently shown in the active WebView 
     */

    public ArrayList<WebElement> getCurrentWebElements(final By by) {
        boolean javaScriptWasExecuted = executeJavaScript(by);

        return getSufficientlyShownWebElements(javaScriptWasExecuted);
    }

    /**
     * Returns the sufficiently shown WebElements
     * 
     * @return the sufficiently shown WebElements
     */

    private ArrayList<WebElement> getSufficientlyShownWebElements(boolean javaScriptWasExecuted) {
        ArrayList<WebElement> currentWebElements = new ArrayList<WebElement>();

        if (javaScriptWasExecuted) {
            for (WebElement webElement : webElementCreator.getWebElementsFromWebViews()) {
                if (isWebElementSufficientlyShown(webElement)) {
                    currentWebElements.add(webElement);
                }
            }
        }
        return currentWebElements;
    }

    /**
     * Prepares for start of JavaScript execution
     * 
     * @return the JavaScript as a String
     */

    public String prepareForStartOfJavascriptExecution() {
        webElementCreator.prepareForStart();
        mWebCLient.enableJavascriptAndSetWebClient(viewFetcher
            .getCurrentViews(WebView.class));
        return getJavaScriptAsString();
    }

    /**
     * Enters text into a web element using the given By method
     * 
     * @param by the By object.
     * @param text the text to enter
     */

    public void enterTextIntoWebElement(final By by, final String text) {
        if (by instanceof By.Id) {
            executeJavaScriptFunction("enterTextById(\"" + by.getValue() + "\", \"" + text + "\");");
        } else if (by instanceof By.Xpath) {
            executeJavaScriptFunction("enterTextByXpath(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        } else if (by instanceof By.CssSelector) {
            executeJavaScriptFunction("enterTextByCssSelector(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        } else if (by instanceof By.Name) {
            executeJavaScriptFunction("enterTextByName(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        } else if (by instanceof By.ClassName) {
            executeJavaScriptFunction("enterTextByClassName(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        } else if (by instanceof By.Text) {
            executeJavaScriptFunction("enterTextByTextContent(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        } else if (by instanceof By.TagName) {
            executeJavaScriptFunction("enterTextByTagName(\"" + by.getValue() + "\", \"" + text
                                      + "\");");
        }
    }

    /**
     * Executes JavaScript determined by the given By object
     * 
     * @param by the By object.
     * @return true if JavaScript function was executed
     */

    private boolean executeJavaScript(final By by) {
        if (by instanceof By.Id) {
            return executeJavaScriptFunction("id(\"" + by.getValue() + "\");");
        } else if (by instanceof By.Xpath) {
            return executeJavaScriptFunction("xpath(\"" + by.getValue() + "\");");
        } else if (by instanceof By.CssSelector) {
            return executeJavaScriptFunction("cssSelector(\"" + by.getValue() + "\");");
        } else if (by instanceof By.Name) {
            return executeJavaScriptFunction("name(\"" + by.getValue() + "\");");
        } else if (by instanceof By.ClassName) {
            return executeJavaScriptFunction("className(\"" + by.getValue() + "\");");
        } else if (by instanceof By.Text) {
            return executeJavaScriptFunction("textContent(\"" + by.getValue() + "\");");
        } else if (by instanceof By.TagName) {
            return executeJavaScriptFunction("tagName(\"" + by.getValue() + "\");");
        }
        return false;
    }

    /**
     * Executes the given JavaScript function
     * 
     * @param function the function as a String
     * @return true if JavaScript function was executed
     */

    public boolean executeJavaScriptFunction(final String function) {
        final WebView webView = viewFetcher.getFreshestView(viewFetcher
            .getCurrentViews(WebView.class));

        if (webView == null) {
            return false;
        }

        final String javaScript = prepareForStartOfJavascriptExecution();

        activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable() {
            public void run() {
                if (webView != null) {
                    webView.loadUrl("javascript:" + javaScript + function);
                }
            }
        });
        return true;
    }

    /**
     * Returns true if the view is sufficiently shown
     *
     * @param view the view to check
     * @return true if the view is sufficiently shown
     */

    public final boolean isWebElementSufficientlyShown(WebElement webElement) {
        final WebView webView = viewFetcher.getFreshestView(viewFetcher
            .getCurrentViews(WebView.class));
        final int[] xyWebView = new int[2];

        if (webView != null && webElement != null) {
            webView.getLocationOnScreen(xyWebView);

            if (xyWebView[1] + webView.getHeight() > webElement.getLocationY())
                return true;
        }
        return false;
    }

    /**
     * Splits a name by upper case.
     * 
     * @param name the name to split
     * @return a String with the split name
     * 
     */

    public String splitNameByUpperCase(String name) {
        String[] texts = name.split("(?=\\p{Upper})");
        StringBuilder stringToReturn = new StringBuilder();

        for (String string : texts) {

            if (stringToReturn.length() > 0) {
                stringToReturn.append(" " + string.toLowerCase());
            } else {
                stringToReturn.append(string.toLowerCase());
            }
        }
        return stringToReturn.toString();
    }

    /**
     * Returns the JavaScript file WebView.js as a String
     *  
     * @return the JavaScript file WebView.js as a String
     */

    private String getJavaScriptAsString() {
        InputStream fis = getClass().getResourceAsStream("WebView.js");
        StringBuffer javaScript = new StringBuffer();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = input.readLine()) != null) {
                javaScript.append(line);
                javaScript.append("\n");
            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return javaScript.toString();
    }

    /**
     * Hides the soft keyboard
     * 
     * @param shouldSleepFirst whether to sleep a default pause first
     */

    public void hideSoftKeyboard(boolean shouldSleepFirst) {
        Activity activity = activityUtils.getCurrentActivity(shouldSleepFirst);

        InputMethodManager inputMethodManager = (InputMethodManager) activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                0);
        }
    }
}