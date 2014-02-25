package com.codepath.adhoc.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.activities.EventDetailsActivity;
import com.codepath.adhoc.application.EventsAdapter;
import com.codepath.adhoc.parsemodels.Events;
import com.google.android.gms.maps.model.LatLng;

import eu.erikw.PullToRefreshListView;

public abstract class EventListFragment extends Fragment {
	
	EventsAdapter eventsAdapter;
	PullToRefreshListView lvEvents;
	ArrayList<Events> events;
	FragmentActivity activityListener;
	
	LatLng loc;
	
	private View mContentView = null;
		
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activityListener = (FragmentActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		events = new ArrayList<Events>();
		eventsAdapter = new EventsAdapter(getActivity(), events);
		//Log.d("DEBUG", "list of events: " + events.toString());
		
		loc = new LatLng(getArguments().getDouble("lat"),
				getArguments().getDouble("long"));
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.fragment_list, container, false);
		this.lvEvents = (PullToRefreshListView) mContentView.findViewById(R.id.lvEvents);
		Log.d("DEBUG", "adapter: " + eventsAdapter.toString());
		//Log.d("DEBUG", "listview: " + lvEvents.toString());
		//setListAdapter(getAdapter());
		lvEvents.setAdapter(getAdapter());
		lvEvents.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos,
					long id) {
				Log.d("DEBUG", "Clicked item at [" + pos +
						"] with Event id ["+events.get(pos).getObjectId()+"]");
				Intent itemDetails = new Intent(getActivity(), EventDetailsActivity.class);
				itemDetails.putExtra(AdHocUtils.intentDetailsId, events.get(pos).getObjectId());
				startActivity(itemDetails);
			}
		});
		//Log.d("DEBUG", "listview: " + lvEvents.toString());
		return mContentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	public EventsAdapter getAdapter() {
		return eventsAdapter;
	}
	
	abstract void getMoreItems(int moreItems);

}
