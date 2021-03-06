package com.codepath.adhoc.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.adhoc.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.internal.AsyncCallback;

public class MainActivity extends ActionBarActivity {
    private static final int 	GPS_ENABLE_ACTIVITY = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		ParseTwitterUtils.initialize("8CxSNUzNaoy1ciE1J5VdWQ", "wWhy3ibas8NQGAo3Q70l8EpQb8QXwzsTWibQRF68Y");
		if (ParseUser.getCurrentUser() != null) {
			ParseTwitterUtils.getTwitter().authorize(this, new AsyncCallback() {
				@Override
				public void onSuccess(Object arg0) {
					Log.d("DEBUG",
							"User successfully authenticated with twitter");
					if (ParseUser.getCurrentUser() != null) {
						if(ParseTwitterUtils.isLinked(ParseUser
									.getCurrentUser())) {
							Log.d("DEBUG", "User is already linked with Parse");
						} else {
							linkParseUser(ParseUser.getCurrentUser());
						}
					}
					
					loginApp();
				}

				@Override
				public void onFailure(Throwable e) {
					Log.d("ERROR", "User failed to authenticate with twitter");
					e.printStackTrace();
				}

				@Override
				public void onCancel() {
					Log.d("DEBUG", "User cancelled authenticating with twitter");
				}
			});
		}
		LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			Toast.makeText(this, "This Application needs GPS. Please enable GPS.", Toast.LENGTH_LONG).show();
			promptGPSEnabling();
		}
		
	}
	private void promptGPSEnabling () {
		Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(gpsOptionsIntent, GPS_ENABLE_ACTIVITY);		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == GPS_ENABLE_ACTIVITY) {
	    	LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
			if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
				Toast.makeText(this, "GPS not enabled. Working in limited feature mode", Toast.LENGTH_LONG).show();
			}
	    }
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void loginWithParse(View view) {
		Log.d("debug", "Login To Rest Clicked");
		ParseTwitterUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(final ParseUser user, ParseException err) {
				if (user == null) {
					Log.d("debug",
							"ParseUser is null, cancelling Twitter login");
				} else {
					Log.d("debug", "User logged in through Twitter");

					if (!ParseTwitterUtils.isLinked(user)) {
						linkParseUser(user);
					}

					loginApp();
				}
			}
		});
	}
	
	public void loginApp() {
		Intent i = new Intent(getApplicationContext(),
				EventListActivity.class);
		startActivity(i);
	}
	
	public void linkParseUser(final ParseUser user) {
		ParseTwitterUtils.link(user, getParent(),
				new SaveCallback() {
					@Override
					public void done(ParseException ex) {
						if (ParseTwitterUtils.isLinked(user)) {
							Log.d("debug",
									"Account linked with ParseUser account");
						}
					}
				});
	}
}
