package com.phoenix.fitness;

import java.text.DecimalFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HealthAdvisor extends Activity implements OnClickListener{

	String date;
	Calendar now;
	String[] activitycalorie;
	String[] consumedCalorie;
	CalorieDatabase cd;
	FoodConsumptionDatabase fcd;
	int calorieSpent=0;
	int calorieConsumed=0;
	TextView caloriesConsumed_textView,caloriesSpent_textView,netCalories_textView,
				remainingCalories_textView,bmi_textview,summaryhealth_textview;
	Button healthtip_button,dialog_ok;
	ImageView home;
	Dialog healthtip_dialog;
	SharedPreferences userdetails;
	SharedPreferences.Editor userdetails_edit;
	String weight,height,age;
	Double weight_in_kg,height_in_cm,height_in_metres;
	Double BMI;
	int bmiflag=1,max_calorie=0,net_calorie=0;
	int modeflag=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthadvisor);
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		init();
	}


	private void setModeFlag() {
		
		if(userdetails.getString("user_mode", "")=="Simple") modeflag=1;
		if(userdetails.getString("user_mode", "")=="Moderate") modeflag=2;
		if(userdetails.getString("user_mode", "")=="Strict") modeflag=3;
		
	}


	private void init() {
		
		
		caloriesConsumed_textView=(TextView)findViewById(R.id.textview_consumedcalories);
		caloriesSpent_textView=(TextView)findViewById(R.id.textview_spentCalories);
		netCalories_textView=(TextView)findViewById(R.id.netcalories_textview);
		remainingCalories_textView=(TextView)findViewById(R.id.textview_remainingcalories);
		healthtip_button=(Button)findViewById(R.id.button_healthtip);
		home=(ImageView) findViewById(R.id.imageView3);
		
		now=Calendar.getInstance();
		date=now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH)+1) + "-" + now.get(Calendar.YEAR);
				
		cd=new CalorieDatabase(HealthAdvisor.this);
		fcd=new FoodConsumptionDatabase(this);
		
		cd.open();
		
		activitycalorie =new String[cd.getCalorieDetailsByDate(date).split("\n").length];
		activitycalorie=cd.getCalorieDetailsByDate(date).split("\n");
		
		cd.close();
		
		fcd.open();
		
		consumedCalorie=new String[fcd.retrieveCaloriesByDate(date).split("\n").length];
		consumedCalorie=fcd.retrieveCaloriesByDate(date).split("\n");
		
		fcd.close();
		try{
		for(int i=0; i<activitycalorie.length; i++){
			calorieSpent=calorieSpent+Integer.parseInt(activitycalorie[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			
		for(int j=0;j<consumedCalorie.length;j++)
			calorieConsumed=calorieConsumed+Integer.parseInt(consumedCalorie[j]);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		caloriesConsumed_textView.setText(calorieConsumed+"");
		caloriesSpent_textView.setText(calorieSpent+"");
		netCalories_textView.setText(calorieConsumed-calorieSpent+"");
		
		if(userdetails.getString("user_gender", "").equals("Male")) 
			max_calorie=2550;
		else 
			max_calorie=1940;
		
		net_calorie=calorieConsumed-calorieSpent;
		remainingCalories_textView.setText(max_calorie-calorieConsumed+"");
		healthtip_button.setOnClickListener(this);
		home.setOnClickListener(this);
		
	}


	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.button_healthtip:
		healthtip_dialog=new Dialog(this);
		healthtip_dialog.setContentView(R.layout.healtip_dialog);
		healthtip_dialog.show();
		healthtip_dialog.setCancelable(true);
		healthtip_dialog.setTitle("Your Health tip today");
		bmi_textview=(TextView)healthtip_dialog.findViewById(R.id.textview_bmi);
		summaryhealth_textview=(TextView)healthtip_dialog.findViewById(R.id.textview_summaryofhealthtip);
		dialog_ok=(Button)healthtip_dialog.findViewById(R.id.button_ok_dialog);
		dialog_ok.setOnClickListener(this);
		calculateBMI();
		break;
		
		case R.id.button_ok_dialog:
			healthtip_dialog.dismiss();
			break;
		case R.id.imageView3:
			Intent toHome=new Intent(HealthAdvisor.this,FitnessnMotionActivity.class);
			startActivity(toHome);
			finish();
			break;
		}


		
	}
		private void calculateBMI() {

		setModeFlag();
		weight=userdetails.getString("user_weight", "");
		height=userdetails.getString("user_height", "");
		age=userdetails.getString("user_age", "");
		
				weight_in_kg=Double.parseDouble(weight);
				height_in_cm=Double.parseDouble(height);
			
				try
				{
			height_in_metres=(height_in_cm/100);
			BMI= (weight_in_kg/(height_in_metres*height_in_metres));
			DecimalFormat twoDForm = new DecimalFormat("###.##");
			
			
			
			if(BMI<18.5){
				bmi_textview.setText(twoDForm.format(BMI)+"");
				bmiflag=1;
			}
			else if(BMI >=18.5 & BMI <=25){
				bmi_textview.setText(twoDForm.format(BMI)+"");
				bmiflag=2;
			}
			else if(BMI>25 & BMI<=30){
				bmi_textview.setText(twoDForm.format(BMI)+"");
				bmiflag=3;
			}
			else if(BMI>30){
				bmi_textview.setText(twoDForm.format(BMI)+"");
				bmiflag=4;
			}
			
			setSummary();
		}
	catch(Exception e){}
	}


	private void setSummary() {
	
		switch(bmiflag){
			case 1:
			switch(modeflag){
				case 1:
				if(net_calorie>=250 & net_calorie<500)
					summaryhealth_textview.setText("You're right on track! Keep that up and you'll be macho in no time!");
				else if(net_calorie>500)
					summaryhealth_textview.setText("Your net intake is high. Cut down a few calories!");
				else if(net_calorie<0)
					summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy. Eat up");
				else
					summaryhealth_textview.setText("Your net intake is low.Come on, Eat up!");
				
				break;
				
				case 2:
				if(net_calorie>=500 & net_calorie<750)
					summaryhealth_textview.setText("You're right on track! Keep that up and you'll be macho in no time!");
				else if(net_calorie>750)
				    summaryhealth_textview.setText("Your net intake is high. Cut down a few calories!");
				else if(net_calorie<0)
					summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy. Eat up");
				else
				    summaryhealth_textview.setText("Your net intake is low.Come on, Eat up!");
			
				break;
				
				case 3:
				if(net_calorie>=750 & net_calorie<1000)
					summaryhealth_textview.setText("You're right on track! Keep that up and you'll be macho in no time!");
				else if(net_calorie>1000)
					summaryhealth_textview.setText("Your net intake is too high. Cut down a few calories!");
				else if(net_calorie<0)
					summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy. Eat up");
				else
					summaryhealth_textview.setText("Your net intake is low.Come on, Eat up!");
				   
			   break;
			
			}
			break;
			
			case 2:
			switch(modeflag){
				case 1:
				if(net_calorie==0 | net_calorie<=100 & net_calorie>=-100)
					summaryhealth_textview.setText("How do you do that? You're perfect!");
				else if(net_calorie>100)
					summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!!");
				else 
					summaryhealth_textview.setText("Come on, Eat up!");
				
				break;
			
				case 2:
				if(net_calorie==0 | net_calorie<=50 & net_calorie>=-50)
					summaryhealth_textview.setText("How do you do that? You're perfect!");
				else if(net_calorie>50)
					summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!!");
				else 
					summaryhealth_textview.setText("Come on, Eat up!");
				
				break;
		
				case 3:
				if(net_calorie==0 | net_calorie<=25 & net_calorie>=-25)
					summaryhealth_textview.setText("How do you do that? You're perfect!");
				else if(net_calorie>25)
					summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!!");
				else 
					summaryhealth_textview.setText("Come on, Eat up!");
			
				break;
			}
			break;
			
			case 3:
			switch(modeflag){
				case 1:
				if(net_calorie>-100)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-100 & net_calorie>=-250)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
				  
				break;
				case 2:
				if(net_calorie>-250)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-250 & net_calorie>=-500)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
				  
				break;
				case 3:
				if(net_calorie>-500)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-500 & net_calorie>=-750)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
				break;
			}
			break;
			case 4: 
			switch(modeflag){
				case 1:
				if(net_calorie>-250)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-250 & net_calorie>=-500)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
				  
				break;
				case 2:
				if(net_calorie>-500)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-500 & net_calorie>=-1000)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
				 
				break;
				case 3:
				if(net_calorie>-1000)
				  summaryhealth_textview.setText("Please. Stop. Gorging. You're going to blow!! Please work out more, as well.");
				else if(net_calorie<=-1000 & net_calorie>=-1250)
				  summaryhealth_textview.setText("That's awesome! Keep it up. You've managed to cut some flab today.");
				else 
				  summaryhealth_textview.setText("Don't you think you're overexerting yourself? Slow down, buddy.");
			
				break;
		
			}
			break;
	}
	
	
}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toFitness);
	}
	

}
