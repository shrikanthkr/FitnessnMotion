package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddUserFood extends Activity implements OnClickListener{

	
	EditText food_name,calories,category;
	Button addcustom;
	ImageView home;
	
	FoodDatabase fd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_food);
		
		food_name=(EditText) findViewById(R.id.editText_foodname);
		category=(EditText) findViewById(R.id.editText_category);
		calories=(EditText) findViewById(R.id.editText_calories);
		calories.setInputType(InputType.TYPE_CLASS_NUMBER);
		addcustom=(Button) findViewById(R.id.button_addcustom);
		home=(ImageView) findViewById(R.id.imageView3);
		
		fd=new FoodDatabase(this);
		
		addcustom.setOnClickListener(this);	
		home.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		
		switch(arg0.getId())
		{
		case R.id.button_addcustom:
			try
			{
			fd.open();
			fd.Insert(food_name.getText().toString().trim(), 0, 0, 0, 0, 0, category.getText().toString().trim(), Double.parseDouble(calories.getText().toString().trim()));
			Toast.makeText(getApplicationContext(), "Successfully Entered", 2).show();
			fd.close();
			Intent toDaily=new Intent(this,DailyMonitor.class);
			startActivity(toDaily);
			finish();
			}catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Failed to Insert", 2).show();
			}
			break;
		case R.id.imageView3:
			Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
			startActivity(toFitness);
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

}
