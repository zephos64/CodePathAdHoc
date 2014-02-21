package com.codepath.adhoc.application;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.parsemodels.Events;
import com.parse.FindCallback;
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

	public static void getParseAllEvents(FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting all events");
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserJoinedEvents(String userId, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user joined with id " + userId);
		
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereContains(AdHocUtils.eventJoinedUsersId, userId);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserCreatedEvents(String userId, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user created with id " + userId);
		
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereEqualTo(AdHocUtils.eventHostUserId, userId);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
	
	public static void getParseEventDetails(String eventId, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting event with id " + eventId);
		
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereEqualTo(AdHocUtils.eventObjectId, eventId);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
}
