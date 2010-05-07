package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListType;

@PListObject(PListType.StringType)
public class Location {

	private float latitude;
	private float longitude;
	
	public Location(float latitude, float longitude) { 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public static Location valueOf(String sloc) {
		String[] latlong = sloc.split(",");
		float latitude = Float.valueOf(latlong[0].trim());
		float longitude = Float.valueOf(latlong[1].trim());
		return new Location(latitude, longitude);
	}
	
	public String toString() {
		return latitude + ", " + longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	
}
