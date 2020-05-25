package com.example.ms3codingchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String ColA = "A";
    public static final String ColB = "B";
    public static final String ColC = "C";
    public static final String ColD = "D";
    public static final String ColE = "E";
    public static final String ColF = "F";
    public static final String ColG = "G";
    public static final String ColH = "H";
    public static final String ColI = "I";
    public static final String ColJ = "J";

    public DatabaseHelper(Context context){super(context,DATABASE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (A TEXT, B TEXT, C TEXT, D TEXT, E TEXT, " +
                "F TEXT, G TEXT, H TEXT, I TEXT, J TEXT) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(new StringBuilder().append("DROP IF TABLE EXISTS ").append(TABLE_NAME).toString());
    }

    public boolean addData(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColA, a);
        contentValues.put(ColB, b);
        contentValues.put(ColC, c);
        contentValues.put(ColD, d);
        contentValues.put(ColE, e);
        contentValues.put(ColF, f);
        contentValues.put(ColG, g);
        contentValues.put(ColH, h);
        contentValues.put(ColI, i);
        contentValues.put(ColJ, j);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

}
