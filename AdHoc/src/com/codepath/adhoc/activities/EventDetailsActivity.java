package com.codepath.adhoc.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.adhoc.R;
import com.parse.ParseUser;

public class EventDetailsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_create_event:
			Intent iCreate = new Intent(this, CreateEventActivity.class);
			startActivity(iCreate);
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
			break;
		default:
			break;
		}

		return true;
	}
}
