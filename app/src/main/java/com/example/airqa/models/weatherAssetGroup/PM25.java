package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// Same types of value as PM10
public class PM25 extends PM10 implements Parcelable {

    protected PM25(Parcel in) {
        super(in);
    }

    public static final Creator<PM25> CREATOR = new Creator<PM25>() {
        @Override
        public PM25 createFromParcel(Parcel in) {
            return new PM25(in);
        }

        @Override
        public PM25[] newArray(int size) {
            return new PM25[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }
}
