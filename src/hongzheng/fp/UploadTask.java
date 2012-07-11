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
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UploadTask extends AsyncTask<String, Void, String> 
{	
//	private SQLiteDatabase database;
//	private Cursor cursor = null;
	private List<Workout> workoutList;
	private Context context;
	
	public UploadTask(List<Workout> workoutList, Context context)
	{		
		this.workoutList = workoutList;
		this.context = context;
	}
		
	
	private boolean upload(Workout workout)
	{		
		HttpClient httpClient = new DefaultHttpClient();
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair
				("id", String.valueOf(workout.getId())));
		pairList.add(new BasicNameValuePair
				("steps", String.valueOf(workout.getSteps())));
		pairList.add(new BasicNameValuePair
				("starttime", workout.getStarttime()));
		pairList.add(new BasicNameValuePair
				("distance", String.valueOf(workout.getDistance())));
		pairList.add(new BasicNameValuePair
				("duration", String.valueOf(workout.getDuration())));
		pairList.add(new BasicNameValuePair
				("avgspeed", String.valueOf(workout.getAvgspeed())));
		pairList.add(new BasicNameValuePair
				("startlocation", workout.getStartLocation()));
		pairList.add(new BasicNameValuePair
				("path", workout.getPath()));
		Log.i("hongzheng.fp", "id: " + workout.getId());
		Log.i("hongzheng.fp", "steps: " + workout.getSteps());
		Log.i("hongzheng.fp", "starttime: " + workout.getStarttime());
		Log.i("hongzheng.fp", "distance: " + workout.getDistance());
		Log.i("hongzheng.fp", "duration: " + workout.getDuration());
		Log.i("hongzheng.fp", "avgspeed: " + workout.getAvgspeed());
		Log.i("hongzheng.fp", "startlocation: " + workout.getStartLocation());
		Log.i("hongzheng.fp", "path: " + workout.getPath());
		//**********************
		HttpPost httpPost = 
			new HttpPost("http://workoutwatcher.appspot.com/uploadtask");
		UrlEncodedFormEntity entity = null;
		try {
			entity = 
				new UrlEncodedFormEntity(pairList, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		httpPost.setEntity(entity);
		
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		Log.i("hongzheng.fp", "status code: " + String.valueOf(statusCode));
		if((statusCode>=200) && (statusCode<300))
		{
			return true;
		}else
			return false;
	}
	
	protected String doInBackground(String...param)
	{		
		String response = "";
		
		for(Workout workout : workoutList)
		{
			boolean successful = upload(workout);
			Log.i("hongzheng.fp", "is successful?-> " + successful);
			if(successful)
			{
				Log.i("hongzheng.fp", "upload successful");
				MySQLiteHelper helper = new MySQLiteHelper(context);
				SQLiteDatabase database = helper.getWritableDatabase();
				ContentValues value = new ContentValues();
				value.put("uploaded", "\"already\"");
				database.update(MySQLiteHelper.TABLE_WORKOUTS, value, 
						MySQLiteHelper.COLUMN_ID + "=?", new String[]{String.valueOf(workout.getId())});
				helper.close();
			}
		}	
		
			
		return response;
		
	}
	
	protected void onPostExecute(String result)
	{
		Log.i("hongzheng.fp", "on post execute");
		Toast.makeText(this.context, "upload finish", Toast.LENGTH_LONG);
	}	

}
