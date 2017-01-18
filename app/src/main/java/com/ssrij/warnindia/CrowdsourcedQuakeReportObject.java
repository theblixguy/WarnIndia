package com.ssrij.warnindia;


public class CrowdsourcedQuakeReportObject {

    private String DeviceID;
    private String Latitude;
    private String Longitude;
    private String Timestamp;

    public CrowdsourcedQuakeReportObject() {}

    public CrowdsourcedQuakeReportObject(String deviceID, String latitude, String longitude, String timestamp){
        this.DeviceID = deviceID;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Timestamp = timestamp;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
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

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
