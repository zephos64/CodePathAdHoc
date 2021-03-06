package com.codepath.adhoc.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.fragments.AdhocMapFragment;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * runOnUiThread exist because data retrieved async but must run on UI thread to
 * change views
 */
public class EventDetailsActivity extends ActionBarActivity {
	TextView tvTitle;
	TextView tvTime;
	TextView tvTimeEnd;
	TextView tvLoc;
	TextView tvAttendance;
	TextView tvTimeFiller;
	ProgressBar pbAttendance;
	TextView tvDesc;
	TextView tvStatus;
	Button btnAction;

	TextView tvLoad;
	ProgressBar pbProgress;
	LinearLayout llProgress;

	ImageView ivDetailStatus;

	Fragment fMap;

	Events item;
	String itemId;
	private AdhocMapFragment mapFrg = null;
	private boolean isHost = false;
	private boolean hasJoined = false;

	private boolean gotDetails = false;
	private boolean gotAtt = false;
	private boolean gotState = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mapFrg = (AdhocMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fMap);
		Log.e("MAP Frag", String.valueOf(mapFrg));

		// for screen rotation
		if (savedInstanceState != null) {
			item = (Events) savedInstanceState.getSerializable("event");
			ParseGeoPoint point = item.getLoc();
			double longitude = point.getLongitude();
			double latitude = point.getLatitude();
			mapFrg.setLocaion(latitude, longitude);
		} else {
			Intent prevIntent = getIntent();
			itemId = prevIntent.getStringExtra(AdHocUtils.intentDetailsId);
			Log.d("DEBUG", "Got item id : " + itemId);
		}

		AdHocUtils.forceShowActionBar(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);

		tvTitle = (TextView) findViewById(R.id.tvInfoAttendance);
		tvTime = (TextView) findViewById(R.id.tvDetailStartTime);
		tvTimeEnd = (TextView) findViewById(R.id.tvDetailEndTime);
		tvLoc = (TextView) findViewById(R.id.tvDetailsLoc);
		tvAttendance = (TextView) findViewById(R.id.tvDetailsAttendance);
		tvTimeFiller = (TextView) findViewById(R.id.tvTimeFiller);
		pbAttendance = (ProgressBar) findViewById(R.id.pbAttendance);
		tvDesc = (TextView) findViewById(R.id.tvDetailDesc);
		tvStatus = (TextView) findViewById(R.id.tvInfoTitle);

		pbProgress = (ProgressBar) findViewById(R.id.pbProgess);
		tvLoad = (TextView) findViewById(R.id.tvLoad);
		llProgress = (LinearLayout) findViewById(R.id.llProgress);
		ivDetailStatus = (ImageView) findViewById(R.id.ivDetailStatus);

		btnAction = (Button) findViewById(R.id.btnAction);

		// for screen rotation
		if (item == null) {
			getItem(itemId);
		} else {
			populateItems();
		}

		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("event", item);
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
			iList.putExtra("BACK", true);
			startActivity(iList);
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
			startActivity(intLogOut);
			break;
		default:
			break;
		}

		return true;
	}

	public void getItem(String eventId) {
		Log.d("DEBUG", "API call to get event details for event : " + eventId);
		ParseClient.getParseEventDetails(eventId, new FindCallback<Events>() {
			public void done(List<Events> itemList, ParseException e) {
				if (e == null) {
					// Access the array of results here
					item = itemList.get(0);
					populateItems();
				} else {
					Log.d("item",
							"Error in populating details : " + e.getMessage());
				}
			}
		});
	}

	public void onAction(View v) {
		showProgressBar();
		if (isHost) {
			Log.d("DEBUG", "Cancelling event : " + item.getObjectId());
			// set event status cancel

			User curUser = (User) ParseUser.getCurrentUser();
			curUser.removeEventHosting(item);
			curUser.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException arg0) {
					item.setEventState(AdHocUtils.EventStates.CANCELLED
							.toString());
					// item.saveInBackground();

					tvStatus.setText(AdHocUtils.EventStates.CANCELLED
							.toString());
					btnAction.setVisibility(View.INVISIBLE);

					item.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							populateAttendance();
							gotoList();
						}
					});
				}
			});
		} else if (hasJoined) {
			// remove name from event list

			final User curUser = (User) ParseUser.getCurrentUser();
			Log.d("DEBUG", "Remove user [" + curUser.getObjectId()
					+ "] from event [" + item.getObjectId() + "]");

			setEmptyEvent();

			curUser.removeEventsJoined(item);
			// item.saveInBackground();
			curUser.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					item.removeJoinedUser(curUser);
					item.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							populateUserState();
							populateAttendance();
						}
					});
				}
			});
		} else {
			// add name to event list

			checkEventStatus(item, new FindCallback<Events>() {
				@Override
				public void done(List<Events> listEvents, ParseException e) {
					if (e == null) {
						String eventStatus = listEvents.get(0).getEventState();
						if (!eventStatus
								.equals(AdHocUtils.EventStates.CANCELLED
										.toString())
								&& !eventStatus
										.equals(AdHocUtils.EventStates.FINISHED
												.toString())) {
							final User curUser = (User) ParseUser
									.getCurrentUser();
							Log.d("DEBUG",
									"Adding user [" + curUser.getObjectId()
											+ "] from event ["
											+ item.getObjectId() + "]");

							setJoinedEvent();

							curUser.addEventsJoined(item);
							curUser.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException arg0) {
									item.addJoinedUser(curUser);
									item.saveInBackground(new SaveCallback() {
										@Override
										public void done(ParseException arg0) {
											populateUserState();
											populateAttendance();
											gotoList();
										}
									});
								}
							});
						} else {
							showErrDialog();
						}
					} else {
						Log.e("ERROR", "Error: " + e);
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void setJoinedEvent() {
		tvStatus.setText("JOINED");
		btnAction.setText("LEAVE");
		hasJoined = true;
		setImage("@drawable/ic_user_joined", ivDetailStatus);
	}

	private void setEmptyEvent() {
		tvStatus.setText("");
		btnAction.setText("JOIN");
		hasJoined = false;
		ivDetailStatus.setVisibility(View.INVISIBLE);
	}

	private void setHostEvent() {
		tvStatus.setText("HOST");
		btnAction.setText("CANCEL EVENT");
		isHost = true;
		setImage("@drawable/ic_star", ivDetailStatus);
	}

	public void setImage(String uri, ImageView ivImage) {
		int imageResource = getResources().getIdentifier(uri, null,
				getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		ivImage.setImageDrawable(res);
		ivImage.setVisibility(View.VISIBLE);
	}

	private void checkEventStatus(Events event, FindCallback<Events> fcb) {
		ParseClient.getParseEventDetails(event.getObjectId(), fcb);
	}

	private void showErrDialog() {
		AlertDialog.Builder badEvent = new AlertDialog.Builder(this);
		badEvent.setTitle(getString(R.string.errSaveEventTitle));
		badEvent.setMessage(getString(R.string.errSaveEventMsg));
		badEvent.setNegativeButton(getString(R.string.OK), null);
		badEvent.show();
	}

	private class AsyncPopulateDetails extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			populateDetails();
			return null;
		}
	}

	private class AsyncPopulateAttendance extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			populateAttendance();
			return null;
		}
	}

	private class AsyncPopulateState extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			populateUserState();
			return null;
		}
	}

	private void populateItems() {
		AsyncPopulateDetails apd = new AsyncPopulateDetails();
		AsyncPopulateAttendance apa = new AsyncPopulateAttendance();
		AsyncPopulateState aps = new AsyncPopulateState();

		aps.execute();
		apd.execute();
		apa.execute();
	}

	private void populateDetails() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvTitle.setText(item.getEventName());
				tvTime.setText("   " + AdHocUtils.getTime(item.getEventTime()));
				tvTimeEnd.setText(AdHocUtils.getTime(item.getEventTimeEnd()));
				tvTimeFiller.setText(" to ");
				tvDesc.setText(item.getDesc());
				if (item.getAddress() != null) {
					tvLoc.setText("   " + item.getAddress());
				} else {
					tvLoc.setText("");
				}

				ParseGeoPoint point = item.getLoc();
				mapFrg.setLocaion(point.getLatitude(), point.getLongitude());

				gotDetails = true;
				hideProgressBar();
			}
		});
	}

	private void populateUserState() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gotAtt = true;
				if (item.getHostUserId().equals(
						ParseUser.getCurrentUser().getObjectId())) {
					Log.d("DEBUG", "User is host of event");
					setHostEvent();
					hideProgressBar();
				} else if (item.getJoinedUserIds().contains(
						ParseUser.getCurrentUser())) {
					Log.d("DEBUG", "User has joined event");
					setJoinedEvent();
					hideProgressBar();
				} else {
					Log.d("DEBUG", "User has NOT joined event");
					setEmptyEvent();
					hideProgressBar();
					if (item.getAttendanceCount() == item.getMaxAttendees()) {
						btnAction.setEnabled(false);
						btnAction.setText("EVENT FULL");
					}
				}
			}
		});
	}

	private void populateAttendance() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (item.getAttendanceCount() == item.getMaxAttendees()) {
					tvAttendance.setText("FULL");
					tvAttendance.setTextColor(Color.parseColor("#FF0000"));

				} else {
					tvAttendance.setText(item.getAttendanceCount() + " / "
							+ item.getMaxAttendees());
					tvAttendance.setTextColor(Color.parseColor("#FFFFFF"));
				}
				pbAttendance.setMax(item.getMaxAttendees());
				pbAttendance.setProgress(item.getAttendanceCount());
				gotState = true;
				hideProgressBar();
			}
		});
	}

	private void hideProgressBar() {
		if (gotAtt && gotState && gotDetails) {
			pbProgress.setVisibility(View.INVISIBLE);
			tvLoad.setVisibility(View.INVISIBLE);
			llProgress.setVisibility(View.INVISIBLE);
		}
	}

	private void showProgressBar() {
		pbProgress.setVisibility(View.VISIBLE);
		tvLoad.setVisibility(View.VISIBLE);
		llProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.d("DEBUG", "Event Details restarted");
		// populateItems();
		getItem(itemId);
	}
	
	public void gotoList() {
		Intent i = new Intent(this, EventListActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("EXIT", true);
		startActivity(i);
	}
}
