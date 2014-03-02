package com.codepath.adhoc.application;

import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.parsemodels.Events;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements InfoWindowAdapter{
	LayoutInflater inflater = null;
	
	List<Events> listEvents;
	TextView tvInfoTitle;
	TextView tvInfoStartTime;
	TextView tvInfoAttendance;
	 
	public CustomInfoWindowAdapter(LayoutInflater inflater, List<Events> events) {
		this.inflater = inflater;
		listEvents=events;
	}

	@Override
	public View getInfoContents(Marker mark) {
		View v = inflater.inflate(R.layout.fragment_info_window, null);
		tvInfoTitle = (TextView) v.findViewById(R.id.tvInfoTitle);
		tvInfoStartTime = (TextView) v.findViewById(R.id.tvInfoStartTime);
		tvInfoAttendance = (TextView) v.findViewById(R.id.tvInfoAttendance);
		
		Log.e("DEBUG", "Marker " + mark.getSnippet() + " getInfoContents called");
		if (mark.getSnippet() != null) {
			int id = Integer.valueOf(mark.getSnippet()); 
			Log.d("DEBUG", "Adapter marker maps to event: " + listEvents.get(id).getObjectId());
		
			Events event = listEvents.get(id);
			tvInfoTitle.setText(event.getEventName());
			tvInfoStartTime.setText("  " + AdHocUtils.getTime(event.getEventTime()));
			tvInfoAttendance.setText("  " + event.getAttendanceCount()
					+ "/"+event.getMaxAttendees());
		}
		else {
			return null;
		}
		return v;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
