package com.codepath.adhoc.parsemodels;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("EventItem")
public class EventItem extends ParseObject {

	public EventItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventItem(String body) {
		super();
		setBody(body);
		// TODO Auto-generated constructor stub
	}

	public void setBody(String string) {
		// TODO Auto-generated method stub
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
}
