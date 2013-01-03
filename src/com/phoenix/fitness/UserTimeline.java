package com.phoenix.fitness;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.phoenix.fitness.categoryList.*;

public class UserTimeline extends Activity implements OnClickListener{

	ArrayList<Item> items = new ArrayList<Item>(31);
	ArrayAdapter<String> adapter;
	ListView lv_user;
	ImageView home;
	FoodConsumptionDatabase fcd;
	CalorieDatabase cd;
	String get_consumption,get_calories;
	
	String[] food_all,food_sub,calories_all,calories_sub;
	
	String food_name,food_date,food_fat;
	int i,j,month,month_limit;
	
	int food_calories=0,workout_calories=0;
	
	Calendar cal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usertimeline);
		
		Bundle get_month=getIntent().getExtras();
		month=Integer.parseInt(get_month.getString("month"));
		
		home=(ImageView) findViewById(R.id.imageView3);
		lv_user=(ListView) findViewById(R.id.user_listView);
		
		home.setOnClickListener(this);
		
		fcd=new FoodConsumptionDatabase(this);
		cd=new CalorieDatabase(this);
		
		cal=Calendar.getInstance();
		
		switch(month)
		{
		case 1: month_limit=31; break;
		case 2: month_limit=29;break;
		case 3: month_limit=31;break;
		case 4: month_limit=30;break;
		case 5: month_limit=31;break;
		case 6: month_limit=30;break;
		case 7: month_limit=31;break;
		case 8: month_limit=31;break;
		case 9: month_limit=30;break;
		case 10:month_limit=31;break;
		case 11:month_limit=30;break;
		case 12:month_limit=31;break;
		}
		
		fcd.open();
		cd.open();
		
		for(j=1;j<=month_limit;j++)
		{
			try
			{
				get_consumption=fcd.retrieveConsumptionByDate(j+"-"+month+"-2012");
				get_calories=cd.getCalorieandWorkoutByDate(j+"-"+month+"-2012");
				
				food_all=get_consumption.split("\n");
				calories_all=get_calories.split("\n");
				
					if(!get_consumption.equals(""))
						{
							items.add(new SectionItem(j+"/"+month+"/2012"));
		
								for(i=0;i<food_all.length;i++)
								{
									food_sub=food_all[i].split("/");
									items.add(new EntryItem(food_sub[0],food_sub[3]+" Cal","Consume"));
									food_calories=food_calories+Integer.parseInt(food_sub[3]);
								}
								
								for(i=0;i<calories_all.length;i++)
								{
									calories_sub=calories_all[i].split("/");
									items.add(new EntryItem(calories_sub[0],calories_sub[1]+" Cal","Workout"));
									workout_calories=workout_calories+Integer.parseInt(calories_sub[1]);
								}
								
								items.add(new EntryItem("Net Calories",(food_calories-workout_calories)+"","Net"));
								food_calories=0;
								workout_calories=0;
						 }
					else
					{
						  items.add(new SectionItem(j+"/"+month+"/2012"));
						  items.add(new EntryItem("","",""));
					}
			}catch(Exception e){
				
			}
		}
		cd.close();
		fcd.close();
	
		CustomAdapter adapter=new CustomAdapter(UserTimeline.this,items);
		lv_user.setAdapter(adapter);
		
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toStats=new Intent(this,Stats.class);
		startActivity(toStats);
	}

	public void onClick(View v) {
		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toFitness);
		finish();
		
	}
	

}
