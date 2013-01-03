package com.phoenix.fitness;

//Show the Map Activity along with the distance covered by the user along with Speed

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class DynamicTracker extends MapActivity  {
    
	 MapView mMapView;
 	 private MapController mapController;
 	 private LocationManager locationManager;
 	 private List<Overlay> mapOverlays;
 	 List<GeoPoint> _points;
 	 float weight;
 	 GeoPoint gP1;
     GeoPoint gP2;
     TextView tv_velocity,tv_distance;
     int drawable;
     
    
     List <GeoPoint> allpoints; 
     int pointflag=0;
     float distance_covered=0;
 	 GeoPoint temp;
	 
 	 
 	 @Override
 	 	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic);
            
        	try
        	{
        		//Getting Distance from CalorieTracker Activity
        		Bundle getFromCalorie=getIntent().getExtras();
        		distance_covered=(float) Double.parseDouble(getFromCalorie.getString("distance"));
        	}
        	catch(Exception e)
        	{
        		
        	}
        
        	tv_velocity=(TextView)findViewById(R.id.mapVelocity_textView);
        	tv_distance=(TextView)findViewById(R.id.Distance_textView);
        	
        	tv_distance.setText(distance_covered+"");
        	
            mMapView = (MapView) findViewById(R.id.mapview);
    	    mMapView.setVisibility(MapView.VISIBLE);
    	    mMapView.setBuiltInZoomControls(true);
    	    mMapView.setStreetView(false);
    	    mMapView.setSatellite(false);
    	  
    	    mapController = mMapView.getController();
    	    drawable=R.drawable.ic_launcher;
    	    mapOverlays = mMapView.getOverlays();    
    	    mapOverlays.add(new MyOverlay());
    	    weight=(float) (55*(2.2024));
    	  
    	  	_points=new ArrayList<GeoPoint>();
    	    temp=new GeoPoint(0,0);
    	   
    	    locationManager =
    	    		(LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	    
    	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,new LocationUpdateHandler());
    	    		
        }
    	
	 	@Override
    	protected boolean isRouteDisplayed() {
    		return false;
    	}
    	
	 	public class LocationUpdateHandler implements LocationListener {

    		//Handle GPS Location Changes
    		int flag=0;
    		Location oldLocation;
    		double velocity;
    		public void onLocationChanged(Location loc) {
    			
    			
    		int lat = (int) (loc.getLatitude()*1E6);
    		int lng = (int) (loc.getLongitude()*1E6);
    		flag++;
    		DecimalFormat twoDForm = new DecimalFormat("##.##");
    		
    		try{
    			
    				velocity=(oldLocation.distanceTo(loc) /  ( (loc.getTime()/1000)- (oldLocation.getTime()/1000))) ;
    				distance_covered+=(float)oldLocation.distanceTo(loc)/1000;
					
					tv_distance.setText(twoDForm.format((float)distance_covered)+"");
					tv_velocity.setText(""+twoDForm.format((float)(velocity*3.6)));
					
			}
    		catch(Exception e){
    			
    		}
    		
    		
    		gP1 = new GeoPoint(lat, lng);
    		_points.add(gP1);
    		
    		oldLocation=new Location(loc);
    		mMapView.postInvalidate();
    		
    		}

    		
    		public void onProviderDisabled(String provider) {}

    		
    		public void onProviderEnabled(String provider) {}

    		
    		public void onStatusChanged(String provider, int status, 
    		Bundle extras) {}
    	
    	}
    	
	 	class MyOverlay extends Overlay{
    		
	 		//Map Activity
    		int count=0;
    		Point pointA;
    		Bitmap markerImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), drawable);
    	    public MyOverlay(){

    	    }
    	    
    	    	public void draw(Canvas canvas, MapView mapv, boolean shadow){
    	        super.draw(canvas, mapv, shadow);

    	        
    	        Paint   mPaint = new Paint();
    	        mPaint.setDither(true);
    	        mPaint.setColor(Color.BLUE);
    	        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    	        mPaint.setStrokeJoin(Paint.Join.ROUND);
    	        mPaint.setStrokeCap(Paint.Cap.ROUND);
    	        mPaint.setStrokeWidth(mMapView.getZoomLevel()-2);
    	        mPaint.setAlpha(80);
    	        
    	        
    	        Projection projection = mMapView.getProjection();
    	        
    	        GeoPoint gPointA =null;
                if (shadow == false && _points != null) {
                        
                	    @SuppressWarnings("unused")
                		Point startPoint=null,endPoint = null;
                        Path path = new Path();
                        count++;
                        
                        try{
                        
                        		gPointA = _points.get(_points.size()-1);
                        
                        		if(count>10){
                        			mapController.animateTo(gPointA);
                        			count=0;
                        		}
                        	
                        		projection.toPixels(_points.get(_points.size()-1), endPoint);
                        }
                        catch(Exception e){
                        	
                        }
                        
                        for (int i = 0; i < _points.size(); i++) {
                                 gPointA = _points.get(i);
                                 pointA = new Point();
                                
                                projection.toPixels(gPointA, pointA);
                                if (i == 0) { 
                                        startPoint = pointA;
                                        path.moveTo(pointA.x, pointA.y);
                                } 
                                
                                else {
                                		Point b=new Point();
                                		projection.toPixels(_points.get(i-1),b);
                                		path.moveTo(b.x,b.y);
                                		
                                	    path.lineTo(pointA.x, pointA.y);
                                	    endPoint=b;
                                	   
                                }
                        }
                        
                        
                        if (!path.isEmpty()){
                            canvas.drawPath(path, mPaint);
                            canvas.drawBitmap(markerImage,
                    	            pointA.x - markerImage.getWidth() / 2,
                    	            pointA.y - markerImage.getHeight() / 2, null);
                           
                        }
                }
    	  
    	    }
    	    
    	}
    		
}


