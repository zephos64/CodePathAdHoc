package com.codepath.adhoc.parsemodels;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
	public User() {
		super();
	}
	
	public String getUserName() {
		return getString(AdHocUtils.userName);
	}
	
	public String getPassword() {
		return getString(AdHocUtils.userPassword);
	}
	
	public String getAuthData() {
		return getString(AdHocUtils.userAuthData);
	}
	
	public String getPhoneNum() {
		return getString(AdHocUtils.userPhoneNum);
	}
	
	public List<Events> getEventsHosting() {
		return getList(AdHocUtils.userEventsHost);
	}
	
	public List<Events> getEventsJoined() {
		return getList(AdHocUtils.userEventsJoined);
	}
	
	public void addEventsJoined(Events eventObj) {
		if(getList(AdHocUtils.userEventsJoined) == null) {
			Log.e("ERROR", "User's list of joined events null, filling...");
			put(AdHocUtils.userEventsJoined, new ArrayList<Events>());
		}
		getList(AdHocUtils.userEventsJoined).add(eventObj);
	}
	
	public void addEventsHosting(Events eventObj) {
		if(getList(AdHocUtils.userEventsHost) == null) {
			Log.e("ERROR", "User's list of host events null, filling...");
			put(AdHocUtils.userEventsHost, new ArrayList<Events>());
		}
		getList(AdHocUtils.userEventsHost).add(eventObj);
	}
	
	public void removeEventsJoined(Events eventObj) {
		List<Events> eventList = getList(AdHocUtils.userEventsJoined);
		if(eventList.contains(eventObj)) {
			eventList.remove(eventObj);
		}
	}
	
	public void removeEventHosting(Events eventObj) {
		List<Events> eventList = getList(AdHocUtils.userEventsHost);
		if(eventList.contains(eventObj)) {
			eventList.remove(eventObj);
		}
	}
	
	@Override
	public boolean equals(Object otherUser) {
		if(otherUser instanceof User && 
				getObjectId().equals(((User)otherUser).getObjectId())) {
			return true;
		}
		return false;
	}
}
