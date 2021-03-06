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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.activities.EventDetailsActivity;
import com.codepath.adhoc.application.EventsAdapter;
import com.codepath.adhoc.parsemodels.Events;
import com.google.android.gms.maps.model.LatLng;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class EventListFragment extends Fragment {

	EventsAdapter eventsAdapter;
	PullToRefreshListView lvEvents;
	ArrayList<Events> events;
	FragmentActivity activityListener;

	RelativeLayout llEmptyList;
	LinearLayout llListProgress;

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
		// Log.d("DEBUG", "list of events: " + events.toString());

		loc = new LatLng(getArguments().getDouble("lat"), getArguments()
				.getDouble("long"));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.fragment_list, container,
				false);
		this.lvEvents = (PullToRefreshListView) mContentView
				.findViewById(R.id.lvEvents);
		llEmptyList = (RelativeLayout) mContentView
				.findViewById(R.id.llEmptyList);
		llListProgress = (LinearLayout) mContentView
				.findViewById(R.id.llListProgress);
		llEmptyList.setVisibility(View.INVISIBLE);

		lvEvents.setAdapter(getAdapter());
		lvEvents.setSelector(android.R.color.transparent);
		lvEvents.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos,
					long id) {
				Log.d("DEBUG", "Clicked item at [" + pos + "] with Event id ["
						+ events.get(pos).getObjectId() + "]");
				final Intent itemDetails = new Intent(getActivity(),
						EventDetailsActivity.class);
				itemDetails.putExtra(AdHocUtils.intentDetailsId, events
						.get(pos).getObjectId());

				startActivity(itemDetails);
				getActivity().overridePendingTransition(R.anim.right_in,
						R.anim.left_out);
			}
		});

		lvEvents.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.d("DEBUG", "Refreshing list view");
				loadList();
			}
		});
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

	abstract void loadList();

	@Override
	public void onResume() {
		super.onResume();
		Log.d("DEBUG", "Event list Fragment resumed");
		llListProgress.setVisibility(View.INVISIBLE);
		// loadList();
		// llListProgress.setVisibility(View.VISIBLE);
	}
}
