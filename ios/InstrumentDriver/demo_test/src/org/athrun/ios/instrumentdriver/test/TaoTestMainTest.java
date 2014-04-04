package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.UIAButton;
import org.athrun.ios.instrumentdriver.UIAElement;
import org.athrun.ios.instrumentdriver.UIASecureTextField;
import org.athrun.ios.instrumentdriver.UIATextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taichan
 * @author ziyu.hch
 * 
 *         This is a demo.
 */
public class TaoTestMainTest extends InstrumentDriverTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		// Do something before each test case running.
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		// Do something after each test method ran end.
	}

	@Test
	public void Demo1() throws Exception {
		win.printElementTree();		
		
		win.findElementByText("Demo 1").touchAndHold();
		
		UIAElement[] eles = win.elements();
		System.out.println(eles.length);
		
		win.findElementByText("name", UIATextField.class).setValue("athrun");
		
		win.secureTextFields()[0].tap();
		app.keyboard().typeString("abcdefg\\n");

		win.findElementByText("Hello Tao").tap();

		assertEquals("Hello,athrun!", win.staticTexts()[3].name());
	}

	@Test
	public void Demo2() throws Exception {

		win.findElementByText("Demo 2").touchAndHold(1);
		win.printElementTree();

		win.pickers()[0].wheels()[2].selectValue(2015);
		win.pickers()[1].wheels()[0].selectValue("1");
		win.pickers()[1].wheels()[1].selectValue("杭州");
//		win.findElementByText("Back").tap();
		win.navigationBar().leftButton().tap();
	}

	@Test
	public void Demo3() throws Exception {

		win.findElementByText("Demo 3").touchAndHold(1);
		win.sliders()[0].dragToValue(0.12);
		target.frontMostApp().mainWindow().switches()[0].setValue(false);
		// app.navigationBar().leftButton().tap();
		win.findElementByText("Back", UIAButton.class).tap();
		target.scrollUp();
		target.scrollDown();
	}

}
