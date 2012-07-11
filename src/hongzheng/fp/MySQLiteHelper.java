package hongzheng.fp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_WORKOUTS = "workouts";//the name of the table
	
	public static final String COLUMN_ID = "_id";//1st column
	public static final String COLUMN_STEPS = "steps";//2nd column
	public static final String COLUMN_STARTTIME = "starttime";//3rd column
	public static final String COLUMN_DISTANCE = "distance";//4th column
	public static final String COLUMN_DURATION = "duration";//5th column
	public static final String COLUMN_AVGSPEED = "avgspeed";//6th column
	public static final String COLUMN_UPLOADED = "uploaded";//7th column
	public static final String COLUMN_STARTLOCATION = "startlocation";//8th column
	public static final String COLUMN_PATH = "path";//9th column
	
	public static final String DATABASE_NAME = "workouts.db";////database name
	public static final int DATABASE_VERSION = 1;//database version
	
	//table creation statement
	public static final String TABLE_CREATE = "create table "
				+ TABLE_WORKOUTS + "(" +COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_STEPS
				+ " integer, " + COLUMN_STARTTIME
				+ " text, " + COLUMN_DISTANCE
				+ " double, " + COLUMN_DURATION
				+ " double, " + COLUMN_AVGSPEED
				+ " double, " + COLUMN_UPLOADED
				+ " text, " + COLUMN_STARTLOCATION
				+ " text," + COLUMN_PATH
				+ " text);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_CREATE);//create the workouts table
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version"
						+ oldVersion + "to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
		onCreate(db);
	}

}
