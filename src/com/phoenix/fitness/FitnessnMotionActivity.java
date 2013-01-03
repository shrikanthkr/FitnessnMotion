package com.phoenix.fitness;

//Main DashBoard Activity which acts as a Home for the Application

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class FitnessnMotionActivity extends Activity implements OnClickListener{
    
	Button dailymonitor_button,calorietracker_button,healthadvisor_button,exercisescheduler_button,bmicalculator_button,facts_button;
	Calendar cal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Initialise();
        setNotification();
    }

    private void setNotification() {
		// TODO Auto-generated method stub
		
    	cal=Calendar.getInstance();
		if(cal.get(Calendar.HOUR_OF_DAY)<21)
		{
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 21, 00);
		Intent alarm=new Intent(this,DailyNotification.class);
		Bundle b=new Bundle();
		b.putString("NOTIFYDAILY", "1111");
		alarm.putExtras(b);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1111, alarm,0);
 		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
		}
		
	}
	
    private void Initialise() {
		
		dailymonitor_button=(Button) findViewById(R.id.dailymonitor_button);
        calorietracker_button=(Button) findViewById(R.id.calorietracker_button);
        healthadvisor_button=(Button) findViewById(R.id.healthadvisor_button);
        exercisescheduler_button=(Button) findViewById(R.id.exercisescheduler_button);
        bmicalculator_button=(Button) findViewById(R.id.bmicalculator_button);
        facts_button=(Button) findViewById(R.id.facts_button);
		
        dailymonitor_button.setOnClickListener(this);
        calorietracker_button.setOnClickListener(this);
        healthadvisor_button.setOnClickListener(this);
        exercisescheduler_button.setOnClickListener(this);
        bmicalculator_button.setOnClickListener(this);
        facts_button.setOnClickListener(this);
	}

	
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.dailymonitor_button:
				Intent toDaily=new Intent(FitnessnMotionActivity.this,DailyMonitor.class);
				startActivity(toDaily);
				break;
		case R.id.calorietracker_button:
				Intent toSelectworkout=new Intent(FitnessnMotionActivity.this,SelectWorkout.class);
				startActivity(toSelectworkout);
				break;
		case R.id.healthadvisor_button:
				Intent toHealth=new Intent(FitnessnMotionActivity.this,HealthAdvisor.class);
				startActivity(toHealth);
				break;
		case R.id.exercisescheduler_button:
				Intent toExcercise=new Intent(FitnessnMotionActivity.this,ExcerciseScheduler.class);
				startActivity(toExcercise);
				break;
		case R.id.bmicalculator_button:
				Intent toBMI=new Intent(FitnessnMotionActivity.this,BMICalculator.class);
				startActivity(toBMI);
				break;
		case R.id.facts_button:
				Intent toStats=new Intent(FitnessnMotionActivity.this,Stats.class);
				startActivity(toStats);
				break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
		}
}