package com.codepath.adhoc.parsemodels;

import java.io.Serializable;
import java.util.Date;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.AdHocUtils.EventStates;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Events")
public class Events extends ParseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Events() {

	}
	
	public Events(EventStates state, String name, int maxAttend, String time, String endTime,
			String desc, ParseUser hostUser) {		
		put(AdHocUtils.eventState, state.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventDesc, desc);
		
		ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		relation.add(hostUser);
	}

	public Events(EventStates state, String name, int maxAttend, String time, String endTime,
			String desc, ParseUser hostUser, double longitude, double latitude) {
		put(AdHocUtils.eventState, state.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventDesc, desc);
		put(AdHocUtils.eventLocLong, longitude);
		put(AdHocUtils.eventLocLat, latitude);

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

	public ParseRelation<User> getHostUserIdRelation() {
		return getRelation(AdHocUtils.eventHostUserId);
	}
	
	public ParseRelation<User> getJoinedUsersRelation() {
	      return getRelation(AdHocUtils.eventJoinedUsersId);
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

	public void addJoinedUser(User userObj) {
		getJoinedUsersRelation().add(userObj);
	}

	public void removeJoinedUser(User userObj) {
		getJoinedUsersRelation().remove(userObj);
	}
}
