package org.athrun.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;

public class ScrollActivity extends Activity {
	private static final String LOG_TAG = "ScrollActivity";
	private Button scrollButtonTop;
	private Button scrollButtonBottom;
	private ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scrollview_layout);
		scrollView = (ScrollView) findViewById(R.id.ScrollView);
		scrollButtonTop = (Button) findViewById(R.id.scroll_button);
		scrollButtonBottom = (Button) findViewById(R.id.scroll_button2);
		final DisplayMetrics dm = new DisplayMetrics(); 
		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 

		scrollButtonTop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(LOG_TAG, "scroll_button clicked");
				Log.v(LOG_TAG, String.valueOf(dm.widthPixels));
				Log.v(LOG_TAG, String.valueOf(dm.heightPixels));
				Log.v(LOG_TAG, String.valueOf(scrollView.getScrollX()));
				Log.v(LOG_TAG, String.valueOf(scrollView.getScrollY()));
				scrollView.fullScroll(View.FOCUS_DOWN);
			}
		});
		
		scrollButtonBottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollView.fullScroll(View.FOCUS_UP);
			}
		});
	}
}
