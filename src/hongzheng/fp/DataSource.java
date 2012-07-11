package hongzheng.fp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource 
{
	private SQLiteDatabase database;
	private MySQLiteHelper helper;
	private String[] allColumns = {
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_STEPS,
			MySQLiteHelper.COLUMN_STARTTIME,
			MySQLiteHelper.COLUMN_DISTANCE,
			MySQLiteHelper.COLUMN_DURATION,
			MySQLiteHelper.COLUMN_AVGSPEED,
			MySQLiteHelper.COLUMN_UPLOADED,
			MySQLiteHelper.COLUMN_STARTLOCATION,
			MySQLiteHelper.COLUMN_PATH
	};
	
	public DataSource(Context context)
	{
		this.helper = new MySQLiteHelper(context);//this won't create a database
	}
	
	public void openDatabase()
	{
		database = this.helper.getWritableDatabase();//this will create a database
	}
	
	public void close()
	{
		this.helper.close();
	}
	
	public void createWorkout(long steps, String starttime, double distance, 
			double duration, double avgspeed, String uploaded, String startlocation, String path)
	{
//		Log.i("hongzheng.fp", "creating a workout");
		//put the key:value pairs into contentValues and then insert it into the database		
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteHelper.COLUMN_STEPS, steps);
		contentValues.put(MySQLiteHelper.COLUMN_STARTTIME, starttime);
		contentValues.put(MySQLiteHelper.COLUMN_DISTANCE, distance);
		contentValues.put(MySQLiteHelper.COLUMN_DURATION, duration);
		contentValues.put(MySQLiteHelper.COLUMN_AVGSPEED, avgspeed);
		contentValues.put(MySQLiteHelper.COLUMN_UPLOADED, uploaded);	
		contentValues.put(MySQLiteHelper.COLUMN_STARTLOCATION, startlocation);
		contentValues.put(MySQLiteHelper.COLUMN_PATH, path);
		database.insert(MySQLiteHelper.TABLE_WORKOUTS, null,
				contentValues);
		//query it, get the content
//		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
//				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//				null, null, null);
//		cursor.moveToFirst();
//		Workout newWorkout = cursorToWorkout(cursor);
//		Log.i("hongzheng.fp", "cursor close");
//		cursor.close();
//		return newWorkout;
	}
	
	public void upload(Context context)
	{
		//query, get all data that have not uploaded yet, upload them,
		//close the cursor;
		Log.i("hongzheng.fp", "getting cursor");
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS, 
				allColumns, MySQLiteHelper.COLUMN_UPLOADED + " = " + "\"notyet\"", 
				null, null, null, null);				
		
		//*************************
		cursor.moveToFirst();		
		List<Workout> workoutList = new ArrayList<Workout>();
		while(!cursor.isAfterLast())
		{
			Workout workout = cursorToWorkout(cursor);
			workoutList.add(workout);	
			cursor.moveToNext();
		}
		//***************************			
		cursor.close();
		Log.i("hongzheng.fp", "about to upload");
		UploadTask uploadTask = new UploadTask(workoutList, context);
		uploadTask.execute("upload");
	}
	public List<Workout> getAllWorkouts()
	{
//		Log.i("hongzheng.fp", "get all workouts");
		List<Workout> workouts = new ArrayList<Workout>();
		Cursor cursor = this.database.query(MySQLiteHelper.TABLE_WORKOUTS, allColumns, 
										null, null, null, null, null);
		Log.i("hongzheng.fp", "count: " + String.valueOf(cursor.getCount()));
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Workout workout = cursorToWorkout(cursor);
//			Log.i("hongzheng.fp", workout.toString());
			workouts.add(workout);
			cursor.moveToNext();
		}
		cursor.close();
		return workouts;
	}

	private Workout cursorToWorkout(Cursor cursor)
	{
//		Log.i("hongzheng.fp", "cursor to workout");
		Workout workout = new Workout();
		workout.setId(cursor.getLong(0));//set the workout id
		workout.setSteps(cursor.getLong(1));//set the workout type
		workout.setStarttime(cursor.getString(2));//set the workout starttime
		workout.setDistance(cursor.getDouble(3));//set the workout distance
		workout.setDuration(cursor.getDouble(4));//set the workout duration
		workout.setAvgspeed(cursor.getDouble(5));//set the workout avgspeed
		workout.setUploaded(cursor.getString(6));//set the workout uploaded
		workout.setStartLocation(cursor.getString(7));// set the workout startlocation
		workout.setPath(cursor.getString(8));//set the workout path
//		Log.i("hongzheng.fp", workout.toString());
		return workout;
	}

}
