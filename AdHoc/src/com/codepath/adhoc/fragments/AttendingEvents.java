package com.codepath.adhoc.fragments;

import java.util.List;

import android.os.Bundle;

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
		
		ParseClient.getParseUserJoinedEvents((User)ParseUser.getCurrentUser(),
				new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException arg1) {
				getAdapter().clear();
				getAdapter().addAll(listEvents);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	void getMoreListData(int numNewItems) {
		// TODO Auto-generated method stub

	}

	@Override
	void getMoreItems(int moreItems) {
		// TODO Auto-generated method stub
		
	}

}
