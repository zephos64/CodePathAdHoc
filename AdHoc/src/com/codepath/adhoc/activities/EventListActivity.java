package com.codepath.adhoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.SupportFragmentTabListener;
import com.codepath.adhoc.fragments.AllEventsFragment;
import com.codepath.adhoc.fragments.CreateEventDataFragment;
import com.codepath.adhoc.fragments.CreateEventMapFragment;
import com.codepath.adhoc.fragments.EventListFragment;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class EventListActivity extends ActionBarActivity {
	// list sorted by % full (fuller = better)
	// then by what's happening soonest

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		
		AdHocUtils.forceShowActionBar(this);
		setupTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_create_event:
			Intent iCreate = new Intent(this, CreateEventActivity.class);
			startActivity(iCreate);
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
				.setText("All")
				.setTag("All")
				.setTabListener(
						new SupportFragmentTabListener<AllEventsFragment>(
								R.id.flEventList, this, "All",
								AllEventsFragment.class));
		actionBar.addTab(tab1);		
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Joined")
				.setTag("Joined")
				.setTabListener(
						new SupportFragmentTabListener<EventListFragment>(
								R.id.flEventList, this, "Joined",
								EventListFragment.class));
		actionBar.addTab(tab2);
		
		Tab tab3 = actionBar
				.newTab()
				.setText("Hosted")
				.setTag("Hosted")
				.setTabListener(
						new SupportFragmentTabListener<EventListFragment>(
								R.id.flEventList, this, "Hosted",
								EventListFragment.class));
		actionBar.addTab(tab3);
	}
}
