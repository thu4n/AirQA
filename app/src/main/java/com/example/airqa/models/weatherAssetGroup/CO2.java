package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CO2 extends BaseInfo implements Parcelable {
    private double value;

    protected CO2(Parcel in) {
        super(in);
        value = in.readDouble();
    }

    public static final Creator<CO2> CREATOR = new Creator<CO2>() {
        @Override
        public CO2 createFromParcel(Parcel in) {
            return new CO2(in);
        }

        @Override
        public CO2[] newArray(int size) {
            return new CO2[size];
        }
    };

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
