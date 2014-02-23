package com.codepath.adhoc.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
	private int mapMode    = MapMode.EVENT_DISPLAY;
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
			//Toast.makeText(getActivity(), "Google Play Service OK" , Toast.LENGTH_LONG).show();
		}
		else{
			//Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
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
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();

		myPos      = new LatLng(currentLat,currentLng);
		if (map == null) {
			map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (map != null) 
			{
			    	map.setOnMarkerDragListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions()
			    									.position(myPos)
			    									.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
			    	myPosMarker.setDraggable(true);
			    	setAddressOnMarker(myPosMarker,currentLat, currentLng);
			    	myPosMarker.showInfoWindow();
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			}
		}
		else{
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
		geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
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

	public void setLocaion(double lattitude, double longitude) {
		this.mapMode =  MapMode.EVENT_DISPLAY; 
		currentLat = lattitude;
		currentLng = longitude;
	
	}
}
