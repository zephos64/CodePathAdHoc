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
		
		Log.d("DEBUG", "Marker " + mark.getId() + " getInfoContents called");
		int id = Integer.valueOf(mark.getId().replace("m", ""));
		
		// When user clicks map, marker == m0, this prevents error
		if(id > 0) {
			Log.d("DEBUG", "Adapter marker maps to event: " + listEvents.get(id-1).getObjectId());
		
			Events event = listEvents.get(id-1);
			tvInfoTitle.setText(event.getEventName());
			tvInfoStartTime.setText(AdHocUtils.getTime(event.getEventTime()));
			tvInfoAttendance.setText("blah / "+event.getMaxAttendees());
		}
		
		return v;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
