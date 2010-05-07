package com.feistypeach.pojos;

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
	private int age;
	
	@PListValue
	private float weight;
	
	@PListValue
	private byte[] avatar;
	
	@PListValue
	private Address address;
	
	@PListValue
	private Location location;
	
	@PListValue(type=PListType.ArrayType)
	private List<Person> friends;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}
	
	
	
}
