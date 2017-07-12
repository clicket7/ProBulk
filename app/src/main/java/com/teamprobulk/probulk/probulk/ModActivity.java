package com.teamprobulk.probulk.probulk;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Kirill on 15.11.2016.
 */

public class ModActivity extends AppCompatActivity {
    Spinner etName;
    EditText etCost, etPrice, etOldPrice;
    DBHelper dbHelper;
    TextView etId;
    Integer activeId;
    String active;
    Button ielade, modificet, dzest;
    ImageView photo;
    ArrayList<Integer> id = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Double> cost = new ArrayList<Double>();
    ArrayList<Double> price = new ArrayList<Double>();
    ArrayList<Double> oldprice = new ArrayList<Double>();
    ArrayList<String> photos = new ArrayList<String>();
    ArrayAdapter<String> adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);

        etId = (TextView) findViewById(R.id.textViewId);
        etName = (Spinner) findViewById(R.id.spinnerNames);
        etCost = (EditText) findViewById(R.id.editTextCount);
        etPrice = (EditText) findViewById(R.id.editTextCooked);
        etOldPrice = (EditText) findViewById(R.id.editTextOldPrice);
        photo = (ImageView) findViewById(R.id.imageView);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BULKAS,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int costIndex = cursor.getColumnIndex(DBHelper.KEY_COST);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int oldPriceIndex = cursor.getColumnIndex(DBHelper.KEY_OLDPRICE);
            int photoIndex = cursor.getColumnIndex(DBHelper.KEY_IMAGE);
            do {
                id.add(cursor.getInt(idIndex));
                names.add(cursor.getString(nameIndex));
                cost.add(cursor.getDouble(costIndex));
                price.add(cursor.getDouble(priceIndex));
                oldprice.add(cursor.getDouble(oldPriceIndex));
                photos.add(cursor.getString(photoIndex));
            } while (cursor.moveToNext());

        } else{
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula ir tukša", Toast.LENGTH_LONG);
            toast.show();
        }

        adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etName.setAdapter(adapt);
        etName.setSelection(0);
        if (!id.isEmpty()) {
            etId.setText(Integer.toString(id.get(0)));
            etCost.setText(Double.toString(cost.get(0)) + " " + getResources().getString(R.string.euro));
            etPrice.setText(Double.toString(price.get(0)) + " " + getResources().getString(R.string.euro));
            etOldPrice.setText(Double.toString(oldprice.get(0)) + " " + getResources().getString(R.string.euro));

            Bitmap tmp;
            if (photos.get(0) != null) {
                tmp = dbHelper.convertToBitmap(photos.get(0));
            }
            else {
                tmp = null;
                photo.setImageResource(0);
            }

            if (tmp != null) photo.setImageBitmap(tmp);

            activeId = id.get(0);
            active = etName.getSelectedItem().toString();
        }
        else {
            ielade = (Button) findViewById(R.id.button11);
            ielade.setVisibility(View.INVISIBLE);
            dzest = (Button) findViewById(R.id.buttonInfoUpd);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.buttonInfoAdd);
            modificet.setVisibility(View.INVISIBLE);
        }
        cursor.close();
        dbHelper.close();
    }

    public void modBulkData(View View){
        if (etCost.getText().toString().equals("")|| etPrice.getText().toString().equals("")|| etOldPrice.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Aizpildiet visus laukus", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            activeId = etName.getSelectedItemPosition();
            String bulkaid = etId.getText().toString();
            String bulkaname = etName.getSelectedItem().toString();
            String bulkacost = etCost.getText().toString();
            String bulkaprice = etPrice.getText().toString();
            String bulkaoldprice = etOldPrice.getText().toString();
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.KEY_NAME, bulkaname);
            contentValues.put(DBHelper.KEY_COST, bulkacost);
            contentValues.put(DBHelper.KEY_PRICE, bulkaprice);
            contentValues.put(DBHelper.KEY_OLDPRICE, bulkaoldprice);

            int updCount = database.update(DBHelper.TABLE_BULKAS, contentValues, DBHelper.KEY_NAME + "=?", new String[]{bulkaname});

            Log.d("mLog", "updates rows count = " + updCount);

            dbHelper.close();
        }
    }

    public void deleteBulkData(View View){
        if (activeId > 0) {
            --activeId;
        }
        else {
            activeId = 0;
        }
        String bulkaid = etId.getText().toString();
        String bulkaname = etName.getSelectedItem().toString();
        String bulkacost = etCost.getText().toString();
        String bulkaprice = etPrice.getText().toString();
        String bulkaoldprice = etOldPrice.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int delCount = database.delete(DBHelper.TABLE_BULKAS, DBHelper.KEY_NAME + "=?" , new String[] {bulkaname});

        etId.setText("");
        etCost.setText("");
        etPrice.setText("");
        etOldPrice.setText("");
        photo.setImageResource(android.R.color.transparent);

        Log.d("mLog", "delete rows count = " + delCount);

        dbHelper.close();
    }

    public void reloadSpinner(View View){
        activeId = etName.getSelectedItemPosition();
        clearing();

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_BULKAS,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int costIndex = cursor.getColumnIndex(DBHelper.KEY_COST);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int oldPriceIndex = cursor.getColumnIndex(DBHelper.KEY_OLDPRICE);
            int photoIndex = cursor.getColumnIndex(DBHelper.KEY_IMAGE);
            do {
                id.add(cursor.getInt(idIndex));
                names.add(cursor.getString(nameIndex));
                cost.add(cursor.getDouble(costIndex));
                price.add(cursor.getDouble(priceIndex));
                oldprice.add(cursor.getDouble(oldPriceIndex));
                photos.add(cursor.getString(photoIndex));
            } while (cursor.moveToNext());
        }

        if (id.isEmpty()){
            ielade = (Button) findViewById(R.id.button11);
            ielade.setVisibility(View.INVISIBLE);
            dzest = (Button) findViewById(R.id.buttonInfoUpd);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.buttonInfoAdd);
            modificet.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula ir tukša", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (id.size() <= activeId) activeId--;
            etName.setAdapter(adapt);
            etName.setSelection(activeId);

            etId.setText(Integer.toString(id.get(activeId)));
            etCost.setText(Double.toString(cost.get(activeId)) + " " + getResources().getString(R.string.euro));
            etPrice.setText(Double.toString(price.get(activeId)) + " " + getResources().getString(R.string.euro));
            etOldPrice.setText(Double.toString(oldprice.get(activeId)) + " " + getResources().getString(R.string.euro));
            Bitmap tmp;
            if (photos.get(activeId) != null) {
                tmp = dbHelper.convertToBitmap(photos.get(activeId));
            }
            else {
                tmp = null;
                photo.setImageResource(0);
            }

            if (tmp != null) photo.setImageBitmap(tmp);
        }
        cursor.close();
        dbHelper.close();
    }

    public void clearing(){
        adapt.clear();
        id.clear();
        names.clear();
        cost.clear();
        price.clear();
        oldprice.clear();
        photos.clear();
    }

    public void modStop(View View){
        finish();
    }
}
