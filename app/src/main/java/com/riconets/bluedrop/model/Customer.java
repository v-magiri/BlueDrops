package com.riconets.bluedrop.model;

public class Customer {
    String Name,userName,Email,Location,PhoneNumber,profilePic,VendorID;

    public Customer(String name, String username, String email, String location, String phoneNumber,String profile_Pic,String vendorID) {
        this.Name = name;
        this.userName = username;
        this.Email = email;
        this.Location = location;
        this.PhoneNumber = phoneNumber;
        this.profilePic= profile_Pic;
        this.VendorID=vendorID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getVendorID() {
        return VendorID;
    }

    public void setVendorID(String vendorID) {
        VendorID = vendorID;
    }
}
