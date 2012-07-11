package hongzheng.fp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class WorkoutMap extends MapActivity{
	//for drawing polyline
	public static List<GeoPoint> points = null;
	public static Polyline polyline = null;
	
	private MapView mapView = null;
	private Drawable drawable = null;
	private List<Overlay> mapOverlays = null;
	private MapItemizedOverlay itemizedOverlay = null;
	public LocationManager mapLocationManager = null;
	public LocationListener mapLocationListener =
		new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if(WorkoutStatus.isRunning)
				{					
					long actualTime = System.currentTimeMillis();
					if((actualTime - WorkoutStatus.avgspeedLastUpdate) > 20000)
					{
						Double lat = location.getLatitude();
						Double lng = location.getLongitude();
						GeoPoint point = 
							new GeoPoint((int)(lat*1000000)+50, (int)(lng*1000000)+50);
						OverlayItem overlayItem = new OverlayItem(point, "hello", "sf");
						
						itemizedOverlay.addOverlay(overlayItem);
//						points = new ArrayList<GeoPoint>();
						points.add(point);
//						mapOverlays.add(polyline);
						mapView.invalidate();
					}
				}
				else
				{
					if(points.size() != 0)
						points = new ArrayList<GeoPoint>();
				}					
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		
	};
	class Polyline extends Overlay
	{
		List<GeoPoint> points;
		Paint paint;
		public Polyline(List<GeoPoint> points)
		{
			Log.i("hongzheng.fp", "polyline constructor");
			this.points = points;
			paint = new Paint();
			paint.setColor(Color.GREEN);
			paint.setAlpha(120);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setStrokeWidth(4);
		}
		public Polyline(List<GeoPoint> points, Paint paint)
		{
			this.points = points;
			this.paint = paint;
		}
		public void draw(Canvas canvas, MapView mapView, boolean shadow)
		{
			Log.i("hongzheng.fp", "polyline draw method");
			if(!shadow)
			{
				Projection projection = mapView.getProjection();
				if(points != null)
				{
					if(points.size() >= 2)
					{
						Log.i("hongzheng.fp", "points size: " + points.size());
						Point start = projection.toPixels(points.get(0), null);
						for (int i = 1; i < points.size(); i++) { 
							Log.i("hongzheng.fp", "drawing point: " + i);
	                        Point end = projection.toPixels(points.get(i), null);  
	                        canvas.drawLine(start.x, start.y, end.x, end.y, paint);// 绘制到canvas上即可  
	                        start = end;  
	                    } 
					}
				}
			}
		}
	}	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maplayout);
		Log.i("hongzheng.fp", "workout map onCreate");
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedOverlay = new MapItemizedOverlay(drawable, this);
		
		points = new ArrayList<GeoPoint>();
		polyline = new Polyline(points);
//		mapOverlays.add(polyline);
//		mapView.invalidate();
		
//		GeoPoint point = new GeoPoint(37733330, -122475420);
//		OverlayItem overlayItem = new OverlayItem(point, "hello", "sf");
//		itemizedOverlay.addOverlay(overlayItem);
//		mapOverlays.add(itemizedOverlay);
//		points.add(point);
//		
//		GeoPoint point2 = new GeoPoint(37733360, -122475450);
//		OverlayItem overlayItem2 = new OverlayItem(point2, "hello", "sf");
//		itemizedOverlay.addOverlay(overlayItem2);
//		points.add(point2);
	
		mapOverlays.add(polyline);
		mapView.invalidate();
		
		mapLocationManager = 
			(LocationManager) WorkoutMap.this.getSystemService(Context.LOCATION_SERVICE);
		mapLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mapLocationListener);
		

	}
	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
