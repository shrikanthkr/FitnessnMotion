package com.phoenix.fitness;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;


public class StopAlarm extends Activity{
	
	
	MediaPlayer mp;
	Intent z;
	Bundle b;
	String time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		b=getIntent().getExtras();
		time=b.getString("TIME");
		if(time.equals("1000"))
			z=new Intent(StopAlarm.this,SelectWorkout.class);
		else
			z=new Intent(StopAlarm.this,FitnessnMotionActivity.class);
		
		Uri alert1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

		mp = MediaPlayer.create(getApplicationContext(), alert1);
		
	    mp.setVolume(100, 100);
	    mp.start();
	    
	    mp.setLooping(true);
	    
		alert();
		
		
	}
	public void alert() {
		// TODO Auto-generated method stub
		
		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   
    	   builder.setMessage("Alarm!!");
    	   builder.setCancelable(false);
    	          builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
    	              public void onClick(DialogInterface dialog, int id) {
    	            	  mp.release();
    	                   dialog.cancel();
    	                   startActivity(z);
    	              }
    	              
					

					
    	          });
    	          builder.show();
    	 
		
	}
	

}
