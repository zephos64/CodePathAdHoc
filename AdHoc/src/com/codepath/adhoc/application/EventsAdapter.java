package com.codepath.adhoc.application;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.codepath.adhoc.R;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;

public class EventsAdapter extends ArrayAdapter<Events> {
	
	private Context mContext;
	private List<Events> mEvents;
	private List<User> mUser;

	public EventsAdapter(Context context, List<Events> objects) {
		super(context, com.codepath.adhoc.R.layout.fragment_item_event, objects);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mEvents = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(com.codepath.adhoc.R.layout.fragment_item_event, null);
		}
		
		Events events = mEvents.get(position);
		
		TextView tvLoginTitle = (TextView) convertView.findViewById(R.id.tvLoginTitle);
		TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDescription);
		TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvMaxAttendees);
		
		tvLoginTitle.setText(events.getEventName());
		tvDistance.setText("Lat: " + events.getLocLat() + ", Long: " + events.getLocLong());
		tvRemainingSpots.setText("Max attendees: " + events.getMaxAttendees());
		tvTime.setText("Event time: " + events.getEventTime());
		
		return convertView;
	}

}
