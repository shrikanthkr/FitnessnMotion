package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SecondEntry extends Activity implements OnClickListener{

	Spinner spinner_gender,spinner_mode;
	SharedPreferences userdetails;
	SharedPreferences.Editor user_edit;
	String[] arrayList_mode={"Simple","Moderate","Strict"};
	String[] arrayList_gender={"Male","Female"};
	ArrayAdapter<String> adapter_mode;
	ArrayAdapter<String> adapter_gender;
	Button done;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getdetails_second);
		
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		user_edit=userdetails.edit();
		
		spinner_gender=(Spinner)findViewById(R.id.spinner_gender);
		spinner_mode=(Spinner)findViewById(R.id.spinner_mode);
		adapter_gender=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList_gender);
		adapter_gender.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_gender.setAdapter(adapter_gender);
		
		
		adapter_mode=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList_mode);
		adapter_mode.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_mode.setAdapter(adapter_mode);
		
		spinner_gender.setSelection(0);
		spinner_mode.setSelection(0);
		
		done=(Button)findViewById(R.id.button_Seconddone);
		done.setOnClickListener(this);
		
	//	user_edit.putString("user_gender", value);
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		user_edit.putString("user_gender", spinner_gender.getSelectedItem().toString());
		user_edit.putString("user_mode", spinner_mode.getSelectedItem().toString());
		Intent x=new Intent(this,FitnessnMotionActivity.class);
		startActivity(x);
		finish();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toGetFirst=new Intent(this,GetDetailsStart.class);
		startActivity(toGetFirst);
	}
	
	
	

}
