package com.codepath.adhoc.parsemodels;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
	// TODO ensure setters work (add/remove)
	
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
	
	public ParseRelation<Events> getEventsHostingRelation() {
		return getRelation(AdHocUtils.userEventsCreated);
	}
	
	public ParseRelation<Events> getEventsJoinedRelation() {
		return getRelation(AdHocUtils.userEventsAttending);
	}
	
	public String[] getEventsAttending() {
		//TODO fix
		String listEvents = getString(AdHocUtils.userEventsAttending);
		return listEvents.split("[,]");
	}
	
	public String[] getEventsCreated() {
		//TODO fix
		String listEvents = getString(AdHocUtils.userEventsCreated);
		return listEvents.split("[,]");
	}
	
	public void addEventAttending(String userId, String[] eventsAttending) {
		//TODO fix
		String listEvents = userId;
		for(int a = 0; a < eventsAttending.length; a++) {
			listEvents += "," + eventsAttending[a];
		}
		put(AdHocUtils.userEventsAttending, listEvents);
	}
	
	public void addEventHosting(Events eventObj) {
		//TODO check
		/*String listEvents = userId;
		for(int a = 0; a < eventsHosting.length; a++) {
			listEvents += "," + eventsHosting[a];
		}
		put(AdHocUtils.userEventsCreated, listEvents);*/
		getEventsHostingRelation().add(eventObj);
	}
	
	public void removeEventsAttending(String userId, String[] eventsAttending) {
		//TODO fix
		String listEvents = "";
		for(int a = 0; a < eventsAttending.length; a++) {
			if(!eventsAttending[a].equals(userId)) {
				listEvents += "," + eventsAttending[a];
			}
		}
		put(AdHocUtils.userEventsAttending, listEvents);
	}
	
	public void removeEventsHosting(String userId, String[] eventsHosting) {
		//TODO fix
		String listEvents = "";
		for(int a = 0; a < eventsHosting.length; a++) {
			if(!eventsHosting[a].equals(userId)) {
				listEvents += "," + eventsHosting[a];
			}
		}
		put(AdHocUtils.userEventsCreated, listEvents);
	}
}
