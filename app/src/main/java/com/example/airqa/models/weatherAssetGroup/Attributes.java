package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Attributes implements Parcelable {
    private AQI AQI;
    private CO2 CO2;
    private PM10 PM10;
    private PM25 PM25;
    private Rainfall rainfall;
    private Temperature temperature;
    private Humidity humidity;
    private Location location;
    private WindSpeed windSpeed;

    protected Attributes(Parcel in) {
        AQI = in.readParcelable(AQI.class.getClassLoader());
        CO2 = in.readParcelable(CO2.class.getClassLoader());
        PM10 = in.readParcelable(PM10.class.getClassLoader());
        PM25 = in.readParcelable(PM25.class.getClassLoader());
        rainfall = in.readParcelable(Rainfall.class.getClassLoader());
        temperature = in.readParcelable(Temperature.class.getClassLoader());
        humidity = in.readParcelable(Humidity.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
        windSpeed = in.readParcelable(WindSpeed.class.getClassLoader());
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public AQI getAQI() {
        return AQI;
    }

    public void setAQI(AQI AQI) {
        this.AQI = AQI;
    }

    public CO2 getCO2() {
        return CO2;
    }

    public void setCO2(CO2 CO2) {
        this.CO2 = CO2;
    }

    public PM10 getPM10() {
        return PM10;
    }

    public void setPM10(PM10 PM10) {
        this.PM10 = PM10;
    }

    public PM25 getPM25() {
        return PM25;
    }

    public void setPM25(PM25 PM25) {
        this.PM25 = PM25;
    }

    public Rainfall getRainfall() {
        return rainfall;
    }

    public void setRainfall(Rainfall rainfall) {
        this.rainfall = rainfall;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(AQI,i);
        parcel.writeParcelable(CO2, i);
        parcel.writeParcelable(PM10, i);
        parcel.writeParcelable(PM25, i);
        parcel.writeParcelable(rainfall, i);
        parcel.writeParcelable(temperature, i);
        parcel.writeParcelable(humidity, i);
        parcel.writeParcelable(location, i);
        parcel.writeParcelable(windSpeed, i);
    }
}
