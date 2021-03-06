package com.codepath.adhoc.application;

import android.app.Application;

import com.codepath.adhoc.parsemodels.Events;
import com.codepath.adhoc.parsemodels.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Register parse models here
		ParseObject.registerSubclass(Events.class);
		ParseObject.registerSubclass(User.class);
		
		// Parse initialization with API keys
		Parse.initialize(this, "SldbNf6UPU5POie3S7bo76x3iEH7tRbkHJmEew5J", "3c2SDflnsYwN2Nhno1sCCb8hKS1P8M3NWz2b36be");
	}

}
