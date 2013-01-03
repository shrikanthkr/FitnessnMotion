package com.phoenix.fitness;

/*DailyMonitor Activity keeps track of the Calorie consumption of  the user.The user can add the food he has consumed and 
 * the Calories are automatically added to the Database.The user can Drag and Drop the Food Item in the ListView.
 * Alternatively the Food details can be View on ListItem Click.*/

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class DailyMonitor extends Activity implements OnItemClickListener,OnClickListener,TextWatcher
{

	//Initialize Views
	ListView lv_food;
	Button b1,b_next;
	ImageView headnew2;
	EditText search_editText;
	LazyAdapter adapter;
	
	Calendar now;
	int today_date,today_month,today_year;
	static int  index;
	static Boolean to_insert=false;
	int flag=0;
	String food_list,search_term="",fat_from;;
	String[] food_array,food_next;
	
	ArrayList<String> food_name_array,calories_array;
	ArrayList<String> f_name,f_calories,search_f_name,search_f_calories;
	
	FoodDatabase fd;
	FoodConsumptionDatabase fcd;
	
	SharedPreferences userdetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailymonitor);
		
		userdetails=getSharedPreferences("User_Details",MODE_WORLD_READABLE);
		
		headnew2=(ImageView) findViewById(R.id.headnew2);
		b1=(Button) findViewById(R.id.button1);
		b_next=(Button) findViewById(R.id.button_done_dailymonitor);
		search_editText=(EditText) findViewById(R.id.search_editText);
		lv_food=(ListView) this.findViewById(R.id.food_listView);
		
		search_editText.clearFocus();
		lv_food.requestFocus();
		
		b1.setOnClickListener(this);
		b_next.setOnClickListener(this);
		search_editText.addTextChangedListener(this);
		
		fd=new FoodDatabase(this);
		fcd=new FoodConsumptionDatabase(this);
		
		//Open FoodDatabase and Get FoodDetails of all Food Items
		fd.open();
		food_list=fd.getFoodDetails();	//Get FoodDetails as String
		fd.close();

		doStringcalc();	//Setting the String to the ListView 
		
		//Getting Today's Date for Database Insert
		now=Calendar.getInstance();
		today_date=now.get(Calendar.DAY_OF_MONTH);
		today_month=now.get(Calendar.MONTH);
		today_year=now.get(Calendar.YEAR);
	
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
				//OnListItemClick Start the Food Details Activity and pass food name as a bundle
				Bundle b=new Bundle();
				if(flag==0)
					b.putString("food_name", food_name_array.get(arg2));
				else
					b.putString("food_name",search_f_name.get(arg2) );
		
				Intent toShowFood=new Intent(DailyMonitor.this,ShowFoodDetails.class);
				toShowFood.putExtras(b);
				startActivity(toShowFood);	
				finish();
	}

	public void onClick(View arg0) {
		
		//Handling OnClick Button Event
		
		switch(arg0.getId())
		{
			case R.id.button1:
			
			//ActionItem pops down from the top and displays a list of Items added below
			
			ActionItem indian=new ActionItem(1,"Indian\t\t",getResources().getDrawable(R.drawable.indian_food_icon));
			ActionItem american=new ActionItem(2,"American\t\t",getResources().getDrawable(R.drawable.burger_icon));
			ActionItem italian=new ActionItem(3,"Italian\t\t",getResources().getDrawable(R.drawable.pizza_icon));
			ActionItem chinese=new ActionItem(4,"Chinese\t\t",getResources().getDrawable(R.drawable.chinese_icon));
			ActionItem add_custom=new ActionItem(5,"Add Custom\t\t",getResources().getDrawable(R.drawable.add_custom_icon));
			ActionItem all=new ActionItem(6,"All\t\t",getResources().getDrawable(R.drawable.all_food_icon));
			ActionItem dummy=new ActionItem(7,"");
			
			//QuickActionItem 
			final QuickAction quickAction = new QuickAction(this);
			
			//Add all the ActionItems to the QuickActionBar or QuickActionMenu
			
			quickAction.addActionItem(all);
			quickAction.addActionItem(indian);
			quickAction.addActionItem(american);
			quickAction.addActionItem(chinese);
			quickAction.addActionItem(italian);
			quickAction.addActionItem(add_custom);
			quickAction.addActionItem(dummy);
			
			//Set OnItemClickListener for QuickActionItems
			
			quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
				
				public void onItemClick(QuickAction source, int pos, int actionId) {				
					ActionItem actionItem = quickAction.getActionItem(pos);
		             
				if(actionItem.getTitle().trim().equals("Indian"))
				{
					//Get Only Indian Foods and Update the ListView
					fd.open();
					food_list=fd.getIndianFoodList();
					doStringcalc();
					fd.close();
				}
				
				else if(actionItem.getTitle().trim().equals("Chinese"))
				{
					//Get Only Chinese Foods and Update the ListView
					fd.open();
					food_list=fd.getChineseFoodList();
					doStringcalc();
					fd.close();
				}
				
				else if(actionItem.getTitle().trim().equals("Italian"))
				{
					//Get Only Italian Foods and Update the ListView
					fd.open();
					food_list=fd.getEuropeanFoodList();
					doStringcalc();
					fd.close();
				}
				else if(actionItem.getTitle().trim().equals("American"))
				{
					//Get Only American Foods and Update the ListView
					fd.open();
					food_list=fd.getAmericanFoodList();
					doStringcalc();
					fd.close();
				}
				
				else if(actionItem.getTitle().trim().equals("Add Custom"))
				{
					//Add a Custom Food 
					
					Intent toAddUserFood=new Intent(DailyMonitor.this,AddUserFood.class);
					startActivity(toAddUserFood);
					finish();
				}
				else if(actionItem.getTitle().trim().equals("All"))
				{
					//Show All Items
					
					fd.open();
					food_list=fd.getFoodDetails();
					doStringcalc();
					fd.close();
				}
					
				
			}
			});
			
			//Dismiss the QuickActionMenu
			quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
				
			public void onDismiss() {
					
				}
			});
			
			quickAction.show(arg0);
			break;
		
		case R.id.button_done_dailymonitor:
			break;
		}
		
	}
	
	public void doStringcalc()
	{
		
		//Split the String set it to the ListAdapter
		
		int i=0;
		flag=0;
		try
		{
			food_array=food_list.split("\n");
			
			//Assign Length to ArrayList
			
			food_name_array=new ArrayList<String>(food_array.length+1);
			calories_array=new ArrayList<String>(food_array.length+1);
		
			for(i=0;i<food_array.length;i++)
			{
				food_next=food_array[i].split("/");
				food_name_array.add(i,food_next[0]);		//Adding Items to the ArrayList
				calories_array.add(i,food_next[1]);
			}
		
			
			//Create a LazyAdapter and pass both ArrayLists to it
			adapter= new LazyAdapter(this, food_name_array,calories_array);
			lv_food.setAdapter(adapter);	//Set Adapter to the ListView
			lv_food.setOnItemClickListener(this);
		
			//Implementing DragNDropListView to handle drag and drop operations of each ListItem
			
			if(lv_food instanceof DragNDropListView){
			
				((DragNDropListView) lv_food).setDragListener(mDragListener);
			}
		}
		catch(Exception e)
		{
			
		}
	}

	private DragListener mDragListener =	new DragListener() {

	    	int backgroundColor = Color.CYAN;
	    	int defaultBackgroundColor;
	    	
			public void onDrag(int x, int y, ListView listView) 
			{
					
			}

			//OnStartDrag
			
			public void onStartDrag(View itemView) {
			
			//Handling the View Visibility on drag start
			
			Toast.makeText(getApplicationContext(), "Drag and Drop To add", 2).show();
			
			itemView.setVisibility(View.INVISIBLE);		//Each ListItem
			headnew2.setVisibility(View.VISIBLE);		//Bottom Footer
			b_next.setVisibility(View.VISIBLE);			//Button
			
			defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
			itemView.setBackgroundColor(backgroundColor);
				
			ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
					
				if (iv != null) 
					iv.setAlpha(40);
			}

			public void onStopDrag(View itemView) {
					
				itemView.setVisibility(View.VISIBLE);
				itemView.setBackgroundColor(defaultBackgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
				if (iv != null) 
					iv.setVisibility(View.VISIBLE);
				b_next.setVisibility(View.GONE);
				headnew2.setVisibility(View.GONE);
					
				
					
				fd.open();
				
				if(flag==0)
				{
					fat_from=fd.getFatFromFood(food_name_array.get(index));
					if(to_insert){
					
						if(CheckCalorieIntake(calories_array.get(index)))
						{
							fcd.open();
							fcd.Insert(food_name_array.get(index), today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(fat_from), Double.parseDouble(calories_array.get(index)));
							fcd.close();
						}
						else
						{
							ShowAlertDialog();
						}
					
					
					}
				}
				else
				{
					fat_from=fd.getFatFromFood(search_f_name.get(index));
					if(to_insert){
						
						if(CheckCalorieIntake(search_f_calories.get(index)))
						{
							fcd.open();
							fcd.Insert(search_f_name.get(index), today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(fat_from), Double.parseDouble(search_f_calories.get(index)));
							fcd.close();
						}
						
						else
						{	
							ShowAlertDialog();
						}
					}
					
				}
					fd.close();
				}

	};
	    
	public static void  setItemIndex(int itmx){
			index=itmx;
	}
	
	public static void toInsert(boolean b) {
			to_insert=b;
    }
	
	private boolean CheckCalorieIntake(String calorie_intake) {
		
		String[] consumedCalorie;
		String date;
		int calorieConsumed=0,max_calorie;
		Boolean result;
		date=today_date+"-"+(today_month+1)+"-"+today_year;
		fcd.open();
		consumedCalorie=new String[fcd.retrieveCaloriesByDate(date).split("\n").length];
		consumedCalorie=fcd.retrieveCaloriesByDate(date).split("\n");
		fcd.close();

		try{
			
			for(int j=0;j<consumedCalorie.length;j++)
				calorieConsumed=calorieConsumed+Integer.parseInt(consumedCalorie[j]);
			}catch(Exception e){
				e.printStackTrace();
			}
		
		if(userdetails.getString("user_gender", "").equals("Male")) 
			max_calorie=2550;
		else 
			max_calorie=1940;
		
		if(calorieConsumed+Double.parseDouble(calorie_intake)<=max_calorie)
		{
			result=true;
		}
		else
		{
			result=false;
		}
			
		return result;
	}

	private void ShowAlertDialog() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
		builder.setMessage("Your Intake is High.Do you still want to continue?")
	           .setCancelable(true).setTitle("IntelliDiet")
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick( final DialogInterface dialog, final int id) {
	            	   if(flag==0)
	            	   {
	            	   fcd.open();
	            	   fcd.Insert(food_name_array.get(index), today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(fat_from), Double.parseDouble(calories_array.get(index)));
	            	   fcd.close();
	            	   }
	            	   else
	            	   {
	            		   fcd.open();
	            		   fcd.Insert(search_f_name.get(index), today_date+"-"+(today_month+1)+"-"+today_year, Double.parseDouble(fat_from), Double.parseDouble(search_f_calories.get(index)));
	            		   fcd.close();
	            	   }
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
	
	public void afterTextChanged(Editable s) {
		
		//Handle onTextChange for the EditText
		
		int k,j=0,l=0;
		
		fd.open();
		String food_details=fd.getFoodDetails();
		String[] food=food_details.split("\n");
		String[] f_array;
		
		f_name=new ArrayList<String>(food.length);
		f_calories=new ArrayList<String> (food.length);
		
			for(int i=0;i<food.length;i++)
			{
				f_array=food[i].split("/");
				f_name.add(i, f_array[0]);
				f_calories.add(i, f_array[1]);
			}
			
		fd.close();
			
		for(int i=0;i<food.length;i++)
		{
			if(f_name.get(i).contains(search_term))	//In order to allocate the size of the ArrayList every time search is done 
			{
					j++;
			}
		}
			
		search_f_name=new ArrayList<String> (j);
		search_f_calories=new ArrayList<String> (j);
			
		for(k=0;k<food.length;k++)
		{
			if(f_name.get(k).toLowerCase().contains(search_term.toLowerCase()))	//Check whether the list contains the search query
			{
				search_f_name.add(l,f_name.get(k));
				search_f_calories.add(l,f_calories.get(k));
				l++;
			}
		}
			
		flag=1;		//Set flag=1 indicating search performed
		
		adapter=new LazyAdapter(DailyMonitor.this,search_f_name,search_f_calories);		//Set Search Query List Items to the ListView Adapter
		lv_food.setAdapter(adapter);
		lv_food.setOnItemClickListener(this);
			
		if(lv_food instanceof DragNDropListView){
			((DragNDropListView) lv_food).setDragListener(mDragListener);	
		}
			
	
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		//Do Nothing
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		search_term=""+s;	//Add the Characters to a String
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//On Back Press go to Previous Acttivity
		Intent toFitness=new Intent(this,FitnessnMotionActivity.class);
		startActivity(toFitness);
	}
	
}
