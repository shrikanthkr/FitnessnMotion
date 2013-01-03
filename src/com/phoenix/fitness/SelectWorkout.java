package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SelectWorkout extends Activity implements OnClickListener{

	Button run,cycle,gym,jog;
	ImageView home;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectworkout);
		
		run=(Button) findViewById(R.id.button_run);
		cycle=(Button) findViewById(R.id.button_cycle);
		gym=(Button) findViewById(R.id.button_gym);
		jog=(Button) findViewById(R.id.button_sports);
		home=(ImageView) findViewById(R.id.imageView3);
		
		home.setOnClickListener(this);
		run.setOnClickListener(this);
		cycle.setOnClickListener(this);
		gym.setOnClickListener(this);
		jog.setOnClickListener(this);
	}
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.button_run:
			Bundle b_run=new Bundle();
			b_run.putString("mode","run");
			
			Intent toCalorie_run=new Intent(SelectWorkout.this,CalorieTracker.class);
			toCalorie_run.putExtras(b_run);
			startActivity(toCalorie_run);
			finish();
			break;
		case R.id.button_cycle:
			Bundle b_cycle=new Bundle();
			b_cycle.putString("mode","cycle");
			
			Intent toCalorie_cyle=new Intent(SelectWorkout.this,CalorieTracker.class);
			toCalorie_cyle.putExtras(b_cycle);
			startActivity(toCalorie_cyle);
			finish();
			break;
		case R.id.button_gym:
			Intent toGym=new Intent(SelectWorkout.this,GymWorkout.class);
			startActivity(toGym);
			finish();
			break;
		case R.id.button_sports:
			Intent toSports=new Intent(SelectWorkout.this,SportsActivity.class);
			startActivity(toSports);
			finish();
			break;
		case R.id.imageView3:
			Intent toHome=new Intent(SelectWorkout.this,FitnessnMotionActivity.class);
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
