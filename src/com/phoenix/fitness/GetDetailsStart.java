package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



public class GetDetailsStart extends Activity implements OnClickListener{

	EditText name_editText,age_editText,weight_editText,height_editText;
	RadioGroup weight_radio_group;
	Button done_button_get_details;
	RadioButton r1,r2;
	String metric_weight="Kg",name,age,weight,height;
	Double weight_in_pounds;
	Boolean check_first;
	
	
	SharedPreferences app_first_time;
	SharedPreferences.Editor app_first_time_editor;
	SharedPreferences userdetails;
	SharedPreferences.Editor userdetails_editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdetails_start);
		
		app_first_time=getSharedPreferences("FitnessnMotion",MODE_WORLD_READABLE);
		app_first_time_editor=app_first_time.edit();
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		userdetails_editor=userdetails.edit();
		Initialise();
		
	}
	
	private void Initialise() {
		
		name_editText=(EditText) findViewById(R.id.name_editText);
		age_editText=(EditText) findViewById(R.id.age_editText);
		weight_editText=(EditText) findViewById(R.id.weight_editText);
		height_editText=(EditText) findViewById(R.id.height_editText);
		done_button_get_details=(Button) findViewById(R.id.button_done);
		
		age_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		height_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		weight_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		done_button_get_details.setOnClickListener(this);
		
	} 
	
	public void onClick(View arg0) {
		
		try
		{
		name=name_editText.getText().toString().trim();
		age=age_editText.getText().toString().trim();
		weight=weight_editText.getText().toString().trim();
		height=height_editText.getText().toString().trim();
		sendToBundle(name,age,weight,height);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Enter Details Correctly", 2).show();
		}
		
	}
	
	private void sendToBundle(String name2, String age2, String weight2, String height2) {
		
		check_first=app_first_time.getBoolean("first", true);
		if(check_first==true)
		{
			app_first_time_editor.putBoolean("first", false);
			app_first_time_editor.commit();
			userdetails_editor.putString("user_name",name2);
			userdetails_editor.putString("user_age", age2);
			userdetails_editor.putString("user_weight", weight2);
			userdetails_editor.putString("user_height", height2);
			userdetails_editor.commit();
			
			Intent toSecond=new Intent(GetDetailsStart.this,SecondEntry.class);
			startActivity(toSecond);
			finish();
		}
	}
	
}
