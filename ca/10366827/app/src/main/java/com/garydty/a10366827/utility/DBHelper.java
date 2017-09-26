package com.garydty.a10366827.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.garydty.a10366827.models.Summoner;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Gary Doherty on 18/09/2017.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leagueyoke.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SummonerTable.CREATE_TABLE);
        db.execSQL(MarkerTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SummonerTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MarkerTable.TABLE_NAME);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + SummonerTable.TABLE_NAME);
        db.execSQL("DELETE FROM " + MarkerTable.TABLE_NAME);
        db.close();
    }

    public void addMarker(double lng, double lat){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MarkerTable.LONGITUDE, lng);
        cv.put(MarkerTable.LATITUDE, lat);

        db.insert(MarkerTable.TABLE_NAME, null, cv);
        db.close();
    }

    public void removeAllMarkers(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + MarkerTable.TABLE_NAME);
        db.close();
    }

    public ArrayList<MarkerOptions> getMarkerOptions(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + MarkerTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        if(cursor == null)
            return markers;

        if(cursor.moveToFirst()) {
            do {
                MarkerOptions m = new MarkerOptions();
                double longitude = cursor.getDouble(0);
                double latitude = cursor.getDouble(1);
                m.position(new LatLng(longitude, latitude));
                markers.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return markers;
    }


    public void addSummoner(Summoner summoner){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SummonerTable.ID, summoner.id);
        cv.put(SummonerTable.NAME, summoner.name);
        cv.put(SummonerTable.ACCOUNT_ID, summoner.accountId);
        cv.put(SummonerTable.ICON, summoner.profileIconId);
        cv.put(SummonerTable.LEVEL, summoner.summonerLevel);
        cv.put(SummonerTable.REV_DATE, summoner.revisionDate);

        db.insert(SummonerTable.TABLE_NAME, null, cv);
        db.close();
    }

    public boolean exists(Summoner s){
        if(s == null || s.name == null)
            return false;

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + SummonerTable.TABLE_NAME + " WHERE " +
                SummonerTable.NAME + "=\"" + s.name + "\"";
        Cursor cursor = db.rawQuery(query, null);
        boolean contains = cursor.moveToFirst();
        cursor.close();
        db.close();
        return contains;
    }

    public void removeSummoner(Summoner s){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SummonerTable.TABLE_NAME, SummonerTable.ID + "=\"" + s.id + "\"", null);
        db.close();
    }

    public ArrayList<Summoner> getAllStoredSummoners(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + SummonerTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Summoner> summoners = new ArrayList<>();
        if(cursor == null)
            return summoners;

        if(cursor.moveToFirst()) {
            do {
                Summoner s = new Summoner();
                s.id = cursor.getLong(0);
                s.name = cursor.getString(1);
                s.profileIconId = cursor.getInt(2);
                s.revisionDate = cursor.getLong(3);
                s.accountId = cursor.getLong(4);
                s.summonerLevel = cursor.getLong(5);
                summoners.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return summoners;
    }

    private class MarkerTable {
        static final String TABLE_NAME = "MarkerTable";

        //  Columns
        static final String LONGITUDE = "longitude";
        static final String LATITUDE = "latitude";

        // Table Create Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                LONGITUDE + " DOUBLE," +
                LATITUDE + " DOUBLE)";
    }

    private class SummonerTable {
        static final String TABLE_NAME = "Summoner";

        //  Columns
        static final String ID = "id";
        static final String NAME = "name";
        static final String ICON = "profileIconId";
        static final String REV_DATE = "revisionDate";
        static final String ACCOUNT_ID = "accountId";
        static final String LEVEL = "summonerLevel";

        // Table Create Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                ID + " LONG PRIMARY KEY," +
                NAME +" TEXT," +
                ICON + " INT," +
                REV_DATE + " LONG," +
                ACCOUNT_ID + " LONG,"
                + LEVEL + " LONG)";
    }
}
