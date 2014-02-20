package com.codepath.adhoc.application;

import java.util.List;

import android.util.Log;

import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseClient {

	public void getParseAllEvents() {
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereEqualTo("owner", ParseUser.getCurrentUser());
		// Execute the find asynchronously
		query.findInBackground(new FindCallback<Events>() {
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
	
	public void getParseUserJoinedEvents() {
		
	}
	
	public static void getParseUserCreatedEvents(int id) {
		Log.d("DEBUG", "Getting event with id " + id);
		
		// Define the class we would like to query
		ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
		// Define our query conditions
		query.whereEqualTo("hosted_by_id", id);
		// Execute the find asynchronously
		query.findInBackground(new FindCallback<Events>() {
			public void done(List<Events> itemList, ParseException e) {
				if (e == null) {
					// Access the array of results here
					Log.d("DEBUG", itemList.get(0).getObjectId()+"");
				} else {
					Log.d("item", "Error: " + e.getMessage());
				}
			}
		});
	}
	
	public void getParseEventDetails() {
		
	}
	
	public static void getParseUser(String userName) {
		// Define the class we would like to query
		ParseQuery<User> query = ParseQuery.getQuery(User.class);
		// Define our query conditions
		query.whereEqualTo("username", userName);
		// Execute the find asynchronously
		query.findInBackground(new FindCallback<User>() {
		    public void done(List<User> itemList, ParseException e) {
		        if (e == null) {
		            // Access the array of results here
		            User firstItem = itemList.get(0);
		        } else {
		            Log.d("item", "Error: " + e.getMessage());
		        }
		    }
		});
	}
}
