package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class WindSpeed implements Parcelable {
    private String type;
    private double value;
    private String name;
    private long timestamp;

    protected WindSpeed(Parcel in) {
        type = in.readString();
        value = in.readDouble();
        name = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<WindSpeed> CREATOR = new Creator<WindSpeed>() {
        @Override
        public WindSpeed createFromParcel(Parcel in) {
            return new WindSpeed(in);
        }

        @Override
        public WindSpeed[] newArray(int size) {
            return new WindSpeed[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeDouble(value);
        parcel.writeString(name);
        parcel.writeLong(timestamp);
    }
}
