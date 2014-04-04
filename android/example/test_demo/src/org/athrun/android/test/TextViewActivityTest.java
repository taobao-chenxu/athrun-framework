package org.athrun.android.test;

import java.util.ArrayList;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.viewelement.CheckableElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;

import android.widget.CompoundButton;
import android.widget.TextView;

public class TextViewActivityTest extends AthrunTestCase {

	private static final String LOG_TAG = "TextViewActivityTest";

	public TextViewActivityTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testTextViewGetText() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		String textString = findElementById("my_textview",
				TextViewElement.class).getText();
		assertEquals("Hello world", textString);
	}

	@Test
	public void testTextViewSetText() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		TextViewElement myTextView = findElementById("my_textview",
				TextViewElement.class);
		myTextView.setText("This is the text set by user");
		assertEquals("This is the text set by user", myTextView.getText());
	}

	@Test
	public void testEditeTextGetText() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		TextViewElement myEditText = findElementById("my_edittext",
				TextViewElement.class);
		assertEquals("Hello world", myEditText.getText());
	}

	@Test
	public void testAutocompleteTextViewGetText() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		TextViewElement myAutoCompleteTextView = findElementById(
				"my_autocompletetextview", TextViewElement.class);
		assertEquals("Hello world", myAutoCompleteTextView.getText());
	}

	@Test
	public void testAutocompleteTextViewSetText() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		TextViewElement myAutoCompleteTextView = findElementById(
				"my_autocompletetextview", TextViewElement.class);
		myAutoCompleteTextView.clearText();
		myAutoCompleteTextView.setText("This is the text set by user");
		assertEquals("This is the text set by user",
				myAutoCompleteTextView.getText());
		myAutoCompleteTextView.clearText();		
		myAutoCompleteTextView.setText("中文好");
		myAutoCompleteTextView.setText("fuyun");		
		assertEquals("fuyun", myAutoCompleteTextView.getText());
	}

	@Test
	public void testCheckedTextViewCheck() throws Exception {
		findElementById("btn_textview_activity", ViewElement.class).doClick();
		TextViewElement myCheckedTextView = findElementById(
				"my_checkedtextview", TextViewElement.class);
		myCheckedTextView.doClick();
		assertEquals("Checked", myCheckedTextView.getText());
	}

	@Test
	public void testLastEditText() throws Exception {
		findElementById("btn_textview_activity").doClick();
		TextViewElement myLastTextView = findElementById("my_last_edittext",
				TextViewElement.class);
		myLastTextView.setText("fuyun");
		assertEquals("fuyun", myLastTextView.getText());
	}

	@Test
	public void testCheckBox() throws Exception {
		findElementById("btn_textview_activity").doClick();
		CheckableElement checkBox = findElementById("textview_checkbox",
				CheckableElement.class);
		assertEquals(false, checkBox.isChecked());
		checkBox.doClick();
		assertEquals(true, checkBox.isChecked());
	}

	@Test
	public void testInputText() throws Exception {
		findElementById("btn_textview_activity").doClick();
		TextViewElement myEditText = findElementById("my_edittext",
				TextViewElement.class);
		myEditText.inputText("123rfgt@#");
		assertEquals("Hello world123rfgt@#", myEditText.getText());
	}
	
	public void testFindChild() throws Exception {
		findElementById("btn_sametext_activity").doClick();
		ViewGroupElement groupElement = findElementById("sameroot", ViewGroupElement.class);
		assertEquals(3, groupElement.getChildCount());
		groupElement.getChildByIndex(0).doClick();
		groupElement.getChildByIndex(1).doClick();
		groupElement.getChildByIndex(2).doClick();
	}
}
