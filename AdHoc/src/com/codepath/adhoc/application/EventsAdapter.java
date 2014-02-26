package com.codepath.adhoc.application;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.ParseGeoPoint;

public class EventsAdapter extends ArrayAdapter<Events> {
	
	private List<Events> mEvents;

	public EventsAdapter(Context context, List<Events> objects) {
		super(context, com.codepath.adhoc.R.layout.fragment_item_event, objects);
		this.mEvents = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.codepath.adhoc.R.layout.fragment_item_event, null);
		}
		
		final Events events = mEvents.get(position);
		
		Log.d("DEBUG", "Position getView at " + position);
		
		TextView tvLoginTitle = (TextView) convertView.findViewById(R.id.tvLoginTitle);
		TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDescription);
		TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvMaxAttendees);
		
		tvLoginTitle.setText(events.getEventName());
		ParseGeoPoint point = events.getLoc();

		tvDistance.setText("Lat: " + point.getLatitude() + ", Long: " + point.getLongitude());
		tvRemainingSpots.setText(events.getAttendanceCount() + " / " + events.getMaxAttendees());
		
		tvTime.setText("Event time: " + AdHocUtils.getTime(events.getEventTime()));
		
		return convertView;
	}

}
