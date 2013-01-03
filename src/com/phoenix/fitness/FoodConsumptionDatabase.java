package com.phoenix.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FoodConsumptionDatabase{
	
    private static final String KEY_ROWID="rowid";
	private static final String KEY_FOOD_NAME="foodname";
	private static final String KEY_FOOD_CALORIES="foodcalories";
	private static final String KEY_FOOD_DATE="fooddate";
	private static final String KEY_FOOD_FAT="fatfromfood";
	private static final String DATABASE_NAME="foodconsumptiondatabase";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="foodconsumptiontable";
	
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	private DbHelp ourHelper;
	
	public FoodConsumptionDatabase(Context c)
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
				        KEY_FOOD_NAME + " TEXT NOT NULL, " +
						KEY_FOOD_DATE + " TEXT NOT NULL, " + 
				        KEY_FOOD_FAT + " DOUBLE NOT NULL, " +
				        KEY_FOOD_CALORIES + " DOUBLE NOT NULL " +
				        " );" );
				    
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
				onCreate(db);	
				
			}
		
		}

	public FoodConsumptionDatabase open()
		{
				
			ourHelper=new DbHelp(ourContext);
			ourDatabase=ourHelper.getWritableDatabase();
			return this;
		}
	
	public void close()
	{
		ourHelper.close();
	}

	public void Insert(String food_name,String date,Double food_fat,Double food_calories)
	{
		ContentValues cv=new ContentValues();
		
		cv.put(KEY_FOOD_NAME, food_name);
		cv.put(KEY_FOOD_DATE, date);
		cv.put(KEY_FOOD_FAT, food_fat);
		cv.put(KEY_FOOD_CALORIES, food_calories);
		
		ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String retrieveConsumption()
	{
		int count,k=0;
		String foodresult="";
		
		final String getconsumption="SELECT " + KEY_FOOD_NAME +" , " + KEY_FOOD_DATE +" , " + KEY_FOOD_FAT + " , " + KEY_FOOD_CALORIES +
				" FROM " + DATABASE_TABLE + " ; " ;
		
		Cursor c= ourDatabase.rawQuery(getconsumption, null);
		c.moveToFirst();
		
		count=c.getCount();
		
		for(k=0;k<count;k++)
			{
				
				foodresult=foodresult+c.getString(c.getColumnIndex(KEY_FOOD_NAME))+"/"+c.getString(c.getColumnIndex(KEY_FOOD_DATE))+
						"/"+c.getString(c.getColumnIndex(KEY_FOOD_FAT))+"/"+c.getString(c.getColumnIndex(KEY_FOOD_CALORIES))+"\n";
				
				c.moveToNext();
				
			}	
		c.close();
		
		return foodresult;
		
	}
	
	public String retrieveConsumptionByDate(String dateStr)
	{
		int count,k=0;
		String foodresult="";
		
		final String getconsumption="SELECT " + KEY_FOOD_NAME +" , " + KEY_FOOD_DATE +" , " + KEY_FOOD_FAT + " , " + KEY_FOOD_CALORIES +
				" FROM " + DATABASE_TABLE +" WHERE " + KEY_FOOD_DATE + " LIKE '"+ dateStr +"%' "+ " ;" ;
		
		Cursor c= ourDatabase.rawQuery(getconsumption, null);
		c.moveToFirst();
		
		count=c.getCount();
		
		for(k=0;k<count;k++)
			{
				
				foodresult=foodresult+c.getString(c.getColumnIndex(KEY_FOOD_NAME))+"/"+c.getString(c.getColumnIndex(KEY_FOOD_DATE))+
						"/"+c.getString(c.getColumnIndex(KEY_FOOD_FAT))+"/"+c.getString(c.getColumnIndex(KEY_FOOD_CALORIES))+"\n";
				
				c.moveToNext();
				
			}	
		c.close();
		
		return foodresult;
		
	}
	
	public String retrieveCaloriesByDate(String dateStr)
    {
            int count,k=0;
            String foodresult="";
            
            final String getconsumption="SELECT " + KEY_FOOD_CALORIES +
                            " FROM " + DATABASE_TABLE +" WHERE " + KEY_FOOD_DATE + " LIKE '"+ dateStr +"%' "+ " ;" ;
            
            Cursor c= ourDatabase.rawQuery(getconsumption, null);
            c.moveToFirst();
            
            count=c.getCount();
            
            for(k=0;k<count;k++)
                    {
                            
                            foodresult=foodresult+c.getString(c.getColumnIndex(KEY_FOOD_CALORIES))+"\n";
                            
                            c.moveToNext();
                            
                    }        
            
            c.close();
            return foodresult;
    }
}
