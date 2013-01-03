package com.phoenix.fitness;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SpecificExercise extends Activity implements OnClickListener{

	String Workout_name,date;
	int minutes=0,Calories=0, today_date,today_month,today_year;
	String[] intensity_array={"Low","High"};
	String[] speed_array={"5 mph(8 kph)","6 mph (9.6 kph)","10 mph (16.09 kph)"};
	String[] duration_array={ "2 min","5 min","10 min","15 min","20 min","25 min","30 min","45 min","50 min","60 min" };
	TextView exercise,calories_burned;
	
	Spinner intensity,duration;
	Button calculate_calories,addEntry,Cancel;
	ImageView home;
	Dialog calculate_calories_dialog;
	
	Float MET;
	CalorieDatabase cd;
	
	ArrayAdapter<String> intensity_adapter,duration_adapter;
	
	SharedPreferences userdetails;
	Calendar now;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specificexercise);
		
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		
		Bundle getWorkout=getIntent().getExtras();
		Workout_name=getWorkout.getString("workout");
		
		cd=new CalorieDatabase(this);
		
		exercise=(TextView) findViewById(R.id.textView_exercise);
		intensity=(Spinner) findViewById(R.id.spinner_intensity);
		duration=(Spinner) findViewById(R.id.spinner_duration);
		calculate_calories=(Button) findViewById(R.id.button_calculatecalories);
		home=(ImageView) findViewById(R.id.imageView3);
		
		exercise.setText(Workout_name);
		
		duration_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,duration_array);
		duration_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		duration.setAdapter(duration_adapter);
		
		if(Workout_name.equals("Running(Treadmill)"))
		  {
				intensity_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,speed_array);
				intensity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				intensity.setAdapter(intensity_adapter);
		  }
		else {
			intensity_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,intensity_array);
			intensity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			intensity.setAdapter(intensity_adapter);
		}
		
		now=Calendar.getInstance();
		today_date=now.get(Calendar.DAY_OF_MONTH);
		today_month=now.get(Calendar.MONTH);
		today_year=now.get(Calendar.YEAR);
		date=today_date+"-"+(today_month+1)+"-"+today_year;
		
		calculate_calories.setOnClickListener(this);
		home.setOnClickListener(this);
		
	}

	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.button_calculatecalories:
			
			if(Workout_name.equals("Aerobics"))
			{
				if(intensity.getSelectedItem().equals("Low"))
					MET=(float) 0.044;
				else
					MET=(float) 0.056;
				
			}
			else if(Workout_name.equals("Bicycling(Gym)"))
			{
				if(intensity.getSelectedItem().equals("Low"))
					MET=(float)0.056;
				else
					MET=(float)0.084;	
					
				
			}
			else if(Workout_name.equals("General Exercise"))
			{
				if(intensity.getSelectedItem().equals("Low"))
					MET=(float)0.056;
				else
					MET=(float)0.080;	
			}
			else if(Workout_name.equals("Running(Treadmill)"))
			{
				if(intensity.getSelectedItem().equals("5 mph(8 kph)"))
					MET=(float)0.064;
				else if(intensity.getSelectedItem().equals("6 mph (9.6 kph)"))
					MET=(float)0.080;	
				else if(intensity.getSelectedItem().equals("10 mph (16.09 kph)"))
					MET=(float)0.132;
			}
			else if(Workout_name.equals("Stair Step Machine"))
			{
				if(intensity.getSelectedItem().equals("Low"))
					MET=(float)0.048;
				else
					MET=(float)0.064;	
			}
			else if(Workout_name.equals("Weight Lifting"))
			{
				if(intensity.getSelectedItem().equals("Low"))
					MET=(float)0.024;
				else
					MET=(float)0.048;	
			}
			
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
			ShowCalorieDialog();
			
			break;
			
		case R.id.button_addentry:
			cd.open();
			cd.Insert(date, Calories, Workout_name);
			cd.close();
			calculate_calories_dialog.dismiss();
			break;
		case R.id.button_cancel:
			calculate_calories_dialog.dismiss();
			break;
		case R.id.imageView3:
			Intent toHome=new Intent(SpecificExercise.this,FitnessnMotionActivity.class);
			startActivity(toHome);
			finish();
			break;
		}
		
	}
	
	public void ShowCalorieDialog()
	{
		String weight_String;
		
		weight_String=userdetails.getString("user_weight", "");
		Calories=(int) (MET * Integer.parseInt(weight_String.trim())*2.204 * minutes);
		
		calculate_calories_dialog=new Dialog(this);
		calculate_calories_dialog.setContentView(R.layout.calculate_calories_dialog);
		calculate_calories_dialog.setTitle("Calories");
		calculate_calories_dialog.setCancelable(true);
		calculate_calories_dialog.show();
		
		addEntry=(Button)calculate_calories_dialog.findViewById(R.id.button_addentry);
		Cancel=(Button)calculate_calories_dialog.findViewById(R.id.button_cancel);
		calories_burned=(TextView)calculate_calories_dialog.findViewById(R.id.textView_calories);
		calories_burned.setText(Calories+"Cal");
		
		addEntry.setOnClickListener(this);
		Cancel.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toGym=new Intent(this,GymWorkout.class);
		startActivity(toGym);
	}
	
}
