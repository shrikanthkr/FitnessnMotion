package com.phoenix.fitness;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter{

	private Activity activity;
	ArrayList<String> food_name_array,calories_array;
	private LayoutInflater inflater=null;
	
	public LazyAdapter(DailyMonitor dailymonitor,ArrayList<String> food_array,ArrayList<String> calories_array)
	{
		activity=dailymonitor;
		food_name_array=food_array;
		this.calories_array=calories_array;
		
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return food_name_array.size();
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
		public TextView food_name;
		public TextView no_of_calories;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View vi;
		final ViewHolder holder;
		
		vi=inflater.inflate(R.layout.list_item, null);
		holder=new ViewHolder();
		
		holder.food_name=(TextView) vi.findViewById(R.id.food_name);
		holder.no_of_calories=(TextView) vi.findViewById(R.id.no_of_calories);
		vi.setTag(holder);
		
		holder.food_name.setText(food_name_array.get(arg0));
		if(!calories_array.get(arg0).equals(""))
			holder.no_of_calories.setText(calories_array.get(arg0)+" kcal");
		else
			holder.no_of_calories.setText(calories_array.get(arg0));
		return vi;
	}

}
