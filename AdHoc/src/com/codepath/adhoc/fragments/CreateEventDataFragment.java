package com.codepath.adhoc.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.CustomEventSpinner;
import com.codepath.adhoc.R;
import com.codepath.adhoc.activities.LocationCreationActivity;
import com.codepath.adhoc.models.LocationData;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.ParseUser;

public class CreateEventDataFragment extends CreateEventFragment implements OnFocusChangeListener{
	CustomEventSpinner spListEvents;
	EditText etStartTime;
	EditText etEndTime;
	EditText etMaxAttendees;
	EditText etDescription;
	EditText etLocation;
	
	int hour = 0;
	int min = 0;
	
	static final int LOCATION_ACTIVITY = 100;
	LocationData lcn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inf.inflate(R.layout.fragment_create_event_data, parent, false);
		spListEvents = (CustomEventSpinner) view.findViewById(R.id.spListEvents);
		etMaxAttendees = (EditText) view.findViewById(R.id.etMaxAttendees);
		etDescription = (EditText) view.findViewById(R.id.etDescription);
		etStartTime = (EditText) view.findViewById(R.id.etStartTime);
		etEndTime  = (EditText) view.findViewById(R.id.etEndTime);
		setTimeListeners(etStartTime, getString(R.string.dialogStartTime), true);
		setTimeListeners(etEndTime, getString(R.string.dialogEndTime), false);
		etLocation = (EditText) view.findViewById(R.id.etLocation);
		etLocation.setOnFocusChangeListener(this);	
		
		return view;
	}
		
	@Override
	public boolean checkData() {
		if(spListEvents.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
			Log.e("err", "Item not selected");
			showErrDialog(getString(R.string.errMissingDataTitle),
					getString(R.string.errNoEvent));
			return false;
		}
		if(etMaxAttendees.getText().length() == 0) {
			Log.e("err", "Max Attendees not inputted");
			showErrDialog(getString(R.string.errMissingDataTitle),
					getString(R.string.errNoAtt));
			return false;
		}
		
		if(etStartTime.getText().length() == 0) {
			Log.e("err", "Start time not inputted");
			showErrDialog(getString(R.string.errMissingDataTitle),
					getString(R.string.errNoStartTime));
			return false;
		}
		if(etEndTime.getText().length() == 0) {
			Log.e("err", "End time not inputted");
			showErrDialog(getString(R.string.errMissingDataTitle),
					getString(R.string.errNoEndTime));
			return false;
		}
		
		if(lcn == null) {
			Log.e("err", "Location not inputted");
			showErrDialog(getString(R.string.errMissingDataTitle),
					getString(R.string.errNoLoc));
			return false;
		}
		
		Log.d("DEBUG", "Finished checking data consistency");
		return true;
	}
	
	public boolean checkTime(boolean isStart, int hour, int min) {
		Calendar cal = Calendar.getInstance();
		if (!checkTimeDetails(cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), hour, min)) {
			showErrDialog(getString(R.string.errSetTime),
					getString(R.string.errLowTime));
			return false;
		}
		
		
		if(isStart && !etEndTime.getText().toString().isEmpty()) {
			if(!checkTimeDetails(hour,
					min,
					getHours(etEndTime.getText().toString()),
					getMinutes(etEndTime.getText().toString()))) {
				showErrDialog(getString(R.string.errSetTime),
						getString(R.string.errHighStart));
				return false;
			}
		}
		
		if(!etStartTime.getText().toString().isEmpty()
				&& !isStart) {
			if(!checkTimeDetails(getHours(etStartTime.getText().toString()),
					getMinutes(etStartTime.getText().toString()),
					hour,
					min)) {
				showErrDialog(getString(R.string.errSetTime),
						getString(R.string.errLowEnd));
				return false;
			}
		}
		Log.d("DEBUG", "Time valid");
		return true;
	}
	
	public void showErrDialog(String title, String msg) {
		AlertDialog.Builder adBuilderErr = new AlertDialog.Builder(getActivity());
		adBuilderErr.setTitle(title);
		adBuilderErr.setMessage(msg);
		adBuilderErr.setNegativeButton(getString(R.string.OK), null);
		adBuilderErr.show();
	}
	
	public boolean checkTimeDetails(int firstHour, int firstMin, int secHour, int secMin) {
		if(firstHour > secHour) {
			Log.e("err", "Too few hours [" + firstHour + "," +secHour+"]");
			return false;
		} else if (firstHour == secHour	&& firstMin > secMin){
			Log.e("err", "Too few minutes [" + firstMin + "," +secMin+"]");
			return false;
		}
		return true;
	}
	
	private int getHours(String time) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat(AdHocUtils.dateFormat).parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	private int getMinutes(String time) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat(AdHocUtils.dateFormat).parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.get(Calendar.MINUTE);
	}

	@Override
	public Events getEvent() {
		Calendar tempTime = Calendar.getInstance();
		tempTime.set(Calendar.HOUR_OF_DAY, getHours(etStartTime.getText().toString()));
		tempTime.set(Calendar.MINUTE, getMinutes(etStartTime.getText().toString()));
		
		Calendar tempEndTime = Calendar.getInstance();
		tempTime.set(Calendar.HOUR_OF_DAY, getHours(etEndTime.getText().toString()));
		tempTime.set(Calendar.MINUTE, getMinutes(etEndTime.getText().toString()));
		
		Events newEvent = new Events(AdHocUtils.EventStates.TBS,
				spListEvents.getSelectedItem().toString(),
				Integer.valueOf(etMaxAttendees.getText().toString()),
				tempTime.getTime().toString(),
				tempEndTime.getTime().toString(),
				etDescription.getText().toString(),
				ParseUser.getCurrentUser(),
				etLocation.getText().toString(),
				lcn.getLongitude(),
				lcn.getLattitude());
		return newEvent;
	}
	
	public void setTimeListeners(final EditText etField, final String title, final boolean isStart) {
		etField.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Calendar cal = Calendar.getInstance();
					if(etField.getId() == R.id.etEndTime &&
							etEndTime.getText().toString().isEmpty() &&
							!etStartTime.getText().toString().isEmpty()) {
						// if have start time and choosing end
						// have time be no less than start time
						Log.d("DEBUG", "Setting end time after start time already set");
						
						hour = getHours(etStartTime.getText().toString());
						min = getMinutes(etStartTime.getText().toString());
					} else if(etField.getText().toString().isEmpty()) {
						Log.d("DEBUG", "Time field empty, getting calendar time");
						hour = cal.get(Calendar.HOUR_OF_DAY);
						min = cal.get(Calendar.MINUTE);
					} else {
						hour = getHours(etField.getText().toString());
						min = getMinutes(etField.getText().toString());
						
						Log.d("DEBUG", "Time field not empty, getting time "+hour+":"+min);
					}
					
					// if selecting end hour, add 1 hour
					//  user can go less, but this assumes 
					//  default length of event is 1 hour
					if(etField.getId() == R.id.etEndTime &&
							etEndTime.getText().toString().isEmpty()) {
						hour += 1;
					}
					
					showCustomTimePicker(title, isStart, etField);
				}
				return false;
			}
		});
	}
	
	/*
	 * Must create custom time view, bug in android time view that
	 *  makes Cancel button not function
	 *  http://stackoverflow.com/questions/15165328/timepickerdialog-ontimesetlistener-not-called
	 */
	public void showCustomTimePicker(String title, final boolean isStart,
			final EditText etField) {

		final TimePicker mTimePicker = (TimePicker) getLayoutInflater(
				getArguments()).inflate(R.layout.time_picker_view, null);
		// Set an initial date for the picker
		mTimePicker.setIs24HourView(false);
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(min);

		// create the dialog
		AlertDialog.Builder mBuilder = new Builder(getActivity());
		// set the title
		mBuilder.setTitle(title)
				// set our date picker
				.setView(mTimePicker)
				// set the buttons
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hour = mTimePicker.getCurrentHour();
						min = mTimePicker.getCurrentMinute();
						Log.d("DEBUG", "New time is " + hour + ":" + min);
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.HOUR_OF_DAY, hour);
						cal.set(Calendar.MINUTE, min);
						SimpleDateFormat format = new SimpleDateFormat(
								AdHocUtils.dateFormat);
						Log.d("DEBUG", "Setting time as " + hour + ":" + min);

						if (checkTime(isStart, hour, min)) {
							etField.setText(format.format(cal.getTime()));
						}
					}
				})
				.setNegativeButton(android.R.string.cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						})
				// create the dialog and show it.
				.create()
				.show();
	}

	@Override
	public void onFocusChange(View view, boolean focusOn) {
		if (view.getId() == R.id.etLocation){
			if (focusOn) {
				Intent i = new Intent(getActivity(), LocationCreationActivity.class);
				Log.e("INTENT MAP", "Already Started");
				startActivityForResult(i, LOCATION_ACTIVITY);
			}	
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == getActivity().RESULT_OK && requestCode == LOCATION_ACTIVITY) {
	     // Extract name value from result extras
	     lcn = (LocationData) data.getSerializableExtra("Location");
	     etLocation.setText(lcn.getAddress()[0]);
	  }
	}		
}
