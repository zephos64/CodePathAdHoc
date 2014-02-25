package com.codepath.adhoc.parsemodels;

import java.io.Serializable;
import java.util.Date;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.AdHocUtils.EventStates;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
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
			String desc, ParseUser hostUser, String addr) {		
		put(AdHocUtils.eventState, state.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventDesc, desc);
		put(AdHocUtils.eventAddress, addr);
		
		ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		relation.add(hostUser);
	}

	public Events(EventStates state, String name, int maxAttend, String time, String endTime,
			String desc, ParseUser hostUser, String addr, double longitude, double latitude) {
		put(AdHocUtils.eventState, state.toString());
		put(AdHocUtils.eventName, name);
		put(AdHocUtils.eventMaxAttend, maxAttend);
		put(AdHocUtils.eventTime, time);
		put(AdHocUtils.eventTimeEnd, endTime);
		put(AdHocUtils.eventDesc, desc);
		put(AdHocUtils.eventAddress, addr);
		//put(AdHocUtils.eventLocLong, longitude);
		//put(AdHocUtils.eventLocLat, latitude);
		put(AdHocUtils.eventLoc, new ParseGeoPoint(latitude, longitude));

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

	/*public double getLocLong() {
		return getDouble(AdHocUtils.eventLocLong);
	}

	public double getLocLat() {
		return getDouble(AdHocUtils.eventLocLat);
	}*/
	
	public ParseGeoPoint getLoc() {
		return getParseGeoPoint(AdHocUtils.eventLoc);
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
	
	public String getAddress() {
		return getString(AdHocUtils.eventAddress);
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
	
	/*public void setLocLat(double lat) {
		put(AdHocUtils.eventLocLat, lat);
	}
	
	public void setLocLong(double longi) {
		put(AdHocUtils.eventLocLat, longi);
	}*/
	
	public void setLoc(double lat, double longi) {
		put(AdHocUtils.eventLoc, new ParseGeoPoint(lat, longi));
	}

	public void addJoinedUser(User userObj) {
		getJoinedUsersRelation().add(userObj);
	}

	public void removeJoinedUser(User userObj) {
		getJoinedUsersRelation().remove(userObj);
	}
}
