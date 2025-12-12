/* So this class is bacially this is our "Super class" it basically works in a way by saying I need alla these items in the code for the code to work Resident wise.  */

package com.greenvile.model;

public class Person {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;

    public Person() {
        this.id = 0;
        this.fullName = "";
        this.phoneNumber = "";
        this.email = "";
        this.address = "";
    }

    public Person(int id, String fullName, String phoneNumber, String email, String address) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return fullName;
    }
}
