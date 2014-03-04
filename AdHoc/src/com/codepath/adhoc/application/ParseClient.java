package com.codepath.adhoc.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class ParseClient {
	/* Example FindCallback<>
	 * new FindCallback<Events>() {
		    public void done(List<Events> itemList, ParseException e) {
		        if (e == null) {
		            // Access the array of results here
		            String firstItemId = itemList.get(0).getObjectId();
		        } else {
		            Log.d("item", "Error: " + e.getMessage());
		        }
		    }
		});
	}
	 */

	public static void getParseAllEvents(LatLng loc, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting all events");
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
		query.whereWithinMiles(AdHocUtils.eventLoc,
				new ParseGeoPoint(loc.latitude, loc.longitude),
				AdHocUtils.milesLocRadius);
		query.setLimit(AdHocUtils.userLoadLimit);
		query.orderByAscending(AdHocUtils.eventTime);
		
		query.include(AdHocUtils.eventHostUser);
		
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserJoinedEvents(User userObj, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user joined with id " + userObj.getObjectId());
		
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		query.include(AdHocUtils.eventJoinedUser);
		ArrayList<User> temp = new ArrayList<User>();
		temp.add(userObj);
		query.whereContainedIn(AdHocUtils.eventJoinedUser, temp);
		
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
		
		query.setLimit(AdHocUtils.userLoadLimit);
		query.orderByAscending(AdHocUtils.eventTime);
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserCreatedEvents(User userObj, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user created with id " + userObj.getObjectId());
		
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		query.include(AdHocUtils.eventHostUser);
		ArrayList<User> temp = new ArrayList<User>();
		temp.add(userObj);
		query.whereContainedIn(AdHocUtils.eventHostUser, temp);
		
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
		
		query.setLimit(AdHocUtils.userLoadLimit);
		query.orderByAscending(AdHocUtils.eventTime);
		query.findInBackground(findCallback);
	}
	
	public static void getParseEventDetails(String eventId, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting event with id " + eventId);
		
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereEqualTo(AdHocUtils.objectId, eventId);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
}
