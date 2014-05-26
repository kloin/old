package com.breadcrumbs.client;

import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;
/*
 * This is an abstract method to contain methods needed for my view classes, that are needed
 * across all pages but are not in FragmentActivity
 */
public class FragmentMaster extends FragmentActivity{

	public void Notify(JSONObject jsonResponse) {
		//ABSTRACT METHOD
	}
}
