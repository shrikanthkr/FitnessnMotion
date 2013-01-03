package com.phoenix.fitness;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SportsActivity extends Activity implements OnItemClickListener,OnClickListener{

	ListView select_sports;
	SportsAdapter adapter;
	ArrayAdapter<String> duration_adapter;
	
	TextView sport,calories;
	Spinner duration;
	Button calculate_calorie,addEntry,Cancel;
	ImageView home;
	
	CalorieDatabase cd;
	int calories_burned=0,minutes=0,today_date,today_month,today_year;;
	Float MET;
	String sport_name,weight_String,date;
	String[] icon_array={"basketball_icon.png","boxing_icon.png","football_icon.png","golf_icon.png","gymnastics_icon.png","swim_icon.png",
			"tennis_icon.png","volley_icon.png"};
	String[] activity_array={"BasketBall","Boxing","FootBall","Golf","Gymnastics","Swimming","Tennis","VolleyBall"};
	String[] duration_array={ "2 min","5 min","10 min","15 min","20 min","25 min","30 min","45 min","50 min","60 min" };
	
	Dialog sportsdialog;
	SharedPreferences userdetails;
	Calendar now;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sports);
		
		cd=new CalorieDatabase(this);
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		adapter=new SportsAdapter(this,icon_array,activity_array);
		select_sports=(ListView) findViewById(R.id.listView_selectsport);
		home=(ImageView) findViewById(R.id.imageView3);
		home.setOnClickListener(this);
		
		select_sports.setAdapter(adapter);
		select_sports.setOnItemClickListener(this);
		weight_String=userdetails.getString("user_weight", "");
		
		duration_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,duration_array);
		duration_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		now=Calendar.getInstance();
		today_date=now.get(Calendar.DAY_OF_MONTH);
		today_month=now.get(Calendar.MONTH);
		today_year=now.get(Calendar.YEAR);
		date=today_date+"-"+(today_month+1)+"-"+today_year;
		
		
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		sport_name=activity_array[arg2];
		ShowDialog();
	}
	
	public void ShowDialog()
	{
		
		sportsdialog=new Dialog(this);
		sportsdialog.setContentView(R.layout.sportsdialog);
		sportsdialog.setTitle("Sport");
		sportsdialog.setCancelable(true);
		sportsdialog.show();
		
		sport=(TextView)sportsdialog.findViewById(R.id.textView_sports);
		calories=(TextView)sportsdialog.findViewById(R.id.textView_calories);
		duration=(Spinner)sportsdialog.findViewById(R.id.spinner_duration);
		calculate_calorie=(Button)sportsdialog.findViewById(R.id.button_calculate);
		addEntry=(Button)sportsdialog.findViewById(R.id.button_addEntry);
		Cancel=(Button)sportsdialog.findViewById(R.id.button_cancel);
		
		duration.setAdapter(duration_adapter);
		calculate_calorie.setOnClickListener(this);
		addEntry.setOnClickListener(this);
		Cancel.setOnClickListener(this);
		
		sport.setText(sport_name);
		
		Calculate();
		
		calories.setText(calories_burned+"");
		
	}
	
	private void Calculate() {
		
		if(sport_name.equals("BasketBall"))
			MET=(float)0.064;
		else if(sport_name.equals("Boxing"))
			MET=(float)0.072;
		else if(sport_name.equals("FootBall"))
			MET=(float)0.072;
		else if(sport_name.equals("Golf"))
			MET=(float)0.044;
		else if(sport_name.equals("Gymnastics"))
			MET=(float)0.032;
		else if(sport_name.equals("Swimming"))
			MET=(float)0.048;
		else if(sport_name.equals("Tennis"))
			MET=(float)0.056;
		else if(sport_name.equals("VolleyBall"))
			MET=(float)0.032;
		
		
		if(duration.getSelectedItem().equals("2 min"))
			minutes=2;
		else if(duration.getSelectedItem().equals("5 min"))
			minutes=5;
		else if(duration.getSelectedItem().equals("10 min"))
			minutes=10;
		else if(duration.getSelectedItem().equals("15 min"))
			minutes=15;
		else if(duration.getSelectedItem().equals("20 min"))
			minutes=20;
		else if(duration.getSelectedItem().equals("25 min"))
			minutes=25;
		else if(duration.getSelectedItem().equals("30 min"))
			minutes=30;
		else if(duration.getSelectedItem().equals("45 min"))
			minutes=45;
		else if(duration.getSelectedItem().equals("50 min"))
			minutes=50;
		else if(duration.getSelectedItem().equals("60 min"))
			minutes=60;
		
		calories_burned=(int) (MET * Integer.parseInt(weight_String.trim())*2.204 * minutes);
		
	}
	
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.button_addEntry:
			Calculate();
			calories_burned=(int) (MET * Integer.parseInt(weight_String.trim())*2.204 * minutes);
			cd.open();
			cd.Insert(date, calories_burned, sport_name);
			cd.close();
			sportsdialog.dismiss();
			break;
		case R.id.button_cancel:
			sportsdialog.dismiss();
			break;
		case R.id.button_calculate:
			Calculate();
			calories.setText(calories_burned+"");
			break;
		case R.id.imageView3:
			Intent toHome=new Intent(SportsActivity.this,FitnessnMotionActivity.class);
			startActivity(toHome);
			finish();
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toSelect=new Intent(this,SelectWorkout.class);
		startActivity(toSelect);
	}
	

}
