package com.codepath.adhoc.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.adhoc.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.internal.AsyncCallback;

public class MainActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		ParseTwitterUtils.initialize("8CxSNUzNaoy1ciE1J5VdWQ", "wWhy3ibas8NQGAo3Q70l8EpQb8QXwzsTWibQRF68Y");
		ParseTwitterUtils.getTwitter().authorize(this, new AsyncCallback() {
			@Override
			public void onSuccess(Object arg0) {
				Log.d("DEBUG", "User successfully authenticated with twitter");
				if(ParseTwitterUtils.isLinked(ParseUser.getCurrentUser())) {
					Log.d("DEBUG", "User is already linked with Parse");
				}
				//test
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
					err.printStackTrace();
				} else {
					Log.d("debug", "User logged in through Twitter");

					if (!ParseTwitterUtils.isLinked(user)) {
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
}
