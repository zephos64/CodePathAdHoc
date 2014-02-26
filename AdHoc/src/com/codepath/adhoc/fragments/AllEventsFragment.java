package com.codepath.adhoc.fragments;

import java.util.List;

import android.os.Bundle;

import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.FindCallback;
import com.parse.ParseException;

public class AllEventsFragment extends EventListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadList();
	}

	@Override
	public void loadList() {
		ParseClient.getParseAllEvents(loc,
				new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException arg1) {
				getAdapter().clear();
				getAdapter().addAll(listEvents);
				lvEvents.onRefreshComplete();
			}
		});
	}
}
