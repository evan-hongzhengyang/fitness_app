package hongzheng.fp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

public class WorkoutStatus extends Activity {

	public static DataSource dataSource = null;
	
	private static final double EARTH_RADIUS = 6378.1;
	
	private SensorManager sensorManager;
	private MyAccelerometerListener myAccelerometerListener = 
				new MyAccelerometerListener(this);
	private LocationManager locationManager;
	
	
	private Button saveButton;
	private Button startButton;
	private Button stopButton;
	
	public TextView starttimeView;
	public TextView stepsView;
	public TextView distanceView;
	public TextView durationView;
	public TextView avgspeedView;
	public TextView currentspeedView;
	
	public String starttime;
	//****************************
	public double starttimeMillisecond;
	//*************************
	public long steps;	
	public double distance;
	public double duration;
	public double avgspeed;
	public double currentspeed;
	public String uploaded = "notyet";
	public long halfsteps;
	public DecimalFormat decimalFormat =
		new DecimalFormat("#.#####");
	public boolean firstLocation = true;
	public String startlocation;
	public Double startlocationLatitude;
	public Double startlocationLongitude;
	
	public long accelerometerLastUpdate;
	//avgspeedLastUpdate
	public static long avgspeedLastUpdate;
	
	//currentspeedLastUpdate
	public static long currentspeedLastUpdate;
	//currentspeedLastLocation
	public double currentspeedLastLatitude;
	public double currentspeedLastLongitude;
	//store all the path location info into this list
	public List<Double> path = new ArrayList<Double>();
	//the boolean to tell if the map should plot the route of the running
	public static boolean isRunning = false;
	
	private LocationListener locationListener =
		new LocationListener()
	{
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub		
			
			Double lat = new Double(decimalFormat.format(location.getLatitude()));
			Double lng = new Double(decimalFormat.format(location.getLongitude()));
//			double alt = location.getAltitude();
			if(firstLocation)
			{				
				startlocationLatitude = lat;				
				startlocationLongitude = lng;
				//add the first latitude and longitude to the path list
				path.add(lat);
				path.add(lng);
				Log.i("hongzheng.fp", "first location: ");				
				startlocation = String.valueOf(startlocationLatitude + "," + startlocationLongitude);
				Log.i("hongzheng.fp", startlocation);
				firstLocation = false;
				
				currentspeedLastLatitude = lat;
				currentspeedLastLongitude = lng;
			}
			
			//calculate the distance
			//the current time
			long actualTime = System.currentTimeMillis();
			if((actualTime - currentspeedLastUpdate) > 10000)
			{
				//calculate the currentspeed
				double deltaSigma = vincenty(currentspeedLastLatitude,
											currentspeedLastLongitude,
											lat,
											lng);
				long timeNow = System.currentTimeMillis();
				currentspeed = (d(EARTH_RADIUS, deltaSigma) / ((timeNow - currentspeedLastUpdate) * 1000)) * 3600;	
				currentspeedLastLatitude = lat;
				currentspeedLastLongitude = lng;
				currentspeedLastUpdate = timeNow;
				WorkoutStatus.this.currentspeedView.setText(String.valueOf(currentspeed));
			}
			if((actualTime - avgspeedLastUpdate) > 20000)
			{
				path.add(lat);
				path.add(lng);
				//calculate the avgspeed
//				double deltaSigma = vincenty(startlocationLatitude,
//											startlocationLongitude,
//											lat,
//											lng);
				double deltaSigma = vincenty(Math.toRadians(path.get(path.size()-4)),
						Math.toRadians(path.get(path.size()-3)),
						lat,
						lng);
				Log.i("hongzheng.fp", "path.size()-4: " + String.valueOf(path.get(path.size()-4)));
				Log.i("hongzheng.fp", "path.size()-3: " + String.valueOf(path.get(path.size()-3)));
				Log.i("hongzheng.fp", "lat: " + String.valueOf(lat));
				Log.i("hongzheng.fp", "lng: " + String.valueOf(lng));
				
//				double deltaSigma = vincenty(37.73333,
//						-122.47539,
//						37.73334,
//						-122.47540);
				double deltaDistance = d(EARTH_RADIUS,deltaSigma);
//				double deltaDistance = distance(path.get(path.size()-4), path.get(path.size()-3), lat, lng);
				//add the distance
				distance = distance + deltaDistance;
				//show the distance in the UI
				WorkoutStatus.this.distanceView.setText(String.valueOf(distance));
				long timeNow = System.currentTimeMillis();
				avgspeed = (/*d(EARTH_RADIUS, deltaSigma)*/deltaDistance / ((timeNow - avgspeedLastUpdate) * 1000)) * 3600;
				Log.i("hongzheng.fp", "avgspeed: " + String.valueOf(avgspeed));
				avgspeedLastUpdate = timeNow;
				
				WorkoutStatus.this.avgspeedView.setText(String.valueOf(avgspeed));
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
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub			
		}		
	};	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statuslayout);		
		
		saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new SaveListener());
		startButton = (Button)this.findViewById(R.id.startButton);
		startButton.setOnClickListener(new StartListener());
		stopButton = (Button)this.findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new StopListener());
		
		this.starttimeView = (TextView)this.findViewById(R.id.starttimeValue);
		this.stepsView = (TextView)this.findViewById(R.id.stepsValue);
		this.distanceView = (TextView)this.findViewById(R.id.distanceValue);
		this.durationView = (TextView)this.findViewById(R.id.durationValue);
		this.avgspeedView = (TextView)this.findViewById(R.id.avgspeedValue);
		this.currentspeedView = (TextView)this.findViewById(R.id.currentspeedValue);
	}
	class SaveListener implements OnClickListener
	{
		public void onClick(View view)
		{		
			//create the database and get the database
			WorkoutStatus.dataSource = new DataSource(WorkoutStatus.this);
			WorkoutStatus.dataSource.openDatabase();
			//put all the key:value pairs except the id pair into the contentValues
			Log.i("hongzheng.fp", "save workout");
			
			//the path data should be like this: lat1, lng1, lat2, lng2, lat3, lng3
			String pathString = (path.toString()).substring(1, path.toString().length()-1);
			
			dataSource.createWorkout(steps, starttime, distance, duration, 
					avgspeed, uploaded, startlocation, pathString);		
			WorkoutStatus.dataSource.close();
			
		}
	}

	class StartListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			isRunning = true;
			// TODO Auto-generated method stub
			sensorManager = (SensorManager)WorkoutStatus.this.getSystemService(SENSOR_SERVICE);
            sensorManager.registerListener(WorkoutStatus.this.myAccelerometerListener, 
            		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            		SensorManager.SENSOR_DELAY_NORMAL);
            accelerometerLastUpdate = System.currentTimeMillis();
            avgspeedLastUpdate = System.currentTimeMillis();
            currentspeedLastUpdate = System.currentTimeMillis();
            
            WorkoutStatus.this.locationManager = 
            	(LocationManager) WorkoutStatus.this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            
            WorkoutStatus.this.starttime = Calendar.getInstance().getTime().toString();
            WorkoutStatus.this.starttimeView.setText(WorkoutStatus.this.starttime);
            WorkoutStatus.this.halfsteps = 0;
            WorkoutStatus.this.steps = 0;
            WorkoutStatus.this.stepsView.setText(String.valueOf(WorkoutStatus.this.steps));
            WorkoutStatus.this.distance = 0;
            WorkoutStatus.this.distanceView.setText(String.valueOf(WorkoutStatus.this.distance));
        	WorkoutStatus.this.duration = 0;
        	WorkoutStatus.this.durationView.setText(String.valueOf(WorkoutStatus.this.duration));        	
        	WorkoutStatus.this.avgspeed = 0;
        	WorkoutStatus.this.avgspeedView.setText(String.valueOf(WorkoutStatus.this.avgspeed));
        	WorkoutStatus.this.currentspeed = 0;
        	WorkoutStatus.this.currentspeedView.setText(String.valueOf(WorkoutStatus.this.currentspeed));
        	//the start time
        	starttimeMillisecond = System.currentTimeMillis();
        	WorkoutMap.points = new ArrayList<GeoPoint>();
		}
		
	}
	
	class StopListener implements OnClickListener
	{
		public void onClick(View view)
		{
			isRunning = false;
			sensorManager.unregisterListener(WorkoutStatus.this.myAccelerometerListener, 
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));			
			locationManager.removeUpdates(locationListener);
			//update the duration time
			long actualTime = System.currentTimeMillis();
			//this is the duration
			duration = actualTime - starttimeMillisecond;
			double stopLatitude = path.get(path.size()-2);
			double stopLongitude = path.get(path.size()-1);
//			distance = vincenty(startlocationLatitude, startlocationLongitude, 
//					stopLatitude, stopLongitude);
			long hours;
			long minutes;
			long seconds;
		}
	}
	
	private double arccos(double num)
	{
		return Math.acos(num);
	}
	private double sin(double num)
	{
		return Math.sin(num);
	}
	private double cos(double num)
	{
		return Math.cos(num);
	}
	private double d(double r, double deltaSigma)
	{
		return r*deltaSigma;
	}
	private double arcsin(double num)
	{
		return Math.asin(num);
	}
	private double sin2(double num)
	{
		return Math.sin(num)*Math.sin(num);
	}
	private double arctan(double num1, double num2)
	{
		return Math.atan2(num1, num2);
	}
	private double vincenty(double LATITUDE, double LONGITUDE,
			double latitude, double longitude)
	{
		//LATITUDE o, LONGITUDE ^: s
		//latitude o, longitude ^: f
		double y1 = cos(latitude)*sin(longitude-LONGITUDE);
		double y2 =
			cos(LATITUDE)*sin(latitude)-
			sin(LATITUDE)*cos(latitude)*cos(longitude-LONGITUDE);
		double x1 = sin(LATITUDE)*sin(latitude);
		double x2 = 
			cos(LATITUDE)*cos(latitude)*cos(longitude-LONGITUDE);
		return
		arctan(Math.sqrt(y1*y1+y2*y2), x1+x2);
	}
	
	public double distance(Double lat1, Double lng1, Double lat2, Double lng2)
	{
		double dLat = Math.toRadians(lat1 - lat2);
		double dLng = Math.toRadians(lng1 - lng2);
		
		
		double a = sin(dLat/2) * sin(dLat/2) +
					sin(dLng/2) * sin(dLng/2) * cos(lat1) * cos(lat2);
		double c = 2 * arctan(Math.sqrt(a), Math.sqrt(1-a));
		return EARTH_RADIUS * c;
		
	}
}
