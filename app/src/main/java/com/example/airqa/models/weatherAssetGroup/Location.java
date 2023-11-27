package com.example.airqa.models.weatherAssetGroup;

import com.example.airqa.models.assetGroup.Value;

public class Location extends BaseInfo{
    private Value value;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

}
