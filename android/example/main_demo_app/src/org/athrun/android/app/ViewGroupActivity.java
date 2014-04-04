package org.athrun.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ViewGroupActivity extends Activity {
	private Button button_3;
	private Button button_4;
	private Button button_6;
	private Button button_7;
	private Button button_9;
	private Button button_10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.viewgroup_layout);
		init();
	}
	
	private void init() {
		button_3 = (Button) findViewById(R.id.viewgroup_3);
		button_4 = (Button) findViewById(R.id.viewgroup_4);
		
		button_3.setVisibility(View.GONE);
		View view = findViewById(R.id.viewgroup_5);
		view.setVisibility(View.GONE);
		
		button_9 = (Button) findViewById(R.id.viewgroup_9);
		button_9.setVisibility(View.INVISIBLE);
	}
}
