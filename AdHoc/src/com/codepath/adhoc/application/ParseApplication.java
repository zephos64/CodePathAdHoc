package com.codepath.adhoc.application;

import android.app.Application;
import android.provider.CalendarContract.Events;

import com.codepath.adhoc.parsemodels.Details;
import com.codepath.adhoc.parsemodels.EventItem;
import com.codepath.adhoc.parsemodels.Location;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Register parse models here
		ParseObject.registerSubclass(EventItem.class);
		
		// Parse initialization with API keys
		Parse.initialize(this, "SldbNf6UPU5POie3S7bo76x3iEH7tRbkHJmEew5J", "3c2SDflnsYwN2Nhno1sCCb8hKS1P8M3NWz2b36be");
		
		ParseObject.registerSubclass(Details.class);
		ParseObject.registerSubclass(com.codepath.adhoc.parsemodels.Events.class);
		ParseObject.registerSubclass(Location.class);
		
		//EventItem ei = new EventItem("Let's play soccer!");
		
		// Set the current user, who we think is the person signed onto the app
		//ei.setOwner(ParseUser.getCurrentUser());
		
		// Now save the data asynchronously
		//ei.saveInBackground();
		
		// for more robust offline save
		// ei.saveEventually();
		
	}

}
