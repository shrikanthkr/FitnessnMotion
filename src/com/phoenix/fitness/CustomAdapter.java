package com.phoenix.fitness;

/*CustomAdpater Class extends BaseAdapter.SO it Implements all Functions of the BaseAdapter.
 * This is used to Set the layout of a Custom List and also Populate it with multiple values 
 * */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.phoenix.fitness.categoryList.EntryItem;
import com.phoenix.fitness.categoryList.Item;
import com.phoenix.fitness.categoryList.SectionItem;

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

public class CustomAdapter extends BaseAdapter{

	private Activity activity;
	private ArrayList<Item> items;
	private LayoutInflater inflater=null;
		
	CustomAdapter(UserTimeline userTimeline, ArrayList<Item> item)
		{
			activity=userTimeline;
			items=item;
			
			inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	public int getCount() {
		
		return items.size();
	}

	public Object getItem(int arg0) {
		
		return arg0;
	}

	public long getItemId(int arg0) {
		
		return arg0;
	}
	
	public class ViewHolder
	{
		public TextView name;
		public TextView no_of_calories;
		public ImageView icon;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		View vi = arg1;
		final ViewHolder holder;
		Bitmap bitmap = null,bmp = null;
		
		Item i = items.get(arg0);
		
		if (i != null) {
			if(i.isSection()){
				
				SectionItem si = (SectionItem)i;
				vi=inflater.inflate(R.layout.list_item_section, null);
				holder=new ViewHolder();
				
				holder.name = (TextView) vi.findViewById(R.id.list_item_section_text);
				
				vi.setOnClickListener(null);
				vi.setOnLongClickListener(null);
				vi.setLongClickable(true);
				vi.setTag(holder);
				holder.name.setText(si.getTitle());
				
			}
			else
			{
				EntryItem ei = (EntryItem)i;
				vi = inflater.inflate(R.layout.list_item_entry, null);
				holder=new ViewHolder();
				
				holder.name = (TextView)vi.findViewById(R.id.list_item_entry_title);
				holder.no_of_calories = (TextView)vi.findViewById(R.id.list_item_entry_summary);
				holder.icon=(ImageView)vi.findViewById(R.id.list_item_entry_drawable);
				
				AssetManager assetsmgr=activity.getAssets();
				try {
					InputStream in=assetsmgr.open("food_consume_icon.png");
					bitmap= BitmapFactory.decodeStream(in);
					in.close();
					
					InputStream ins=assetsmgr.open("runner_icon.png");
					bmp=BitmapFactory.decodeStream(ins);
					ins.close();
					} catch (IOException e) {
						e.printStackTrace();
			}
				

				if (holder.name != null) 
					holder.name.setText(ei.title);
				if(holder.no_of_calories != null)
					holder.no_of_calories.setText(ei.subtitle);
				vi.setTag(holder);
			
				if(ei.category.equals("Consume"))
					holder.icon.setImageBitmap(bitmap);
				else if(ei.category.equals("Workout"))
					holder.icon.setImageBitmap(bmp);
			}
		
		}
		
		return vi;
	}

}
