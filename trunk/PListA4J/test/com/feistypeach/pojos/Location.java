package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListType;

@PListObject(PListType.StringType)
public class Location {

	private double latitude;
	private double longitude;
	
	public Location(double latitude, double longitude) { 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public static Location valueOf(String sloc) {
		String[] latlong = sloc.split(",");
		Double latitude = Double.valueOf(latlong[0].trim());
		Double longitude = Double.valueOf(latlong[1].trim());
		return new Location(latitude, longitude);
	}
	
	public String toString() {
		return latitude + ", " + longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
