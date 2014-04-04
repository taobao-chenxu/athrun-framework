package org.athrun.android.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends Activity {
	private static final String TAG = "WebViewActivity";
	private WebView webview;
	private ProgressBar progressBar;
	private Button button;
	private static String jsResult;
	
//	private static final String URL = "http://wap.etao.com/";
	private static final String URL = "http://m.taobao.com/";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d("sysout", "thread-->" + Thread.currentThread().getId());
		Log.d("sysout", "activity-->" + Thread.currentThread().getName());
		setContentView(R.layout.webview_extend_layout);
		initWebView();
		
		final Activity activity = this;

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});

		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				// webview.loadUrl("javascript:document.all.aftertest.style.color = 'red'");
				// webview.loadUrl("javascript:window.webdriver.showToast('<head>'+document.getElementById(\"txtMultiLine1\").value+'</head>');");
				// webview.loadUrl("javascript:window.webdriver.resultAvailable('<head>'+document+'</head>');");
				// String string = interface1.getResult();
				// Log.d("Mywebview", "yes:" + string);
				// String script =
				// Uri.encode("<script>var d=document.getElementById(\"link_id\");d.innerHTML;</script>");
				// String script =
				// "<script>var d=document.getElementById(\"link_id\");d.innerHTML;</script>"
				// ;
				// webview.loadUrl("javascript:function b(){var a=document.getElementById('link_id');return a.innerHTML};");
				// webview.loadUrl("javascript:window.nativeApp.back(b())");
				progressBar.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

		// webview.loadUrl("file:///sdcard/android_webview_html/item_detail-0db2-null.jhtml.htm");
		// webview.loadUrl("file:///sdcard/android_webview_html/textfields1.html");
		// webview.loadUrl("file:///sdcard/android_webview_html/link1.html");
		webview.loadUrl(URL);
		webview.clearCache(true);

		// webview.set
		// JavascriptExecutor executor = new JavascriptExecutor();
		// final JavascriptInterface interface1 = new
		// JavascriptInterface(this,executor);
		//
		// webview.addJavascriptInterface(interface1, "GoBackAction");

	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		View parentView = findViewById(R.id.webview_extend);

		progressBar = (ProgressBar) parentView.findViewById(R.id.my_progressbar);
		button = (Button) findViewById(R.id.next_webview);
		button.setVisibility(View.GONE);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WebViewActivity.this, WebViewActivity2.class);
				startActivity(intent);
			}
		});

		webview = (WebView) parentView.findViewById(R.id.mywebview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
//		webview.addJavascriptInterface(this, "nativeApp");
	}

	public void back(String result) {
		Toast.makeText(this, result, 1000);
		Log.d("TmtsActivity", result + "---native");
	}
}