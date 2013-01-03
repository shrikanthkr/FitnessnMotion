package com.phoenix.fitness;

//Calculate BMI ratio for the user

import java.text.DecimalFormat;
import android.app.Activity;
import android.app.Dialog;
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

public class BMICalculator extends Activity implements OnClickListener{

	//Initializing Views
	
	SharedPreferences userdetails;
	SharedPreferences.Editor userdetails_edit;
	
	String weight,height,age,user;
	Double weight_in_kg,height_in_cm,height_in_metres;
	Double BMI;
	
	TextView bmi_textView,age_textView,height_textView,weight_textView,bmirange_textView,user_textView;
	EditText age_editText,weight_editText,height_editText;
	Button update,save,cancel;
	ImageView home;
	Dialog update_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmicalculator);
		
		//Getting User Details From SharedPreferences
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		
		bmi_textView=(TextView) findViewById(R.id.bmi_textView);
		age_textView=(TextView) findViewById(R.id.age_textView);
		height_textView=(TextView) findViewById(R.id.height_textView);
		weight_textView=(TextView) findViewById(R.id.weight_textView);
		bmirange_textView=(TextView) findViewById(R.id.bmirange_textView);
		user_textView=(TextView) findViewById(R.id.textView_user);
		home=(ImageView) findViewById(R.id.imageView3);
		
		home.setOnClickListener(this);
		
		update=(Button) findViewById(R.id.button_update);
		
		update.setOnClickListener(this);
		
		calculateBMI();			//Calculate BMI ratio Function
		
		}
	private void calculateBMI() {
		
		try
		{
			//get User Details from SharedPreferences
			
			weight=userdetails.getString("user_weight", "");
			height=userdetails.getString("user_height", "");
			age=userdetails.getString("user_age", "");
			user=userdetails.getString("user_name", "");
				try
				{
					
					weight_in_kg=Double.parseDouble(weight);
					height_in_cm=Double.parseDouble(height);
				
				}catch(Exception e){
				
				}
		
				height_in_metres=(height_in_cm/100);
				BMI= (weight_in_kg/(height_in_metres*height_in_metres));
				
				DecimalFormat twoDForm = new DecimalFormat("###.##"); 
				
				height_textView.setText(height+"");
				weight_textView.setText(weight+"");
				age_textView.setText(age+"");
				user_textView.setText(user+"");
				bmi_textView.setText(twoDForm.format(BMI)+"");
				
				//Accessing User by Comparing the BMI Ratios with some Known Ranges
				
				if(BMI<18.5)
					bmirange_textView.setText("UnderWeight");
				else if(BMI >=18.5 & BMI <=25)
					bmirange_textView.setText("normal");
				else if(BMI>25 & BMI<=30)
					bmirange_textView.setText("Overweight");
				else if(BMI>30)
					bmirange_textView.setText("Obese");
		}
		catch(Exception e){
			
		}
		
	}
	
	public void onClick(View arg0) {
		
		switch(arg0.getId())
		{
			case R.id.button_update:
				
				update_dialog=new Dialog(BMICalculator.this);	//Show Update User Dialog
				update_dialog.setContentView(R.layout.updateuserdialog);
				update_dialog.setCancelable(true);
				update_dialog.setTitle("Update Details");
				update_dialog.show();
			
				age_editText=(EditText)update_dialog.findViewById(R.id.editText_age);
				height_editText=(EditText)update_dialog.findViewById(R.id.editText_height);
				weight_editText=(EditText)update_dialog.findViewById(R.id.editText_weight);
				
				age_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				height_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				weight_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				save=(Button)update_dialog.findViewById(R.id.button_save);
				cancel=(Button)update_dialog.findViewById(R.id.button_cancel);
			
				//Setting the current values to the EditText
				age_editText.setText(age+"");
				height_editText.setText(height+"");
				weight_editText.setText(weight+"");
				
				save.setOnClickListener(this);
				cancel.setOnClickListener(this);
			
				break;
		
			case R.id.button_save:
			
				userdetails_edit=userdetails.edit();
				
				//Getting User input and Updating Shared Preferences
				try
				{
				userdetails_edit.putString("user_age", age_editText.getText().toString());
				userdetails_edit.putString("user_height", height_editText.getText().toString().trim());
				userdetails_edit.putString("user_weight", weight_editText.getText().toString().trim());
				}
				catch(Exception e){
					
				}
				
				userdetails_edit.commit();
				
				calculateBMI();
				update_dialog.dismiss();
			
				break;
			
			case R.id.button_cancel:
				//Dismiss Dialog
				update_dialog.dismiss();
				
				break;
				
			case R.id.imageView3:
				//To FitnessnMotionActivity
				Intent toHome=new Intent(BMICalculator.this,FitnessnMotionActivity.class);
				startActivity(toHome);
				finish();
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
