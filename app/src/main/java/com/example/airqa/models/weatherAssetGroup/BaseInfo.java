package com.example.airqa.models.weatherAssetGroup;

// All sub-attributes (AQI, temperature, CO2...) share these types of information
public class BaseInfo {
    private String type;
    private String name;
    private long timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
