package com.codepath.adhoc.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.models.LocationData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

public class LocationCreationActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks,
																   GooglePlayServicesClient.OnConnectionFailedListener,
																   LocationListener, 
																   OnMapClickListener,
																   OnMarkerClickListener{
	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    private Marker 				myPosMarker;
    private LatLng 				myPos =new LatLng(currentLat,currentLng);
    LocationRequest 			mLocationRequest;
    LocationData				prevLoc;
    private boolean             mapUpdateRcvd = false;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 5;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdHocUtils.forceShowActionBar(this);
		
		setContentView(R.layout.activity_location_create);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(this,this, this);
			locationclient.connect();
			//Toast.makeText(this, "Google Play Service OK" , Toast.LENGTH_LONG).show();
		}
		else{
			//Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
			Log.e("ERROR", "Error with Google play services " + resp);
		}				
		 mLocationRequest = LocationRequest.create();
	     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     //mLocationRequest.setInterval(UPDATE_INTERVAL);
	     //mLocationRequest.setFastestInterval(FASTEST_INTERVAL);	
	     
	     Intent intent = getIntent();
	     prevLoc = (LocationData) intent.getSerializableExtra("PrevLoc");
	}

    //public void sendDetailsBack(LatLng pos) {
	public void onClickSave(View v) {
		String [] address = null;
		address = getAddressFromGeoCode(myPos.latitude, myPos.longitude);
		LocationData lcn = new LocationData(myPos.latitude, myPos.longitude);
		lcn.setAddress(address);
		Intent data = new Intent();
		data.putExtra("Location", lcn);
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();				
	}
	
	public void onClickCancel(View v) {
		Intent data = new Intent();
		//data.putExtra("Location", null);
		setResult(RESULT_CANCELED, data);
		finish();
	}

	@Override
	public void onLocationChanged(Location location) {
		if(prevLoc != null) {
	    	 Log.d("DEBUG", "Previous locaiton not null, using that");
	    	 currentLat = prevLoc.getLattitude();
	    	 currentLng = prevLoc.getLongitude();
	     } else {
	    	 currentLat = location.getLatitude();
	    	 currentLng = location.getLongitude();
	     }
		
		myPos      = new LatLng(currentLat,currentLng);
		mapUpdateRcvd = true;
	     
		
		if (map == null) {
			Log.d("DEBUG", "Map creation is null");

			SupportMapFragment supportMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			map = supportMap.getMap();

			if (map != null) {
					setMarkerCurrentUserLocation();
			    	map.setOnMapClickListener(this);
			    	map.setOnMarkerClickListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions()
			    									.position(myPos)
			    									.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
			    	//myPosMarker.setDraggable(true);
			    	setAddressOnMarker(myPosMarker,currentLat, currentLng);
			    	myPosMarker.showInfoWindow();
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			}
		}
		else {
			Log.d("DEBUG", "Map creation is not null");
			myPosMarker.setPosition(myPos);
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
			textAddresses = new String[3];
			textAddresses[0] = addresses.get(0).getAddressLine(0);
			textAddresses[1]= addresses.get(0).getAddressLine(1);
			textAddresses[2]= addresses.get(0).getAddressLine(2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
        /*if (this.mapUpdateRcvd == true) {
        	sendDetailsBack(myPos);
        }*/
    }
	@Override
	public void onConnected(Bundle connectionHint) {
        locationclient.requestLocationUpdates(mLocationRequest, 
				(com.google.android.gms.location.LocationListener) this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
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
			startActivity(iList);
			break;
		case R.id.action_logout:
			ParseUser.logOut();
			ParseUser.getCurrentUser();
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
		mCurrentLocation = locationclient.getLastLocation();
        Log.d("DEBUG", "Current user location is: lat["+
        mCurrentLocation.getLatitude() + "] long is [" + mCurrentLocation.getLongitude()+"]");
	}

	@Override
	public void onMapClick(LatLng latLng) {
		// TODO Auto-generated method stub
		if (myPosMarker != null) {
			myPosMarker.remove();
		}
		myPosMarker = map.addMarker(new MarkerOptions()
		.position(latLng)
		.draggable(false)
		.visible(true)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
		);
		setAddressOnMarker(myPosMarker, latLng.latitude, latLng.longitude);
		//this.myPos = latLng;
		//sendDetailsBack(latLng);
		
		Log.d("DEBUG", "Map clicked at lat["+latLng.latitude+
				"], long["+latLng.longitude+"]");
		myPos = latLng;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Log.d("DEBUG", "Marker clicked at lat["+marker.getPosition().latitude+
				"], long["+marker.getPosition().longitude+"]");
		myPos = marker.getPosition();
		//sendDetailsBack(myPos);
		return true;
	}
}
