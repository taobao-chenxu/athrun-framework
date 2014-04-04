package org.athrun.android.test;

import java.util.concurrent.TimeUnit;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.viewelement.AbsListViewElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

import android.widget.AbsListView;

public class ListViewActivityTest extends AthrunTestCase {

	public ListViewActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
		AthrunTestCase.setMaxTimeToFindView(5000);
	}

	@Test
	public void testFindElementByIndex() throws Exception {
		findElementById("btn_listview_activity", ViewElement.class).doClick();
		AbsListViewElement mListView = findElementById("my_listview",
				AbsListViewElement.class);
		ViewGroupElement mView = mListView.getChildByIndex(3,
				ViewGroupElement.class);
		TextViewElement mTextView = mView.findElementById("ItemTitle",
				TextViewElement.class);
		assertEquals("Item3", mTextView.getText());
	}

	@Test
	public void testScrollListToLine() throws Exception {
		findElementByText("ListView").doClick();
		AbsListViewElement tmtsListView = findElementById("my_listview",
				AbsListViewElement.class);
		tmtsListView.scrollToLine(200);
		assertEquals(99, tmtsListView.getLastVisiblePosition());
	}

	@Test
	public void testGetChildBeyondIndex() throws Exception {
		findElementById("btn_listview_activity", ViewElement.class).doClick();
		AbsListViewElement tmtsListView = findElementById("my_listview",
				AbsListViewElement.class);
		ViewGroupElement viewGroup = tmtsListView.getChildByIndex(200,
				ViewGroupElement.class);
		assertEquals("Item99Thank you!", viewGroup.fetchText());
	}

	@Test
	public void testFindWithoutId() throws Exception {
		findElementById("btn_listview_activity").doClick();
		ViewGroupElement viewGroup = findElementByIndex(0, AbsListView.class,
				AbsListViewElement.class).getChildByIndex(33,
				ViewGroupElement.class);
		assertEquals("Item33Thank you!", viewGroup.fetchText());
	}

	@Test
	public void testFetchText() throws Exception {
		findElementById("btn_listview_activity").doClick();
		AbsListViewElement tmtsListView = findElementById("my_listview",
				AbsListViewElement.class);
		ViewGroupElement viewGroup = tmtsListView.getChildByIndex(22,
				ViewGroupElement.class);
		assertEquals("Item22Thank you!", viewGroup.fetchText());
	}

	@Test
	public void testRequestFocus() throws Exception {
		findElementById("btn_listview_activity").doClick();
		getDevice().pressDown();
		getDevice().pressDown();
		getDevice().pressEnter();
		assertEquals(true, waitForText("Item 1 clicked!", 2000));
	}

	@Test
	public void testScrollToNextScreen() throws Exception {
		findElementById("btn_listview_activity").doClick();
		AbsListViewElement tmtsListView = findElementById("my_listview",
				AbsListViewElement.class);
		assertEquals(8, tmtsListView.getLastVisiblePosition());
		tmtsListView.scrollToNextScreen();
		TimeUnit.SECONDS.sleep(3);
		assertEquals(8, tmtsListView.getFirstVisiblePosition());
	}
	
	public void testGetText() throws Exception {
		findElementById("btn_listview_activity").doClick();
		AbsListViewElement tmtsListView = findElementById("my_listview",
				AbsListViewElement.class);
		assertEquals("Item0Thank you!", tmtsListView.getTextOfLine(0));
	}
}
