package com.phoenix.fitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;



public class Splash extends Activity{

	SharedPreferences app_first_time;
	Boolean check_first;
	FoodDatabase fd;
	String indian,italian,mexican,chinese,american,common;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		fd=new FoodDatabase(this);
		
		indian="Indian";
		italian="Italian";
		mexican="Mexican";
		american="American";
		chinese="Chinese";
		common="Common";
		
		app_first_time=getSharedPreferences("FitnessnMotion",MODE_WORLD_READABLE);
		
		check_first=app_first_time.getBoolean("first", true);
		
		Thread t2=new Thread(){
			
		public void run()
		{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(check_first==true)
		{
			Thread t1=new Thread(){
				public void run()
				{
					InsertIntoDatabase();
				}
			};
			
			t1.start();
					Intent toGetDet=new Intent(Splash.this,GetDetailsStart.class);
					startActivity(toGetDet);
					finish();
			
			
		}
		else
		{
			Intent toFitness=new Intent(Splash.this,FitnessnMotionActivity.class);
			startActivity(toFitness);
			finish();
		}
		}
		};
		t2.start();
	}
	
	public void InsertIntoDatabase()
	{
		fd.open();
		
		fd.clearDatabase();
		
		fd.Insert("Aloo Chaat", 50, 5, 0.2, 1.3, 13, indian, 231);
		fd.Insert("Aloo Channa Chat", 34, 6, 0, 2, 16.2, indian, 170);
		fd.Insert("Aloo Gobi", 23, 6, 3.1, 14, 126, indian, 239);
		fd.Insert("Aloo Paratha", 47, 18, 4, 11, 99, indian, 360);
		fd.Insert("Aloo Tikki", 28, 3, 2, 8.2, 74, indian, 196);
		fd.Insert("Bhajji", 0, 0, 3, 27, 185, indian, 350);
		fd.Insert("Bhel", 61, 10, 3.4, 15, 135, indian, 415);
		fd.Insert("Buttermilk", 1, 1, 0, 2, 3.6, indian, 19);
		fd.Insert("Curd", 0, 3, 0, 4, 10, indian, 60);
		fd.Insert("Chapathi", 22, 4, 1.6, 4.5, 41, indian, 137);
		fd.Insert("Channa Masala", 0, 0, 0, 0, 91, indian, 158);
		fd.Insert("Chicken Tikka", 2, 27, 0, 16, 144, indian, 260);
		fd.Insert("Chicken Rice", 81, 23, 4, 12, 142, indian, 520);
		fd.Insert("Chilli Paneer", 18, 4, 0, 10, 82.2, indian, 161);
		fd.Insert("Chutney", 19, 1, 0, 0.1, 1, indian, 75);
		fd.Insert("Coffee", 0, 0, 0, 0, 0, indian, 90);
		fd.Insert("Curd Rice", 28, 6, 5.4, 9.7, 88, indian, 222);
		fd.Insert("Dosa", 23, 4, 3.9, 6.3, 57, indian, 165);
		fd.Insert("Dhal", 30, 9, 1.7, 8.1, 73, indian, 230);
		fd.Insert("Egg Fried", 0, 6, 2, 7, 64.5, indian, 92);
		fd.Insert("Egg Fried Rice", 26, 6, 1, 8, 45.1, indian, 194);
		fd.Insert("Egg Hard Boiled", 2, 17, 4, 14, 129.8, indian, 211);
		fd.Insert("Gobi Masala", 9.3, 3.5, 0.3, 3.8, 32.4, indian, 78.6);
		fd.Insert("Idli", 22, 4, 0.5, 0.2, 2, indian, 122);
		fd.Insert("Indian Butter Chicken", 27, 40, 16, 28, 172.5, indian, 522);
		fd.Insert("Kadai Paneer", 10, 2, 4, 7, 47.8, indian, 103);
		fd.Insert("Khichadi", 37, 7, 2.6, 4.7, 42, indian, 219);
		fd.Insert("Kofta", 42, 5, 0, 27, 162, indian, 413);
		fd.Insert("Lemon Rice", 43, 5, 3.9, 6.5, 59, indian, 249);
		fd.Insert("Masala Dosa", 37, 6, 4.7, 3.4, 31, indian, 260);
		fd.Insert("Masala Puris", 24, 5, 0.9, 12.8, 116, indian, 221);
		fd.Insert("Milk(Non fat)", 12, 8, 0, 0, 0, indian, 86);
		fd.Insert("Milk(reduced fat)", 11, 8, 3, 5, 43, indian, 122);
		fd.Insert("Mutter Kulcha", 0, 0, 0, 0, 30, indian, 180);
		fd.Insert("Mutter Paneer", 16, 4, 4, 9, 81, indian, 190);
		fd.Insert("Onion Paratha", 33, 13, 4, 11, 99, indian, 200);
		fd.Insert("Palak Panner", 12, 7, 6.9, 16.8, 152, indian, 228);
		fd.Insert("Paneer Masala", 21, 18, 30, 48, 432, indian, 601);
		fd.Insert("Paneer Paratha", 33, 9, 5.1, 14.9, 135, indian, 292);
		fd.Insert("Paneer Tikka", 36, 8.1, 7, 18, 162, indian, 320);
		fd.Insert("Pani Puri", 0, 0, 0, 3, 27, indian, 50);
		fd.Insert("Pappad", 5, 2, 0, 0, 0, indian, 32);
		fd.Insert("Parotta", 55, 7, 0, 8, 65.7, indian, 274);
		fd.Insert("Pav Bhaji", 63, 9, 8.8, 15.3, 138, indian, 416);
		fd.Insert("Payasam", 42, 5, 0, 3, 32, indian, 215);
		fd.Insert("Phulka", 11, 2, 0.8, 2.2, 21, indian, 69);
		fd.Insert("Plain Paratha", 42, 11, 3, 9, 81, indian, 290);
		fd.Insert("Pongal", 23, 5, 2, 7, 32, indian, 200);
		fd.Insert("Porridge", 25, 9, 2, 5, 20, indian, 183);
		fd.Insert("Raita", 8, 5, 2.6, 5.8, 52, indian, 104);
		fd.Insert("Rajma", 41, 14, 3.1, 13.6, 123, indian, 343);
		fd.Insert("Rasam", 13, 4, 1.4, 1.3, 12, indian, 126);
		fd.Insert("Rice", 46, 5, 0.5, 4.1, 37, indian, 238);
		fd.Insert("Roomali Roti", 29, 5, 0.9, 5.6, 51, indian, 181);
		fd.Insert("Sambar", 30, 10, 4.8, 13.5, 122, indian, 278);
		fd.Insert("Samosa", 16, 2, 1.5, 6.8, 61, indian, 132);
		fd.Insert("Tandoori Roti", 29, 6, 1.6, 9, 81, indian, 213);
		fd.Insert("Tea", 10, 4, 0, 2, 0, indian, 60);
		fd.Insert("Tomato Soup", 40, 4, 0, 0, 0, indian, 180);
		fd.Insert("Upma", 45, 8, 1, 4, 52, indian, 248);
		fd.Insert("Vada", 43, 14, 3.1, 11, 100, indian, 334);
		fd.Insert("Vegetable Uttapam", 24, 4, 4.6, 7.3, 66, indian, 176);
		fd.Insert("Vegetable Pulao", 43, 4, 5.2, 9, 81, indian, 269);
		fd.Insert("Vegetable Roll", 18, 2, 0, 6, 75, indian, 145);
		fd.Insert("Vegetable Sandwich", 40, 7, 1, 3, 26, indian, 204);
		
		fd.Insert("Antipasto", 4, 11, 4.3, 9.8, 89, italian, 151);
		fd.Insert("Bologna", 0, 7, 3.1, 9.1, 82, italian, 114);
		fd.Insert("Bread(Brioche)", 36, 7, 2.2, 10.1, 91, italian, 266);
		fd.Insert("Bread(Toasted)", 13, 2, 0.7, 3.6, 33, italian, 94);
		fd.Insert("Calzone", 39, 26, 14.7, 31, 279, italian, 544);
		fd.Insert("Capicola", 0, 8, 0.6, 2, 19, italian, 55);
		fd.Insert("Cappuccino", 6, 4, 2.2, 4.8, 44, italian, 79);
		fd.Insert("Cheese(Fontina)", 0, 7, 5, 8, 72, italian, 110);
		fd.Insert("Cheese(Provolone)", 0, 7, 4, 7, 63, italian, 99);
		fd.Insert("Cherries(Maraschino)", 2, 0, 0, 0, 0, italian, 7);
		fd.Insert("Coffee(Espresso)", 4, 0, 0.2, 0.4, 4, italian, 21);
		fd.Insert("Gnocchi(Cheese)", 6, 7, 3.1, 8.2, 75, italian, 125);
		fd.Insert("Italian Beans Boiled", 7, 2, 0, 0.2, 2, italian, 30);
		fd.Insert("Lasagna(Broccoli)", 55, 25, 5.2, 12.5, 113, italian, 390);
		fd.Insert("Lasagna(Beef)", 20, 18, 6.8, 14.6, 132, italian, 262);
		fd.Insert("Lasagna(Meat)", 37, 22, 7.1, 13.6, 123, italian, 362);
		fd.Insert("Linguine", 66, 39, 0.8, 7.6, 69, italian, 500);
		fd.Insert("Mortadella", 1, 8, 4.3, 11.6, 105, italian, 143);
		fd.Insert("Pasta(Fusilli)", 42, 7, 0.1, 0.8, 8, italian, 210);
		fd.Insert("Pasta(Spaghetti)", 42, 7, 0.1, 0.8, 8, italian, 210);
		fd.Insert("Pasta(Tortellini)", 51, 15, 3.8, 7.8, 70, italian, 332);
		fd.Insert("Pastrami(Turkey)", 1, 9, 0.9, 3.5, 32, italian, 75);
		fd.Insert("Pepperoni", 0, 6, 4.2, 12.4, 112, italian, 140);
		fd.Insert("Pizza(Veg & Cheese)", 35, 13, 4.9, 11.9, 108, italian, 298);
		fd.Insert("Pizza(Thick)", 55, 14, 5.2, 13.8, 125, italian, 406);
		fd.Insert("Pizza(Veg + Meat)", 37, 17, 7.6, 19.1, 172, italian, 386);
		fd.Insert("Pizza(Grilled)", 94, 21, 2.9, 6.3, 57, italian, 524);
		fd.Insert("Pizza(Beef Topped)", 62, 36, 12.4, 28.2, 254, italian,652 );
		fd.Insert("Pizza(Meat)", 57, 18, 7.7, 20.4, 184, italian, 487);
		fd.Insert("Prosciutto Ham", 0, 15, 1.5, 4.4, 40, italian, 105);
		fd.Insert("Risotto", 51, 24, 0.8, 2.6, 24, italian, 337);
		fd.Insert("Salami & Beef", 0, 3, 2.5, 5.7, 52, italian, 68);
		fd.Insert("Sandwich(Non Veg)", 32, 28, 7.8, 20.4, 184, italian, 431);
		fd.Insert("Spaghetti(Meat Sauce)", 38, 26, 6, 16.1, 145, italian, 404);
		fd.Insert("Vegetable Marinara", 46, 8, 1.1, 8.3, 75, italian, 285);
		
		
		
		fd.Insert("Arroz Con Queso", 31, 14, 6.6, 11.1, 100, mexican, 281);
		fd.Insert("Arroz Pulido", 79, 9, 0, 0.6, 5, mexican, 353);
		fd.Insert("Arroz Variedad", 78, 7, 0, 2.7, 24, mexican, 352);
		fd.Insert("Bread(Cheese)", 24, 9, 0, 8.3, 76, mexican, 208);
		fd.Insert("Bread(Chicken)", 24, 12, 0, 4, 36, mexican, 182);
		fd.Insert("Bread(Cheese,Ham)", 21, 9, 0, 7.4, 67, mexican, 183);
		fd.Insert("Bread(Wheat)", 61, 10, 0, 2.5, 23, mexican, 306);
		fd.Insert("Burrito(Bean)", 32, 16, 8.4, 14.8, 134, mexican, 322);
		fd.Insert("Burrito(Beef)", 20, 20, 10.6, 20.6, 188, mexican, 349);
		fd.Insert("Burrito(Pork)", 20, 22, 10, 19.2, 174, mexican, 340);
		fd.Insert("Casserole", 21, 26, 16.3, 31, 279, mexican, 453);
		fd.Insert("Cheese(Mexican)", 1, 6, 5.3, 8.4, 76, mexican, 106);
		fd.Insert("Chili Verde", 109, 22, 0.6, 2.4, 22, mexican, 600);
		fd.Insert("Chipotle Mexican", 12, 0, 4, 24.5, 220, mexican, 260);
		fd.Insert("Cole Saw", 9, 1, 1.5, 10, 90, mexican, 130);
		fd.Insert("Deep Fried Burrito", 43, 20, 8.5, 19.6, 177, mexican, 425);
		fd.Insert("Enchilda", 30, 12, 9, 17.6, 159, mexican, 323);
		fd.Insert("Fried Burrito", 31, 24, 11.4, 37.6, 339, mexican, 558);
		fd.Insert("French Fries", 42, 4, 3, 17, 153, mexican, 330);
		fd.Insert("Nachos(Beef)", 56, 20, 12.4, 30.7, 276, mexican, 569);
		fd.Insert("Quesadilla", 13, 10, 5.2, 11.3, 102, mexican, 197);
		fd.Insert("Salad", 8, 2, 1, 3.5, 32, mexican, 70);
		fd.Insert("Soup(Tortilla)", 19, 10, 4.2, 13.5, 122, mexican, 238);
		fd.Insert("Taquito", 20, 10, 2.5, 12, 108, mexican, 230);
		fd.Insert("Tacos", 51, 27, 9.4, 22.7, 204, mexican, 524);
		
		
		fd.Insert("Egg Roll Skin",36,5,0,1,9,chinese,170);
		fd.Insert("Spice Powder",0,0,0,0,0,chinese,0);
		fd.Insert("Fried Rice Mix",2,0,0,0,0,chinese,10);
		fd.Insert("Hoisin Sause",9,1,0,1,9,chinese,50);
		fd.Insert("Hot chilli Oil",0,0,0.5,4,36,chinese,40);
		fd.Insert("Rice Stick Noodles",48,0,0,0.5,4,chinese,200);
		fd.Insert("Straw Mushrooms",3,5,0,0,0,chinese,35);
		fd.Insert("Egg Rolls",18,10,2.8,12.4,112,chinese,225);
		fd.Insert("Beef",18,10,2.8,12.4,112,chinese,225);
		fd.Insert("Pork",18,10,2.8,12.4,112,chinese,225);
		fd.Insert("Kung Pao Pork",12,26,6.8,33.9,305,chinese,460);
		fd.Insert("Rice, Fried",29,6,0.9,3.8,34,chinese,181);
		fd.Insert("Soup",1,8,1.1,3.8,34,chinese,73);
		fd.Insert("Egg Drop",1,8,1.1,3.8,34,chinese,73);
		fd.Insert("Waterchestnuts",15,1,0,0,1,chinese,60);
		fd.Insert("Wine",2,0,0,0,0,chinese,81);
		fd.Insert("Mustard Paste",1,0,0,0,0,chinese,5);
		fd.Insert("Broccoli,Chinese",3,1,0,0.6,6,chinese,19);
		fd.Insert("Chicken Foo Yung",4,8,1.9,8,73,chinese,121);
		fd.Insert("Chinese Pomegranate",32,3,0,0.9,9,chinese,150);
		
		fd.Insert("American Burger", 31, 22, 3.2, 10.8, 98, american, 310);
		fd.Insert("Beef & Brocolli", 136, 36, 2, 12, 100, american, 780);
		fd.Insert("Cheese(American spread)", 2, 4, 3, 6, 54, american, 82);
		fd.Insert("Cheese Egg Muffin", 28, 19, 11.1, 29.7, 268, american, 458);
		fd.Insert("FlatBread Pizza", 45, 17, 6, 12, 110, american, 360);
		fd.Insert("MeatBall", 9, 25, 7.9, 21.5, 194, american, 337);
		fd.Insert("Spaghetti", 168, 28, 0, 4, 36, american, 840);
		
		fd.Insert("Apple", 33, 1, 0, 0, 4, common, 126);
		fd.Insert("BlueBerries", 21, 1, 0, 0.4, 4, common, 83);
		fd.Insert("Choclate", 26, 1, 2.1, 3.5, 32, common, 140);
		fd.Insert("Chocolate Shake", 48, 7, 3.8, 6.1, 55, common, 270);
		fd.Insert("Coke", 0, 0, 0, 0, 0, common, 0);
		fd.Insert("Cookies", 32, 3, 4.5, 12, 100, common, 240);
		fd.Insert("Eggs", 0, 6, 1.5, 4.9, 45, common, 72);
		fd.Insert("Nuts(Acorn)", 12, 2, 0.8, 6.7, 61, common, 110);
		fd.Insert("Strawberry", 11, 1, 0, 0.4, 4, common, 46);
		fd.Insert("Sweet Potato", 29, 1, 0, 1.4, 31, common, 151);
		fd.close();
	
		
	}

	
}
