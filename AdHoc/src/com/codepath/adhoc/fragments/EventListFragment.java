package com.codepath.adhoc.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.adhoc.R;
import com.codepath.adhoc.application.EventsAdapter;
import com.codepath.adhoc.parsemodels.EventItem;

public class EventListFragment extends ListFragment {
	
	ArrayList<EventItem> eventItem = new ArrayList<EventItem>();
	EventsAdapter eventsAdapter;
	ListView lvEvents;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_item_event, container, false);
		eventsAdapter = new EventsAdapter(getActivity(), eventItem);
		lvEvents = (ListView) view.findViewById(R.id.lvEvents);
		lvEvents.setAdapter(eventsAdapter);
		return view;
	}

}
