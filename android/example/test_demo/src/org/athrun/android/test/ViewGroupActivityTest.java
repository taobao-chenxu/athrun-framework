package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

public class ViewGroupActivityTest extends AthrunTestCase {

	public ViewGroupActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	public void testGetChildCount() throws Exception {
		getDevice().pressMenu();
		findElementByText("ViewGroup").doClick();
		ViewGroupElement groupElement = findElementById("viewgroup_2",
				ViewGroupElement.class);
		assertEquals(3, groupElement.getChildCount());
		assertEquals(2, groupElement.getDirectChildCount());
	}

	public void testGetChild() throws Exception {
		getDevice().pressMenu();
		findElementByText("ViewGroup").doClick();
		ViewGroupElement groupElement = findElementById("viewgroup_2",
				ViewGroupElement.class);
		assertEquals("4", groupElement
				.getChildByIndex(0, TextViewElement.class).getText());
		assertEquals("10",
				groupElement.getChildByIndex(1, ViewGroupElement.class)
						.getChildByIndex(0, TextViewElement.class).getText());
		assertEquals(1,
				groupElement.getChildByIndex(1, ViewGroupElement.class)
						.getDirectChildCount());
	}
	
	public void testFetchText() throws Exception {
		getDevice().pressMenu();
		findElementByText("ViewGroup").doClick();
		ViewGroupElement groupElement = findElementById("viewgroup_2",
				ViewGroupElement.class);
		assertEquals("410", groupElement.fetchText());
	}
}
