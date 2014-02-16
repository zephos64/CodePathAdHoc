package com.codepath.adhoc.application;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FacebookClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = FacebookApi.class;
    public static final String REST_URL = "http://api.twitter.com";
    public static final String REST_CONSUMER_KEY = "SOME_KEY_HERE";
    public static final String REST_CONSUMER_SECRET = "SOME_SECRET_HERE";
    public static final String REST_CALLBACK_URL = "oauth://arbitraryname.com";

    public FacebookClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
          REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // ENDPOINTS BELOW

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
      String apiUrl = getApiUrl("statuses/home_timeline.json");
      RequestParams params = new RequestParams();
      params.put("page", String.valueOf(page));
      client.get(apiUrl, params, handler);
    }

}
