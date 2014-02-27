package com.codepath.adhoc.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdhocMapFragment extends SupportMapFragment implements GooglePlayServicesClient.ConnectionCallbacks,
												  GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    private Marker 				eventMarker;
    private Marker				myPosMarker;
    private LatLng 				myPos;
    private LatLng				eventPos =new LatLng(currentLat,currentLng);
    LocationRequest 			mLocationRequest;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 5;

    @Override
    public void onCreate(Bundle savedInstancseState) {
    	super.onCreate(savedInstancseState);    	
    	
    }
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if(resp == ConnectionResult.SUCCESS){
			Log.d("DEBUG", "Succesfully connected client in map details");
			locationclient = new LocationClient(getActivity(),this, this);
			locationclient.connect();
		}
		else{
			Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}			
		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

//	@Override
	public void onLocationChanged(Location location) {
		eventPos = new LatLng(currentLat,currentLng);
		
		if (map == null) {
			Log.d("DEBUG", "Map details is null");
			map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		if (map != null) {
			// Disable map controls
			UiSettings mapUi = map.getUiSettings();
			mapUi.setAllGesturesEnabled(false);
			mapUi.setZoomControlsEnabled(false);
			
			setMarkerCurrentUserLocation();
				
			eventMarker = map.addMarker(new MarkerOptions().
											position(eventPos).
											icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
			setAddressOnMarker(eventMarker, currentLat, currentLng);
			eventMarker.showInfoWindow();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventPos, 15));
		} else {
			Log.d("DEBUG", "Map details is null");
			eventMarker.setPosition(eventPos);
			eventMarker.showInfoWindow();
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
		geocoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if ((addresses != null) && (addresses.size() > 0)){
				textAddresses = new String[3];
				textAddresses[0] = addresses.get(0).getAddressLine(0);
				textAddresses[1]= addresses.get(0).getAddressLine(1);
				textAddresses[2]= addresses.get(0).getAddressLine(2);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			Log.e("INVALID Location" , "Latitude : " + String.valueOf(latitude)
									  + " Longitude : " + String.valueOf(longitude)
									  + "Lat Range = -90 , 90  Lng Range = -180, 180");
			e.printStackTrace();
			
		}

		return textAddresses;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	}

	
	@Override
	public void onDisconnected() {
	}

	public void setLocaion(double lattitude, double longitude) {
		Log.e("MAP MODE SET", "Called with lat["+lattitude+"] and long["+longitude+"]");
		currentLat = lattitude;
		currentLng = longitude;
		Location eventLoc = new Location("asdf");
		eventLoc.setLatitude(currentLat);
		eventLoc.setLongitude(currentLng);
		onLocationChanged(eventLoc);
	}
	
	public void setMarkerCurrentUserLocation() {
		Location mCurrentLocation;
		LatLng userLoc;

		mCurrentLocation = locationclient.getLastLocation();
		if (mCurrentLocation == null) {
			Log.e("ERROR", "Current location is null");
			return;
		}

		userLoc = new LatLng(mCurrentLocation.getLatitude(),
				mCurrentLocation.getLongitude());

		Log.d("DEBUG",
				"Current user location is: lat["
						+ mCurrentLocation.getLatitude() + "] long is ["
						+ mCurrentLocation.getLongitude() + "]");

		Marker usermarker = map.addMarker(new MarkerOptions()
				.position(userLoc)
				.draggable(false)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)));
	}
}
