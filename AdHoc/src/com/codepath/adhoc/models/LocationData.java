package com.codepath.adhoc.models;

import java.io.Serializable;

public class LocationData implements Serializable{
	private static final long serialVersionUID = -3496101484242816891L;
	public String address[]   = null;
	public double lattitude;
	public double longitude;

	public String[] getAddress() {
		return address;
	}
	public void setAddress(String[] address) {
		this.address = address;
	}

	public double getLattitude() {
		return lattitude;
	}
	public double getLongitude() {
		return longitude;
	}

	public LocationData (double lattitude, double longitude) {
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

}
