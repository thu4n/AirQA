package com.example.airqa;
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
    public static final int DATABASE_VERSION = 1;

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
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("USER", "_id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS ASSET");
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

