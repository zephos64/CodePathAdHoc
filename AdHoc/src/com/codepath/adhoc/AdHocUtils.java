package com.codepath.adhoc;

public class AdHocUtils {
	public final static String eventObjectId = "objectId";
	public final static String eventState = "event_state";
	public final static String eventName = "event_name";
	public final static String eventMaxAttend = "max_attendees";
	public final static String eventTime = "event_time";
	public final static String eventLocLong = "longitude";
	public final static String eventLocLat = "latitude";
	public final static String eventDesc = "description";
	public final static String eventCreatedAt = "createdAt";
	public final static String eventUpdatedAt = "updateAt";
	public final static String eventHostUserId = "hosted_by_id";
	public final static String eventJoinedUsersId = "joined_user_id";

	public enum EventStates {
		INPROG ("IN PROGRESS"),
		TBS("TO BE STARTED"),
		CANCELLED("CANCELLED"),
		FINISHED("FINISHED");
		
		private final String state;
		EventStates(String state) {
			this.state = state;
		}
		
		public String toString() {
			return state;
		}
	}

	public final static String userObjectId = "objectId";
	public final static String userName = "username";
	public final static String userPassword = "password";
	public final static String userAuthData = "authData";
	public final static String userPhoneNum = "phone";
	public final static String userEventsAttending = "events_attending";
	public final static String userEventsCreated = "events_created";
}
