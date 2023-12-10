package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Rainfall extends BaseInfo implements Parcelable {
    private double value;

    protected Rainfall(Parcel in) {
        super(in);
        value = in.readDouble();
    }

    public static final Creator<Rainfall> CREATOR = new Creator<Rainfall>() {
        @Override
        public Rainfall createFromParcel(Parcel in) {
            return new Rainfall(in);
        }

        @Override
        public Rainfall[] newArray(int size) {
            return new Rainfall[size];
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
