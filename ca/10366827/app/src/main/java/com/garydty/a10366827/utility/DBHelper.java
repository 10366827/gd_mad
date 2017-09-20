package com.garydty.a10366827.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Gary Doherty on 18/09/2017.
 */
public class DBHelper {//public class DBHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "busyoke.db";
//    private static final int DATABASE_VERSION = 1;
//
//    public DBHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(LureTable.CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + LureTable.TABLE_NAME);
//        onCreate(db);
//    }
//
//    public void clearDatabase(){
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + LureTable.TABLE_NAME);
//        db.close();
//    }
//
//    public void addLure(String id, String name, Date time){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(LureTable.ID, id);
//        cv.put(LureTable.NAME, name);
//        cv.put(LureTable.TIME, time.getTime());
//        db.insert(LureTable.TABLE_NAME, null, cv);
//        db.close();
//    }
//
//    public boolean containsLure(String id){
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "SELECT * FROM " + LureTable.TABLE_NAME + " WHERE " +
//                LureTable.ID + "=\"" + id + "\"";
//        Cursor cursor = db.rawQuery(query, null);
//        boolean contains = cursor.moveToFirst();
//        cursor.close();
//        db.close();
//        return contains;
//    }
//
//    public void removeLure(String id){
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(LureTable.TABLE_NAME, LureTable.ID + "=\"" + id + "\"", null);
//        db.close();
//    }
//
////    public void putLures(HashMap<String, Date> lures){
////        if(lures == null)
////            return;
////
////        Set<String> keys = lures.keySet();
////        for(String key : keys)
////            addLure(key, lures.get(key));
////    }
//
//    public HashMap<String, Date> getLures(){
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "SELECT * FROM " + LureTable.TABLE_NAME;
//        Cursor cursor = db.rawQuery(query, null);
//        HashMap<String, Date> lures = new HashMap<String, Date>();
//        if(cursor == null)
//            return lures;
//
//        if(cursor.moveToFirst()) {
//            do {
//                String id = cursor.getString(0);
////                String name = cursor.getString(1);
//                long time = cursor.getLong(2);
//                Date tmp  = new Date(time);
//                lures.put(id, tmp);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return lures;
//    }
//
//    private class StopTable {
//
//    }
//
//    private class RouteTable {
//        static final String TABLE_NAME = "RouteTimetable";
//
//        //  Columns
//        static final String ID = "RouteNumber";
//        static final String NAME = "name";
//        static final String TIME = "time";
//
//        // Table Create Statement
//        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
//                + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY," + NAME +" TEXT," + TIME + " LONG)";
//    }
}
