package com.ssrij.warnindia;

import android.os.Parcel;
import android.os.Parcelable;

public class CycloneObject implements Parcelable {

    public String CycloneName;
    public String WindSpeed;
    public String BulletinLink;

    public CycloneObject() {
    }

    public CycloneObject(String cycloneName, String windSpeed, String bulletinLink) {
        this.CycloneName = cycloneName;
        this.WindSpeed = windSpeed;
        this.BulletinLink = bulletinLink;
    }

    public String getCycloneName() {
        return CycloneName;
    }

    public void setCycloneName(String cycloneName) {
        CycloneName = cycloneName;
    }

    public String getBulletinLink() {
        return BulletinLink;
    }

    public void setBulletinLink(String bulletinLink) {
        BulletinLink = bulletinLink;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    protected CycloneObject(Parcel in) {
        CycloneName = in.readString();
        WindSpeed = in.readString();
        BulletinLink = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CycloneName);
        dest.writeString(WindSpeed);
        dest.writeString(BulletinLink);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CycloneObject> CREATOR = new Parcelable.Creator<CycloneObject>() {
        @Override
        public CycloneObject createFromParcel(Parcel in) {
            return new CycloneObject(in);
        }

        @Override
        public CycloneObject[] newArray(int size) {
            return new CycloneObject[size];
        }
    };
}