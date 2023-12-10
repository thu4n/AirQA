package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Humidity extends BaseInfo implements Parcelable {
    private int value;

    protected Humidity(Parcel in) {
        super(in);
        value = in.readInt();
    }

    public static final Creator<Humidity> CREATOR = new Creator<Humidity>() {
        @Override
        public Humidity createFromParcel(Parcel in) {
            return new Humidity(in);
        }

        @Override
        public Humidity[] newArray(int size) {
            return new Humidity[size];
        }
    };

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(value);
    }
}
