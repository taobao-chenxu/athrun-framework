package org.athrun.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class TextViewActivity extends Activity {
	private TextView textView;
	private EditText editText;
	private AutoCompleteTextView autoCompleteTextView;
	private CheckedTextView checkedTextView;
	private MultiAutoCompleteTextView multiAutoCompleteTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.textview_layout);
		init();
	}

	private void init() {
		final String[] strings = new String[] { "abc", "abd", "abe", "bcd",
				"bce", "bcf" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, strings);

		textView = (TextView) findViewById(R.id.my_textview);
		editText = (EditText) findViewById(R.id.my_edittext);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.my_autocompletetextview);
		checkedTextView = (CheckedTextView) findViewById(R.id.my_checkedtextview);
		multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.my_multiautocompletetextview);

		checkedTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkedTextView.toggle();
				if (checkedTextView.isChecked()) {
					checkedTextView.setText("Checked");

				} else {
					checkedTextView.setText("Unchecked");
				}
			}
		});
		
		autoCompleteTextView.setAdapter(adapter);

		multiAutoCompleteTextView.setAdapter(adapter);
		multiAutoCompleteTextView
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}
}
