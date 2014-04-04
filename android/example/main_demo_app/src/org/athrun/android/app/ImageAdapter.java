package org.athrun.android.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;

	private Integer[] images = { R.drawable.gallery_01, R.drawable.gallery_02,
			R.drawable.gallery_03, R.drawable.gallery_04,
			R.drawable.gallery_05, R.drawable.gallery_06,
			R.drawable.gallery_07, R.drawable.gallery_08,
			R.drawable.gallery_09, R.drawable.gallery_10,
			R.drawable.gallery_11, R.drawable.gallery_12,
			R.drawable.gallery_13, R.drawable.gallery_14,
			R.drawable.gallery_15, R.drawable.gallery_16,
			R.drawable.gallery_17, R.drawable.gallery_18,
			R.drawable.gallery_19, R.drawable.gallery_20,
			R.drawable.gallery_21, R.drawable.gallery_22,
			R.drawable.gallery_23, R.drawable.gallery_24 };

	public ImageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return context.getResources().getDrawable(images[position]);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(context);
		i.setImageResource(images[position]);
		i.setLayoutParams(new Gallery.LayoutParams(256, 256));
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		return i;
	}

}
