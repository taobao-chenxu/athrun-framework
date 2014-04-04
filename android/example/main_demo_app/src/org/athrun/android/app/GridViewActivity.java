package org.athrun.android.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GridViewActivity extends Activity {
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gridview_layout);
		init();
		setOnListener();
	}

	private int[] images = { R.drawable.gridview01, R.drawable.gridview02,
			R.drawable.gridview03, R.drawable.gridview04,
			R.drawable.gridview05, R.drawable.gridview06,
			R.drawable.gridview07, R.drawable.gridview08,
			R.drawable.gridview09, R.drawable.gridview10,
			R.drawable.gridview11, R.drawable.gridview12,
			R.drawable.gridview13, R.drawable.gridview14,
			R.drawable.gridview15, R.drawable.gridview16,
			R.drawable.gridview17, R.drawable.gridview18,
			R.drawable.gridview19, R.drawable.gridview20 };

	private void init() {
		gridView = (GridView) findViewById(R.id.my_gridview);

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> gridMap = new HashMap<String, Object>();
			gridMap.put("GridImage", images[i]);
			gridMap.put("GridText", "Item." + String.valueOf(i));
			lstImageItem.add(gridMap);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, lstImageItem,
				R.layout.gridview_item_layout, new String[] { "GridImage",
						"GridText" }, new int[] { R.id.gridviewitem_image,
						R.id.gridviewitem_text });
		
		gridView.setAdapter(adapter);
	}
	
	private void setOnListener() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(GridViewActivity.this, "Item " + position + " is clicked!", Toast.LENGTH_SHORT).show();
				switch (position) {
				case 0:
					startActivity(ListViewActivity.class);
					break;
					
				case 1:
					startActivity(ScrollActivity.class);
					break;
					
				case 2:
					startActivity(TextViewActivity.class);
					break;
					
				case 3:
					startActivity(TextViewTreeActivity.class);
					
				case 19:
					startActivity(ListViewActivity.class);
				
				default:
					break;
				}
			}
		});
	}
	
	private void startActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(GridViewActivity.this, cls);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		SubMenu zero = menu.addSubMenu("Menu 0");
		SubMenu one = menu.addSubMenu("Menu 1");
		
		zero.add(0, 1, 0, "SubMenu 0");
		zero.add(0, 2, 0, "SubMenu 1");
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			startActivity(ListViewActivity.class);
			break;
		
		case 2:
			startActivity(MainActivity.class);

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
