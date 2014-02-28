package com.codepath.adhoc.application;

import java.util.ArrayList;
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
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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
		TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDescription);
		TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvMaxAttendees);
		final TextView tvHostedOrJoined = (TextView) convertView.findViewById(R.id.tvHostedOrJoined);
		
		//tvHostedOrJoined.setText(events.getRel());
		//Log.d("TEST", "TEST: " + events.getHostUserId());
		tvHostedOrJoined.setText(events.getHostUserId());
		
		tvLoginTitle.setText(events.getEventName());
		//ParseGeoPoint point = events.getLoc();
		
		//Log.d("Which event", "Related host" + events.getHostUserIdRelation());
		
		tvDistance.setText("Location: " + events.getAddress());
		tvRemainingSpots.setText(events.getAttendanceCount() + " / " + events.getMaxAttendees());
		
		tvTime.setText("Event time: " + AdHocUtils.getTime(events.getEventTime()));
		
		return convertView;
	}

}
