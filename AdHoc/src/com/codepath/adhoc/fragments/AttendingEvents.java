package com.codepath.adhoc.fragments;

import java.util.List;

import android.os.Bundle;
import android.util.Log;

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
		ParseClient.getParseUserJoinedEvents((User)ParseUser.getCurrentUser(),
				new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException e) {
				if(e == null) {
					getAdapter().clear();
					getAdapter().addAll(listEvents);
					lvEvents.onRefreshComplete();
				} else {
					Log.e("ERROR", "ParseException on all events: " + e);
					e.printStackTrace();
				}
			}
		});
	}

}
