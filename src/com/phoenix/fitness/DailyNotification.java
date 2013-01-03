package com.phoenix.fitness;

//Notify the user at the end of the day on Calories consumed and Net Calories
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DailyNotification extends BroadcastReceiver {
	Bundle b;
	Intent myIntent;
	Calendar cal;
	Context context;
	 CharSequence tickerText;
	 String time;
	 CalorieDatabase cd;
	FoodConsumptionDatabase fcd;
	@Override
	public void onReceive(Context con, Intent arg1) {
		// TODO Auto-generated method stub
		 b=arg1.getExtras();
		 myIntent=arg1;
		 cal=Calendar.getInstance();
		 context=con;
		 time=b.getString("NOTIFYDAILY");
		 PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(time), arg1, 0);
		 tickerText=time;
		 long when = System.currentTimeMillis();
		 cd=new CalorieDatabase(context);
		 fcd=new FoodConsumptionDatabase(context);		 
		 setText();
		 
		 
		 
		 NotificationManager notofManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
		 Notification notification = new Notification(R.drawable.ic_launcher,tickerText,when );
		 notification.setLatestEventInfo(context, "Your Summary Today : ", tickerText, contentIntent);
		 notification.flags = Notification.FLAG_AUTO_CANCEL;
		 notofManager.notify(Integer.parseInt(time),notification);
		 
		 contentIntent=PendingIntent.getBroadcast(context, Integer.parseInt(time), myIntent, 0);
		 AlarmManager alarmManager = (AlarmManager)context.getSystemService(Service.ALARM_SERVICE);
     	
     	 if(cal.get(Calendar.DAY_OF_MONTH)+1<=cal.getMaximum(Calendar.DAY_OF_MONTH))
 	    	 cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)+1,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
     	 else
     		 cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
     		 
     		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),contentIntent);
     		
		
	}
	private void setText() {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		String[] activitycalorie = null;
		String[] consumedCalorie = null;
		String date = now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH)+1) + "-" + now.get(Calendar.YEAR);
		cd.open();
		try{
			activitycalorie = new String[cd.getCalorieDetailsByDate(date).split("\n").length];
		activitycalorie=cd.getCalorieDetailsByDate(date).split("\n");
		}catch(Exception e){}
		cd.close();
		
		fcd.open();
		try{
			consumedCalorie = new String[fcd.retrieveCaloriesByDate(date).split("\n").length];
		consumedCalorie=fcd.retrieveCaloriesByDate(date).split("\n");
		}catch(Exception e){}
		fcd.close();
		int calorieSpent=0,calorieConsumed=0;
		try{
		for(int i=0; i<activitycalorie.length; i++){
			 calorieSpent = calorieSpent+Integer.parseInt(activitycalorie[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			
		
		for(int j=0;j<consumedCalorie.length;j++)
			calorieConsumed=calorieConsumed+Integer.parseInt(consumedCalorie[j]);
		}catch(Exception e){
			e.printStackTrace();
		}
	 
	 
	tickerText="You have consumed " + calorieConsumed +" and \nspent " + calorieSpent + " \nwith " + (calorieConsumed-calorieSpent) +"remaining";	
	}

}
