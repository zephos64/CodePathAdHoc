package com.codepath.adhoc.parsemodels;

import java.util.Date;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.AdHocUtils.EventStates;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Events")
public class Events extends ParseObject {
	public Events() {

	}
	
	public Events(EventStates tbs, String name, int maxAttend, String time,
			String desc, String hostedId) {
		put(AdHocUtils.eventState, tbs.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventDesc, desc);
		put(AdHocUtils.eventHostUserId, hostedId);
		
		put(AdHocUtils.eventJoinedUsersId, "");
	}

	public Events(String state, String name, int maxAttend, String time,
			int longitude, int latitude, String desc, String hostedId) {
		put(AdHocUtils.eventState, state);
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventLocLong, longitude);
		put(AdHocUtils.eventLocLat, latitude);
		put(AdHocUtils.eventDesc, desc);
		put(AdHocUtils.eventHostUserId, hostedId);
		
		put(AdHocUtils.eventJoinedUsersId, "");
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

	public String getHostUserId() {
		return getString(AdHocUtils.eventHostUserId);
	}
	
	public int getCountUsersJoined() {
		String listUsers= getString(AdHocUtils.eventJoinedUsersId);
		if(listUsers.isEmpty()) {
			return 0;
		}
		String[] list = listUsers.split("[,]");
		return list.length;
	}
	
	public boolean checkUserInJoinedList(String id) {
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
