package com.codepath.adhoc.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.adhoc.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends ActionBarActivity {
	ProgressBar pbProgress;
	TextView tvProgressText;
	Button btnLoginFB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
		tvProgressText = (TextView) findViewById(R.id.tvProgressText);
		btnLoginFB = (Button) findViewById(R.id.btnLoginFB);
		
		//pbProgress.setVisibility(ProgressBar.VISIBLE);
		//tvProgressText.setVisibility(TextView.VISIBLE);
		//btnLoginFB.setVisibility(Button.INVISIBLE);
		//test

		//ParseFacebookUtils.initialize(getString(R.string.app_id));
		ParseTwitterUtils.initialize("8CxSNUzNaoy1ciE1J5VdWQ", "wWhy3ibas8NQGAo3Q70l8EpQb8QXwzsTWibQRF68Y");
		/*FacebookClient.getFacebookUser(new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				pbProgress.setVisibility(ProgressBar.INVISIBLE);
				tvProgressText.setVisibility(TextView.INVISIBLE);
				btnLoginFB.setVisibility(Button.VISIBLE);
				
				Log.d("debug", "Facebook user sesssion errors: " + response.getError());
				if(response.getError() == null) {
					loginFacebook();
				}
			}
	    });*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loginFacebook() {
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
