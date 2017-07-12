package com.teamprobulk.probulk.probulk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kirill on 01.12.2016.
 */

public class ModSaleActivity extends AppCompatActivity{
    DBHelper dbHelper;
    Spinner etName, etDate;
    EditText etCooked, etSold;
    String active, active_2;
    Button modificet, dzest, ieladesana;
    Integer active_id_1, active_id_2;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> datums = new ArrayList<String>();
    ArrayList<Integer> cooked = new ArrayList<Integer>();
    ArrayList<Integer> sold = new ArrayList<Integer>();
    ArrayAdapter<String> adapt, adapt_2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modsale);

        etName = (Spinner) findViewById(R.id.spinner2);
        etDate = (Spinner) findViewById(R.id.spinner);
        etCooked = (EditText) findViewById(R.id.editText10);
        etSold = (EditText) findViewById(R.id.editText9);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BULKAS, null, null, null, DBHelper.KEY_NAME, null, null);

        if (cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                names.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());;
        }

        if (names.isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula ir tukša", Toast.LENGTH_LONG);
            toast.show();
            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.INVISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.INVISIBLE);
        }
        else
        {
            adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etName.setAdapter(adapt);

            active = etName.getSelectedItem().toString();

            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.VISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.VISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.VISIBLE);
        }

        cursor = database.query(DBHelper.TABLE_SALES, null, "b_name = \""+active+"\"", null, DBHelper.KEY_SDATE, null, null);

        if (cursor.moveToFirst()){
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_SDATE);
            int soldIndex = cursor.getColumnIndex(DBHelper.KEY_SSOLD);
            int cookedIndex = cursor.getColumnIndex(DBHelper.KEY_SCOOKED);
            do {
                datums.add(cursor.getString(dateIndex));
                sold.add(cursor.getInt(soldIndex));
                cooked.add(cursor.getInt(cookedIndex));
            } while (cursor.moveToNext());
        }

        if (datums.isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula par pardošanam ir tukša", Toast.LENGTH_LONG);
            toast.show();
            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.INVISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.INVISIBLE);
        }
        else
        {
            adapt_2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datums);
            adapt_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etDate.setAdapter(adapt_2);
            active_id_1 = etDate.getSelectedItemPosition();
            etSold.setText(Integer.toString(sold.get(active_id_1)));
            etCooked.setText(Integer.toString(cooked.get(active_id_1)));

            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.VISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.VISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.VISIBLE);
        }

        cursor.close();
        dbHelper.close();
    }

    public void modsalesStop(View View){
        finish();
    }

    public void modData(View View){
        if (etSold.getText().toString().equals("") || etCooked.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Aizpildiet visus laukus", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            active_id_1 = etName.getSelectedItemPosition();
            active_id_2 = etDate.getSelectedItemPosition();

            String salescooked = etCooked.getText().toString();
            String salesold = etSold.getText().toString();

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.KEY_SCOOKED, salescooked);
            contentValues.put(DBHelper.KEY_SSOLD, salesold);

            sold.set(active_id_2, Integer.parseInt(etSold.getText().toString()));
            cooked.set(active_id_2, Integer.parseInt(etCooked.getText().toString()));

            int updCount = database.update(DBHelper.TABLE_SALES, contentValues, DBHelper.KEY_SBULKA + "= " + "\"" + etName.getSelectedItem().toString() + "\" AND " + DBHelper.KEY_SDATE + "= " + "\"" + etDate.getSelectedItem().toString() + "\"", null);

            Log.d("mLog", "updates rows count = " + updCount);

            dbHelper.close();
        }
    }

    public void delData(View view) {
        String salesbulka = etName.getSelectedItem().toString();
        String salescooked = etCooked.getText().toString();
        String salesold = etSold.getText().toString();
        String salesdate = etDate.getSelectedItem().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_SBULKA, salesbulka);
        contentValues.put(DBHelper.KEY_SCOOKED, salescooked);
        contentValues.put(DBHelper.KEY_SSOLD, salesold);
        contentValues.put(DBHelper.KEY_SDATE, salesdate);

        int delCount = database.delete(DBHelper.TABLE_SALES,DBHelper.KEY_SBULKA + "= " + "\""+etName.getSelectedItem().toString()+"\" AND " + DBHelper.KEY_SDATE + "= " + "\""+etDate.getSelectedItem().toString()+"\"", null);

        etCooked.setText("");
        etSold.setText("");

        Log.d("mLog", "delete rows count = " + delCount);
        dbHelper.close();
        Button update = (Button) findViewById(R.id.button13);
        update.performClick();
    }
    public void reloadDates(View View){
        active = etName.getSelectedItem().toString();
        cleaningSpinners();

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_SALES, null, "b_name = \""+active+"\"", null, DBHelper.KEY_SDATE, null, null);

        if (cursor.moveToFirst()){
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_SDATE);
            int soldIndex = cursor.getColumnIndex(DBHelper.KEY_SSOLD);
            int cookedIndex = cursor.getColumnIndex(DBHelper.KEY_SCOOKED);
            do {
                datums.add(cursor.getString(dateIndex));
                sold.add(cursor.getInt(soldIndex));
                cooked.add(cursor.getInt(cookedIndex));
            } while (cursor.moveToNext());
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula par pardošanam ir tukša", Toast.LENGTH_LONG);
            toast.show();
        }

        if (datums.isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula par pardošanam ir tukša", Toast.LENGTH_LONG);
            toast.show();
            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.INVISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.INVISIBLE);
        }
        else
        {
            adapt_2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datums);
            adapt_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            etDate.setAdapter(adapt_2);
            active_id_1 = etDate.getSelectedItemPosition();
            etSold.setText(Integer.toString(sold.get(active_id_1)));
            etCooked.setText(Integer.toString(cooked.get(active_id_1)));

            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.VISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.VISIBLE);
            ieladesana = (Button) findViewById(R.id.button12);
            ieladesana.setVisibility(View.VISIBLE);
        }

        cursor.close();
        dbHelper.close();
    }

    public void reloadSpinners(View View){
        active = etDate.getSelectedItem().toString();
        active_id_2 = etDate.getSelectedItemPosition();

        if (datums.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula ir tukša", Toast.LENGTH_LONG);
            toast.show();
            dzest = (Button) findViewById(R.id.button9);
            dzest.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.button10);
            modificet.setVisibility(View.INVISIBLE);
        }
        else
        {
            etCooked.setText(Integer.toString(cooked.get(active_id_2)));
            etSold.setText(Integer.toString(sold.get(active_id_2)));
        }
    }

    public void cleaningSpinners(){
        datums.clear();
        sold.clear();
        cooked.clear();
        if (adapt_2 != null) adapt_2.clear();
    }
}
