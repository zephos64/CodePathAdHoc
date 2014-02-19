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
import com.codepath.adhoc.parsemodels.EventItem;
import com.codepath.adhoc.parsemodels.User;
import com.parse.ParseUser;

public class EventsAdapter extends ArrayAdapter {

	public EventsAdapter(Context context, List<EventItem> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(com.codepath.adhoc.R.layout.fragment_item_event, null);
		}
		
		EventItem eventItem = (EventItem) getItem(position);
		
		ParseUser user = eventItem.getUser();
		
		TextView tvLoginTitle = (TextView) view.findViewById(R.id.tvLoginTitle);
		TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
		TextView tvRemainingSpots = (TextView) view.findViewById(R.id.tvRemainingSpots);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		RadioButton rbSelectedEvent = (RadioButton) view.findViewById(R.id.rbSelectedEvent);
		
		return view;
	}

}
