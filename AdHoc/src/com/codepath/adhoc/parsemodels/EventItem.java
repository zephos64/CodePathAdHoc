package com.codepath.adhoc.parsemodels;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("EventItem")
public class EventItem extends ParseObject {

	public EventItem() {
		super();
	}

	public EventItem(String body) {
		super();
		setBody(body);
	}

	public void setBody(String string) {
		put("body", string);
	}
	
	public String getBody() {
		return getString("body");
	}
	
	public ParseUser getUser() {
		return getParseUser("owner");
	}
	
	public void setOwner(ParseUser user) {
		put("owner", user);
	}
	
    public static EventItem fromJson(JSONObject jsonObject) {
        EventItem eventItem = new EventItem();
        try {
        	// ID
        	eventItem.put(AdHocUtils.eventItemCreatorId, jsonObject.getString(AdHocUtils.eventItemCreatorId));
        	eventItem.put(AdHocUtils.eventItemState, jsonObject.getString(AdHocUtils.eventItemState));
        	eventItem.put(AdHocUtils.eventItemName, jsonObject.getString(AdHocUtils.eventItemName));
        	eventItem.put(AdHocUtils.eventItemMinAttend, jsonObject.getString(AdHocUtils.eventItemMinAttend));
        	eventItem.put(AdHocUtils.eventItemMaxAttend, jsonObject.getString(AdHocUtils.eventItemMaxAttend));
        	eventItem.put(AdHocUtils.eventItemTime, jsonObject.getString(AdHocUtils.eventItemTime));
        	eventItem.put(AdHocUtils.eventItemLocLong, jsonObject.getString(AdHocUtils.eventItemLocLong));
        	eventItem.put(AdHocUtils.eventItemLocLat, jsonObject.getString(AdHocUtils.eventItemLocLat));
        	eventItem.put(AdHocUtils.eventItemDesc, jsonObject.getString(AdHocUtils.eventItemDesc));
        	eventItem.put(AdHocUtils.eventItemCreationTime, jsonObject.getString(AdHocUtils.eventItemCreationTime));
        	eventItem.put(AdHocUtils.eventItemCurrCount, jsonObject.getString(AdHocUtils.eventItemCurrCount));
        	
        	//eventItem.setBody(jsonObject.getString("body"));
        	
        } catch (JSONException e) {
            Log.e("err", "Error in parsing EventItem " + e.toString());
            e.printStackTrace();
            return null;
        }
        return eventItem;
    }

    public static ArrayList<EventItem> fromJson(JSONArray jsonArray) {
        ArrayList<EventItem> events = new ArrayList<EventItem>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject eiJson = null;
            try {
                eiJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
             Log.e("err", "Error in parsing EventItem in event item list " + e.toString());
             e.printStackTrace();
                continue;
            }

            EventItem eventItem = EventItem.fromJson(eiJson);
            if (eventItem != null) {
                events.add(eventItem);
            }
        }

        return events;
    }
}
