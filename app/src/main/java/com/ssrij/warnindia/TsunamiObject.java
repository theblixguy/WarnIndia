package com.ssrij.warnindia;

import android.os.Parcel;
import android.os.Parcelable;

public class TsunamiObject implements Parcelable {

    public String Magnitude;
    public String Latitude;
    public String Longitude;
    public String Depth;
    public String RegionName;
    public String OriginTime;
    public String AutomaticManual;
    public String BulletinLink;

    public TsunamiObject() {
    }

    public TsunamiObject(String originTime, String magnitude, String latitude,
                         String longitude, String depth, String automaticManual, String regionName,
                         String bulletinLink) {
        this.OriginTime = originTime;
        this.Magnitude = magnitude;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Depth = depth;
        this.RegionName = regionName;
        this.AutomaticManual = automaticManual;
        this.BulletinLink = bulletinLink;
    }

    public String getAutomaticManual() {
        return AutomaticManual;
    }

    public void setAutomaticManual(String automaticManual) {
        AutomaticManual = automaticManual;
    }

    public String getBulletinLink() {
        return BulletinLink;
    }

    public void setBulletinLink(String bulletinLink) {
        BulletinLink = bulletinLink;
    }

    public String getDepth() {
        return Depth;
    }

    public void setDepth(String depth) {
        Depth = depth;
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

    public String getMagnitude() {
        return Magnitude;
    }

    public void setMagnitude(String magnitude) {
        Magnitude = magnitude;
    }

    public String getOriginTime() {
        return OriginTime;
    }

    public void setOriginTime(String originTime) {
        OriginTime = originTime;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    protected TsunamiObject(Parcel in) {
        Magnitude = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Depth = in.readString();
        RegionName = in.readString();
        OriginTime = in.readString();
        AutomaticManual = in.readString();
        BulletinLink = in.readString();
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
        dest.writeString(AutomaticManual);
        dest.writeString(BulletinLink);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TsunamiObject> CREATOR = new Parcelable.Creator<TsunamiObject>() {
        @Override
        public TsunamiObject createFromParcel(Parcel in) {
            return new TsunamiObject(in);
        }

        @Override
        public TsunamiObject[] newArray(int size) {
            return new TsunamiObject[size];
        }
    };
}