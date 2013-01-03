package com.phoenix.fitness;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.google.android.maps.GeoPoint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*CalorieTracker Activity tracks down calories the user has spent on Performing a Workout.This is done by Getting the Location of the User and 
 * By Calculating the Position and Velocity.From these known values we can determine the Calories Burned for a specific time period.
 * The Various details shown to the user are Distance covered,Velocity,Calories Burned and time period of the workout.
 * These Calorie value will be added to the Database once the user presses the stop button.A summary of the workout is also shown to the 
 * user
 */

	public class CalorieTracker extends Activity implements OnClickListener{

	//Initializing Views
	TextView speed,calories,timer,distance,tv_distancecov,tv_avgspeed,tv_calburned,tv_timerfinal,start_label,pause_label,mode;
	TextView gpsStatus;
	Button map,start,stop,pause,ok_summary_dialog;
	ImageView home;
	Dialog summary;
	
	//Initialize Variables
	Calendar now;
	Boolean isPaused=false;
	float calories_burned=0,distance_covered=0,temp_calories=0,temp_distance=0;
	int startflag=1,loc_flag=0;
	int today_date,today_month,today_year,weight;
	long startTime,stopTime,diff_Time,pause_Time;
	long mLastLocationMillis;
	String mode_string,Avg_speed="";
	DecimalFormat twoDForm;
	//Initialize Thread and Handler for the Thread
	Thread t1;
	Handler handler;
	
	//SharedPreferences to get the User Weight
	SharedPreferences userdetails;
	
	//Database Entry
	CalorieDatabase cd;
	
	//Initializing the Location Related Variables
	LocationUpdateHandler loc_handler;
	private LocationManager locman;
	GeoPoint gp1,gp2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calorietracker);
		
		twoDForm = new DecimalFormat("##.##");
		
		//get Mode from the user
		try
		{
		Bundle get=getIntent().getExtras();
		mode_string=get.getString("mode");
		}catch(Exception e)
		{
			
		}
		
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		speed=(TextView) findViewById(R.id.speed_textView);
		calories=(TextView) findViewById(R.id.calories_textView);
		timer=(TextView) findViewById(R.id.timer_textView);
		distance=(TextView) findViewById(R.id.distance_textView);
		start_label=(TextView) findViewById(R.id.textView_startlabel);
		pause_label=(TextView) findViewById(R.id.textView_pauselabel);
		mode=(TextView) findViewById(R.id.textView_mode);
		gpsStatus=(TextView) findViewById(R.id.textView_gpsStatus);
		
		map=(Button) findViewById(R.id.map_button);
		start=(Button) findViewById(R.id.start_button);
		stop=(Button) findViewById(R.id.stop_button);
		pause=(Button) findViewById(R.id.pause_button);
		home=(ImageView) findViewById(R.id.imageView3);
		
		weight=Integer.parseInt(userdetails.getString("user_weight", "65"));
		handler=new Handler();
		
		mode.setText(mode_string);
		
		//getting today's Date from Calendar
		now=Calendar.getInstance();
		today_date=now.get(Calendar.DAY_OF_MONTH);
		today_month=now.get(Calendar.MONTH);
		today_year=now.get(Calendar.YEAR);
		
		cd=new CalorieDatabase(CalorieTracker.this);
		
		map.setOnClickListener(this);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		pause.setOnClickListener(this);
		home.setOnClickListener(this);
	
		//Pause button initially invisible
		pause.setVisibility(View.INVISIBLE);
		
		loc_handler=new LocationUpdateHandler();
		MyGPSListener mGpsListener = new MyGPSListener();
		
		try
	    {
	    	locman=(LocationManager) getSystemService(Context.LOCATION_SERVICE);	//Using  System Location service
	    	locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,loc_handler);	//Requesting Location changed updated initially
	    	locman.addGpsStatusListener(mGpsListener);		//Adding GPSListener to get GPS Status 
	    }
	    catch(Exception e)
	    {
	    	Toast.makeText(getApplicationContext(), "fail", 2).show();	
	    }

	    if ( !locman.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        buildAlertMessageNoGps();	//AlertDialog to Switch GPS on
	    }
	    
}
	
	public void onClick(View arg0) {
	
		switch(arg0.getId())
		{
		
			case R.id.start_button:
				
				//Updating Views on Button Click
				pause.setVisibility(View.VISIBLE);
				start.setVisibility(View.INVISIBLE);
				start_label.setVisibility(View.INVISIBLE);
				pause_label.setVisibility(View.VISIBLE);
			
				
				startflag=1;	//startflag used to control the Clock
				loc_flag=1;		//loc_flag used to control Gps updation
			 
					//Handling Start,Pause and Resume of Timer as well as the GPS
					if(!isPaused)
					{
						startTime=System.nanoTime();
						locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,loc_handler);
						distance_covered=0;
						calories_burned=0;
						
					}	
					else
					{
						startTime=System.nanoTime();
						diff_Time=pause_Time;
						distance_covered=temp_distance;
						calories_burned=temp_calories;
						locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,loc_handler);
					}
			
					//Thread to Control the Timer
					t1=new Thread()
					{
						public void run()
						{
							while(startflag==1)
							{ 
								startClock(startTime);
						
								//Updating Views inside thread using a Handler
								handler.post(new Runnable() {
									
									public void run() {
										setTimer(timer);
									}
								});
						
								//Thread Sleep for a second to implement the Timer functionality
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					};
			
					//Starting Thread based on the startflag condition
					if(startflag==1)
						t1.start();
			break;
		
			case R.id.stop_button:
				
				String[] Avg_speed_array;
				double Avg_speed_abs=0,Total_speed=0;
				try
				{
				
				Avg_speed_array=Avg_speed.split("/");
				
				for(int i=0;i<Avg_speed_array.length;i++)
					Total_speed=Total_speed+Double.parseDouble(Avg_speed_array[i]);
					
				Avg_speed_abs=Total_speed/Avg_speed_array.length;
				}
				catch(Exception e){
					
				}
				
				//Updating Views on Button Click
				pause.setVisibility(View.INVISIBLE);
				start.setVisibility(View.VISIBLE);
				start_label.setVisibility(View.VISIBLE);
				pause_label.setVisibility(View.INVISIBLE);
			
				//Summary Dialog is Shown to the user after Stop Button is pressed
				summary=new Dialog(CalorieTracker.this);
				summary.setContentView(R.layout.summarydialog);
				summary.setCancelable(true);
				summary.setTitle("Summary");
				summary.show();
			
				tv_distancecov=(TextView)summary.findViewById(R.id.distancecov_tv);
				tv_avgspeed=(TextView)summary.findViewById(R.id.avgspeed_tv);
				tv_calburned=(TextView)summary.findViewById(R.id.finalcal_tv);
				tv_timerfinal=(TextView)summary.findViewById(R.id.finaltimer_tv);
				ok_summary_dialog=(Button)summary.findViewById(R.id.button_ok_dialog);
			
				ok_summary_dialog.setOnClickListener(this);
			
				tv_distancecov.setText(distance.getText().toString().trim());
				tv_avgspeed.setText(twoDForm.format(Avg_speed_abs)+"");
				tv_calburned.setText(calories.getText().toString().trim());
				tv_timerfinal.setText(timer.getText().toString().trim());
			
				//Reset the Views and Clock on Stop
				resetClock();
				
				timer.setText("00:00:00");
				
				distance.setText("0");
				calories.setText("0");
				
				isPaused=false;
			
				//Database Open
				if((int)calories_burned>0)
				{
				cd.open();
				cd.Insert(today_date+"-"+(today_month+1)+"-"+today_year, (int) calories_burned, mode_string);	//Database Insert
				cd.close();
				}
				//Database Close
				
				Avg_speed="";
				loc_handler.StopGps();	//Stop GPS Location Updates
				loc_flag=0;		//Set loc_flag=0 to stop updating the Views
			
			break;
		
			case R.id.pause_button:
				
				//Updating Views on Button Click
				pause.setVisibility(View.INVISIBLE);
				start.setVisibility(View.VISIBLE);
				start_label.setVisibility(View.VISIBLE);
				pause_label.setVisibility(View.INVISIBLE);
			
				//Save the Values to Temporary Variables and Resetting everything
				startflag=0;
				loc_flag=0;
				pause_Time=diff_Time;
				diff_Time=0;
			
				temp_distance=distance_covered;
				temp_calories=calories_burned;
				loc_handler.StopGps();	//Stop GPS Location Updates
			
				isPaused=true;	
			
			break;
		
			case R.id.map_button:
				
				Bundle b=new Bundle();		
				b.putString("distance", distance_covered+"");	//Send the distance covered in a bundle to Map Activity
				Intent toMapActivity=new Intent(CalorieTracker.this,DynamicTracker.class);	
				toMapActivity.putExtras(b);
				startActivity(toMapActivity);	//Go to Map
			
			break;
		
			case R.id.button_ok_dialog:
				
				summary.dismiss();	//Dismiss Dialog
				
				break;
		
			case R.id.imageView3:
				
				Intent toHome=new Intent(CalorieTracker.this,FitnessnMotionActivity.class);	//Go to FitnessnMotionActivity
				startActivity(toHome);
				finish();
				
			break;
		
		}
	}
	
	private void startClock(long nanoTime) {
		
		if(!isPaused)
				diff_Time=((System.nanoTime())-nanoTime)/1000000000;
			else
				diff_Time=pause_Time+((System.nanoTime())-nanoTime)/1000000000;
	}
	
	private void resetClock()
	{
		//Resetting Clock values
		startTime=0;
		stopTime=diff_Time;
		diff_Time=0;
		startflag=0;
    }
	
	private void setTimer(TextView timer)
	{
		//Function to Calculate the time each second and format it to the user
		
		if((diff_Time/3600)<24)	//Check Hours Less than 24
        {
			if((diff_Time/60)<60)	//Checking Minutes
        	   {
        		   
        			  if((diff_Time/3600)<10)	//Formatting the output to the user based on the condition
        			  {
        				  if((diff_Time/60)<10)
        				  {	
        					  if((diff_Time%60)<10)	
        						  timer.setText("0"+diff_Time/3600+":"+"0"+diff_Time/60+":"+"0"+diff_Time%60+"");
        					  else
        						  timer.setText("0"+diff_Time/3600+":"+"0"+diff_Time/60+":"+diff_Time%60+"");
        				  }
        				  else
        				  {
        					  if((diff_Time%60)<10)
        						  timer.setText("0"+diff_Time/3600+":"+diff_Time/60+":"+"0"+diff_Time%60+"");
        					  else
        						  timer.setText("0"+diff_Time/3600+":"+diff_Time/60+":"+diff_Time%60+"");
        				  }
        			 }
        			else
        			{
        				
          				  if((diff_Time/60)<10)
          				  {	
          					  if((diff_Time%60)<10)
          						  timer.setText(diff_Time/3600+":"+"0"+diff_Time/60+":"+"0"+diff_Time%60+"");
          					  else
          						  timer.setText(diff_Time/3600+":"+"0"+diff_Time/60+":"+diff_Time%60+"");
          				  }
          				  else
          				  {
          					  if((diff_Time%60)<10)
          						  timer.setText(diff_Time/3600+":"+diff_Time/60+":"+"0"+diff_Time%60+"");
          					  else
          						  timer.setText(diff_Time/3600+":"+diff_Time/60+":"+diff_Time%60+"");
          				  }
        			}
        	   }
        			
        	else	//If Minutes greater than 60
        	{
        		if((diff_Time/3600)<10)
  			  	{
        			if(((diff_Time/60)%60)<10)
        		    {	
        				if((diff_Time%60)<10)
        					timer.setText("0"+diff_Time/3600+":"+"0"+((diff_Time/60)%60)+":"+"0"+diff_Time%60+"");
        				else
        					timer.setText("0"+diff_Time/3600+":"+"0"+((diff_Time/60)%60)+":"+diff_Time%60+"");
        		    }
        			else
        			{
        				if((diff_Time%60)<10)
        					timer.setText("0"+diff_Time/3600+":"+((diff_Time/60)%60)+":"+"0"+diff_Time%60+"");
        				else
        					timer.setText("0"+diff_Time/3600+":"+((diff_Time/60)%60)+":"+diff_Time%60+"");
        			}
  			   }
        		else
        		{
        			if(((diff_Time/60)%60)<10)
        		    {	
        				if((diff_Time%60)<10)
        					timer.setText(diff_Time/3600+":"+"0"+((diff_Time/60)%60)+":"+"0"+diff_Time%60+"");
        				else
        					timer.setText(diff_Time/3600+":"+"0"+((diff_Time/60)%60)+":"+diff_Time%60+"");
        		    }
        			else
        			{
        				if((diff_Time%60)<10)
        					timer.setText(diff_Time/3600+":"+((diff_Time/60)%60)+":"+"0"+diff_Time%60+"");
        				else
        					timer.setText(diff_Time/3600+":"+((diff_Time/60)%60)+":"+diff_Time%60+"");
        			}
        		}
			
           }
        }
		else
		{
			timer.setText("00:00:00");
			
		}
	}
	
	private void buildAlertMessageNoGps() {
	    
		//Turn GPS on Alert Dialog
		//On Clicking yes it takes the user to the system location settings menu 
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(true)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick( final DialogInterface dialog, final int id) {
	                   
	            	   Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            			   startActivity(callGPSSettingIntent);
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

	
	public class LocationUpdateHandler implements LocationListener
	{
		//Separate Class that Handles the Location Changes
		
		int flag=0;
		Location oldLocation;
		double velocity;
    		
		public void onLocationChanged(Location loc) {
    			
    		mLastLocationMillis = SystemClock.elapsedRealtime();
    			
    		if(loc_flag==1){
    			
    			
    			int lat = (int) (loc.getLatitude()*1E6);
    			int lng = (int) (loc.getLongitude()*1E6);
    			flag++;
    			
    			//Calculating Velocity,Distance and Calories using the Input from Gps and Known Formulas
    			try{
    					if(oldLocation.distanceTo(loc)>0.3)
    					{
    						velocity=(oldLocation.distanceTo(loc) /  ( (loc.getTime()/1000)- (oldLocation.getTime()/1000))) ;
    						calories_burned=calories_burned+(((float)((float)velocity*3.6)*((loc.getTime()/1000)- (oldLocation.getTime()/1000))*weight)/3600);
    						distance_covered+=(float)oldLocation.distanceTo(loc)/1000;
    								
    						distance.setText(twoDForm.format((float)distance_covered)+"");
    						speed.setText(""+twoDForm.format((float)(velocity*3.6)));
    						calories.setText(twoDForm.format((float)calories_burned)+"");
    						
    						Avg_speed=Avg_speed+(velocity*3.6)+"/";
    					}
    					else
    						speed.setText(""+twoDForm.format(0.00));
    												
    							
    			    }catch(Exception e){
    			    			
    			    }
    			    		
    			gp1 = new GeoPoint(lat, lng);
    			oldLocation=new Location(loc);
    			
    		}
    		else
    		{}
        }

    	public void onProviderDisabled(String arg0) {
    		
    	}

    	public void onProviderEnabled(String arg0) {
			
    	}

    	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
    	}
    		
    	public void StopGps()
    	{
    		//Stop receiving GPS Location updates 
    		locman.removeUpdates(this);
    		distance_covered=0;
    		calories_burned=0;
    	}
    		
    }
	
	
	private class MyGPSListener implements GpsStatus.Listener {
		public void onGpsStatusChanged(int event) {
				
				//Used to Get the Gps Status
			
            	boolean isGPSFix = false;
            	switch (event) {
                	case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                		isGPSFix = ((SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000 * 2);

                		if (isGPSFix) { // A fix has been acquired.
                			gpsStatus.setText("Established");
                		} else { // The fix has been lost.
                			gpsStatus.setText("Still Searching..");
                		}
                		
                	break;

                	case GpsStatus.GPS_EVENT_FIRST_FIX:
                    
                    break;
                	case GpsStatus.GPS_EVENT_STARTED:
                    
                    break;
                	case GpsStatus.GPS_EVENT_STOPPED:
                    
                    break;
            	}
        }
    }


	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toSelect=new Intent(this,SelectWorkout.class);
		startActivity(toSelect);
	}
	
	
}
