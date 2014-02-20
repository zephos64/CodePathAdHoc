package com.codepath.adhoc.parsemodels;

import java.util.Date;

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
	}

	public String getObjectId() {
		return getString(AdHocUtils.eventObjectId);
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

	public void setEventState(String newState) {
		put(AdHocUtils.eventState, newState);
	}
	
	public void setLocLat(int lat) {
		put(AdHocUtils.eventLocLat, lat);
	}
	
	public void setLocLong(int longi) {
		put(AdHocUtils.eventLocLat, longi);
	}

	public void addJoinedUser(String userId, String[] usersJoined) {
		String listUsers = userId;
		for (int a = 0; a < usersJoined.length; a++) {
			listUsers += "," + usersJoined[a];
		}
		put(AdHocUtils.eventJoinedUsersId, listUsers);
	}

	public void removeJoinedUser(String userId, String[] usersJoined) {
		String listUsers = "";
		for (int a = 0; a < usersJoined.length; a++) {
			if (!usersJoined[a].equals(userId)) {
				listUsers += "," + usersJoined[a];
			}
		}
		put(AdHocUtils.eventJoinedUsersId, listUsers);
	}
}
