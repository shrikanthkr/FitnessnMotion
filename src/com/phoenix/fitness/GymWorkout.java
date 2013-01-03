package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;



public class GymWorkout extends Activity implements OnItemClickListener,OnClickListener {

	ListView workout_list;
	ImageView home;
	
	GymAdapter adapter; 
	String[] icon_array={"aerobics_icon.png","bicycle_gym_icon.png","general_exercise_icon.png","treadmill_icon.png","stairstep_icon.png","weight_lift_icon.png"},
			activity_array={"Aerobics","Bicycling(Gym)","General Exercise","Running(Treadmill)","Stair Step Machine","Weight Lifting"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gymworkout);

		workout_list=(ListView) findViewById(R.id.listView_selectworkout);
		home=(ImageView) findViewById(R.id.imageView3);
		
		adapter=new GymAdapter(this,icon_array,activity_array);
		workout_list.setAdapter(adapter);
		workout_list.setOnItemClickListener(this);
		home.setOnClickListener(this);
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
		Bundle workout=new Bundle();
		workout.putString("workout", activity_array[arg2]);
		Intent toSpecific=new Intent(GymWorkout.this,SpecificExercise.class);
		toSpecific.putExtras(workout);
		startActivity(toSpecific);
		finish();
		
	}
	public void onClick(View arg0) {
		Intent toHome=new Intent(GymWorkout.this,FitnessnMotionActivity.class);
		startActivity(toHome);
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toSelect=new Intent(this,SelectWorkout.class);
		startActivity(toSelect);
	}
	

}
