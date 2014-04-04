package org.athrun.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class SameTextActivity extends Activity implements OnTouchListener,
		OnGestureListener {
	private View view;
	
	private Button sameButtonOne;
	private Button sameButtonTwo;
	private Button sameButtonThree;

	private GestureDetector mGestureDetector;

	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sametext_layout);
		mGestureDetector = new GestureDetector(this);
		init();
	}

	private void init() {
		view = findViewById(R.id.sameroot);
		sameButtonOne = (Button) findViewById(R.id.same_one);
//		sameButtonOne.setLongClickable(true);
		sameButtonTwo = (Button) findViewById(R.id.same_two);
		sameButtonThree = (Button) findViewById(R.id.same_three);

		sameButtonOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button One Clicked.",
						Toast.LENGTH_SHORT).show();
			}
		});
		view.setLongClickable(true);
		view.setOnTouchListener(this);

		sameButtonTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button Two Clicked.",
						Toast.LENGTH_SHORT).show();
			}
		});

		sameButtonThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button Three Clicked.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
			sameButtonOne.scrollBy(-200, 0);
			sameButtonTwo.scrollBy(-200, 0);
			sameButtonThree.scrollBy(-200, 0);

		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
			sameButtonOne.scrollBy(200, 0);
			sameButtonTwo.scrollBy(200, 0);
			sameButtonThree.scrollBy(200, 0);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
//		Toast.makeText(this, "onScroll()", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
}
