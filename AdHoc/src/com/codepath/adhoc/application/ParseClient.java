package com.codepath.adhoc.application;

import java.util.Arrays;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

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
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
		query.orderByAscending(AdHocUtils.eventTime);
		// Execute the find asynchronously
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserJoinedEvents(User userObj, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user joined with id " + userObj.getObjectId());
		
		ParseRelation<Events> userRel = userObj.getEventsAttendingRelation();
		ParseQuery<Events> query = userRel.getQuery();
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
		query.orderByAscending(AdHocUtils.eventTime);
		query.findInBackground(findCallback);
	}
	
	public static void getParseUserCreatedEvents(User userObj, FindCallback<Events> findCallback) {
		Log.d("DEBUG", "Getting events user created with id " + userObj.getObjectId());
		
		ParseRelation<Events> userRel = userObj.getEventsHostingRelation();
		ParseQuery<Events> query = userRel.getQuery();
		query.whereNotContainedIn(AdHocUtils.eventState, 
				Arrays.asList(AdHocUtils.EventStates.FINISHED.toString(),
						AdHocUtils.EventStates.CANCELLED.toString()));
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
	
	public static void getJoinedUsers(Events event, FindCallback<User> findCallback) {
		Log.d("DEBUG", "Getting joined users for event : " + event.getObjectId());
		
		ParseRelation<User> userRel = event.getJoinedUsersRelation();
		ParseQuery<User> query = userRel.getQuery();
		query.findInBackground(findCallback);
	}
	
	public static void getHostUser(Events event, FindCallback<User> findCallback) {
		Log.d("DEBUG", "Getting host user for event : " + event.getObjectId());
		
		ParseRelation<User> userRel = event.getHostUserIdRelation();
		ParseQuery<User> query = userRel.getQuery();
		query.findInBackground(findCallback);
	}
}
