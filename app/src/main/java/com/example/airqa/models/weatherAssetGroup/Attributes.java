package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Attributes implements Parcelable {
    private AQI aqi;
    private CO2 co2;
    private PM10 pm10;
    private PM25 pm25;
    private Rainfall rainfall;
    private Temperature temperature;
    private Humidity humidity;
    private Location location;
    private WindSpeed windSpeed;

    protected Attributes(Parcel in) {
        co2 = in.readParcelable(CO2.class.getClassLoader());
        pm10 = in.readParcelable(PM10.class.getClassLoader());
        pm25 = in.readParcelable(PM25.class.getClassLoader());
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

    public AQI getAqi() {
        return aqi;
    }

    public void setAqi(AQI aqi) {
        this.aqi = aqi;
    }

    public CO2 getCo2() {
        return co2;
    }

    public void setCo2(CO2 co2) {
        this.co2 = co2;
    }

    public PM10 getPm10() {
        return pm10;
    }

    public void setPm10(PM10 pm10) {
        this.pm10 = pm10;
    }

    public PM25 getPm25() {
        return pm25;
    }

    public void setPm25(PM25 pm25) {
        this.pm25 = pm25;
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
        parcel.writeParcelable(co2, i);
        parcel.writeParcelable(pm10, i);
        parcel.writeParcelable(pm25, i);
        parcel.writeParcelable(rainfall, i);
        parcel.writeParcelable(temperature, i);
        parcel.writeParcelable(humidity, i);
        parcel.writeParcelable(location, i);
        parcel.writeParcelable(windSpeed, i);
    }
}
