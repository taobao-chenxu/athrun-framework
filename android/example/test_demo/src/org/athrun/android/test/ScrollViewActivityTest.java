package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.viewelement.ScrollViewElement;

import android.view.View;
import android.widget.ScrollView;

/**
 * TestCases for ScrollViewElement
 * 
 * @author bingyang.djj Added in 2011-05-24
 */
public class ScrollViewActivityTest extends AthrunTestCase {
	private static final String LOG_TAG = "ScrollViewActivityTest";

	public ScrollViewActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testFullScroll() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		ScrollViewElement mScrollView = findElementById("ScrollView",
				ScrollViewElement.class);
		mScrollView.fullScroll(View.FOCUS_DOWN);
	}

	@Test
	public void testScrollTo() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		ScrollViewElement mScrollView = findElementById("ScrollView",
				ScrollViewElement.class);
		mScrollView.scrollTo(480, 400);
	}

	@Test
	public void testScrollBy() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		ScrollViewElement mScrollView = findElementById("ScrollView",
				ScrollViewElement.class);
		mScrollView.scrollBy(480, 400);
	}

	@Test
	public void testFullScrollUp() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		findElementById("scroll_button2").doClick();
		Thread.sleep(10000);
	}

	@Test
	public void testFindScrollViewByIndex() throws Exception {
		findElementByText("ScrollView").doClick();
		findElementByIndex(0, ScrollView.class, ScrollViewElement.class)
				.scrollBy(0, 400);
		Thread.sleep(3000);
	}

	@Test
	public void testScrollToNextScreen() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		findElementById("ScrollView", ScrollViewElement.class)
				.scrollToNextScreen();
		Thread.sleep(5000);
	}
}
