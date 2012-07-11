package hongzheng.fp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> overlays =
		new ArrayList<OverlayItem>();
	private Context mContext;
	
	public MapItemizedOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}
	public MapItemizedOverlay(Drawable defaultMarker, Context context)
	{//set up the ability to handle touch events on the overlay items.
		//need a reference to the application context as a member of this class
		//see onTap(int) method
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	protected boolean onTap(int index)
	{
		//onTap(int) method handles the event when an item is tapped
		//by the user
		OverlayItem item = overlays.get(index);
		AlertDialog.Builder dialog = 
			new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay)
	{
		overlays.add(overlay);
		populate();
	}
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return overlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlays.size();
	}
	

}
