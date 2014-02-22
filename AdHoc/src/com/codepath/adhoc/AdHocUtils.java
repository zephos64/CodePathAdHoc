package com.codepath.adhoc;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.view.ViewConfiguration;

public class AdHocUtils {
	public final static String objectId = "objectId";
	public final static String dateFormat = "hh:mm aa";
	
	public final static String intentDetailsId = "intent_item_details";
	
	public final static String eventState = "event_state";
	public final static String eventName = "event_name";
	public final static String eventMaxAttend = "max_attendees";
	public final static String eventTime = "event_time";
	public final static String eventTimeEnd = "event_time_end";
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

	public final static String userName = "username";
	public final static String userPassword = "password";
	public final static String userAuthData = "authData";
	public final static String userPhoneNum = "phone";
	public final static String userEventsAttending = "events_attending";
	public final static String userEventsCreated = "events_created";
	
	public static String getTime(String time) {
		String[] broken = time.split("");
		String hour = broken[12]+broken[13];
		String min = broken[15]+broken[16];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
		cal.set(Calendar.MINUTE, Integer.valueOf(min));
		
		SimpleDateFormat sdf = new SimpleDateFormat(AdHocUtils.dateFormat);
		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Forces actionbar items to be in actionbar
	 *  otherwise phones might show it in bottom bar spinner
	 *  instead of actionbar
	 * @param the activity, should be 'this'
	 */
	public static void forceShowActionBar(Activity act) {
		try {
	        ViewConfiguration config = ViewConfiguration.get(act);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
	}
}
