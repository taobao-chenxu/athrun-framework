package org.athrun.ios.instrumentdriver.plugin.demo;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.athrun.ios.instrumentdriver.UIAButton;
import org.athrun.ios.instrumentdriver.UIAElement;
import org.athrun.ios.instrumentdriver.plugin.JSObjectLibrary;
import org.athrun.ios.instrumentdriver.test.InstrumentDriverTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
 * @author dylan.zhang
 * 
 *         This is a demo.
 */

public class JSTest extends InstrumentDriverTestCase {
	JSObjectLibrary jsl = new JSObjectLibrary();
	JSONObject jo = jsl.returnScriptJSONObjectType(jsl
			.perScriptSource());
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
	public void enterMessageVoicePage() throws Exception {
		//target.delay(2);
		//win.printElementTree();

		JSONObject text = JSONObject.fromObject(jo.get("test"));
		JSObjectLibrary jso = new JSObjectLibrary();
		
		System.out.println(text.get("menu").toString());
		
		//click all, text, edit,cancel
		jso.getElementByScript("UIAButton", text.get("all").toString()).tap();

		jso.getElementByScript("UIAButton", text.get("text").toString()).tap();
		jso.getElementByScript("UIAButton", text.get("edit").toString()).tap();
		win.findElementByText("Cancel", UIAButton.class).tap();
		
		UIAElement elm = jso.getElementByScript("UIAElement", text.get("title").toString());
		System.out.println("value : "+elm.value()+"  name=  "+ elm.name());
		
		assertTrue(elm.value()!=null);
		assertTrue(elm.name().equals("Messages"));
		
		win.tapWithOptions(text.get("menu").toString());
		win.tapWithOptions(text.get("setting").toString());
		win.tapWithOptions(text.get("application").toString());
		

	}
}
