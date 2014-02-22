package com.codepath.adhoc.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import com.codepath.adhoc.activities.LocationActivity;
import com.codepath.adhoc.models.LocationData;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.ParseUser;

public class CreateEventDataActivity extends CreateEventFragment implements OnFocusChangeListener{
	CustomEventSpinner spListEvents;
	EditText etStartTime;
	EditText etEndTime;
	EditText etMaxAttendees;
	EditText etDescription;
	EditText etLocation;
	
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
	public void checkData() {
		//TODO add better errors
		if(spListEvents.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
			Log.e("err", "Item not selected");
		}
		if(etMaxAttendees.getText().length() == 0) {
			Log.e("err", "Max Attendees not inputted");
		}
		
		if(lcn == null) {
			Log.e("err", "Location not inputted");
		}
		
		Log.d("DEBUG", "Finished checking data consistency");
	}
	
	public boolean checkTime(boolean isStart, int hour, int min) {
		Calendar cal = Calendar.getInstance();
		if (!checkTimeDetails(cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), hour, min)) {
			showErrDialog("Time is before current time");
			return false;
		}
		
		
		if(isStart && !etEndTime.getText().toString().isEmpty()) {
			if(!checkTimeDetails(hour,
					min,
					getHours(etEndTime.getText().toString()),
					getMinutes(etEndTime.getText().toString()))) {
				showErrDialog("Start time is greater than end time");
				return false;
			}
		}
		
		if(!etStartTime.getText().toString().isEmpty()
				&& !isStart) {
			if(!checkTimeDetails(getHours(etStartTime.getText().toString()),
					getMinutes(etStartTime.getText().toString()),
					hour,
					min)) {
				showErrDialog("End time is not greater than start time");
				return false;
			}
		}
		return true;
	}
	
	public void showErrDialog(String msg) {
		AlertDialog.Builder adBuilderErr = new AlertDialog.Builder(getActivity());
		adBuilderErr.setTitle("Error with setting time");
		adBuilderErr.setMessage(msg);
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
		return cal.get(Calendar.HOUR);
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
				lcn.getLattitude(),
				lcn.getLongitude());
		return newEvent;
	}
	
	public void setTimeListeners(final EditText etField, final String title, final boolean isStart) {
		
		etField.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Calendar cal = Calendar.getInstance();
					int hour;
					int min;
					if(etField.getId() == R.id.etEndTime &&
							!etStartTime.getText().toString().isEmpty()) {
						// if have start time and choosing end
						// have time be no less than start time
						hour = getHours(etStartTime.getText().toString());
						min = getMinutes(etStartTime.getText().toString());
					} else if(etField.getText().toString().isEmpty()) {
						hour = cal.get(Calendar.HOUR_OF_DAY);
						min = cal.get(Calendar.MINUTE);
					} else {
						hour = getHours(etField.getText().toString());
						min = getMinutes(etField.getText().toString());
					}
					
					// if selecting end hour, add 1 hour
					//  user can go less, but this assumes 
					//  default length of event is 1 hour
					if(etField.getId() == R.id.etEndTime) {
						hour+=1;
					}
					final TimePickerDialog tpDialog = new TimePickerDialog(
							getActivity(),
							new TimePickerDialog.OnTimeSetListener() {								
								@Override
								public void onTimeSet(TimePicker timePicker,
										int selectedHour, int selectedMinute) {

									Calendar cal = Calendar.getInstance();
									cal.set(Calendar.HOUR_OF_DAY, selectedHour);
									cal.set(Calendar.MINUTE, selectedMinute);
									SimpleDateFormat format = new SimpleDateFormat(AdHocUtils.dateFormat);
									
									if(checkTime(isStart, selectedHour, selectedMinute)) {
										etField.setText(format.format(cal.getTime()));
									}
								}
							}, hour, min, false);
					tpDialog.setTitle(title);
					tpDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tpDialog.dismiss();
						}
					});
					tpDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Set", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing, logic in onTimeSet
						}
					});
					tpDialog.show();
				}
				return false;
			}
		});
	}

	@Override
	public void onFocusChange(View view, boolean focusOn) {
		if (view.getId() == R.id.etLocation){
			if (focusOn) {
				Intent i = new Intent(getActivity(), LocationActivity.class);
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
