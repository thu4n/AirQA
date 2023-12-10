package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AQI extends BaseInfo implements Parcelable {
    private Integer value;

    protected AQI(Parcel in) {
        super(in);
        value = in.readInt();
    }

    public static final Creator<AQI> CREATOR = new Creator<AQI>() {
        @Override
        public AQI createFromParcel(Parcel in) {
            return new AQI(in);
        }

        @Override
        public AQI[] newArray(int size) {
            return new AQI[size];
        }
    };

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
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
