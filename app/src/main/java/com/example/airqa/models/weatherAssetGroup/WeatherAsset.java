package com.example.airqa.models.weatherAssetGroup;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class WeatherAsset implements Parcelable {
    private String id;
    private int version;
    private long createdOn;
    private String name;
    private boolean accessPublicRead;
    private String parentId;
    private String realm;
    private String type;
    private List<String> path;
    private Attributes attributes;

    public WeatherAsset(){

    }

    protected WeatherAsset(Parcel in) {
        id = in.readString();
        version = in.readInt();
        createdOn = in.readLong();
        name = in.readString();
        accessPublicRead = in.readByte() != 0;
        parentId = in.readString();
        realm = in.readString();
        type = in.readString();
        path = in.createStringArrayList();
        attributes = in.readParcelable(Attributes.class.getClassLoader());
    }

    public static final Creator<WeatherAsset> CREATOR = new Creator<WeatherAsset>() {
        @Override
        public WeatherAsset createFromParcel(Parcel in) {
            return new WeatherAsset(in);
        }

        @Override
        public WeatherAsset[] newArray(int size) {
            return new WeatherAsset[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccessPublicRead() {
        return accessPublicRead;
    }

    public void setAccessPublicRead(boolean accessPublicRead) {
        this.accessPublicRead = accessPublicRead;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(version);
        parcel.writeLong(createdOn);
        parcel.writeString(name);
        parcel.writeByte((byte) (accessPublicRead ? 1 : 0));
        parcel.writeString(parentId);
        parcel.writeString(realm);
        parcel.writeString(type);
        parcel.writeStringList(path);
        parcel.writeParcelable(attributes, i);
    }
}
