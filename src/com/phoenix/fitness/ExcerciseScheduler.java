package com.phoenix.fitness;

//Schedule Exercises and Select Mode based on which the Scheduler works 


import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;


public class ExcerciseScheduler extends Activity implements OnClickListener, OnSeekBarChangeListener{
	
	SeekBar excercise_seekbar1,excercise_seekbar2,excercise_seekbar3;
	//Spinner time_spinner;
	Button setSchedule_button,settime,setEndTimeButton;
	ImageView home;
	ArrayAdapter<String> adapter;
	String[] time_array;
	TextView tv;
	
	Calendar startcal,endcal;
	int seekMax;
	int progresSetVariable=1;
	int starthour,endhour,startminute,endminute;
	ToggleButton tb;
	int pickId=0;
	TextView startTime,endTime;
	
	static final int DATE_DIALOG_ID = 0;
	
	SharedPreferences mode;
	SharedPreferences.Editor mode_edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.excercisescheduler);
		
		mode=getSharedPreferences("User_exercise",MODE_WORLD_READABLE);
		mode_edit=mode.edit();
		
		tv=(TextView) findViewById(R.id.textView_exmode);
		
		tb=(ToggleButton)findViewById(R.id.alarm_togglebutton);
		excercise_seekbar1=(SeekBar)findViewById(R.id.excercise_seekbar1);
		excercise_seekbar1.setProgressDrawable(getResources().getDrawable(R.drawable.progress1));
		excercise_seekbar1.setProgress(10);
		excercise_seekbar2=(SeekBar)findViewById(R.id.excercise_seekbar2);
		excercise_seekbar2.setProgressDrawable(getResources().getDrawable(R.drawable.progress2));
		
		excercise_seekbar3=(SeekBar)findViewById(R.id.excercise_seekbar3);
		excercise_seekbar3.setProgressDrawable(getResources().getDrawable(R.drawable.progress3));
		setSchedule_button=(Button)findViewById(R.id.setSchedule_button);
		settime=(Button) findViewById(R.id.time_button);
		setEndTimeButton=(Button) findViewById(R.id.time_end_button);
		startTime=(TextView) findViewById(R.id.textView_startTime);
		endTime=(TextView) findViewById(R.id.textView_endTime);
		home=(ImageView) findViewById(R.id.imageView3);
		setTextView("Simple");
		
		
		startcal=Calendar.getInstance();
		endcal=Calendar.getInstance();
		setSchedule_button.setOnClickListener(this);
		settime.setOnClickListener(this);
		setEndTimeButton.setOnClickListener(this);
		home.setOnClickListener(this);
		excercise_seekbar1.setOnSeekBarChangeListener(this);
		seekMax=excercise_seekbar1.getMax()/3;
		excercise_seekbar2.setOnSeekBarChangeListener(this);
		excercise_seekbar3.setOnSeekBarChangeListener(this);
		excercise_seekbar2.setVisibility(View.INVISIBLE);
		excercise_seekbar3.setVisibility(View.INVISIBLE);
		
		starthour=endhour=startcal.get(Calendar.HOUR_OF_DAY);
		startminute=endminute=startcal.get(Calendar.MINUTE);
		
	}
	
	public String getType(){
		String type="";
		return type;
	}
	public void setTextView(String type){
		if(type.equals("Simple"))
		{
			tv.setText(type);
			mode_edit.putString("exercise_mode", "simple");
			mode_edit.commit();
		}
		else if(type.equals("Moderate"))
		{
			tv.setText(type);
			mode_edit.putString("exercise_mode", "moderate");
			mode_edit.commit();
		}
		else
		{
			tv.setText(type);
			mode_edit.putString("exercise_mode", "strict");
			mode_edit.commit();
		}
	}
	
	
	public void onClick(View arg0) {
		
		switch(arg0.getId())
		{
		case R.id.setSchedule_button:
		
		// TODO Auto-generated method stub
		if(arg0.getId()==setSchedule_button.getId()){
			
			
			
			
			 Bundle b=new Bundle();
			 b.putString("TIME", "1000" );
			 Intent alarm=new Intent(this,NotificationReceiver.class);
	    	 alarm.putExtras(b);
	    	 if(startcal.get(Calendar.DAY_OF_MONTH)+1<=startcal.getMaximum(Calendar.DAY_OF_MONTH))
	    	 {
	    		 
	    	 startcal.set(startcal.get(Calendar.YEAR),startcal.get(Calendar.MONTH),startcal.get(Calendar.DAY_OF_MONTH)+1, starthour, startminute);
	    	 endcal.set(endcal.get(Calendar.YEAR),endcal.get(Calendar.MONTH),endcal.get(Calendar.DAY_OF_MONTH)+1, endhour, endminute);
	    	 }
	    	 else{
	    		 startcal.set(startcal.get(Calendar.YEAR),startcal.get(Calendar.MONTH)+1,1, starthour, startminute);
	    		 endcal.set(endcal.get(Calendar.YEAR),endcal.get(Calendar.MONTH)+1,1, endhour, endminute);
	    	 }
	    	 
	 		
	 		if(tb.isChecked() && starthour>=endhour){
	 			PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1000, alarm,0);
		 		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	 			alarmManager.set(AlarmManager.RTC_WAKEUP, startcal.getTimeInMillis(),pendingIntent);
	 			Bundle b1=new Bundle();
	 			b1.putString("TIME", "1001");
	 			alarm.replaceExtras(b1);
	 			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,1001, alarm,0);
		 		AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
		 		 
	 			alarmManager1.set(AlarmManager.RTC_WAKEUP, endcal.getTimeInMillis(),pendingIntent1);
	 			
	 		}
	 		else
	 		{
	 			PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1000, alarm,0);
	 			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,1001, alarm,0);
	 			pendingIntent.cancel();
	 			pendingIntent1.cancel();
	 		}
	 		
	 		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
	 		startActivity(toFitness);
	 		finish();
	    	 
	    }
		break;
		case R.id.time_button:
			pickId=0;
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.time_end_button:
			pickId=1;
			showDialog(DATE_DIALOG_ID);
		break;
		case R.id.imageView3:
			Intent toDashBoard=new Intent(this,FitnessnMotionActivity.class);
			startActivity(toDashBoard);
			finish();
	}
		
	}

	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Intent toDashBoard=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toDashBoard);
		finish();
		
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	if(progress<seekMax){
		
		excercise_seekbar1.setVisibility(View.VISIBLE);
		excercise_seekbar2.setVisibility(View.INVISIBLE);
		excercise_seekbar3.setVisibility(View.INVISIBLE);
		excercise_seekbar1.setProgress(progress);
		
	
		setTextView("Simple");
		
	}
	if(progress<seekMax*2 && progress>seekMax){
	
	
		excercise_seekbar1.setVisibility(View.INVISIBLE);
		excercise_seekbar2.setVisibility(View.VISIBLE);
		excercise_seekbar3.setVisibility(View.INVISIBLE);
		excercise_seekbar2.setProgress(progress);
		
		setTextView("Moderate");
		
	}
	if(progress<seekMax*3 && progress>seekMax*2){
		excercise_seekbar1.setVisibility(View.INVISIBLE);
		excercise_seekbar2.setVisibility(View.INVISIBLE);
		excercise_seekbar3.setVisibility(View.VISIBLE);
		excercise_seekbar3.setProgress(progress);
		
		setTextView("Strict");
	
	}
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
		
	}
	
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new TimePickerDialog(this,
	                    mTimeSetListener,
	                    starthour,startminute,true);
	    }
	    return null;
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                
				public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
					if(pickId==0){
					starthour=arg1;
					startminute=arg2;
					
					startTime.setText(starthour+":"+startminute);
					}
					else{
						endhour=arg1;
						endminute=arg2;
						
						endTime.setText(endhour+":"+endminute);
						
					}
					
				}  };

				public String getStartTime(){
					return ""+(starthour*100+startminute);
					
				}
				public String getEndTime(){
					return ""+(endhour*100+endminute);
				}

				
				

}
