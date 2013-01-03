package com.phoenix.fitness;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SportsAdapter extends BaseAdapter{

	private Activity activity;
	String[] icon_array,activity_array;
	private LayoutInflater inflater=null;
	
	public SportsAdapter(SportsActivity activity,String[] icon_array,String[] activity_array)
	{
		this.activity=activity;
		this.icon_array=icon_array;
		this.activity_array=activity_array;
		
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return icon_array.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public class ViewHolder
	{
		public ImageView icon;
		public TextView exercise;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View vi;
		final ViewHolder holder;
		
		
		vi=inflater.inflate(R.layout.list_gym_item, null);
		holder=new ViewHolder();
		
		holder.icon=(ImageView) vi.findViewById(R.id.exercise_icon);
		holder.exercise=(TextView) vi.findViewById(R.id.exercise_name);
		vi.setTag(holder);
		
		AssetManager assetsmgr=activity.getAssets();
		try {
			InputStream in=assetsmgr.open(icon_array[arg0]);
			Bitmap bitmap= BitmapFactory.decodeStream(in);
			holder.icon.setImageBitmap(bitmap);
			in.close();
			} catch (IOException e) {
				e.printStackTrace();
		}

		holder.exercise.setText(activity_array[arg0]);
		
		return vi;
	}

}
