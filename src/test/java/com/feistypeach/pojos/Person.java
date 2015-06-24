package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListType;
import com.feistypeach.plista4j.PListValue;

import java.util.Date;
import java.util.List;

@PListObject(PListType.DictType)
public class Person {

    @PListValue(key = "fname")
    private String firstName;

    @PListValue(key = "lname")
    private String lastname;

    @PListValue
    private Date dob;

    @PListValue(key = "has.iphone")
    private boolean hasIPhone = false;

    @PListValue
    private Float weight;

    @PListValue
    private byte[] avatar;

    @PListValue
    private Address address;

    @PListValue
    private Location location;

    @PListValue(type = PListType.ArrayType)
    private List<Person> friends;

    @PListValue
    private List<String> tags;

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

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
