package com.codepath.adhoc.parsemodels;

import com.parse.ParseObject;

public class Events extends ParseObject {
	public Events() {
		
	}
	
	public String getEventId() {
		return getString("event_id");
	}
	
	public String getHostedById() {
		return getString("hosted_by_id");
	}
	
	public String getJoinedUserId() {
		return getString("joined_user_id");
	}
	
	public void setEventId(int eventId) {
		put("event_id", eventId);
	}
	
	public void setHostedById(int hostedById) {
		put("hosted_by_id", hostedById);
	}
	
	public void setJoinedUserId(int joinedUserId) {
		put("joined_user_id", joinedUserId);
	}
}
