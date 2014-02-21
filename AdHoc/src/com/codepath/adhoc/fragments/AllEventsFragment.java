package com.codepath.adhoc.fragments;

import java.util.List;

import android.os.Bundle;

import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class AllEventsFragment extends EventListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ParseClient.getParseAllEvents(new FindCallback<Events>() {

			@Override
			public void done(List<Events> listEvents, ParseException arg1) {
				// TODO Auto-generated method stub
				getAdapter().clear();
				getAdapter().addAll(listEvents);
			}
		});
		
	}
}
