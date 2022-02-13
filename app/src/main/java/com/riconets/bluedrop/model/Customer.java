package com.riconets.bluedrop.model;

public class Customer {
    String FirstName,LastName,Email,Location,PhoneNumber,profilePic;

    public Customer(String firstName, String lastName, String email, String location, String phoneNumber,String profile_Pic) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Location = location;
        this.PhoneNumber = phoneNumber;
        this.profilePic= profile_Pic;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
