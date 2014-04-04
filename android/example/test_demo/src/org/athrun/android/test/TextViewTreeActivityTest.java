package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

public class TextViewTreeActivityTest extends AthrunTestCase {
	private static final String LOG_TAG = "TextViewTreeActivityTest";

	public TextViewTreeActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testFetchText() throws Exception {
		findElementById("btn_textviewtree_activity").doClick();
		ViewGroupElement viewGroup = findElementById("textviewtree_root",
				ViewGroupElement.class);
		assertEquals(
				"First level first nodeFirst level second nodeSecond level first nodeSecond level second nodeThird level first nodeThird level second node",
				viewGroup.fetchText());
	}

	@Test
	public void testGetChildInViewGroup() throws Exception {
		findElementById("btn_textviewtree_activity").doClick();
		ViewGroupElement viewGroup = findElementById("textviewtree_root",
				ViewGroupElement.class);

		assertEquals("First level first node",
				viewGroup.getChildByIndex(0, TextViewElement.class).getText());
	}

	@Test
	@Deprecated
	public void testGetChildInDeep() throws Exception {
		findElementById("btn_textviewtree_activity").doClick();
		ViewGroupElement viewGroup = findElementById("textviewtree_root",
				ViewGroupElement.class);
		// log(viewGroup.getChildCount() + "");
		assertEquals("Third level second node",
		viewGroup.getChildByIndex(7, TextViewElement.class).getText());
	}

}
