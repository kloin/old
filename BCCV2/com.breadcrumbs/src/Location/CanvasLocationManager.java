package Location;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class CanvasLocationManager extends Service implements LocationListener{
		private LocationManager locationManager;
		private Context context;
		private static CanvasLocationManager clmInstance;
		
		private CanvasLocationManager() {
			//Making constructor private.
		}
		
		public static CanvasLocationManager GetCanvasLocationManager() {
			if (clmInstance == null) {
				clmInstance = new CanvasLocationManager();
			}
			
			return clmInstance;
		}
		
		public void StartListening(Context context) {

	    	// Acquire a reference to the system Location Manager
	    	locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

	    	// Define a listener that responds to location updates
	    	LocationListener locationListener = new LocationListener() {
	    	    public void onLocationChanged(Location location) {
	    	      // Called when a new location is found by the network location provider.
	    	    }

	    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    	    public void onProviderEnabled(String provider) {}

	    	    public void onProviderDisabled(String provider) {}
	    	  };

	    	// Register the listener with the Location Manager to receive location updates
	    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	    }
		//Get our latest Latitude
		public double GetLatitude() {
			return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		}
		
		//Get our latest Longitude
		public double GetLongitude() {
			return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
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

		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
}	
