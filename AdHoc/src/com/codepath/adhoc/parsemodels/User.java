package com.codepath.adhoc.parsemodels;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseRelation;
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
	
	public ParseRelation<Events> getEventsHostingRelation() {
		return getRelation(AdHocUtils.userEventsCreated);
	}
	
	public ParseRelation<Events> getEventsAttendingRelation() {
		return getRelation(AdHocUtils.userEventsAttending);
	}
	
	public void addEventAttending(Events eventObj) {
		getEventsAttendingRelation().add(eventObj);
	}
	
	public void addEventHosting(Events eventObj) {
		getEventsHostingRelation().add(eventObj);
	}
	
	public void removeEventsAttending(Events eventObj) {
		//TODO check
		getEventsAttendingRelation().remove(eventObj);
	}
	
	public void removeEventsHosting(Events eventObj) {
		//TODO check
		getEventsHostingRelation().remove(eventObj);
	}
}
