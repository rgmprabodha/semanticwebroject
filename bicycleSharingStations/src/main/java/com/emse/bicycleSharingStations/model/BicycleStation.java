package com.emse.bicycleSharingStations.model;

public class BicycleStation {

    private String ID;
    private String name;
    private String lat;
    private String lon;
    private String capacity;
    private String availableBikes;
    private String localUpdateDateTime;

    public BicycleStation(){

    }

    public BicycleStation(String ID, String name, String lat, String lon, String capacity, String availableBikes, String localUpdateDateTime) {
        this.ID = ID;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.capacity = capacity;
        this.availableBikes = availableBikes;
        this.localUpdateDateTime = localUpdateDateTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }


    public String getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(String availableBikes) {
        this.availableBikes = availableBikes;
    }

    public String getLocalUpdateDateTime() {
        return localUpdateDateTime;
    }

    public void setLocalUpdateDateTime(String localUpdateDateTime) {
        this.localUpdateDateTime = localUpdateDateTime;
    }

}
