package com.codepath.adhoc.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.SupportFragmentTabListener;
import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.fragments.CreateEventDataActivity;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateEventActivity extends ActionBarActivity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//setupTabs();
		setupFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_list_events:
			Intent iList = new Intent(this, EventListActivity.class);
			startActivity(iList);
			break;
		case R.id.action_map:
			Intent iMap = new Intent(this, LocationActivity.class);
			startActivity(iMap);
			break;
		case R.id.action_logout:
			ParseUser.logOut();
			ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
			Intent intLogOut = new Intent(this, MainActivity.class);
			startActivity(intLogOut);
			break;
		case R.id.action_gotodetails:
			//TODO remove
			Intent iTest = new Intent(this, EventDetailsActivity.class);
			startActivity(iTest);
			break;
		default:
			break;
		}

		return true;
	}
	
	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
				.newTab()
				.setText("Data")
				.setTag("Data")
				.setTabListener(
						new SupportFragmentTabListener<CreateEventDataActivity>(
								R.id.flContainerEvent, this, "Data",
								CreateEventDataActivity.class));
		actionBar.addTab(tab1);		
		actionBar.selectTab(tab1);
//
//		Tab tab2 = actionBar
//				.newTab()
//				.setText("Map")
//				.setTag("Map")
//				.setTabListener(
//						new SupportFragmentTabListener<CreateEventMapActivity>(
//								R.id.flContainerEvent, this, "Map",
//								CreateEventMapActivity.class));
//		actionBar.addTab(tab2);
	}
	
	private void setupFragment() {
		CreateEventDataActivity data = new CreateEventDataActivity();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fts = fm.beginTransaction();
		fts.replace(R.id.flContainerEvent, data);
		fts.commit();
	}
	
	public void clickSave(View v) {
		Log.d("DEBUG", "Saving new event...");
		FragmentManager fm = getSupportFragmentManager();
		CreateEventDataActivity dataAct = (CreateEventDataActivity)fm.getFragments().get(0);		
		dataAct.checkData();
		//CreateEventMapActivity dataMap = (CreateEventMapActivity)fm.getFragments().get(1);
		// not sure if above line is correct - Ryan
		//dataMap.checkData();
		
		final Events newEvent = dataAct.getEvent();
		//Events newMap = dataMap.getEvent();

		newEvent.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null) {
					Log.d("DEBUG", "Saving host...");
					User hostUser = (User)ParseUser.getCurrentUser();
					hostUser.addEventHosting(newEvent);
					hostUser.saveInBackground();
					Log.d("DEBUG", "Save completed for host");
		        } else {
		            Log.e("ERROR", "Error: " + e.getMessage());
		        }
				
			}
		});

		Log.d("DEBUG", "Save completed for event");
		
		Intent listIntent = new Intent(this, EventListActivity.class);
		startActivity(listIntent);
	}
}
