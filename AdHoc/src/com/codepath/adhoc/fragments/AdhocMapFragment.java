package com.codepath.adhoc.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.adhoc.R;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdhocMapFragment extends SupportMapFragment implements GooglePlayServicesClient.ConnectionCallbacks,
												  GooglePlayServicesClient.OnConnectionFailedListener,
												  LocationListener, 
												  OnMarkerDragListener{

	public class MapMode {
		   public static final int EVENT_DISPLAY   = 1;
		   public static final int EVENT_SELECTION = 2;
	};
	public int mapMode    = MapMode.EVENT_DISPLAY;
	private boolean             locationSet  = false;
	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    private Marker 				myPosMarker;
    private LatLng 				myPos =new LatLng(currentLat,currentLng);
    LocationRequest 			mLocationRequest;
    private static final int 	MILLISECONDS_PER_SECOND = 1000;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long   UPDATE_INTERVAL =MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int 	FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long 	FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(getActivity(),this, this);
			locationclient.connect();
		}
		else{
			Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}				
		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	    mLocationRequest.setInterval(UPDATE_INTERVAL);
	    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);	
		return;
	}


	@Override
	public void onMarkerDrag(Marker arg0) {
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		if (this.mapMode ==  MapMode.EVENT_SELECTION) {
			currentLat = location.getLatitude();
			currentLng = location.getLongitude();
		}
		else if (locationSet == false) {
			return;
		}
//		Log.e("Location changed" , " LAT: " +String.valueOf(currentLat) + " LAT: " +String.valueOf(currentLng) + 
//				"  Map " + String.valueOf(map));
//		currentLat = location.getLatitude();
//		currentLng = location.getLongitude();

		myPos      = new LatLng(currentLat,currentLng);
		if (map == null) {
			map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			Log.e("@@@@ Location changed" , " LAT: " +String.valueOf(currentLat) + " LAT: " +String.valueOf(currentLng) + 
					"  Map " + String.valueOf(map));
			if (map != null) 
			{
					setMarkerCurrentUserLocation();
			    	myPosMarker = map.addMarker(new MarkerOptions()
			    									.position(myPos)
			    									.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
					if (this.mapMode ==  MapMode.EVENT_SELECTION) {
						map.setOnMarkerDragListener(this);
				    	myPosMarker.setDraggable(true);
				    	Log.e ("Draggable Marker", "True");
					}
					else {
						myPosMarker.setDraggable(false);
						Log.e ("Draggable Marker", "False");
					}
			    	setAddressOnMarker(myPosMarker,currentLat, currentLng);
			    	myPosMarker.showInfoWindow();	
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			}
		}	
		else{
			myPosMarker.setPosition(myPos);
			myPosMarker.showInfoWindow();
		}
//		if (this.mapMode !=  MapMode.EVENT_SELECTION) {
//			locationclient.removeLocationUpdates(this);
//		}

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
		Log.e("ON Connected" , String.valueOf(this.mapMode));
        locationclient.requestLocationUpdates(mLocationRequest, 
				(com.google.android.gms.location.LocationListener) this);
	}

	
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void setLocaion(double lattitude, double longitude) {
		this.mapMode =  MapMode.EVENT_DISPLAY; 
		Log.e("MAP MODE SET", "Called with lat["+lattitude+"] and long["+longitude+"]");
		currentLat = lattitude;
		currentLng = longitude;
		locationSet  = true;
		//		locationclient.removeLocationUpdates(this);
	}
	
	public void setMarkerCurrentUserLocation() {
		Location mCurrentLocation;
		LatLng userLoc;
		
        mCurrentLocation = locationclient.getLastLocation();
        userLoc = new LatLng(mCurrentLocation.getLatitude(),
        		mCurrentLocation.getLongitude());
        
        Log.d("DEBUG", "Current user location is: lat["+
        mCurrentLocation.getLatitude() + "] long is [" + mCurrentLocation.getLongitude()+"]");
        
        Marker usermarker = map.addMarker(new MarkerOptions()
        .position(userLoc)
        .draggable(false));
	}
}
