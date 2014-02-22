package com.codepath.adhoc.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class EventDetailsActivity extends ActionBarActivity {
	TextView tvTitle;
	TextView tvTime;
	TextView tvTimeEnd;
	TextView tvLoc;
	TextView tvAttendance;
	TextView tvDesc;
	TextView tvStatus;
	Button btnAction;
	
	Events item;
	
	private boolean isHost = false;
	private boolean hasJoined = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// for screen rotation
		if(savedInstanceState != null) {
			item = (Events)savedInstanceState.getSerializable("event");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
		
		tvTitle = (TextView) findViewById(R.id.textView3);
		tvTime = (TextView) findViewById(R.id.textView5);
		tvTimeEnd = (TextView) findViewById(R.id.textView8);
		tvLoc = (TextView) findViewById(R.id.textView6);
		tvAttendance = (TextView) findViewById(R.id.textView4);
		tvDesc = (TextView) findViewById(R.id.textView2);
		tvStatus = (TextView) findViewById(R.id.textView1);
		
		btnAction = (Button) findViewById(R.id.button1);
		
		// for screen rotation
		if(item == null) {
			getItem("3wXCAyexxW");
		} else {
			populateDetails();
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
		            populateDetails();
		        } else {
		            Log.d("item", "Error in populating details : " + e.getMessage());
		        }
		    }
		});
	}
	
	public void populateDetails() {

		tvTitle.setText(item.getEventName());
		tvTime.setText(AdHocUtils.getTime(item.getEventTime()));
		tvTimeEnd.setText(AdHocUtils.getTime(item.getEventTimeEnd()));
		tvDesc.setText(item.getDesc());

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
						tvStatus.setText("HOST");
						btnAction.setText("CANCEL");
						isHost = true;
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
												joinEvent();
											} else {
												leaveEvent();
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

		tvLoc.setText("TESTING");

		ParseClient.getJoinedUsers(item, new FindCallback<User>() {
			@Override
			public void done(List<User> listUsersJoined, ParseException e) {
				if (e == null) {
					tvAttendance.setText(listUsersJoined.size() + " / "
							+ item.getMaxAttendees());
				} else {
					Log.e("ERROR",
							"Error getting joined users for event details");
					e.printStackTrace();
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
}
