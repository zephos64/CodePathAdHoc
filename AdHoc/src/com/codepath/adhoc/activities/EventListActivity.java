package com.codepath.adhoc.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.SupportFragmentTabListener;
import com.codepath.adhoc.fragments.AllEventsFragment;
import com.codepath.adhoc.fragments.AttendingEvents;
import com.codepath.adhoc.fragments.CreatedEvents;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

public class EventListActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	// list sorted by % full (fuller = better)
	// then by what's happening soonest

	LatLng userLoc;
	LocationClient locationclient;
	LocationRequest mLocationRequest;
	LinearLayout llProgressList;
	private long mLastPress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		AdHocUtils.forceShowActionBar(this);
		llProgressList = (LinearLayout) findViewById(R.id.llProgressList);
		getCurrentUserLoc();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Toast onBackPressedToast = Toast.makeText(this,
				"Press back once again to log out.", Toast.LENGTH_SHORT);
		long currentTime = System.currentTimeMillis();

		if (getIntent().getBooleanExtra("BACK", false)) {
			super.onBackPressed();
		} else {
			if (currentTime - mLastPress > 5000) {
				onBackPressedToast.show();
				mLastPress = currentTime;
			} else {
				if (getIntent().getBooleanExtra("EXIT", false)) {
					finish();
				} else {
					onBackPressedToast.cancel();
					super.onBackPressed();
				}
			}
		}
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
			overridePendingTransition(R.anim.expand_in, R.anim.shrink_out);
			break;
		case R.id.action_logout:
			ParseUser.logOut();
			ParseUser currentUser = ParseUser.getCurrentUser(); // this will now
																// be null
			Intent intLogOut = new Intent(this, MainActivity.class);
			intLogOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intLogOut.putExtra("EXIT", true);
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
								AllEventsFragment.class, userLoc));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Joined")
				.setTag("Joined")
				.setTabListener(
						new SupportFragmentTabListener<AttendingEvents>(
								R.id.flEventList, this, "Joined",
								AttendingEvents.class, userLoc));
		actionBar.addTab(tab2);

		Tab tab3 = actionBar
				.newTab()
				.setText("Hosting")
				.setTag("Hosting")
				.setTabListener(
						new SupportFragmentTabListener<CreatedEvents>(
								R.id.flEventList, this, "Hosting",
								CreatedEvents.class, userLoc));
		actionBar.addTab(tab3);
	}

	public void getCurrentUserLoc() {
		int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		locationclient = new LocationClient(this, this, this);
		if (resp == ConnectionResult.SUCCESS) {
			locationclient.connect();
		} else {
			Log.e("ERROR", "Error with Google play services " + resp);
		}
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	@Override
	public void onConnected(Bundle arg0) {
		locationclient.requestLocationUpdates(mLocationRequest,
				(com.google.android.gms.location.LocationListener) this);
	}

	@Override
	public void onLocationChanged(Location loc) {
		Log.d("DEBUG", "Event list onLocaitonChanged");
		llProgressList.setVisibility(View.INVISIBLE);

		Log.d("DEBUG",
				"Current user location (for event list) is: "
						+ loc.getLatitude() + "," + loc.getLongitude());
		userLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
		// Location temp = locationclient.getLastLocation();
		// userLoc = new LatLng(temp.getLatitude(), temp.getLongitude());
		locationclient.disconnect();

		setupTabs();
	}// test

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onDisconnected() {
	}

	public LatLng getUserLoc() {
		return userLoc;
	}

	public void onClickCreateEvent(View v) {
		Intent iCreate = new Intent(this, CreateEventActivity.class);
		startActivity(iCreate);
		overridePendingTransition(R.anim.down_in, R.anim.up_out);
	}
}
