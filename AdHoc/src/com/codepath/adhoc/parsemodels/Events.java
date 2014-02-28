package com.codepath.adhoc.parsemodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.AdHocUtils.EventStates;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Events")
public class Events extends ParseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdHocUtils.EventRelation userRelation = AdHocUtils.EventRelation.NONE;

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
		put(AdHocUtils.eventAttendanceCount, 1);
		
		put(AdHocUtils.eventHostUser, hostUser);
		put(AdHocUtils.eventJoinedUser, new ArrayList<User>());
		//ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		//relation.add(hostUser);
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
		put(AdHocUtils.eventLoc, new ParseGeoPoint(latitude, longitude));
		put(AdHocUtils.eventAttendanceCount, 1);

		put(AdHocUtils.eventHostUser, hostUser);
		put(AdHocUtils.eventJoinedUser, new ArrayList<User>());
		//ParseRelation<ParseUser> relation = getRelation(AdHocUtils.eventHostUserId);
		//relation.add(hostUser);
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
	
	public int getAttendanceCount() {
		return getInt(AdHocUtils.eventAttendanceCount);
	}

	public void setEventState(String newState) {
		put(AdHocUtils.eventState, newState);
	}
	
	public void setLoc(double lat, double longi) {
		put(AdHocUtils.eventLoc, new ParseGeoPoint(lat, longi));
	}

	/*public ParseRelation<User> getHostUserIdRelation() {
		return getRelation(AdHocUtils.eventHostUserId);
	}

	public ParseRelation<User> getJoinedUsersRelation() {
		return getRelation(AdHocUtils.eventJoinedUsersId);
	}
	
	public void addJoinedUser(User userObj) {
		getJoinedUsersRelation().add(userObj);
		increment(AdHocUtils.eventAttendanceCount);
	}

	public void removeJoinedUser(User userObj) {
		getJoinedUsersRelation().remove(userObj);
		increment(AdHocUtils.eventAttendanceCount, -1);
	}*/
	
	public String getHostUserId() {
		return getParseObject(AdHocUtils.eventHostUser).getObjectId();
	}
	
	public List<User> getJoinedUserIds() {
		return getList(AdHocUtils.eventJoinedUser);
	}
	
	public void addJoinedUser(User userObj) {
		getList(AdHocUtils.eventJoinedUser).add(userObj);
	}
	
	public void removeJoinedUser(User userObj) {
		List<Events> eventList = getList(AdHocUtils.eventJoinedUser);
		if(eventList.contains(userObj)) {
			eventList.remove(userObj);
		}
	}
	
	@Override
	public boolean equals(Object otherEvent) {
		if(otherEvent instanceof Events && 
				getObjectId().equals(((Events)otherEvent).getObjectId())) {
			return true;
		}
		return false;
	}
}
