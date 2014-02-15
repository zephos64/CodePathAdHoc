package com.codepath.adhoc.parsemodels;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codepath.adhoc.AdHocUtils;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject{
	public User() {
		super();
	}
	
    public static User fromJson(JSONObject jsonObject) {
        User userObject = new User();
        try {
        	userObject.put(AdHocUtils.userFBid, jsonObject.getString(AdHocUtils.userFBid));
        	userObject.put(AdHocUtils.userName, jsonObject.getString(AdHocUtils.userName));
        	userObject.put(AdHocUtils.userEmail, jsonObject.getString(AdHocUtils.userEmail));
        	userObject.put(AdHocUtils.userLoc, jsonObject.getString(AdHocUtils.userLoc));
        	userObject.put(AdHocUtils.userPhoneNum, jsonObject.getString(AdHocUtils.userPhoneNum));
        	
        	// may want to parse this, since it would be a list of EventItems
        	userObject.put(AdHocUtils.userListEventsAttending, jsonObject.getString(AdHocUtils.userListEventsAttending));
        	
        } catch (JSONException e) {
            Log.e("err", "Error in parsing User " + e.toString());
            e.printStackTrace();
            return null;
        }
        return userObject;
    }

    public static ArrayList<User> fromJson(JSONArray jsonArray) {
        ArrayList<User> listUsers = new ArrayList<User>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject userJson = null;
            try {
            	userJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
             Log.e("err", "Error in parsing User in user item list " + e.toString());
             e.printStackTrace();
                continue;
            }

            User userItem = User.fromJson(userJson);
            if (userItem != null) {
            	listUsers.add(userItem);
            }
        }

        return listUsers;
    }
}
