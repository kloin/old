package com.breadcrumbs.client;

import com.breadcrumbs.client.tabs.AddFragment;
import com.breadcrumbs.client.tabs.HomePageFragment;
import com.breadcrumbs.client.tabs.SettingsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {

	public TabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Movies fragment activity
			return new HomePageFragment();
			
		case 1:
			// Games fragment activity
			return new  AddFragment();	

		case 2:
			// Top Rated fragment activity
			return new SettingsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
