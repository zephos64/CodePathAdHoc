package com.codepath.adhoc.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.parse.ParseUser;

/**
 * runOnUiThread exist because data retrieved async
 *  but must run on UI thread to change views
 */
public class EventDetailsActivity extends ActionBarActivity {
	TextView tvTitle;
	TextView tvTime;
	TextView tvTimeEnd;
	TextView tvLoc;
	TextView tvAttendance;
	TextView tvDesc;
	TextView tvStatus;
	Button btnAction;
	
	TextView tvLoad;
	ProgressBar pbProgress;
	LinearLayout llProgress;
	
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
		
		mapFrg = (AdhocMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		Log.e("MAP Frag", String.valueOf(mapFrg));
		
		// for screen rotation
		if(savedInstanceState != null) {
			item = (Events)savedInstanceState.getSerializable("event");
			double longitude  = item.getLocLong();
			double latitude  = item.getLocLat();
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
		tvTime = (TextView) findViewById(R.id.textView5);
		tvTimeEnd = (TextView) findViewById(R.id.textView8);
		tvLoc = (TextView) findViewById(R.id.textView6);
		tvAttendance = (TextView) findViewById(R.id.textView4);
		tvDesc = (TextView) findViewById(R.id.tvInfoStartTime);
		tvStatus = (TextView) findViewById(R.id.tvInfoTitle);
		
		pbProgress = (ProgressBar) findViewById(R.id.pbProgess);
		tvLoad = (TextView) findViewById(R.id.tvLoad);
		llProgress = (LinearLayout) findViewById(R.id.llProgress);
		
		btnAction = (Button) findViewById(R.id.button1);
		
		// for screen rotation
		if(item == null) {
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
	
	public void getItem(String eventId) {
		Log.d("DEBUG", "API call to get event details for event : " + eventId);
		ParseClient.getParseEventDetails(eventId, new FindCallback<Events>() {
		    public void done(List<Events> itemList, ParseException e) {
		        if (e == null) {
		            // Access the array of results here
		            item = itemList.get(0);
		            populateItems();
		        } else {
		            Log.d("item", "Error in populating details : " + e.getMessage());
		        }
		    }
		});
	}
	
	public void onAction(View v) {
		if(isHost) {
			Log.d("DEBUG", "Cancelling event : " + item.getObjectId());
			//set event status cancel
			
			User curUser = (User) ParseUser.getCurrentUser();
			item.setEventState(AdHocUtils.EventStates.CANCELLED.toString());
			curUser.removeEventsHosting(item);
			item.saveInBackground();
			curUser.saveInBackground();
			
			tvStatus.setText(AdHocUtils.EventStates.CANCELLED.toString());
			btnAction.setVisibility(View.INVISIBLE);
		} else if(hasJoined) {
			//remove name from event list	

			User curUser = (User) ParseUser.getCurrentUser();
			Log.d("DEBUG", "Remove user [" + curUser.getObjectId()
					+"] from event [" + item.getObjectId()+"]");
			item.removeJoinedUser(curUser);
			curUser.removeEventsAttending(item);
			item.saveInBackground();
			curUser.saveInBackground();
		
			leaveEvent();
		} else {
			//add name to event list

			checkEventStatus(item, new FindCallback<Events>() {
				@Override
				public void done(List<Events> listEvents, ParseException e) {
					if(e == null) {
						String eventStatus = listEvents.get(0).getEventState(); 
						if(!eventStatus.equals(AdHocUtils.EventStates.CANCELLED.toString())
							&& !eventStatus.equals(AdHocUtils.EventStates.FINISHED.toString())) {
								User curUser = (User) ParseUser.getCurrentUser();
								Log.d("DEBUG", "Adding user [" + curUser.getObjectId()
									+"] from event [" + item.getObjectId()+"]");
								item.addJoinedUser(curUser);
								curUser.addEventAttending(item);
								item.saveInBackground();
								curUser.saveInBackground();
							
								joinEvent();
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

		item.saveEventually();
	}
	
	private void joinEvent() {
		tvStatus.setText("JOINED");
		btnAction.setText("LEAVE");
		hasJoined=true;
	}
	
	private void leaveEvent() {
		tvStatus.setText("");
		btnAction.setText("JOIN");
		hasJoined=false;
	}
	
	private void checkEventStatus(Events event, FindCallback<Events> fcb) {
		ParseClient.getParseEventDetails(event.getObjectId(), fcb);
	}
	
	private void showErrDialog() {
		AlertDialog.Builder badEvent = new AlertDialog.Builder(
				this);
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
		apd.execute();
		apa.execute();
		aps.execute();
	}
	
	private void populateDetails() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvTitle.setText(item.getEventName());
				tvTime.setText(AdHocUtils.getTime(item.getEventTime()));
				tvTimeEnd.setText(AdHocUtils.getTime(item.getEventTimeEnd()));
				tvDesc.setText(item.getDesc());
				if(item.getAddress() != null) {
					tvLoc.setText(item.getAddress());
				} else {
					tvLoc.setText("");
				}
				
				mapFrg.setLocaion(item.getLocLat(),item.getLocLong());
				
				gotDetails = true;
				hideProgressBar();
			}
		});
	}
	
	private void populateAttendance() {
		ParseClient.getHostUser(item, new FindCallback<User>() {
			@Override
			public void done(List<User> listUsersJoined, ParseException e) {
				if (e == null) {
					if (listUsersJoined.size() > 0
							&& listUsersJoined
									.get(0)
									.getObjectId()
									.equals(ParseUser.getCurrentUser()
											.getObjectId())) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tvStatus.setText("HOST");
								btnAction.setText("CANCEL");
								isHost = true;
								gotAtt = true;
								hideProgressBar();
							}
						});
						
					} else {
						ParseClient.getJoinedUsers(item,
								new FindCallback<User>() {
									@Override
									public void done(
											List<User> listUsersJoined,
											ParseException e) {
										if (e == null) {
											if (listUsersJoined
													.contains(ParseUser
															.getCurrentUser())) {
												
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														joinEvent();
														gotAtt = true;
														hideProgressBar();
													}
												});
											} else {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														leaveEvent();
														gotAtt = true;
														hideProgressBar();
													}
												});
											}
										} else {
											Log.e("ERROR",
													"Error getting userId for event details");
											e.printStackTrace();
										}
									}
								});
					}
				} else {
					Log.e("ERROR", "Error getting userId for event details");
					e.printStackTrace();
				}
			}
		});
	}
		
	private void populateUserState() {
		ParseClient.getJoinedUsers(item, new FindCallback<User>() {
			@Override
			public void done(final List<User> listUsersJoined, ParseException e) {
				if (e == null) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvAttendance.setText(listUsersJoined.size() + " / "
									+ item.getMaxAttendees());
							gotState = true;
							hideProgressBar();
						}
					});
					
				} else {
					Log.e("ERROR",
							"Error getting joined users for event details");
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void hideProgressBar() {
		if(gotAtt && gotState && gotDetails) {
			pbProgress.setVisibility(View.INVISIBLE);
			tvLoad.setVisibility(View.INVISIBLE);
			llProgress.setVisibility(View.INVISIBLE);
		}
	}
}
