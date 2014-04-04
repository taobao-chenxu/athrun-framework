package org.athrun.android.test;


import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.viewelement.AbsListViewElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

public class GridViewActivityTest extends AthrunTestCase {
	
	public GridViewActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testGetChildByIndex() throws Exception {
		assertEquals(true, getDevice()
				.waitForActivity("MainActivity", 2000));
		findElementById("btn_gridview_activity").doClick();
		AbsListViewElement gridView = findElementById("my_gridview",
				AbsListViewElement.class);
		assertNotNull(gridView);
		ViewGroupElement item = gridView.getChildByIndex(0, ViewGroupElement.class);
		assertNotNull(item);
		TextViewElement view = item.getChildByIndex(1, TextViewElement.class);
		assertEquals("Item.0", view.getText());
		item.doClick();
		assertEquals(true, waitForText("Item 0 is clicked!", 2000));
		assertEquals(true,
				getDevice().waitForActivity("ListViewActivity", 2000));
		getDevice().pressBack();
		assertEquals(true,
				getDevice().waitForActivity("GridViewActivity", 2000));
	}

	@Test
	public void testGetChildByIndexMixed() throws Exception {
		findElementById("btn_gridview_activity").doClick();
		AbsListViewElement gridView = findElementById("my_gridview",
				AbsListViewElement.class);
		ViewGroupElement item = gridView.getChildByIndex(19, ViewGroupElement.class);
		assertNotNull(item);
		assertEquals(2, item.getChildCount());
		assertEquals("Item.19", item.getChildByIndex(1, TextViewElement.class)
				.getText());
		item.doClick();
		assertEquals(true, waitForText("Item 19 is clicked!", 2000));
		AbsListViewElement listView = findElementById("my_listview",
				AbsListViewElement.class);
		assertNotNull(listView);
		ViewGroupElement group = listView.getChildByIndex(20, ViewGroupElement.class);
		assertNotNull(group);
		assertEquals("Item20Thank you!", group.fetchText());
		TextViewElement groupChild = group.getChildByIndex(1, TextViewElement.class);
		assertEquals("Item20", groupChild.getText());
		group.doLongClick();
		findElementByText("Item One").doClick();
		assertEquals(true, waitForText("1 pressed!", 2000));
	}

	@Test
	public void testFindMixed() throws Exception {
		findElementByText("ListView").doClick();
		AbsListViewElement listView = findElementById("my_listview",
				AbsListViewElement.class);
		ViewGroupElement group = listView.getChildByIndex(20, ViewGroupElement.class);
		TextViewElement textView = group.getChildByIndex(1, TextViewElement.class);
		assertEquals("Item20", textView.getText());
		TextViewElement textView2 = findElementByText("Item20");
		assertEquals("Item20", textView2.getText());
		group.doLongClick();
		findElementByText("Item One").doClick();
		assertEquals(true, waitForText("1 pressed!", 2000));
	}
	
	@Test
	public void testScrollListInDialog() throws Exception {
		getDevice().pressMenu();
		findElementByText("Dialog With List").doClick();
		AbsListViewElement listView = findElementById("my_listview", AbsListViewElement.class);
		listView.scrollToLine(9);
		assertEquals(9, listView.getLastVisiblePosition());
		findElementByText("OK").doClick();
		assertEquals(true,
				waitForText("Botton OK in dialog with list is pressed!", 2000));
	}

}
