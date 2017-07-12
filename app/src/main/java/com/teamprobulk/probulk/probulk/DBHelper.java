package com.teamprobulk.probulk.probulk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProBulk.db";

//Table names
    public static final String TABLE_BULKAS = "bulkas";
    public static final String TABLE_SALES = "sales";

// Table bulkas column names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_COST = "cost";
    public static final String KEY_PRICE = "price";
    public static final String KEY_OLDPRICE = "oldprice";
    public static final String KEY_IMAGE = "image";

// Table sales column names
    public static final String KEY_SID = "_id";
    public static final String KEY_SDATE = "date";
    public static final String KEY_SBULKA = "b_name";
    public static final String KEY_SCOOKED = "cooked";
    public static final String KEY_SSOLD = "sold";

//Table Create Statements
    private static final String CREATE_TABLE_BULKAS = "CREATE TABLE "
        + TABLE_BULKAS + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
        + KEY_NAME + " TEXT, " + KEY_COST + " REAL, "
        + KEY_IMAGE + "BLOB, "
        + KEY_PRICE + " REAL, " + KEY_OLDPRICE + " REAL, " + KEY_IMAGE + " TEXT" + ")";

    private static final String CREATE_TABLE_SALES = "CREATE TABLE "
            + TABLE_SALES + "(" + KEY_SID + " INTEGER PRIMARY KEY, "
            + KEY_SBULKA + " TEXT, "
            + KEY_SSOLD + " INTEGER, " + KEY_SCOOKED + " INTEGER, "
            + KEY_SDATE + " DATETIME" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SALES);
        db.execSQL(CREATE_TABLE_BULKAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int neVersion) {
        db.execSQL("drop table if exists " + TABLE_SALES);
        db.execSQL("drop table if exists " + TABLE_BULKAS);

        onCreate(db);
    }

    public String convertToBase64(Bitmap bitmap){
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] byteArray = os.toByteArray();
            return Base64.encodeToString(byteArray, 0);
    }

    public Bitmap convertToBitmap(String base64String) {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return bitmapResult;
    }

    public List<Bulka> getAllBulkas() {
        List<Bulka> bulkalist = new ArrayList<Bulka>();
        String selectQuery = "SELECT * FROM " + TABLE_BULKAS + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int costIndex = cursor.getColumnIndex(KEY_COST);
            int priceIndex = cursor.getColumnIndex(KEY_PRICE);
            do{
                Bulka b = new Bulka();
                b.setId(cursor.getInt(idIndex));
                b.setName(cursor.getString(nameIndex));
                b.setCost(cursor.getDouble(costIndex));
                b.setPrice(cursor.getDouble(priceIndex));
                bulkalist.add(b);
            } while (cursor.moveToNext());

        }
        db.close();
        cursor.close();
        return bulkalist;
    }

    public List<Sales> getAllSales() {
        List<Sales> saleslist = new ArrayList<Sales>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(KEY_SID);
            int bulkaIndex = cursor.getColumnIndex(KEY_SBULKA);
            int dateIndex = cursor.getColumnIndex(KEY_SDATE);
            int cookedIndex = cursor.getColumnIndex(KEY_SCOOKED);
            int soldIndex = cursor.getColumnIndex(KEY_SSOLD);
            do{
                Sales s = new Sales();
                s.setId(cursor.getInt(idIndex));
                s.setBulka(cursor.getString(bulkaIndex));
                s.setDate(cursor.getString(dateIndex));
                s.setCooked(cursor.getInt(cookedIndex));
                s.setSold(cursor.getInt(soldIndex));
                saleslist.add(s);
            } while (cursor.moveToNext());

        }
        db.close();
        cursor.close();
        return saleslist;
    }
}
