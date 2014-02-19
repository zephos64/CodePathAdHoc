package com.codepath.adhoc.parsemodels;

import com.parse.ParseObject;

public class Details extends ParseObject {
	public Details() {
		
	}
	
	public String getLocationId() {
		return getString("location_id");
	}
	
	public String getEventName() {
		return getString("event_name");
	}
	
	public String getNumberOfAttendees() {
		return getString("number_of_attendees");
	}
	
	public String getMaxAttendees() {
		return getString("max_attendees");
	}
	
	public String getDescription() {
		return getString("description");
	}
	
	public String getTimeOfEvent() {
		return getString("time_of_event");
	}
	
	public void setLocationId(int locId) {
		put("location_id", locId);
	}
	
	public void setEventName(String event) {
		put("event_name", event);
	}
	
	public void setNumberOfAttendees(int numAttendees) {
		put("number_of_attendees", numAttendees);
	}
	
	public void setMaxAttendees(int maxAttendees) {
		put("max_attendees", maxAttendees);
	}
	
	public void setDescription(String description) {
		put("description", description);
	}
	
	public void setTimeOfEvent(String timeOfEvent) {
		put("time_of_event", timeOfEvent);
	}
}
