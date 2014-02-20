package com.codepath.adhoc.application;

import java.util.List;

import android.content.Context;
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
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		View view = convertView;
		if (view == null) {
			//LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(com.codepath.adhoc.R.layout.fragment_item_event, null);
		}
		
		//EventItem eventItem = (EventItem) getItem(position);
		
		//ParseUser user = eventItem.getUser();
		Events events = mEvents.get(position);
		
		
		TextView tvLoginTitle = (TextView) view.findViewById(R.id.tvLoginTitle);
		TextView tvDistance = (TextView) view.findViewById(R.id.tvDescription);
		TextView tvRemainingSpots = (TextView) view.findViewById(R.id.tvStartTime);
		TextView tvTime = (TextView) view.findViewById(R.id.tvMaxAttendees);
		RadioButton rbSelectedEvent = (RadioButton) view.findViewById(R.id.rbSelectedEvent);
		
		return view;
	}

}
