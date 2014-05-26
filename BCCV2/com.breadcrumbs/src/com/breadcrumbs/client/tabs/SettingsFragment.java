package com.breadcrumbs.client.tabs;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.breadcrumbs.client.MainPage;
import com.breadcrumbs.client.R;

import Location.CanvasLocationManager;
import ServiceProxy.MasterProxy;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;


public class SettingsFragment extends Fragment {
	private View rootView;
	private MasterProxy clientServiceProxy;
	private Context context;
	private Map<String, String> map = new HashMap<String, String>();
	private static Bitmap media;
	private static ImageView iv;
	private CanvasLocationManager locationManager;
	/*
	 * Do as LITTLE as possible in the constructors. If possible, load at run-time
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.settings, container, false);
		context = rootView.getContext();
		
		locationManager = CanvasLocationManager.GetCanvasLocationManager();
		locationManager.StartListening(context);
		//Add our control over the screen
		addTapListeners();
		return rootView;
	}
	
	private void addTapListeners() {
		//adding listeners
		iv = (ImageView)rootView.findViewById(R.id.media);
		TextView newTrailButton = (TextView) rootView.findViewById(R.id.createTrail);
		newTrailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				createNewTrail();
			}
		});
		
		SwitchView();
		addMedia();
		
		}
	
	public static void SetMedia(Bitmap imageBitmap) {
		media = imageBitmap;
		iv.setImageBitmap(media);
		
	}
	private void addMedia() {
		final Button addMediaButton = (Button) rootView.findViewById(R.id.addMedia);
		addMediaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MainPage.setViewPager(3);
			}
		});
	}
	private void SwitchView() {
		final ToggleButton toggleButton = (ToggleButton) rootView.findViewById(R.id.trailAndCrumbToggle);
		toggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (toggleButton.isChecked()) {
					//Crumb state
					showCrumbView();
					GetDataForTrailSpinner();
				}
				else {

					showTrailView();
				}
			}
		});
		
	}
	/*
	 *  This shits temporary s0 who cares
	 */
	private void showCrumbView() {
		LinearLayout crumbAdd = (LinearLayout) rootView.findViewById(R.id.addCrumbLayout);
		crumbAdd.setVisibility(View.VISIBLE);
		LinearLayout trailAdd = (LinearLayout) rootView.findViewById(R.id.addTrailView);
		trailAdd.setVisibility(View.GONE);
	}
	
	private void showTrailView() {
		LinearLayout crumbAdd = (LinearLayout) rootView.findViewById(R.id.addCrumbLayout);
		crumbAdd.setVisibility(View.GONE);
		LinearLayout trailAdd = (LinearLayout) rootView.findViewById(R.id.addTrailView);
		trailAdd.setVisibility(View.VISIBLE);		
	}
	
	//Create a new trail based on the data entered in the field.
	private void createNewTrail() {
		final ToggleButton toggleButton = (ToggleButton) rootView.findViewById(R.id.trailAndCrumbToggle);
		String userId = "0";
		String trailId = "0"; //by default
		String description = "Shit that didnt work";
		//Check that we havent already created it.
		if (clientServiceProxy == null) {
			//QUESTION - should I be notifying all views??
			clientServiceProxy = MasterProxy.GetProxyInstance();
		}
		
		//The trail title. Should be trailTitleInput...
		EditText title = (EditText) rootView.findViewById(R.id.trailName);
		EditText crumb = (EditText) rootView.findViewById(R.id.crumbTitle);
		
		//If no crumb set, just create a trail
		if (!toggleButton.isChecked()) {
			String trailTitle = title.getText().toString();
			//Title, Description, userId
			clientServiceProxy.AddTrail(trailTitle, "testing", "0");
		}
		else {
			description = getDiscription();
			trailId = getTrailId();
			String latitude = String.valueOf(locationManager.GetLatitude());
			String longitude = String.valueOf(locationManager.GetLongitude());
			
			clientServiceProxy.AddCrumbToTrail(crumb.getText().toString(), 
					description, //Description
					userId, //userId
					trailId, //TrailId
					"c:/",
					latitude,
					longitude);
		}
		
	}
	private String getDiscription() {
		String description;
		EditText descText = (EditText) rootView.findViewById(R.id.crumbTitle);
		description = descText.getText().toString();
		return description;
	}
	
	private String getTrailId() {
		String id = "";
		Spinner spinner = (Spinner) rootView.findViewById(R.id.trailSpinner);
		String titleName = spinner.getSelectedItem().toString();
		id = map.get(titleName);
		return id;
	}
	
	public void GetDataForTrailSpinner() {
		
		ArrayList<String> array = new ArrayList<String>();
		Spinner s = (Spinner) rootView.findViewById(R.id.trailSpinner);
		JSONObject ourData = MasterProxy.GetProxyInstance().GetCachedData();	
		getNamesFromJSON(ourData);
		array.addAll(map.keySet());

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, array); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(spinnerArrayAdapter);

	}
	
	private void getNamesFromJSON(JSONObject json) {
		ArrayList<String> names = new ArrayList<String>();
		try {
   	 		int i = 0;
   	 		while (i < json.length()) {
        		System.out.println(json.length());
    			//Iterate through the list
				JSONObject singleNode = new JSONObject(json.get("Node" + i).toString());
				System.out.println("singleNode: " + singleNode);
				
				//Get name
				String tit = singleNode.getString("TrailName");
				//We need to know this to get crumbs/data about the trail.
				//We cannot pull down all the crumbs for every trail on load.
				String id = singleNode.getString("trailId");
				map.put(tit, id);
				i+= 1;
   	 		}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		//return names;
	}
	
}
