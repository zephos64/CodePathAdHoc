package com.codepath.adhoc.fragments;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class AttendingEvents extends EventListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadList();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void loadList() {
		getAdapter().clear();
		ParseClient.getParseUserJoinedEvents((User)ParseUser.getCurrentUser(),
				new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException e) {
				if(e == null) {
					llListProgress.setVisibility(View.INVISIBLE);
					getAdapter().addAll(listEvents);
					lvEvents.onRefreshComplete();
					if(listEvents.size() == 0) {
						llEmptyList.setVisibility(View.VISIBLE);
					} else {
						llEmptyList.setVisibility(View.INVISIBLE);
					}
				} else {
					Log.e("ERROR", "ParseException on all events: " + e);
					e.printStackTrace();
				}
			}
		});
	}

}
