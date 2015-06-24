package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListValue;

import java.util.Map;

@PListObject
public class Address {
	@PListValue()
	private String street1;
	@PListValue
	private String street2;
	@PListValue
	private String city;
	@PListValue
	private String state;

    @PListValue(key="customfields")
	private Map<String, String> custom;

    @PListValue(key="nearBy")
    private Map<String, Address> nearBy;
	
	private String zipcode;
	
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@PListValue
	public String getZipcode() {
		return zipcode;
	}
	
	@PListValue
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Map getCustomFields() {
		return custom;
	}

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

    public Map<String, Address> getNearBy() {
        return nearBy;
    }

    public void setNearBy(Map<String, Address> nearBy) {
        this.nearBy = nearBy;
    }
}
