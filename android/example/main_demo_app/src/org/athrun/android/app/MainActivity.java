package org.athrun.android.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author bingyang.djj
 *
 */
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Button btn_anotherapp;
	private Button btn_textview;
	private Button btn_textviewtree;
	private Button btn_gridview;
	private Button btn_scrollview;
	private Button btn_webview;
	private Button btn_listview;
	private Button btn_sametextview;
	private View includeView;
	private CheckBox checkBox;
	private TextView textView;
	private Gallery gallery;
	private ImageView imageView;
	//add by chenxu
	private Button btn_tabactivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		init();
		setListeners();
	}

	private void init() {
		imageView = (ImageView) findViewById(R.id.my_imageview);
		btn_anotherapp = (Button) findViewById(R.id.btn_another_app);
		btn_textview = (Button) findViewById(R.id.btn_textview_activity);
		btn_textviewtree = (Button) findViewById(R.id.btn_textviewtree_activity);
		btn_gridview = (Button) findViewById(R.id.btn_gridview_activity);
		btn_scrollview = (Button) findViewById(R.id.btn_scrollview_activity);
		btn_webview = (Button) findViewById(R.id.btn_webview_activity);
		btn_listview = (Button) findViewById(R.id.btn_listview_activity);
		btn_sametextview = (Button) findViewById(R.id.btn_sametext_activity);
		
		includeView = findViewById(R.id.include_checkbox);
		checkBox = (CheckBox) includeView.findViewById(R.id.my_checkbox);
		textView = (TextView) includeView.findViewById(R.id.textview);
		
		gallery = (Gallery) findViewById(R.id.my_gallery);
		gallery.setAdapter(new ImageAdapter(this));
		// add by chenxu
		btn_tabactivity = (Button) findViewById(R.id.btn_tabs_activity);
	}

	private void setListeners() {
		imageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(MainActivity.this, "LongClick", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
			}
		});
		
		btn_anotherapp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("org.athrun.android.app2", "org.athrun.android.app2.CrossAppActivity"));
				startActivity(intent);
			}
		});
		
		setOnListener(btn_textview, TextViewActivity.class);
		setOnListener(btn_textviewtree, TextViewTreeActivity.class);
		setOnListener(btn_gridview, GridViewActivity.class);
		setOnListener(btn_scrollview, ScrollActivity.class);
		setOnListener(btn_webview, WebViewActivity.class);
		setOnListener(btn_listview, ListViewActivity.class);
		setOnListener(btn_sametextview, SameTextActivity.class);
		//add by chenxu
		setOnListener(btn_tabactivity, MyTabActivity.class);
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					textView.setText("CheckBox is checked!");
					
				} else {
					textView.setText("default text");
				}
			}
		});
		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}

	private void startActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, cls);
		startActivity(intent);
	}

	private void setOnListener(View view, final Class<?> cls) {
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(cls);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, Menu.NONE, "Dialog");
		menu.add(Menu.NONE, 2, Menu.NONE, "Toast");
		menu.add(Menu.NONE, 3, Menu.NONE, "Dialog With List");
		menu.add(Menu.NONE, 4, Menu.NONE, "ViewGroup");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			createDialog();
			break;

		case 2:
			Toast.makeText(MainActivity.this, "Hello World", Toast.LENGTH_SHORT)
					.show();
			break;

		case 3:
			createDialogWithList();
			break;
			
		case 4:
			startActivity(ViewGroupActivity.class);

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void createDialog() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("AlartDialog");

		dialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "OK is pressed!", Toast.LENGTH_SHORT).show();
			}
		});

		dialog.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Cancel is pressed!", Toast.LENGTH_SHORT).show();
			}
		});

		dialog.create();
		dialog.show();
	}

	private View getViewFromLayout(Context context, int layout) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(layout, null);
		return view;
	}

	private ListView initListView(ListView listView) {
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemimage", R.drawable.icon);
			map.put("itemTitle", "Item" + i);
			map.put("itemText", "Thank you!");
			listitem.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitem,
				R.layout.listview_item_layout, new String[] { "itemimage",
						"itemTitle", "itemText" }, new int[] { R.id.ItemImage,
						R.id.ItemTitle, R.id.ItemText });
		listView.setAdapter(simpleAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this, "Item " + position + " clicked!", Toast.LENGTH_SHORT).show();
			}
		});
		return listView;
	}

	private void createDialogWithList() {
		Builder dialog = new AlertDialog.Builder(this);
		View view = getViewFromLayout(this, R.layout.listview_layout);
		initListView((ListView) view.findViewById(R.id.my_listview));
		dialog.setView(view);
		dialog.setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Botton OK in dialog with list is pressed!", Toast.LENGTH_SHORT).show();
			}
		});
		dialog.create();
		dialog.show();
	}
}