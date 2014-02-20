package com.codepath.adhoc.parsemodels;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject{
	// TODO ensure setters work (add/remove)
	
	public User() {
		super();
	}

	public String getID() {
		return getString(AdHocUtils.userObjectId);
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
	
	public String[] getEventsAttending() {
		String listEvents = getString(AdHocUtils.userEventsAttending);
		return listEvents.split("[,]");
	}
	
	public String[] getEventsCreated() {
		String listEvents = getString(AdHocUtils.userEventsCreated);
		return listEvents.split("[,]");
	}
	
	public void addEventAttending(String userId, String[] eventsAttending) {
		String listEvents = userId;
		for(int a = 0; a < eventsAttending.length; a++) {
			listEvents += "," + eventsAttending[a];
		}
		put(AdHocUtils.userEventsAttending, listEvents);
	}
	
	public void addEventHosting(String userId, String[] eventsHosting) {
		String listEvents = userId;
		for(int a = 0; a < eventsHosting.length; a++) {
			listEvents += "," + eventsHosting[a];
		}
		put(AdHocUtils.userEventsCreated, listEvents);
	}
	
	public void removeEventsAttending(String userId, String[] eventsAttending) {
		String listEvents = "";
		for(int a = 0; a < eventsAttending.length; a++) {
			if(!eventsAttending[a].equals(userId)) {
				listEvents += "," + eventsAttending[a];
			}
		}
		put(AdHocUtils.userEventsAttending, listEvents);
	}
	
	public void removeEventsHosting(String userId, String[] eventsHosting) {
		String listEvents = "";
		for(int a = 0; a < eventsHosting.length; a++) {
			if(!eventsHosting[a].equals(userId)) {
				listEvents += "," + eventsHosting[a];
			}
		}
		put(AdHocUtils.userEventsCreated, listEvents);
	}
}
