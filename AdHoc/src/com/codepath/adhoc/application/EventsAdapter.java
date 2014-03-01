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

public class EventsAdapter extends ArrayAdapter<Events> {
	
	private List<Events> mEvents;
	protected String str = "";

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
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvListMaxAttendees);
		final TextView tvHostedOrJoined = (TextView) convertView.findViewById(R.id.tvHostedOrJoined);
		
		tvHostedOrJoined.setText(events.getHostUserId());
		tvLoginTitle.setText(events.getEventName());
		tvRemainingSpots.setText("  -  " + events.getAttendanceCount()
				+ "/" + events.getMaxAttendees());
		tvTime.setText("   " + AdHocUtils.getTime(events.getEventTime()));
		
		return convertView;
	}

}
