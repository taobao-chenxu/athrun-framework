package org.athrun.android.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListViewActivity extends Activity {
	private static final String LOG_TAG = "ListViewActivity";
	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listview_layout);

		listView = (ListView) findViewById(R.id.my_listview);
		
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 100; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemimage", R.drawable.icon);
			map.put("itemTitle", "Item" + i);
			map.put("itemText", "Thank you!");
			listitem.add(map);
		}

		SimpleAdapter listitemAdapter = new SimpleAdapter(this, listitem,
				R.layout.listview_item_layout, new String[] { "itemimage",
						"itemTitle", "itemText" }, new int[] { R.id.ItemImage,
						R.id.ItemTitle, R.id.ItemText });

		listView.setAdapter(listitemAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(ListViewActivity.this, "Item " + position + " clicked!", Toast.LENGTH_SHORT).show();
				Log.i(LOG_TAG, "last" + listView.getLastVisiblePosition());
				Log.i(LOG_TAG, "first" + listView.getFirstVisiblePosition());
				Log.i(LOG_TAG, "max" + (listView.getLastVisiblePosition() - listView.getFirstVisiblePosition()));
				Log.i(LOG_TAG, parent.getContext().getClass().getName());
				Log.i(LOG_TAG, parent.getClass().getSuperclass().getSimpleName());
			}

		});

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("Hello");
				menu.setHeaderIcon(R.drawable.gridview10);
				menu.add(0, 0, 0, "Item One");
				menu.add(0, 1, 0, "Item Two");
			}
		});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Toast.makeText(ListViewActivity.this, "" + (item.getItemId()+ 1) + " pressed!", Toast.LENGTH_SHORT).show();
		return super.onContextItemSelected(item);
	}
	
}
