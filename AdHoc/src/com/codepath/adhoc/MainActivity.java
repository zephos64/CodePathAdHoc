package com.codepath.adhoc;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.adhoc.application.FacebookClient;
import com.facebook.Request;
import com.facebook.Response;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// TODO uncomment, commenting out because requires cleaning every time
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		//parseUserSignup();
		//parseUserLogin();

		ParseFacebookUtils.initialize(getString(R.string.app_id));
		FacebookClient.getFacebookUser(new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				Log.d("debug", "Facebook user sesssion errors: " + response.getError());
				if(response.getError() == null) {
					loginFacebook();
				}
			}
	    });
	}

	public void parseUserSignup() {
		// Create the ParseUser
		ParseUser user = new ParseUser();
		// Set core properties
		user.setUsername("joestevens");
		user.setPassword("secret123");
		user.setEmail("email@example.com");
		// Set custom properties
		user.put("phone", "650-253-0000");
		// Invoke signUpInBackground
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					// Hooray! Let them use the app now.
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
				}
			}
		});
	}

	public void parseUserLogin() {
		ParseUser.logInInBackground("joestevens", "secret123",
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							// Hooray! The user is logged in.
						} else {
							// Signup failed. Look at the ParseException to see
							// what happened.
						}
					}
				});
	}

	public void getCurrentParseUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user
			// ParseUser.logOut(); // logs the current user off.
			// currentUser = ParseUser.getCurrentUser();
		} else {
			// show the signup or login screen
		}
	}

	public void queryUsers() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereGreaterThan("age", 20); // find adults
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					// The query was successful.
				} else {
					// Something went wrong.
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loginFacebook() {
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(final ParseUser user, ParseException err) {
				if (user == null) {
					Log.d("debug",
							"Uh oh. The user cancelled the Facebook login.");
				} else {
					Log.d("debug", "User logged in through Facebook!");

					if (!ParseFacebookUtils.isLinked(user)) {
						ParseFacebookUtils.link(user, getParent(),
								new SaveCallback() {
									@Override
									public void done(ParseException ex) {
										if (ParseFacebookUtils.isLinked(user)) {
											Log.d("debug",
													"Account linked with ParseUser acc");
										}
									}
								});
					}

					Intent i = new Intent(getApplicationContext(),
							EventListActivity.class);
					startActivity(i);
				}
			}
		});
	}

	public void loginWithParse(View view) {
		Log.d("debug", "Login To Rest Clicked");
		loginFacebook();
	}
}
