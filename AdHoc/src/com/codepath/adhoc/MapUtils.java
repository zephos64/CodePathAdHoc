package com.codepath.adhoc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.model.Marker;

public class MapUtils {

	public static void setAddressOnMarker(ActionBarActivity activity, Marker marker, double latitude, double longitude) {
		String [] address = null;
		address = getAddressFromGeoCode(activity, latitude, longitude);
		if (address != null) {
			marker.setTitle(address[0]);
		}
		marker.showInfoWindow();
	}
	// Method to translate geo coded location to human readable address
	public static   String[] getAddressFromGeoCode(ActionBarActivity activity, double latitude, double longitude) {
		Geocoder geocoder;
		List<Address> addresses;
		String [] textAddresses = null;
		geocoder = new Geocoder(activity, Locale.getDefault());
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
}
