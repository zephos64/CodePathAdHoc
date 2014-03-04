package com.codepath.adhoc.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.MapUtils;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
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
																   OnMarkerClickListener,
																   OnMarkerDragListener{
	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    LocationData				prevLoc;
    
    private Marker				evtMarker;
    private Marker 				myPosMarker;
    private LatLng 				myPos;
    private LatLng 				evtPos =new LatLng(currentLat,currentLng);
    
    LocationRequest 			mLocationRequest;
    private boolean             mapUpdateRcvd = false;
    private boolean             chosenByDrag  = false;
    public static final int 	MINUTE_IN_SECONDS = 60;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 60 *MINUTE_IN_SECONDS;
    public static final int 	UPDATE_INTERVAL_FASTEST_IN_SECONDS =  MINUTE_IN_SECONDS;
    LinearLayout llProgressLocCreate;
    
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
//			Toast.makeText(this, "Google Play Service OK" , Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
//			Log.e("ERROR", "Error with Google play services " + resp);
		}				
		 mLocationRequest = LocationRequest.create();
	     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     mLocationRequest.setInterval(UPDATE_INTERVAL_IN_SECONDS);
	     mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_SECONDS);	
	     
	     Intent intent = getIntent();
	     prevLoc = (LocationData) intent.getSerializableExtra("PrevLoc");
	}

    //public void sendDetailsBack(LatLng pos) {
	public void onClickSave(View v) {
		String [] address = null;
		Log.e("Chosen by Drag", String.valueOf(chosenByDrag));
		
		if(evtMarker == null) {
			Log.d("DEBUG", "No location set, so defaulting to cancel");
			onClickCancel(v);
			return;
		}
		
		/*if (chosenByDrag == true) {
			Log.e("Address ", " LAT  :" + String.valueOf(evtPos.latitude) + 
							  " LONG :" + String.valueOf(evtPos.longitude));
			address = MapUtils.getAddressFromGeoCode(this, evtPos.latitude, evtPos.longitude);
		}*/
		Log.d("DEBUG", "Address set to["+evtPos.latitude+","+evtPos.longitude+"]");
		address = MapUtils.getAddressFromGeoCode(this, evtPos.latitude, evtPos.longitude);
		//address = MapUtils.getAddressFromGeoCode(this, myPos.latitude, myPos.longitude);
		//LocationData lcn = new LocationData(myPos.latitude, myPos.longitude);
		LocationData lcn = new LocationData(evtPos.latitude, evtPos.longitude);
		lcn.setAddress(address);
		Intent data = new Intent();
		data.putExtra("Location", lcn);
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();
		overridePendingTransition(R.anim.up_in,	R.anim.down_out);
	}
	
	public void onClickCancel(View v) {
		Intent data = new Intent();
		//data.putExtra("Location", null);
		setResult(RESULT_CANCELED, data);
		finish();
		overridePendingTransition(R.anim.up_in, R.anim.down_out);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.up_in,	R.anim.down_out);
	}

	@Override
	public void onLocationChanged(Location location) {
		llProgressLocCreate = (LinearLayout) findViewById(R.id.llProgressLocCreate);
		llProgressLocCreate.setVisibility(View.INVISIBLE);
		if(prevLoc != null) {
	    	 Log.d("DEBUG", "Previous locaiton not null, using that");
	    	 currentLat = prevLoc.getLattitude();
	    	 currentLng = prevLoc.getLongitude();
	     } else {
	    	 Log.d("DEBUG", "Previous locaiton null");
	    	 currentLat = location.getLatitude();
	    	 currentLng = location.getLongitude();
	     }
		
		//myPos      = new LatLng(currentLat,currentLng);
		setMarkerCurrentUserLocation();
		if (map == null) {
			Log.d("DEBUG", "Map creation is null");

			SupportMapFragment supportMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			map = supportMap.getMap();

			if (map != null) {
					//user marker stuff
			    	map.setOnMapClickListener(this);
			    	map.setOnMarkerClickListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions()
			    									.position(myPos)
			    									.draggable(false)
			    									.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)));
			    	//myPosMarker.setDraggable(true);
//			    	MapUtils.setAddressOnMarker(this, myPosMarker,currentLat, currentLng);
//			    	myPosMarker.showInfoWindow();
			        map.setOnMarkerDragListener(this);
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			    	
			    	if(prevLoc!=null) {
			    		onMapClick(new LatLng(prevLoc.lattitude, prevLoc.longitude));
			    	}
			}
		}
		else {
			Log.d("DEBUG", "Map creation is not null");
			myPosMarker.setPosition(myPos);
		}
		mapUpdateRcvd = true;

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}
	@Override
    protected void onPause() {
        super.onPause();
        locationclient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);	
    }
	
	@Override
	public void onConnected(Bundle connectionHint) {
        locationclient.requestLocationUpdates(mLocationRequest, 
				(com.google.android.gms.location.LocationListener) this);
	}

	@Override
	public void onDisconnected() {
        locationclient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event_map, menu);
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
        myPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
	}

	@Override
	public void onMapClick(LatLng latLng) {
		// TODO Auto-generated method stub
		if (evtMarker != null) {
			evtMarker.remove();
		}
		evtMarker = map.addMarker(new MarkerOptions()
						.position(latLng)
						.draggable(true)
						.visible(true)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
		);
		MapUtils.setAddressOnMarker(this, evtMarker, latLng.latitude, latLng.longitude);
		Log.d("DEBUG", "Map clicked at lat["+latLng.latitude+
				"], long["+latLng.longitude+"]");
		evtPos = latLng;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Log.d("DEBUG", "Marker clicked at lat["+marker.getPosition().latitude+
				"], long["+marker.getPosition().longitude+"]");
		myPos = marker.getPosition();
		return true;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
    	//myPosMarker.setTitle("Drop Me");
    	evtMarker.showInfoWindow();
		
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		evtPos = marker.getPosition();

		String [] address = null;
		address = MapUtils.getAddressFromGeoCode(this, evtPos.latitude, evtPos.longitude);
		chosenByDrag = true;
		evtMarker.setTitle(address[0]);
    	evtMarker.showInfoWindow();
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		chosenByDrag = false;
		//myPosMarker.setTitle("Drop Me");
    	//evtMarker.showInfoWindow();
	}
}
