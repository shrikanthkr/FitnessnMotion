package com.phoenix.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CalorieDatabase {
	
	private static final String KEY_ROWID="rowid";
	private static final String KEY_CALORIES_DATE="date";
	private static final String KEY_ACTIVITY="activity";
	private static final String KEY_CALORIES_CONSUMED="calories";
	
	private static final String DATABASE_NAME="CalorieDatabase_updated";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="CalorieTable_updated";
	
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	private DbHelp ourHelper;
	
	public CalorieDatabase(Context c)
	{
		ourContext=c;
	}
	
	private static class DbHelp extends SQLiteOpenHelper
	{
		public DbHelp(Context context){
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE  " + DATABASE_TABLE + " ( " +
				    KEY_ROWID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			        KEY_CALORIES_DATE + " TEXT NOT NULL, " +
			        KEY_ACTIVITY + " TEXT NOT NULL, " +
					KEY_CALORIES_CONSUMED + " FLOAT NOT NULL " + 
					" );" );
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public CalorieDatabase open()
	{
		ourHelper=new DbHelp(ourContext);
		ourDatabase=ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		ourHelper.close();
	}
	
	public void Insert(String date,int calories,String activity)
	{
		//Inserting User Input Values to Database
		
		ContentValues cv=new ContentValues();
		cv.put(KEY_CALORIES_DATE, date);
		cv.put(KEY_CALORIES_CONSUMED, calories);
		cv.put(KEY_ACTIVITY, activity);
		ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	public String getCalorieDetailsByDate(String date)
	{
		//Get Calories as a String from the Database for a particular date  
		
		String Calorielist="";
		int count,k=0;
		
		final String getCalorie="SELECT " + KEY_CALORIES_CONSUMED
				+" FROM " + DATABASE_TABLE +" WHERE " + KEY_CALORIES_DATE + " LIKE '"+ date +"%' "+ " ;" ;
		
		//Executing Raw Query
		Cursor c=ourDatabase.rawQuery(getCalorie, null); 	//Initialize Cursor
		
		c.moveToFirst();
		count=c.getCount();
		
		for(k=0;k<count;k++){
			Calorielist=Calorielist+c.getString(c.getColumnIndex(KEY_CALORIES_CONSUMED))+"\n";
			c.moveToNext();
		}
		
		c.close();	//Close Cursor
		
		return Calorielist;
	}
	
	public String getCalorieandWorkoutByDate(String date)
	{
		//Get Calories along with the Activity(Work-out) name as a String from the Database for a particular date
		
		String Calorielist="";
		int count,k=0;
		
		final String getCalorie="SELECT " + KEY_ACTIVITY+" ," + KEY_CALORIES_CONSUMED
				+" FROM " + DATABASE_TABLE +" WHERE " + KEY_CALORIES_DATE + " LIKE '"+ date +"%' "+ " ;" ;
		
		Cursor c=ourDatabase.rawQuery(getCalorie, null);	//Initialize Cursor
		
		c.moveToFirst();
		count=c.getCount();
		
		for(k=0;k<count;k++){
			Calorielist=Calorielist+c.getString(c.getColumnIndex(KEY_ACTIVITY))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES_CONSUMED))+"\n";
			c.moveToNext();
		}
		
		c.close();	//Close Cursor
		
		return Calorielist;
	}
}




