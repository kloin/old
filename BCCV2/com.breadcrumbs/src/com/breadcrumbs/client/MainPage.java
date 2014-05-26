package com.breadcrumbs.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.breadcrumbs.client.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import Location.CanvasLocationManager;
import Network.OData;
import ServiceProxy.MasterProxy;

public class MainPage extends FragmentMaster {

	protected static final String MESSAGE = null;
	private AsyncTask userData;
	private LinearLayout storyBoard;
	JSONObject tempJsonNode;
	private JSONObject json;
	private int i = 0;
	
	private MasterProxy clientRequestProxy;
	
	private static ViewPager viewPager;
	private TabsAdapter mAdapter;
	private ActionBar actionBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//Start getting the data at the very start. This HAS to be at the to be above the creation of storyBoard
        clientRequestProxy = MasterProxy.GetProxyInstance(this);
        clientRequestProxy.GetAllTrailsForUser("0");
        setContentView(R.layout.screen_container);
    	
        //Start the grabbing locations

 		viewPager = (ViewPager) findViewById(R.id.pager);
 		mAdapter = new TabsAdapter(getSupportFragmentManager());
 		actionBar = getActionBar();
 		actionBar.hide();
 		viewPager.setAdapter(mAdapter);
 		actionBar.setHomeButtonEnabled(false);
 		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
 		viewPager.setCurrentItem(0);
 		viewPager.setOffscreenPageLimit(3);
     		/**
     		 * on swiping the viewpager make respective tab selected
     		 * */
     		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

     			@Override
     			public void onPageSelected(int position) {
     				
     				try {
     					// on changing the page
     					// make respected tab selected
     					actionBar.setSelectedNavigationItem(position);
     				} catch(Exception ex) {
     					System.out.println("And exception has been thrown by ViewPager: " + ex);
     					//An exception is thrown here that stops the swipe working. Need to sort this out.
     				}
     			}


     			@Override
     			public void onPageScrollStateChanged(int arg0) {
     				
     			}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
     		});
     		
     		
    }
    
    public static void setViewPager(int view) {
    	viewPager.setCurrentItem(view);
    }
    @Override
    public void Notify(JSONObject json) {
    	//DEBUGGER
    	System.out.println("App has been given this data: ");
    	//Try loading our data
		storyBoard = (LinearLayout)findViewById(R.id.storyBoard);
		addClickListeners();
		LoadCrumbs(json);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Create an item (e.g a crumb or a trail) Called sbo for ease of typing
    private void createStoryObject(int index, String storyTitle, String storyDescription, String trailId) {
    	//Infate that shit
    	LayoutInflater inflater = getLayoutInflater();
    	//Create a base object, which we will attatch to the storyboard, and later manipulate
    	View sbo = inflater.inflate(R.layout.story_board_item, null);
    	TextView title = (TextView) sbo.findViewById(R.id.Title);
    	final TextView description = (TextView) sbo.findViewById(R.id.Description);
    	title.setText(storyTitle);
    	description.setText(storyDescription);
    	description.setTag(trailId);
    	storyBoard.addView(sbo);
    	
    	//set click listener to load map page. Needs to change so that it relates to the correct id.
    	description.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	//Launch crumbs view
    	    	System.out.println("Launching crumb intent");
    	    	Intent trailIntent = new Intent();   
    	    	trailIntent.setClassName("com.breadcrumbs.client", "com.breadcrumbs.client.TrailMapViewer");
    	    	String trailId = description.getTag().toString();
 
    	    	trailIntent.putExtra("message", trailId);
    	    	startActivity(trailIntent);
    	    }
    	});
    }
   	private void addClickListeners() {
		//Add new
		ImageButton btn = (ImageButton) findViewById(R.id.addButton);
			//Add camera
			 //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		     //startActivityForResult(intent, 1);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        //Set us to the add screen
	     		viewPager.setCurrentItem(2);
	     		//lientRequestProxy.AddCrumb("Shiiiit", "Hope this works", "0", "broken");
		    }
		});
	}
   	
	//Load up all the crumbs
	public void LoadCrumbs(JSONObject result) {
		//lets do it
		try {
   	 		i = 0;
   	 		while (i < result.length()) {
        		System.out.println(result.length());
    			//Iterate through the list
				JSONObject singleNode = new JSONObject(result.get("Node" + i).toString());
				System.out.println("singleNode: " + singleNode);
				
				//Get details.
				String desc = singleNode.getString("Description"); //Is description just going to be a dateTime?
				String tit = singleNode.getString("TrailName");
				//We need to know this to get crumbs/data about the trail.
				//We cannot pull down all the crumbs for every trail on load.
				String id = singleNode.getString("trailId");
            	createStoryObject(i, tit, desc, id);
        		i += 1;
        	}
		} catch(Exception ex) {
			//exception occured creating data
			System.out.println("Exception occured creating data: ");
			ex.printStackTrace();
		}		
	}
}


/*
  
        */

	

