package com.phoenix.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabase {

	private static final String KEY_ROWID="id";
	private static final String KEY_FOODNAME="foodname";
	private static final String KEY_CARBOHYDRATES="carbohydrates";
	private static final String KEY_PROTEINS="proteins";
	private static final String KEY_SATURATEDFAT="saturatedfat";
	private static final String KEY_TOTALFAT="totalfat";
	private static final String KEY_CALORIES_FAT_FOOD="caloriesfatfood";
	private static final String KEY_CATEGORY="category";
	private static final String KEY_CALORIES="calories";
	private static final String DATABASE_NAME="FoodDatabase_updated";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="FoodDatabaseTable_updated";
	
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	private DbHelp ourHelper;
	
	private static class DbHelp extends SQLiteOpenHelper {
		public DbHelp(Context context){
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase arg0) {
			arg0.execSQL("CREATE TABLE  " + DATABASE_TABLE + " ( " +
				    KEY_ROWID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			        KEY_FOODNAME + " TEXT NOT NULL, " +
					KEY_CARBOHYDRATES + " DOUBLE NOT NULL, " + 
			        KEY_PROTEINS + " DOUBLE NOT NULL, " +
			        KEY_SATURATEDFAT + " DOUBLE NOT NULL, " +
			        KEY_TOTALFAT + " DOUBLE NOT NULL, " +
			        KEY_CALORIES_FAT_FOOD + " DOUBLE NOT NULL, " +
			        KEY_CATEGORY + " TEXT NOT NULL, " +
			        KEY_CALORIES + " DOUBLE NOT NULL " +
			        " );"
			      );
			}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			arg0.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(arg0);	
		}
	
	}

	public FoodDatabase(Context c)
	{
		ourContext=c;
	}
	
	public FoodDatabase open()
	{
		ourHelper=new DbHelp(ourContext);
    	ourDatabase=ourHelper.getWritableDatabase();
    	return this;
	}
	
	public void close()
	{
		ourHelper.close();
	}
	
	public void Insert(String foodname,double carbohydrates,double proteins,double saturatedfat,double totalfat,double caloriesfood,String category,double calories)
	{
		ContentValues cv=new ContentValues();
		cv.put(KEY_FOODNAME, foodname);
		cv.put(KEY_CARBOHYDRATES, carbohydrates);
		cv.put(KEY_PROTEINS, proteins);
		cv.put(KEY_SATURATEDFAT, saturatedfat);
		cv.put(KEY_TOTALFAT, totalfat);
		cv.put(KEY_CALORIES_FAT_FOOD, caloriesfood);
		cv.put(KEY_CATEGORY, category);
		cv.put(KEY_CALORIES, calories);
		
		ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	public String getFoodDetails()
	{
		int count,k=0;
		String foodresult="";
		
		final String getfood="SELECT " + KEY_FOODNAME + " , " + KEY_CALORIES + " FROM " + DATABASE_TABLE + " ;" ;
		
		Cursor c=ourDatabase.rawQuery(getfood, null);
		c.moveToFirst();
    	count=c.getCount();
    	for(k=0;k<count;k++){
    					foodresult=foodresult+c.getString(c.getColumnIndex(KEY_FOODNAME))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES))+"\n";
    					c.moveToNext();
    	}
    	
    	c.close();
		
    	return foodresult;
	}

	public String getIndianFoodList()
	{
		String foodlist="";
		int k=0,count;
		
		final String getfood="SELECT " + KEY_FOODNAME + ", " + KEY_CALORIES
				+" FROM " + DATABASE_TABLE +" WHERE " + KEY_CATEGORY + " LIKE '"+"Indian"+"%' "+ " ;" ;
		
		Cursor c=ourDatabase.rawQuery(getfood, null);
		c.moveToFirst();
    	count=c.getCount();
    	for(k=0;k<count;k++){
    		foodlist=foodlist+c.getString(c.getColumnIndex(KEY_FOODNAME))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES))+"\n";
    		c.moveToNext();
    	}
    	
    	c.close();
    	return foodlist;
		
	}
	
	public String getChineseFoodList()
	{
		String foodlist="";
		int k=0,count;
		
		final String getfood="SELECT " + KEY_FOODNAME + ", " + KEY_CALORIES
				+" FROM " + DATABASE_TABLE +" WHERE " + KEY_CATEGORY + " LIKE '"+"Chinese"+"%' "+ " ;" ;
		
		Cursor c=ourDatabase.rawQuery(getfood, null);
		c.moveToFirst();
    	count=c.getCount();
    	for(k=0;k<count;k++){
    		
    		foodlist=foodlist+c.getString(c.getColumnIndex(KEY_FOODNAME))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES))+"\n";
    		c.moveToNext();
    	}
    	
    	c.close();
    	return foodlist;
		
	}
	
	public String getEuropeanFoodList()
	{
		String foodlist="";
		int k=0,count;
		
		final String getfood="SELECT " + KEY_FOODNAME + ", " + KEY_CALORIES
				+" FROM " + DATABASE_TABLE +" WHERE " + KEY_CATEGORY + " LIKE '"+"Italian"+"%' "+ " ;" ;
		
		Cursor c=ourDatabase.rawQuery(getfood, null);
		c.moveToFirst();
    	count=c.getCount();
    	for(k=0;k<count;k++){
    		foodlist=foodlist+c.getString(c.getColumnIndex(KEY_FOODNAME))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES))+"\n";
    		c.moveToNext();
    	}
    	
    	c.close();
    	return foodlist;
		
	}
	
	public String getAmericanFoodList()
	{
		String foodlist="";
		int k=0,count;
		
		final String getfood="SELECT " + KEY_FOODNAME + ", " + KEY_CALORIES
				+" FROM " + DATABASE_TABLE +" WHERE ( " + KEY_CATEGORY + " LIKE '"+"Mexican"+"%' ) "+ "OR ( " +KEY_CATEGORY + " LIKE '"+"American"+"%' ) " +
				" ;" ;
		
		Cursor c=ourDatabase.rawQuery(getfood, null);
		c.moveToFirst();
    	count=c.getCount();
    	for(k=0;k<count;k++){
    		foodlist=foodlist+c.getString(c.getColumnIndex(KEY_FOODNAME))+"/"+c.getString(c.getColumnIndex(KEY_CALORIES))+"\n";
    		c.moveToNext();
    	}
    	
    	c.close();
    	return foodlist;
		
	}
	
	public String getFatFromFood(String food_name)
	{
		String result="";
		
		String[] columns={KEY_CALORIES_FAT_FOOD};
		Cursor c=ourDatabase.query(DATABASE_TABLE, columns, KEY_FOODNAME + " LIKE '" + food_name +"%' " , null, null, null, null);
		
		c.moveToFirst();
		
		int fatfood=c.getColumnIndex(KEY_CALORIES_FAT_FOOD);
		result=c.getString(fatfood);
		
		c.close();
		
		return result;
	}

	public String getFoodDetails(String food_name)
	{
		String food_details="";
		String[] columns={KEY_FOODNAME,KEY_CARBOHYDRATES,KEY_PROTEINS,KEY_SATURATEDFAT,KEY_TOTALFAT,KEY_CALORIES_FAT_FOOD,KEY_CATEGORY,KEY_CALORIES};
		
		Cursor c=ourDatabase.query(DATABASE_TABLE, columns, KEY_FOODNAME + " LIKE '" + food_name +"%' ", null, null, null, null);
		
		c.moveToFirst();
		
		int foodname_index=c.getColumnIndex(KEY_FOODNAME);
		int carbo_index=c.getColumnIndex(KEY_CARBOHYDRATES);
		int prot_index=c.getColumnIndex(KEY_PROTEINS);
		int sat_index=c.getColumnIndex(KEY_SATURATEDFAT);
		int tot_index=c.getColumnIndex(KEY_TOTALFAT);
		int fatfood_index=c.getColumnIndex(KEY_CALORIES_FAT_FOOD);
		int category_index=c.getColumnIndex(KEY_CATEGORY);
		int calories_index=c.getColumnIndex(KEY_CALORIES);
	
		food_details=c.getString(foodname_index)+"/"+c.getString(carbo_index)+"/"+c.getString(prot_index)+"/"+c.getString(sat_index)+"/"+
					 c.getString(tot_index)+"/"+c.getString(fatfood_index)+"/"+c.getString(category_index)+"/"+c.getString(calories_index);
	
		c.close();
		return food_details;
	}
	
	public void clearDatabase()
	{
		ourDatabase.delete(DATABASE_TABLE, null, null);
		SQLiteDatabase.releaseMemory();
	}
}
