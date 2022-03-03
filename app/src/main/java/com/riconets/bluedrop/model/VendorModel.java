package com.riconets.bluedrop.model;

public class VendorModel {
    String Name,Email,PhoneNumber,Address,Account_Number,Profile_Pic,Vendor_Desc,Latitude,Longitude;

    public VendorModel() {
    }

    public VendorModel(String name, String email, String phoneNumber, String address,
                       String account_Number, String profile_Pic, String vendor_Desc,
                       String latitude, String longitude) {
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
        Address = address;
        Account_Number = account_Number;
        Profile_Pic = profile_Pic;
        Vendor_Desc = vendor_Desc;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    public String getAccount_Number() {
        return Account_Number;
    }

    public void setAccount_Number(String account_Number) {
        Account_Number = account_Number;
    }

    public String getProfile_Pic() {
        return Profile_Pic;
    }

    public void setProfile_Pic(String profile_Pic) {
        Profile_Pic = profile_Pic;
    }

    public String getVendor_Desc() {
        return Vendor_Desc;
    }

    public void setVendor_Desc(String vendor_Desc) {
        Vendor_Desc = vendor_Desc;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}

