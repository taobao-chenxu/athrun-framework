package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;

public class MyTabActivityTest extends AthrunTestCase {
	
	private static final String LOG_TAG = "MyTabActivityTest";
	public MyTabActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}
	
	@Test
	public void testGetCurrentActivityName() throws Exception {
		String curActivityName = null;
		assertEquals(true, getDevice().waitForActivity("MainActivity", 5000));
		curActivityName = getCurrentActivityName();
		assertEquals(true, curActivityName.equals("org.athrun.android.app.MainActivity"));
		
		findElementById("btn_tabs_activity").doClick();
		curActivityName = getCurrentActivityName();
		assertEquals(true, curActivityName.equals("org.athrun.android.app.TabOneActivity"));
		
		findElementByText("Second tab").doClick();
		curActivityName = getCurrentActivityName();
		assertEquals(true, curActivityName.equals("org.athrun.android.app.TabTwoActivity"));
	}


}
