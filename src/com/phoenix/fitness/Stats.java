package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Stats extends Activity implements OnItemClickListener, OnClickListener{

	ListView listView_month;
	String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
	ImageView home;
	
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		listView_month=(ListView) findViewById(R.id.listView_month);
		adapter=new ArrayAdapter<String> (this,R.layout.list_month_items,months);
		home=(ImageView) findViewById(R.id.imageView3);
		
		home.setOnClickListener(this);
		listView_month.setAdapter(adapter);
		listView_month.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Bundle b=new Bundle();
		b.putString("month",(arg2+1)+"");
		Intent toUserTimeline=new Intent(Stats.this,UserTimeline.class);
		toUserTimeline.putExtras(b);
		startActivity(toUserTimeline);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toFitness);
	}

	public void onClick(View arg0) {
		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toFitness);
		finish();
	}
	
}
