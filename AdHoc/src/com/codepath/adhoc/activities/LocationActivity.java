package com.codepath.adhoc.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.application.CustomInfoWindowAdapter;
import com.codepath.adhoc.application.ParseClient;
import com.codepath.adhoc.models.LocationData;
import com.codepath.adhoc.parsemodels.Events;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class LocationActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks,
																   GooglePlayServicesClient.OnConnectionFailedListener,
																   LocationListener, 
																   OnMarkerDragListener{
	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    private Marker 				myPosMarker;
    private List<Marker>		eventsMarkers = null;
    private List<Events>		allEvents;
    private LatLng 				myPos =new LatLng(currentLat,currentLng);
    LocationRequest 			mLocationRequest;
//    private static final int 	GPS_ENABLE_ACTIVITY = 100;
    private static final int 	MILLISECONDS_PER_SECOND = 1000;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long   UPDATE_INTERVAL =MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int 	FASTEST_INTERVAL_IN_SECONDS = 5;
    private static final long 	FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    private static final long   INTEREST_RADIUS_MILES = AdHocUtils.milesLocRadius;
    private static final double INTEREST_RADIUS_METERS = 1609.34 * INTEREST_RADIUS_MILES;
    private static final double METER_TO_MILE_FACTOR = 0.000621371;
    private boolean  firstMapUpdate = true;
    LinearLayout llProgressLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdHocUtils.forceShowActionBar(this);
		
		allEvents=new ArrayList<Events>();
		
		setContentView(R.layout.activity_location);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(this,this, this);
			locationclient.connect();
		}
		else{
			Log.e("ERROR", "Error with Google play services " + resp);
		}				

		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	    mLocationRequest.setInterval(UPDATE_INTERVAL);
	    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);	
	}
	
	@Override
	public void onMarkerDrag(Marker marker) {
    }

	@Override
	public void onMarkerDragEnd(Marker marker) {
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.expand_in, R.anim.shrink_out);
	}


	@Override
	public void onLocationChanged(Location location) {
		Log.d("DEBUG", "onLocationChanged called");
		llProgressLoc = (LinearLayout) findViewById(R.id.llProgressLoc);
		llProgressLoc.setVisibility(View.INVISIBLE);
		
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();
		myPos      = new LatLng(currentLat,currentLng);
		if (map == null) {
			Log.e("DEBUG", "Map is null");
			SupportMapFragment supportMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			map = supportMap.getMap();
		}

		if (map != null) {
			if(firstMapUpdate == true) {
				Intent i = getIntent();
				LocationData zoomPos = (LocationData) i.getSerializableExtra(AdHocUtils.intentLoc);
				
				if(zoomPos == null) {
					Log.d("DEBUG", "Did not get zoom position, zooming to user");
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 14.0f));
				} else {
					Log.d("DEBUG", "Did get zoom position, zooming to event");
					LatLng newPos = new LatLng(zoomPos.getLattitude(), zoomPos.getLongitude());
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 14.0f));
				}
				
				map.addCircle(new CircleOptions()
							  .center(new LatLng(location.getLatitude(), location.getLongitude()))
							  .radius(INTEREST_RADIUS_METERS)
							  .strokeColor(Color.RED));
				setMarkerCurrentUserLocation();
				addListOfEvents();
				firstMapUpdate = false;
			}
			else {
				Log.d("DEBUG", "Need to get the events again");
				//addListOfEvents();
				updatingListEvents();
			}
		}
	}
	private void setAddressOnMarker(Marker marker, double latitude, double longitude) {
		String [] address = null;
		address = getAddressFromGeoCode(latitude, longitude);
		if (address != null) {
			marker.setTitle(address[0]);
		}
		
		marker.showInfoWindow();
	}
	// Method to translate geo coded location to human readable address
	private  String[] getAddressFromGeoCode(double latitude, double longitude) {
		Geocoder geocoder;
		List<Address> addresses;
		String [] textAddresses = null;
		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			Log.d("DEBUG", "With geopoints (lat["+latitude+"],long["+longitude+
					"]) found this many locations: " + addresses.size());
			if(addresses.size() > 0) {
				textAddresses = new String[3];
				textAddresses[0] = addresses.get(0).getAddressLine(0);
				textAddresses[1]= addresses.get(0).getAddressLine(1);
				textAddresses[2]= addresses.get(0).getAddressLine(2);
			} else {
				textAddresses = new String[3];
				textAddresses[0]="";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return textAddresses;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}
	@Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFE CYCLE", "Pausing map updates");
        locationclient.removeLocationUpdates(this);
    }
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("LIFE CYCLE", "Restarting view");
		locationclient.requestLocationUpdates(mLocationRequest,
				(com.google.android.gms.location.LocationListener) this);
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.e("LOCATION", "Connected");
        locationclient.requestLocationUpdates(mLocationRequest,
				(com.google.android.gms.location.LocationListener) this);
	}

	@Override
	public void onDisconnected() {
		Log.d("DEBUG", "Map disconnected");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_create_event:
			Intent iCreate = new Intent(this, CreateEventActivity.class);
			startActivity(iCreate);
			break;
		case R.id.action_list_events:
			Intent iList = new Intent(this, EventListActivity.class);
			iList.putExtra("BACK", true);
			startActivity(iList);
			break;
		case R.id.action_logout:
			ParseUser.logOut();
			ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
			Intent intLogOut = new Intent(this, MainActivity.class);
			startActivity(intLogOut);
			break;
		default:
			break;
		}

		return true;
	}
	
	public void setMarkerCurrentUserLocation() {
		Location mCurrentLocation;
		LatLng userLoc;
		
        mCurrentLocation = locationclient.getLastLocation();
        userLoc = new LatLng(mCurrentLocation.getLatitude(),
        		mCurrentLocation.getLongitude());
		
        Log.e("DEBUG", "Current user location is: lat["+
        myPos.latitude + "] long is [" + myPos.longitude+"]");
        
        Marker usermarker = map.addMarker(new MarkerOptions()
        					                 .position(userLoc)
        					                 .draggable(false)
        					                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)));
	}
	
	public void addListOfEvents() {
		Log.d("DEBUG", "Adding list of events");
		map.setOnMarkerDragListener(this);
		if (( eventsMarkers != null ) && (eventsMarkers.size() != 0 )) {
			// Clear all the existing markers if any
			eventsMarkers.clear();
		}
			
		eventsMarkers = new ArrayList<Marker>();
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker mark) {
				// need to go through list and find event
				//  due to bug with adding events dynamically
				int listPos = 0;
				Log.d("ERROR", "Marker " + mark.getSnippet() + " clicked");
				for(int a = 0; a < allEvents.size(); a++) {
					if(mark.getSnippet().equals(allEvents.get(a).getObjectId())) {
						listPos = a;
					}
				}
				
				Log.d("DEBUG", "Maps to event: " + allEvents.get(listPos).getObjectId());
				gotoDetails(allEvents.get(listPos).getObjectId());
			}
		});
		
		ParseClient.getParseAllEvents(myPos, new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException arg1) {
				allEvents=listEvents;
				int markerCount = 0;
				for(int a = 0; a < listEvents.size(); a++) {
					double evtLat = 0;
					double evtLng = 0;
					ParseGeoPoint point = listEvents.get(a).getLoc();
					
					Log.d("DEBUG", "CHECKING creating marker ["+a+"] at loc: lat["+
							point.getLatitude()+"] and long[" +
							point.getLongitude()+"] for "+listEvents.get(a).getObjectId());
					
					// Get the event location
					evtLat = point.getLatitude();
					evtLng = point.getLongitude();
					LatLng 	evtPos =new LatLng(evtLat,evtLng);

					float[] dist  = new float[1];
					Location.distanceBetween(myPos.latitude, myPos.longitude, evtLat, evtLng, dist);
					if ((dist[0] * METER_TO_MILE_FACTOR) <= INTEREST_RADIUS_MILES) {
						Log.d("DEBUG", "Creating marker ["+a+"] at loc: lat["+
								point.getLatitude()+"] and long[" +
								point.getLongitude()+"] for "+listEvents.get(a).getObjectId());
						eventsMarkers.add(map.addMarker(new MarkerOptions()
											 .position(evtPos)
											 .anchor(0.5f, 1)
											 .snippet(String.valueOf(listEvents.get(a).getObjectId()))
											 .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))));
						eventsMarkers.get(markerCount).setDraggable(false);
					} else {
						Log.d("DEBUG", "Removed event " + listEvents.get(a).getObjectId());
						listEvents.remove(a);
						a--;
					}
				}

				map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater(), allEvents));
			}
		});
	}
	
	public void updatingListEvents() {
		Log.d("DEBUG", "Updating list of events");
		
		ParseClient.getParseAllEvents(myPos, new FindCallback<Events>() {
			@Override
			public void done(List<Events> listEvents, ParseException arg1) {
				int markerCount = 0;
				
				List<Events> parseEvents = listEvents;
				List<Events> mapEvents = new ArrayList<Events>();
				for(int a = 0; a < allEvents.size(); a++) {
					mapEvents.add(allEvents.get(a));
				}
				
				Log.d("DEBUG", "Size of map events is " + mapEvents.size());
				Log.d("DEBUG", "Size of parse events is " + parseEvents.size());
				
				// first go through and determine new/old events
				for(int a = 0; a < mapEvents.size(); a++) {
					for(int b = 0; b < parseEvents.size(); b++) {
						if(mapEvents.size() != 0 &&
								mapEvents.get(a).equals(parseEvents.get(b))) {
							Log.d("DEBUG", "Contains both " +
									mapEvents.get(a).getObjectId() + ","
									+ parseEvents.get(b).getObjectId());
							mapEvents.remove(a);
							parseEvents.remove(b--);
						}
					}
				}
				
				// at this point mapEvents has deleted events
				//  and parseEvents has new events
				Log.d("DEBUG", "Size of map events became " + mapEvents.size());
				Log.d("DEBUG", "Size of parse events became " + parseEvents.size());
				
				// remove deleted events by going through map events
				for(int a = 0; a < mapEvents.size(); a++) {
					for(int b = 0; b < allEvents.size(); b++) {
						//Log.v("DELETE", "Checking events to delete " + mapEvents.get(a).getObjectId() + "," + allEvents.get(b).getObjectId());
						if(mapEvents.get(a).equals(allEvents.get(b))) {
							Log.d("DEBUG", "Removing event [" +allEvents.get(b).getObjectId()+"]");
							allEvents.remove(b);
							
							eventsMarkers.get(b).remove();
							eventsMarkers.remove(b--);
						}
					}
				}
				
				// add new events from parseEvents
				for(int a = 0; a < parseEvents.size(); a++) {
					Log.d("DEBUG", "Checking to add event " + parseEvents.get(a).getObjectId());
					
					ParseGeoPoint point = listEvents.get(a).getLoc();
					double evtLat = point.getLatitude();
					double evtLng = point.getLongitude();
					LatLng 	evtPos =new LatLng(evtLat,evtLng);
					float[] dist  = new float[1];
					Location.distanceBetween(myPos.latitude, myPos.longitude, evtLat, evtLng, dist);
					
					if ((dist[0] * METER_TO_MILE_FACTOR) <= INTEREST_RADIUS_MILES) {
						Log.d("DEBUG", "Adding event " + parseEvents.get(a).getObjectId());
						allEvents.add(parseEvents.get(a));
					
						eventsMarkers.add(map.addMarker(new MarkerOptions()
						 	.position(evtPos)
						 	.anchor(0.5f, 1)
						 	.snippet(String.valueOf(parseEvents.get(a).getObjectId()))
						 	.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))));
						eventsMarkers.get(markerCount).setDraggable(false);
					}
				}

				map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater(), allEvents));
			}
		});
	}
	
	private void gotoDetails(String objId) {
		Intent itemDetails = new Intent(this, EventDetailsActivity.class);
		itemDetails.putExtra(AdHocUtils.intentDetailsId, objId);
		startActivity(itemDetails);
		overridePendingTransition(R.anim.expand_in, R.anim.shrink_out);
	}
}
