package com.codepath.adhoc.application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

public class EventsAdapter extends ArrayAdapter<Events> {
	
	private Context mContext;
	private List<Events> mEvents;
	private List<User> mUser;
	
	private List<Integer> mAttendance;

	public EventsAdapter(Context context, List<Events> objects) {
		super(context, com.codepath.adhoc.R.layout.fragment_item_event, objects);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mEvents = objects;
	}
	
	public void blah(final int a) {
		ParseClient.getCountJoinedUsers(mEvents.get(a), new CountCallback() {
			@Override
			public void done(int count, ParseException e) {
				if(e == null) {
					mAttendance.set(a, count);
					//tvRemainingSpots.setText(count + " / " + events.getMaxAttendees());
				} else {
					Log.e("ERROR", "Events adapter count error");
				}
			}
		});
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
		
		final Events events = mEvents.get(position);
		
		Log.d("DEBUG", "Position getView at " + position);
		
		Geocoder gc;
		List<Address> addresses;
		gc = new Geocoder(mContext, Locale.getDefault());
//		try {
//			addresses = gc.getFromLocation(events.getLocLat(), events.getLocLong(), 1);
//			if (addresses != null && addresses.size() > 0)
//				Log.d("DBG", "Address: " + addresses.get(position).getAddressLine(1) + " " + addresses.get(position).getAddressLine(2));
//			else
//				Toast.makeText(mContext, "Nothing returned", Toast.LENGTH_SHORT).show();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		TextView tvLoginTitle = (TextView) convertView.findViewById(R.id.tvLoginTitle);
		TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDescription);
		final TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvMaxAttendees);
		
		tvLoginTitle.setText(events.getEventName());
		ParseGeoPoint point = events.getLoc();

		tvDistance.setText("Lat: " + point.getLatitude() + ", Long: " + point.getLongitude());
		//tvRemainingSpots.setText(mAttendance.get(position) + " / " + events.getMaxAttendees());
		tvRemainingSpots.setText("test / " + events.getMaxAttendees());
		
		tvTime.setText("Event time: " + AdHocUtils.getTime(events.getEventTime()));
		
		return convertView;
	}

}
