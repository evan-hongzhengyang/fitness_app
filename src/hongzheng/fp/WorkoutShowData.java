package hongzheng.fp;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class WorkoutShowData extends ListActivity{
	public ArrayAdapter<Workout> adapter;
	
	private Button refreshButton;
	private Button uploadButton;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showdatalayout);			
		
		refreshButton = (Button)this.findViewById(R.id.refresh);
		uploadButton = (Button)this.findViewById(R.id.upload);
		refreshButton.setOnClickListener(new RefreshListener());
		uploadButton.setOnClickListener(new UploadListener());
		
		
		
	}
	
	class RefreshListener implements OnClickListener
	{
		public void onClick(View view)
		{
//			Log.i("hongzheng.fp", "refresh data");
			WorkoutStatus.dataSource = new DataSource(WorkoutShowData.this);
			WorkoutStatus.dataSource.openDatabase();
			
			List<Workout> data = WorkoutStatus.dataSource.getAllWorkouts();
			adapter = new ArrayAdapter<Workout>
			(WorkoutShowData.this, android.R.layout.simple_list_item_1, data);
//			Log.i("hongzheng.fp", "set list adapter");
			setListAdapter(adapter);
			
			WorkoutStatus.dataSource.close();
		}
	}

	class UploadListener implements OnClickListener
	{
		public void onClick(View view)
		{			
			WorkoutStatus.dataSource = new DataSource(WorkoutShowData.this);
			WorkoutStatus.dataSource.openDatabase();
			Log.i("hongzheng.fp", "about to upload");
			WorkoutStatus.dataSource.upload(WorkoutShowData.this);
			WorkoutStatus.dataSource.close();
		}
	}
	
}
