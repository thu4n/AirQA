package com.example.airqa.activities;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper{

    private Context context;
    public static final String DATABASE_NAME = "AIRQA.db";
    public static final int DATABASE_VERSION = 2;

    public MyDatabaseHelper(@Nullable Context context)
    {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context = context;
    }
    public void onCreate(SQLiteDatabase db) {
        // create table USER
        String createUSERTableQuery = "CREATE TABLE USER " +
                "(ID INTEGER PRIMARY KEY," +
                " NAME TEXT," +
                " EMAIL STRING);";
        db.execSQL(createUSERTableQuery);

        // create Table ASSET
        String createAddressTableQuery = "CREATE TABLE ASSET " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DAYTIME TEXT," +
                "RAINFALL TEXT," +
                "TEMPERATURE TEXT," +
                "HUMIDITY TEXT,"+
                "WINDSPEED TEXT," +
                "WINDIRIRECTIOM TEXT," +
                "PLACE TEXT);";
        db.execSQL(createAddressTableQuery);
        // create table weatherAsset
        String createWeatherAssetTableQuery = "CREATE TABLE WeatherAsset " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idAsset TEXT," +
                "CreatedOn BIGINT," +
                "Name TEXT," +
                "AccessPublicRead INTEGER," +
                "AQI INTEGER," +
                "CO2 INTEGER," +
                "Humidity INTEGER," +
                "LocationX REAL," +
                "LocationY REAL," +
                "PM10 REAL," +
                "PM25 REAL," +
                "Rainfall REAL," +
                "Temperature REAL," +
                "Timestamp BIGINT);";

        db.execSQL(createWeatherAssetTableQuery);
    }

    public void insertUSER(int ID, String NAME,String EMAIL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", ID);
        values.put("NAME", NAME);
        values.put("EMAIL", EMAIL);
        long personId = db.insert("USER", null, values);
        db.close();
    }

    public void insertAssetData(String rainfall, String temperature, String humidity, String windSpeed, String windDirection, String place,String daytime) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("RAINFALL", rainfall);
        values.put("TEMPERATURE", temperature);
        values.put("HUMIDITY", humidity);
        values.put("WINDSPEED", windSpeed);
        values.put("WINDIRIRECTIOM", windDirection);
        values.put("PLACE", place);
        values.put("DAYTIME", daytime);

        db.insert("ASSET", null, values);
        db.close();
    }
    // insert WeatherAsset
    public void insertWeatherAssetData(
            String idAsset,
            long createdOn,
            String name,
            int accessPublicRead,
            int aqi,
            int co2,
            int humidity,
            double locationX,
            double locationY,
            double pm10,
            double pm25,
            double rainfall,
            double temperature,
            long timestamp
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idAsset", idAsset);
        values.put("CreatedOn", createdOn);
        values.put("Name", name);
        values.put("AccessPublicRead", accessPublicRead);
        values.put("AQI", aqi);
        values.put("CO2", co2);
        values.put("Humidity", humidity);
        values.put("LocationX", locationX);
        values.put("LocationY", locationY);
        values.put("PM10", pm10);
        values.put("PM25", pm25);
        values.put("Rainfall", rainfall);
        values.put("Temperature", temperature);
        values.put("Timestamp", timestamp);

        db.insert("WeatherAsset", null, values);
        db.close();
    }

    // Get all data in weatherAsset
    public Cursor readAllweatherASSETData(){
        String query = "SELECT * FROM WeatherAsset";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    //
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("USER", "_id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS ASSET");
        db.execSQL("DROP TABLE IF EXISTS WeatherAsset");
        onCreate(db);
    }
    public Cursor getAllAssetData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("ASSET", null, null, null, null, null, null);
    }
    public Cursor readAllASSETData(){
        String query = "SELECT * FROM ASSET";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public void deleteAllAssetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ASSET", null, null);
        db.close();
    }
    public void dropTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.close();
    }
}

