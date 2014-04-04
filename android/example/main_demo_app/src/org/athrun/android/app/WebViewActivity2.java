package org.athrun.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity2 extends Activity {
	private static final String LOG_TAG = "WebViewActivity2";
	private WebView webview;
	private ProgressBar progressBar;
	private Button button;
	
	private static final String URL = "http://wap.etao.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_extend_layout);
		
		
		initWebView();
		
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(WebViewActivity2.this, "Oh no! " + description,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

		webview.loadUrl(URL);
		webview.clearCache(true);


	}
	
	private void initWebView() {
		View parentView = findViewById(R.id.webview_extend);

		progressBar = (ProgressBar) parentView.findViewById(R.id.my_progressbar);
		button = (Button) findViewById(R.id.next_webview);
		button.setVisibility(View.GONE);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WebViewActivity2.this, WebViewActivity.class);
				startActivity(intent);
			}
		});

		webview = (WebView) parentView.findViewById(R.id.mywebview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.addJavascriptInterface(this, "nativeApp");
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int n = event.getPointerCount();
//		Log.e(LOG_TAG, "1111" + n);
//		return true;
//	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		int n = ev.getPointerCount();
//		Log.e(LOG_TAG, "1111" + n);
//		return false;
//	}
	
}
