package com.phoenix.fitness;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;


public class NotificationReceiver extends BroadcastReceiver{

	String time;
	Bundle b;
	Intent myIntent;
	Calendar cal;
	Context context;
	CharSequence tickerText;
	
	SharedPreferences mode;
	
	@Override
	public void onReceive(Context con, Intent arg1) {
		// TODO Auto-generated method stub
		
		  mode=con.getSharedPreferences("User_exercise_mode", Activity.MODE_WORLD_READABLE);
		  
		  b=arg1.getExtras();
		  myIntent=arg1;
		  cal=Calendar.getInstance();
		  context=con;
		  
		 try
		 {
			 time=b.getString("TIME");
		 
		 }
		 catch(Exception ex)
		 {
			 
		 }
		 finally{
			 b.clear();
		 }
		 
		 PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(time), arg1, 0);
		 tickerText="";
		 
		 if(time.equals("1000"))
		 {	 if(mode.getString("exercise_mode", "simple").equals("simple"))
			 	tickerText ="Wake up for your Schedule!\n You still have a 1000 Calories to Burn!";
			 else if(mode.getString("exercise_mode", "simple").equals("moderate"))
				 tickerText ="Wake up for your Schedule!\n You still have a 1500 Calories to Burn!";
			 else 
		 		tickerText ="Wake up for your Schedule!\n You still have a 2000 Calories to Burn!";
		 }	
		 else
			  tickerText ="Schedule completed";
	     long when = System.currentTimeMillis();
	        
	     //making vibration
	      Vibrator v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		  v.vibrate(3000);   
	     //set notification
	      NotificationManager notofManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
	        
	      Notification notification = new Notification(R.drawable.ic_launcher,tickerText,when );
	      notification.setLatestEventInfo(context, "Have a healthy Morning :P "+time, tickerText, contentIntent);
	        
	      notification.flags = Notification.FLAG_AUTO_CANCEL;
	      notofManager.notify(Integer.parseInt(time),notification);
	      
	      //ringing alarm
	      Intent x=new Intent(context,StopAlarm.class);
	      Bundle b=new Bundle();
	      b.putString("TIME", time);
	      x.putExtras(b);
	      x.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      context.startActivity(x);
	      contentIntent.cancel();
	      contentIntent=PendingIntent.getBroadcast(context, Integer.parseInt(time), myIntent, 0);
	      AlarmManager alarmManager = (AlarmManager)context.getSystemService(Service.ALARM_SERVICE);
        	
        		
	      if(cal.get(Calendar.DAY_OF_MONTH)+1<=cal.getMaximum(Calendar.DAY_OF_MONTH))
	    	  cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)+1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
	      else
	    	  cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        		 
	      alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),contentIntent);
        		
        	
       }

}
