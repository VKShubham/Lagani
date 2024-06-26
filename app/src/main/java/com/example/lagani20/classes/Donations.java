package com.example.lagani20.classes;

import java.io.Serializable;

public class Donations implements Serializable {
    private String donationtype;
    private String donationweight;
    private String suggestedvehicle;
    private String pincode;
    private String address;
    private String mobileno;
    private String userid;
    private String userid2;
    private  String resturant;
    private String status;

    private String donationid;
    private String date;
    private String donationnumber;
    private double longitude;
    private double latitude;
    private double longitude2;
    private double latitude2;
    public Donations() {}

    public Donations(String donationnumber,String donationtype, String donationweight, String suggestedvehicle, String pincode, String address, String mobileno, String userid, String resturant, String status , String donationid, String userid2, String date, Double longitude, Double latitude) {
        this.donationtype = donationtype;
        this.donationweight = donationweight;
        this.suggestedvehicle = suggestedvehicle;
        this.pincode = pincode;
        this.address = address;
        this.mobileno = mobileno;
        this.userid = userid;
        this.resturant = resturant;
        this.status = status;
        this.donationnumber = donationnumber;
        this.donationid = donationid;
        this.userid2 = userid2;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
    }

    public String getDonationtype() {
        return donationtype;
    }

    public void setDonationtype(String donationtype) {
        this.donationtype = donationtype;
    }

    public String getDonationweight() {
        return donationweight;
    }

    public void setDonationweight(String donationweight) {
        this.donationweight = donationweight;
    }

    public String getSuggestedvehicle() {
        return suggestedvehicle;
    }

    public void setSuggestedvehicle(String suggestedvehicle) {
        this.suggestedvehicle = suggestedvehicle;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getResturant() {
        return resturant;
    }

    public void setResturant(String resturant) {
        this.resturant = resturant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonationid() {
        return donationid;
    }

    public void setDonationid(String donationid) {
        this.donationid = donationid;
    }

    public String getDonationnumber() {
        return donationnumber;
    }

    public void setDonationnumber(String donationnumber) {
        this.donationnumber = donationnumber;
    }

    public String getUserid2() {
        return userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(double longitude2) {
        this.longitude2 = longitude2;
    }

    public double getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(double latitude2) {
        this.latitude2 = latitude2;
    }
}
