package hongzheng.fp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyAccelerometerListener implements SensorEventListener 
{

	private WorkoutStatus workoutStatus;
//	private long halfsteps = 0;
	
	public MyAccelerometerListener(WorkoutStatus workoutStatus)
	{
		this.workoutStatus = workoutStatus;
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			getAccelerometer(event);
	}
	
	private void getAccelerometer(SensorEvent event)
	{
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];
		
		double accelerationValue = 
			Math.sqrt(x * x + y * y + z * z);
		double accelerationRatio = accelerationValue / SensorManager.GRAVITY_EARTH;
		long actualTime = System.currentTimeMillis();
		if(accelerationRatio > 2)
		{
			if((actualTime - this.workoutStatus.accelerometerLastUpdate) > 200)
			{
				this.workoutStatus.halfsteps++;
				this.workoutStatus.steps = this.workoutStatus.halfsteps / 2;
				this.workoutStatus.stepsView.setText(String.valueOf(this.workoutStatus.steps));
				//added this???
				this.workoutStatus.accelerometerLastUpdate = actualTime;
			}
		}
//		this.handDataCollector.accelerationView.setText(String.valueOf(accelerationValue));
//		if((actualTime - this.handDataCollector.lastUpdate) > 50)
//		{
//			this.collectedData.add(accelerationRatio);
//			this.handDataCollector.lastUpdate = actualTime;
//		}
	}

}
