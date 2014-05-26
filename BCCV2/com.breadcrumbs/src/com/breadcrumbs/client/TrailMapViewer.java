package com.breadcrumbs.client;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ServiceProxy.MasterProxy;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


public class TrailMapViewer extends FragmentMaster implements OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener{
    private GoogleMap mMap;
    private JSONObject json;
	private MasterProxy clientRequestProxy;
	private PopupWindow popUp;
	private LinearLayout parent; 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
     
     setContentView(R.layout.main_page);
     //Start getting the data at the very start.
     clientRequestProxy = MasterProxy.GetProxyInstance(this);
     parent = (LinearLayout) findViewById(R.id.mapHolder);
     popUp = new PopupWindow(this);
     //Set up bundle to retrieve extras from, and trail to set
     Bundle extras;
     String trailId;
     extras = getIntent().getExtras();
     
     //Check if extras found anything. If not, let the console/user know
     if(extras == null) {
     	System.out.println("Cant find the id for a trail");
         trailId = null;
     } else {
         trailId = extras.getString("message");
     }
     
     //Get our data
     System.out.println(trailId);
     clientRequestProxy.GetAllCrumbsForTrail(trailId);

	}
	private void parseAndDisplayData() {
		Iterator<String> jsonKeyIterator = json.keys();
		while(jsonKeyIterator.hasNext()) {
			JSONObject node;
			
			try {
				node = json.getJSONObject(jsonKeyIterator.next());
				displayData(node);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	//Show all the points on the map
	private void displayData(JSONObject nodeToDisplay) {
	     double Latitude = 0;
	     double Longitude = 0;
	     String Title = "Hello World";
	     System.out.println("Node is: " + nodeToDisplay.toString());
	     try {
			Latitude = Double.valueOf(nodeToDisplay.getString("Latitude"));
			Longitude = Double.valueOf(nodeToDisplay.getString("Longitude"));
			Title = nodeToDisplay.getString("Title");			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	     LatLng theBurn = new LatLng(Latitude, Longitude);
	     mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	     mMap.setOnMarkerClickListener(this);
	     //Show the shit on the map
	     mMap.setMyLocationEnabled(true);

	     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(theBurn, 13));
	     mMap.addMarker(new MarkerOptions()
	             .position(theBurn)
	             .title(Title));
	}
	
	public void Notify(JSONObject jsonResponse) {
		//DEBUGGER
    	json = jsonResponse;
        parseAndDisplayData();
    	
	}
	@Override
	public boolean onMarkerClick(Marker marker) {
		System.out.println("Pressed");
		//Popup window with image

        popUp.showAtLocation(parent, Gravity.BOTTOM, 10, 10);
        popUp.update(50, 50, 300, 80);

		return true;
	}
	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		
	}
	
	
}
