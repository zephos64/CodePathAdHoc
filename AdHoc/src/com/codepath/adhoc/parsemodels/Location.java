package com.codepath.adhoc.parsemodels;

import android.location.Geocoder;

import com.parse.ParseObject;

public class Location extends ParseObject {
	public Location() {
		
	}
	
	public String getLocationId() {
		return getString("location_id");
	}
	
	public String getEventId() {
		return getString("event_id");
	}
	
	public String getAddress() {
		return getString("address");
	}
	
	public String getCity() {
		return getString("city");
	}
	
	public String getState() {
		return getString("state");
	}
	
	public String getZip() {
		return getString("zip");
	}
	
	public String getCoordinates() {
		return getString("coordinates_based_on_address");
	}
	
	public void setLocationId(int locationId) {
		put("location_id", locationId);
	}
	
	public void setEventId(int eventId) {
		put("event_id", eventId);
	}
	
	public void setAddress(String address) {
		put("address", address);
	}
	
	public void setCity(String city) {
		put("city", city);
	}
	
	public void setState(String state) {
		put("state", state);
	}
	
	public void setZip(String zip) {
		put("zip", zip);
	}
	
	public void setCoordinates(Geocoder coordinates) {
		put("coordinates_based_on_address", coordinates);
	}
}
