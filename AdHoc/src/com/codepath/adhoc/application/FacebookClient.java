package com.codepath.adhoc.application;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.parse.ParseFacebookUtils;

public class FacebookClient {
	
	public static void getFacebookUser(Callback callback) {
		new Request(
			    ParseFacebookUtils.getSession(),
			    "/me",
			    null,
			    HttpMethod.GET,
			    callback
			).executeAsync();
	}
}
