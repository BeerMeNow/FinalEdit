package com.argumedo.kevin.beerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private String[] projection = {
            //beerId, name, description, abv, pic, styleId
            Contract.beerEntry._ID,
            Contract.beerEntry.BEERID,
            Contract.beerEntry.NAME,
            Contract.beerEntry.ABV,
            Contract.beerEntry.STYLE,
            Contract.beerEntry.TYPE,};

    private static final String DATABASE_CREATE =
            "CREATE TABLE " +
                    Contract.beerEntry.TABLE_NAME + " (" +
                    Contract.beerEntry._ID + " TEXT PRIMARY KEY, " +
                    Contract.beerEntry.BEERID + " TEXT NOT NULL, " +
                    Contract.beerEntry.NAME + " TEXT NOT NULL, " +
                    Contract.beerEntry.ABV + " TEXT NOT NULL, " +
                    Contract.beerEntry.STYLE + " TEXT NOT NULL," +
                    Contract.beerEntry.TYPE + " TEXT NOT NULL" +")";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.beerEntry.TABLE_NAME;

    public DataBaseHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DB HELPER", "Create table command: " + DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void insertbeerEntry(Beer beer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
//    private String id,day,min,max,night,eve,morn,wtf,description;
        cv.put(Contract.beerEntry._ID, beer.getBeerId());
        cv.put(Contract.beerEntry.BEERID, beer.getBeerId());
        cv.put(Contract.beerEntry.NAME, beer.getName());
        cv.put(Contract.beerEntry.ABV, beer.getAbv());
        cv.put(Contract.beerEntry.STYLE, beer.getStyleId());
        cv.put(Contract.beerEntry.TYPE, beer.getType());


        db.insert(Contract.beerEntry.TABLE_NAME, null, cv);
    }

    public Cursor getAllRows() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(Contract.beerEntry.TABLE_NAME, projection, null, null, null, null, null);

//        Here's the method with arguments:
//        public Cursor query (String table, String[] columns, String selection, String[]
//        selectionArgs, String groupBy, String orderBy, String limit)

    }

    public Cursor getRowByID(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] ids = {String.valueOf(id)};
        return db.query(Contract.beerEntry.TABLE_NAME, projection, Contract.beerEntry._ID + "==?", ids, null, null, null);
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] ids = {id};
        db.delete(Contract.beerEntry.TABLE_NAME, "_id" + "==?", ids);
        Log.i("what id","ID:"+id);
        Log.i("delete rows","IDS:"+java.util.Arrays.toString(ids));
    }

    public void addRows(List<Beer> beers) {
        for (Beer beer : beers) {
            insertbeerEntry(beer);
        }
    }

    public void addRow(Beer beer) {

        insertbeerEntry(beer);
    }
    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + Contract.beerEntry.TABLE_NAME);
    }

    public void dropTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
    }


}
