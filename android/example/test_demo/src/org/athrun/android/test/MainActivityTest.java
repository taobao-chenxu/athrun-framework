package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.ViewOperation;
import org.athrun.android.framework.viewelement.AbsListViewElement;
import org.athrun.android.framework.viewelement.SlideableElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

import android.util.Log;
import android.widget.AbsListView;

public class MainActivityTest extends AthrunTestCase {
	private static final String LOG_TAG = "MainActivityTest";

	public MainActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
		AthrunTestCase.setMaxTimeToFindView(10000);
	}
	@Test
	public void testWaitForActivity() throws Exception {
		// log("This is a test for log() method");
		assertEquals(true, getDevice().waitForActivity("MainActivity", 5000));
		assertEquals(false, getDevice().waitForActivity("ScrollActivity", 5000));
		findElementByText("ScrollView").doClick();
		assertEquals(true, getDevice().waitForActivity("ScrollActivity", 5000));
		assertEquals(false, getDevice().waitForActivity("MainActivity", 5000));
		getDevice().pressBack();
		assertEquals(true, getDevice().waitForActivity("MainActivity", 5000));
		assertEquals(false, getDevice().waitForActivity("ScrollActivity", 5000));
	}

	@Test
	public void testFindElementInTree() throws Exception {
		ViewGroupElement include = findElementById("include_checkbox",
				ViewGroupElement.class);
		include.findElementById("my_checkbox", ViewElement.class).doClick();
		TextViewElement mTextView = include.findElementById("textview",
				TextViewElement.class);
		assertEquals("CheckBox is checked!", mTextView.getText());
	}

	@Test
	public void testFindViewByIdDirect() throws Exception {
		findElementById("my_checkbox").doClick();
		assertEquals("CheckBox is checked!",
				findElementById("textview", TextViewElement.class).getText());
	}

	@Test
	public void testLongClick() throws Exception {
		findElementById("my_imageview", ViewElement.class).doLongClick();
		assertEquals(true, waitForText("LongClick", 2000));
	}

	@Test
	public void testPressMenu() throws Exception {
		getDevice().pressMenu();
		findElementByText("Toast").doClick();
		assertEquals(true, waitForText("Hello World", 2000));
	}

	@Test
	public void testPressHome() throws InterruptedException {
		getDevice().pressHome();
		Thread.sleep(2000);
	}

	@Test
	public void testPressBack() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		findElementByText("Bottom Button").doClick();
		getDevice().pressBack();
		assertEquals(true, getDevice().waitForActivity("MainActivity", 2000));
	}

	@Test
	public void testFindViewByText() throws Exception {
		findElementByText("ListView").doClick();
		AbsListViewElement listView = findElementById("my_listview",
				AbsListViewElement.class);
		listView.scrollToNextScreen();
		ViewElement tmtsView = listView.getChildByIndex(35, ViewElement.class);
		tmtsView.doLongClick();
		findElementByText("Item One").doClick();
		assertEquals(true, waitForText("1 pressed!", 2000));
	}

	@Test
	public void testScrollListInDialog() throws Exception {
		getDevice().pressMenu();
		findElementByText("Dialog With List").doClick();
		AbsListViewElement listView = findElementByIndex(0, AbsListView.class,
				AbsListViewElement.class);
		listView.scrollToLine(9);
		assertEquals(9, listView.getLastVisiblePosition());
		findElementByText("OK").doClick();
		assertEquals(true,
				waitForText("Botton OK in dialog with list is pressed!", 2000));
	}

	@Test
	public void testSetScreen() throws InterruptedException {
		getDevice().setScreenOrientation(
				android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		assertEquals("landscape", getDevice().getScreenOrientation());
	}

	@Test
	public void testGetStringById() throws Exception {
		findElementByText("ScrollView").doClick();
		assertEquals(true, getStringById("scroll_text").contains("道可道"));
		// log(getStringById("scroll_text"));
	}

	@Test
	public void testFindToastById() throws Exception {
		findElementById("my_imageview", ViewElement.class).doLongClick();
		assertEquals(true, waitForText("LongClick", 2000));
	}

	@Test
	public void testRequestFocus() throws Exception {
		findElementByText("ScrollView").requestFocus();
		getDevice().pressDown();
		getDevice().pressUp();
		getDevice().pressEnter();
		assertEquals(true, getDevice().waitForActivity("ScrollActivity", 2000));
	}

	@Test
	public void testPressEnter() throws Exception {
		findElementByText("ListView").requestFocus();
		getDevice().pressDown();
		getDevice().pressEnter();
		getDevice().pressEnter();
		Thread.sleep(2000);
	}

	@Test
	public void testFindViewByIntId() throws Exception {
		findElementById("btn_scrollview_activity", ViewElement.class)
				.doClick();
		assertEquals(true, getDevice().waitForActivity("ScrollActivity", 5000));
	}

	@Test
	public void testDevicePressKey() throws Exception {
		findElementByText("ScrollView").requestFocus();
		getDevice().pressEnter();
		assertEquals(true, getDevice().waitForActivity("ScrollActivity", 1000));
		getDevice().pressBack();
		assertEquals(true, getDevice().waitForActivity("MainActivity", 1000));
	}

	@Test
	public void testFindViewOutOfScreen() throws Exception {
		findElementById("btn_scrollview_activity").doClick();
		TextViewElement tb = findElementByText("Top Button");
		// log("x is " + tb.getLocation().getX());
		// log("y is " + tb.getLocation().getY());
		tb.doClick();
		Thread.sleep(3000);
		// log("x is " + tb.getLocation().getX());
		// log("y is " + tb.getLocation().getY());
		findElementByText("Top Button").doClick();
		Thread.sleep(3000);
	}

	@Test
	public void testFindViewOutOfScreen2() throws Exception {
		ViewGroupElement rootGroup = findElementById("mainroot",
				ViewGroupElement.class);
		rootGroup.findElementById("btn_scrollview_activity", ViewElement.class)
				.doClick();
	}

	@Test
	public void testFindViewOutOfScreen3() throws Exception {
		ViewGroupElement rootGroup = findElementById("mainroot",
				ViewGroupElement.class);
		rootGroup.getChildByIndex(4, ViewElement.class).doClick();
	}

	@Test
	public void testSlide() throws Exception {
		SlideableElement gallery = findElementById("my_gallery",
				SlideableElement.class);

		for (int i = 0; i < 10; i++) {
			gallery.slide(ViewOperation.Direction.LEFT);
		}
	}
	
	@Test
	public void testGetChildCount() throws Exception {
		ViewGroupElement root = findElementById("mainroot", ViewGroupElement.class);
		Log.i(LOG_TAG, String.valueOf(root.getDirectChildCount()));
	}
	
	@Test
	public void testFindMixed() throws Exception {
		findElementByText("ListView").doClick();
		AbsListViewElement listView = findElementById("my_listview",
			AbsListViewElement.class);
		ViewGroupElement group = listView.getChildByIndex(20, 	ViewGroupElement.class);
		TextViewElement textView = group.getChildByIndex(1, TextViewElement.class);
		assertEquals("Item20", textView.getText());
		TextViewElement textView2 = findElementByText("Item20");
		assertEquals("Item20", textView2.getText());
		group.doLongClick();
		findElementByText("Item One").doClick();		
		assertTrue(waitForText("1 pressed!", 2000));
	}

}
