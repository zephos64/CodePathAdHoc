package com.codepath.adhoc.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.ParseUser;

public class CreateEventDataActivity extends CreateEventFragment {
	Spinner spListEvents;
	EditText etMaxAttendees;
	EditText etDescription;
	TimePicker tpStartTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inf.inflate(R.layout.fragment_create_event_data, parent, false);
		
		spListEvents = (Spinner) view.findViewById(R.id.spListEvents);
		etMaxAttendees = (EditText) view.findViewById(R.id.etMaxAttendees);
		etDescription = (EditText) view.findViewById(R.id.etDescription);
		tpStartTime = (TimePicker) view.findViewById(R.id.tpStartTime);
		
		return view;
	}
	
	//TODO initial spinner:
	// http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one?rq=1
	
	@Override
	public void checkData() {
		if(spListEvents.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
			Log.e("err", "Item not selected");
		}
		if(etMaxAttendees.getText().length() == 0) {
			Log.e("err", "Max Attendees not inputted");
		}
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.HOUR_OF_DAY) > tpStartTime.getCurrentHour()) {
			Log.e("err", "Too few hours");
		} else if (cal.get(Calendar.HOUR_OF_DAY) == tpStartTime.getCurrentHour()
				&& cal.get(Calendar.MINUTE) > tpStartTime.getCurrentMinute()){
			Log.e("err", "Too few minutes");
		}
		
		Log.d("DEBUG", "Finished checking data consistency");
	}

	@Override
	public Events getEvent() {
		Calendar tempTime = Calendar.getInstance();
		tempTime.set(Calendar.HOUR_OF_DAY, tpStartTime.getCurrentHour());
		tempTime.set(Calendar.MINUTE, tpStartTime.getCurrentMinute());
		
		Events newEvent = new Events(AdHocUtils.EventStates.TBS,
				spListEvents.getSelectedItem().toString(),
				Integer.valueOf(etMaxAttendees.getText().toString()),
				tempTime.getTime().toString(),
				etDescription.getText().toString(),
				ParseUser.getCurrentUser().getObjectId());
		return newEvent;
	}
}
