package com.codepath.adhoc.fragments;

import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateEventMapActivity extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
																GooglePlayServicesClient.OnConnectionFailedListener,
																LocationListener, 
																OnMarkerDragListener{
	private GoogleMap 			map;
	private SupportMapFragment  fragment;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inf.inflate(R.layout.fragment_create_event_map, parent, false);
		return view;
	}
	@Override

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		android.support.v4.app.FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
		if (fragment == null) {
		    fragment = SupportMapFragment.newInstance();
		    fm.beginTransaction().replace(R.id.mapView, fragment).commit();
		}
		map = fragment.getMap();
		if (map!= null){
			map.setOnMarkerDragListener(this);
		}

		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(getActivity(),this, this);
			locationclient.connect();
			Toast.makeText(getActivity(), "Google Play Service OK" , Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}				
		 mLocationRequest = LocationRequest.create();
	     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     mLocationRequest.setInterval(UPDATE_INTERVAL);
	     mLocationRequest.setFastestInterval(FASTEST_INTERVAL);	
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		LatLng pos = marker.getPosition();
		Log.e("DragPos:", String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.setTitle(String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.showInfoWindow();
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();
		Toast.makeText(getActivity(), "Received onLocation :" + String.valueOf(currentLat) + 
													   "  : " + String.valueOf(currentLng) , 
													   Toast.LENGTH_SHORT).show();

		myPos = new LatLng(currentLat,currentLng);
		if (map == null) {
			map = fragment.getMap();
			if (map != null) 
			{
			    	map.setOnMarkerDragListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions().position(myPos).title(String.valueOf(currentLat)+"\n" +String.valueOf(currentLng)));
			    	myPosMarker.setDraggable(true);
			    	myPosMarker.showInfoWindow();
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			}
		}
		else{
			myPosMarker.setPosition(myPos);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
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


}
