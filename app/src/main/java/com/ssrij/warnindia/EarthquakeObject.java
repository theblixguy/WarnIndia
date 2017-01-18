package com.ssrij.warnindia;

import android.os.Parcel;
import android.os.Parcelable;

public class EarthquakeObject implements Parcelable {

    public String Magnitude;
    public String Latitude;
    public String Longitude;
    public String Depth;
    public String RegionName;
    public String OriginTime;

    public EarthquakeObject() {
    }

    public EarthquakeObject(String originTime, String magnitude, String latitude,
                            String longitude, String depth, String regionName) {
        this.OriginTime = originTime;
        this.Magnitude = magnitude;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Depth = depth;
        this.RegionName = regionName;
    }

    public String getOriginTime() {
        return OriginTime;
    }

    public void setOriginTime(String originTime) {
        OriginTime = originTime;
    }

    public String getMagnitude() {
        return Magnitude;
    }

    public void setMagnitude(String magnitude) {
        Magnitude = magnitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getDepth() {
        return Depth;
    }

    public void setDepth(String depth) {
        Depth = depth;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }


    protected EarthquakeObject(Parcel in) {
        Magnitude = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Depth = in.readString();
        RegionName = in.readString();
        OriginTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Magnitude);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Depth);
        dest.writeString(RegionName);
        dest.writeString(OriginTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EarthquakeObject> CREATOR = new Parcelable.Creator<EarthquakeObject>() {
        @Override
        public EarthquakeObject createFromParcel(Parcel in) {
            return new EarthquakeObject(in);
        }

        @Override
        public EarthquakeObject[] newArray(int size) {
            return new EarthquakeObject[size];
        }
    };
}