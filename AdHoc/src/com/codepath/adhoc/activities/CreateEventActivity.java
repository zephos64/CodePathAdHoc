package com.codepath.adhoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.adhoc.R;
import com.codepath.adhoc.SupportFragmentTabListener;
import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.fragments.CreateEventDataActivity;
import com.codepath.adhoc.fragments.CreateEventMapActivity;
import com.parse.ParseUser;

public class CreateEventActivity extends ActionBarActivity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setupTabs();
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

		Tab tab2 = actionBar
				.newTab()
				.setText("Map")
				.setTag("Map")
				.setTabListener(
						new SupportFragmentTabListener<CreateEventMapActivity>(
								R.id.flContainerEvent, this, "Map",
								CreateEventMapActivity.class));
		actionBar.addTab(tab2);
	}
}
