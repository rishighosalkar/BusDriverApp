package com.example.busdriverapp.Classes;

public class LocationHelper {

    private double latitude;
    private double longitude;
    private String bus_no;

    public LocationHelper(double Latitude,double Longitude,String Bus_no){
        latitude=Latitude;
        longitude=Longitude;
        bus_no=Bus_no;
    }

    public double getlongitude(){
        return longitude;
    }
    public void setlongitude(double Longitude){
        longitude=Longitude;
    }

    public String getbus_no(){
        return bus_no;
    }
    public void setBus_no(String Bus_no){
        bus_no=Bus_no;
    }


    public double getlatitude(){
        return latitude;
    }
    public void setlatitude(double Latitude){
        latitude=Latitude;
    }
}
