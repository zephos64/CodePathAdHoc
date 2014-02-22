package com.codepath.adhoc.parsemodels;

import java.util.Date;
import java.util.List;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.AdHocUtils.EventStates;
import com.codepath.adhoc.application.ParseClient;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Events")
public class Events extends ParseObject {
	public Events() {

	}
	
	public Events(EventStates tbs, String name, int maxAttend, String time, String endTime,
			String desc, ParseUser hostUser) {		
		put(AdHocUtils.eventState, tbs.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventDesc, desc);
		
		ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		relation.add(hostUser);
	}

	public Events(String state, String name, int maxAttend, String time, String endTime,
			int longitude, int latitude, String desc, ParseUser hostUser) {
		put(AdHocUtils.eventState, state);
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventLocLong, longitude);
		put(AdHocUtils.eventLocLat, latitude);
		put(AdHocUtils.eventDesc, desc);

		ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		relation.add(hostUser);
	}

	public String getEventState() {
		return getString(AdHocUtils.eventState);
	}

	public String getEventName() {
		return getString(AdHocUtils.eventName);
	}

	public int getMaxAttendees() {
		return getInt(AdHocUtils.eventMaxAttend);
	}

	public String getEventTime() {
		return getString(AdHocUtils.eventTime);
	}
	
	public String getEventTimeEnd() {
		return getString(AdHocUtils.eventTimeEnd);
	}

	public int getLocLong() {
		return getInt(AdHocUtils.eventLocLong);
	}

	public int getLocLat() {
		return getInt(AdHocUtils.eventLocLat);
	}

	public String getDesc() {
		return getString(AdHocUtils.eventDesc);
	}

	public Date getCreatedAt() {
		return getDate(AdHocUtils.eventCreatedAt);
	}

	public Date getUpdatedAt() {
		return getDate(AdHocUtils.eventUpdatedAt);
	}

	public ParseUser getHostUserIdRelation() {
		//TODO check
		return getParseUser(AdHocUtils.eventHostUserId);
	}
	
	public ParseRelation<User> getJoinedUsersRelation() {
	      return getRelation(AdHocUtils.eventJoinedUsersId);
	  }
	
	public int getCountUsersJoined() {
		//TODO check
		ParseClient.getCountJoinedUsers(this, new FindCallback<Events>() {
			
			@Override
			public void done(List<Events> listUsers, ParseException e) {
				if (e == null) {
		            // Access the array of results here
		            //return listUsers.size();
		        } else {
		            Log.d("ERROR", "Error: " + e.getMessage());
		        }
			}
		});
		return -1;
	}
	
	public boolean checkUserInJoinedList(String id) {
		//TODO fix
		String listUsers= getString(AdHocUtils.eventJoinedUsersId);
		if(listUsers.isEmpty()) {
			return false;
		}
		if(listUsers.contains(id)) {
			return true;
		}
		return false;
	}

	public void setEventState(String newState) {
		put(AdHocUtils.eventState, newState);
	}
	
	public void setLocLat(int lat) {
		put(AdHocUtils.eventLocLat, lat);
	}
	
	public void setLocLong(int longi) {
		put(AdHocUtils.eventLocLat, longi);
	}

	public void addJoinedUser(String userId) {
		//TODO fix
		Log.d("DEBUG", "Adding user " + userId + " to event "
				+ this.getObjectId());
		String usersJoined = getString(AdHocUtils.eventJoinedUsersId);
		Log.d("DEBUG", "Old user list : " + usersJoined);
		String listUsers;
		if(usersJoined.isEmpty()) {
			listUsers = userId;
		} else {
			listUsers = usersJoined + "," + userId;
		}

		Log.d("DEBUG", "New user list : " + listUsers);
		put(AdHocUtils.eventJoinedUsersId, listUsers);
	}

	public void removeJoinedUser(String userId, String[] usersJoined) {
		//TODO fix
		String listUsers = "";
		for (int a = 0; a < usersJoined.length; a++) {
			if (!usersJoined[a].equals(userId)) {
				listUsers += "," + usersJoined[a];
			}
		}
		put(AdHocUtils.eventJoinedUsersId, listUsers);
	}
}
