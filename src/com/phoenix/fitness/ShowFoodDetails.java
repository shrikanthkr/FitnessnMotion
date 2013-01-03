package com.phoenix.fitness;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class ShowFoodDetails extends Activity implements OnClickListener{

	TextView foodname_textView,carbo_textView,protein_textView,satfat_textView,totfat_textView,
			 fatfood_textView,category_textView,calories_textView;
	Button button_add_food;
	ImageView home;
	EditText no_of_servings;
	
	FoodDatabase fd;
	FoodConsumptionDatabase fcd;
	Calendar now;
	int today_date,today_month,today_year,servings;
	
	String food_name,food_string;
	String[] food_name_array;
	
	SharedPreferences userdetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showfooddetails);
		
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		
		Bundle getb=getIntent().getExtras();
		food_name=getb.getString("food_name");
		Initialise();
		
		fd.open();
		food_string=fd.getFoodDetails(food_name);
		fd.close();
		try
		{
			food_name_array=food_string.split("/");
		
			foodname_textView.setText(food_name_array[0]);
			carbo_textView.setText(food_name_array[1]+" g");
			protein_textView.setText(food_name_array[2]+" g");
			satfat_textView.setText(food_name_array[3]+" g");
			totfat_textView.setText(food_name_array[4]+" g");
			fatfood_textView.setText(food_name_array[5]+" g");
			category_textView.setText(food_name_array[6]+" Food");
			calories_textView.setText(food_name_array[7]+"Cal");
		}
		catch(Exception e){
			
		}
		
	}
	
	public void Initialise() {
		
		fd=new FoodDatabase(this);
		fcd=new FoodConsumptionDatabase(this);
		foodname_textView=(TextView) findViewById(R.id.foodname_textView);
		carbo_textView=(TextView) findViewById(R.id.carbo_textView);
		protein_textView=(TextView) findViewById(R.id.protein_textView);
		satfat_textView=(TextView) findViewById(R.id.satfat_textView);
		totfat_textView=(TextView) findViewById(R.id.totfat_textView);
		fatfood_textView=(TextView) findViewById(R.id.fatfromfood_textView);
		category_textView=(TextView) findViewById(R.id.category_textView);
		calories_textView=(TextView) findViewById(R.id.calories_textView);
		no_of_servings=(EditText) findViewById(R.id.editText_servings);
		
		no_of_servings.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		button_add_food=(Button) findViewById(R.id.button_add_food);
		home=(ImageView) findViewById(R.id.imageView3);
		
		button_add_food.setOnClickListener(this);
		home.setOnClickListener(this);
		
		now=Calendar.getInstance();
		today_date=now.get(Calendar.DAY_OF_MONTH);
		today_month=now.get(Calendar.MONTH);
		today_year=now.get(Calendar.YEAR);
		
	}

	public void onClick(View arg0) {
		
		switch(arg0.getId())
		{
		case R.id.button_add_food:
			servings=Integer.parseInt(no_of_servings.getText().toString().trim());
			if(CheckCalorieIntake(food_name_array[7]))
			{
			fcd.open();
			fcd.Insert(food_name_array[0], today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(food_name_array[5]), servings*Double.parseDouble(food_name_array[7]));
			fcd.close();
			Intent toDaily=new Intent(ShowFoodDetails.this,DailyMonitor.class);
			startActivity(toDaily);
			finish();
			}
			else
			{
				ShowAlertDialog();
			}
			break;
		case R.id.imageView3:
			Intent toHome=new Intent(ShowFoodDetails.this,FitnessnMotionActivity.class);
			startActivity(toHome);
			finish();
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toDaily=new Intent(this,DailyMonitor.class);
		startActivity(toDaily);
	}
	

	private boolean CheckCalorieIntake(String calorie_intake) {
	
		String[] consumedCalorie;
		String date;
		int calorieConsumed=0,max_calorie;
		Boolean result;
		date=today_date+"-"+(today_month+1)+"-"+today_year;
		fcd.open();
		consumedCalorie=new String[fcd.retrieveCaloriesByDate(date).split("\n").length];
		consumedCalorie=fcd.retrieveCaloriesByDate(date).split("\n");
		fcd.close();

		try{
			
			for(int j=0;j<consumedCalorie.length;j++)
				calorieConsumed=calorieConsumed+Integer.parseInt(consumedCalorie[j]);
			}catch(Exception e){
				e.printStackTrace();
			}
		
		if(userdetails.getString("user_gender", "").equals("Male")) 
			max_calorie=2550;
		else 
			max_calorie=1940;
		
		if(calorieConsumed+(servings*Double.parseDouble(calorie_intake))<=max_calorie)
		{
			result=true;
		}
		else
		{
			result=false;
		}
			
		return result;
	}

	private void ShowAlertDialog() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
		builder.setMessage("Your Intake is High.Do you still want to continue?")
	           .setCancelable(true).setTitle("IntelliDiet")
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick( final DialogInterface dialog, final int id) {
	            	
	            	fcd.open();
	       			fcd.Insert(food_name_array[0], today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(food_name_array[5]), servings*Double.parseDouble(food_name_array[7]));
	       			fcd.close();
	       			Intent toDaily=new Intent(ShowFoodDetails.this,DailyMonitor.class);
	       			startActivity(toDaily);
	       			finish();
	            	   
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                    dialog.cancel();
	               }
	           });
		
		final AlertDialog alert = builder.create();
	    alert.show();
	}
}
