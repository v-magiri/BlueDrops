package com.magiri.bluedropvendor.model;

public class Vendor {
    String Name,Email,PhoneNumber,Location,Account_Number;

    public Vendor(String name, String email, String phoneNumber, String location, String account_Number) {
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
        Location = location;
        Account_Number = account_Number;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAccount_Number() {
        return Account_Number;
    }

    public void setAccount_Number(String account_Number) {
        Account_Number = account_Number;
    }
}

