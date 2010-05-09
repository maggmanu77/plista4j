package com.feistypeach.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListType;
import com.feistypeach.plista4j.PListValue;

@PListObject(PListType.DictType)
public class Person {
	
	@PListValue(key="fname")
	private String firstName;
	
	@PListValue(key="lname")
	private String lastname;
	
	@PListValue
	private Date dob;
	
	@PListValue(key="has.iphone")
	private boolean hasIPhone = false;
	
	@PListValue
	private float weight;
	
	@PListValue
	private byte[] avatar;
	
	@PListValue
	private Address address;
	
	@PListValue
	private Location location;
	
	@PListValue(type=PListType.ArrayType)
	private ArrayList<Person> friends = new ArrayList<Person>();
	
	@PListValue
	private ArrayList<String> tags = new ArrayList<String>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastname;
	}

	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<Person> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<Person> friends) {
		this.friends = friends;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public boolean hasIPhone() {
		return hasIPhone;
	}

	public void setHasIPhone(boolean hasIPhone) {
		this.hasIPhone = hasIPhone;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	
	
	
}
