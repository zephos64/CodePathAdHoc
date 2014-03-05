package com.codepath.adhoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.fragments.CreateEventDataFragment;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateEventActivity extends ActionBarActivity {
	Events newEvent;
	
	ProgressBar pbSpinnerSaving;
	TextView tvSavingText;
	LinearLayout llSaving;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		pbSpinnerSaving = (ProgressBar) findViewById(R.id.pbSpinnerSaving);
		tvSavingText = (TextView) findViewById(R.id.tvSavingText);
		llSaving = (LinearLayout) findViewById(R.id.llSaving);
		hideProgressBar();
		
		CreateEventDataFragment data;
		FragmentManager fm = getSupportFragmentManager();
		if(savedInstanceState != null) {
			data = (CreateEventDataFragment)fm.findFragmentByTag("createData");
		} else {
			data = new CreateEventDataFragment();
			FragmentTransaction fts = fm.beginTransaction();
			fts.add(R.id.flContainerEvent, data, "createData");
			fts.commit();
		}
		
		AdHocUtils.forceShowActionBar(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
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
			overridePendingTransition(R.anim.expand_in, R.anim.shrink_out);
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
	
	public void clickSave(View v) {
		Log.d("DEBUG", "Saving new event...");
		showProgressBar();
		
		FragmentManager fm = getSupportFragmentManager();
		CreateEventDataFragment dataAct = (CreateEventDataFragment)fm.getFragments().get(0);		
		if(!dataAct.checkData()) {
			hideProgressBar();
			return;
		}
		
		newEvent = dataAct.getEvent();

		newEvent.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if(e == null) {
					Log.d("DEBUG", "Saving host...");
					User hostUser = (User)ParseUser.getCurrentUser();

					hostUser.addEventsHosting(newEvent);
					hostUser.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							Log.d("DEBUG", "Save completed for host");
							Log.d("DEBUG", "Save completed for event");
							hideProgressBar();
							
							Intent listIntent = new Intent(getBaseContext(), EventListActivity.class);
							listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							listIntent.putExtra("EXIT", true);
							startActivity(listIntent);
						}
					});
		        } else {
		            Log.e("ERROR", "Error: " + e.getMessage());
		        }	
			}
		});
	}
	
	private void hideProgressBar() {
		pbSpinnerSaving.setVisibility(View.INVISIBLE);
		tvSavingText.setVisibility(View.INVISIBLE);
		llSaving.setVisibility(View.INVISIBLE);
	}

	private void showProgressBar() {
		pbSpinnerSaving.setVisibility(View.VISIBLE);
		tvSavingText.setVisibility(View.VISIBLE);
		llSaving.setVisibility(View.VISIBLE);
	}
}
