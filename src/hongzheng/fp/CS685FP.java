package hongzheng.fp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class CS685FP extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //the resource object in order to get drawables
        Resources resources = this.getResources();
        //activity tabHost
        TabHost tabHost = this.getTabHost();
        //tabSpec for each tab
        TabHost.TabSpec spec;
        //intent for each tab
        Intent intent;
        
        //for each tab, create an intent to launcg the activity
        //and then do the initialization of the tabSpec on
        //every tab and put it into the tabHost
        //the status tab
        intent = new Intent().setClass(this, WorkoutStatus.class);        
        spec = 
        	tabHost.newTabSpec("workoutstatus")
        					.setIndicator("workoutstatus",
        						resources.getDrawable(R.drawable.status))
        							.setContent(intent);
        tabHost.addTab(spec);
        
        //the map tab
        intent = new Intent().setClass(this, WorkoutMap.class);
        spec =
        	tabHost.newTabSpec("workoutmap")
        				.setIndicator("workoutmap",
        					resources.getDrawable(R.drawable.map))
        						.setContent(intent);
        tabHost.addTab(spec);
        
        //the showdata tab
        intent = new Intent().setClass(this, WorkoutShowData.class);
        spec =
        	tabHost.newTabSpec("workoutshowdata")
        				.setIndicator("workoutshowdata",
        						resources.getDrawable(R.drawable.showdata))
        							.setContent(intent);
        
        tabHost.addTab(spec);
      //show the first tab at the beginning
        tabHost.setCurrentTab(0);
    }
}