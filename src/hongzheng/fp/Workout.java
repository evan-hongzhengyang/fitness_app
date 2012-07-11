package hongzheng.fp;

public class Workout 
{
	private long id;
	private long steps;
	private String starttime;
	private double distance;
	private double duration;
	private double avgspeed;
	private String uploaded;
	private String startlocation;
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getStartLocation() {
		return startlocation;
	}
	public void setStartLocation(String startLocation) {
		this.startlocation = startLocation;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	
	public long getSteps() {
		return steps;
	}
	public void setSteps(long steps) {
		this.steps = steps;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getAvgspeed() {
		return avgspeed;
	}
	public void setAvgspeed(double avgspeed) {
		this.avgspeed = avgspeed;
	}
	public String getUploaded() {
		return uploaded;
	}
	public void setUploaded(String uploaded) {
		this.uploaded = uploaded;
	}
	public String toString()
	{
		return steps + "; " + starttime + "; " + 
				distance + "; " + duration + "; " + avgspeed + 
				"; " + uploaded + "; " +
				startlocation;
//		return this.path;
	}
	

}
