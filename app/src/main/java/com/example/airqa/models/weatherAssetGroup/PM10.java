package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PM10 extends BaseInfo implements Parcelable {
    private double value;

    protected PM10(Parcel in) {
        super(in);
        value = in.readDouble();
    }

    public static final Creator<PM10> CREATOR = new Creator<PM10>() {
        @Override
        public PM10 createFromParcel(Parcel in) {
            return new PM10(in);
        }

        @Override
        public PM10[] newArray(int size) {
            return new PM10[size];
        }
    };

    public PM10() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeDouble(value);
    }
}
