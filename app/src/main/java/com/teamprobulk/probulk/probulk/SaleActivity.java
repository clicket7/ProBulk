package com.teamprobulk.probulk.probulk;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.widget.DatePicker;

/**
 * Created by Kirill on 15.11.2016.
 */

public class SaleActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Spinner etName;
    EditText etSold, etMade;
    TextView etId;
    Integer activeId;
    String active;
    Button pievienot, modificet;
    EditText etDate;
    Calendar calendar = Calendar.getInstance();
    Date currentDate = new Date();
    long currentMillis = currentDate.getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        etId = (TextView) findViewById(R.id.textViewBulkId);
        etName = (Spinner) findViewById(R.id.spinnerSaleBulk);
        etSold = (EditText) findViewById(R.id.editTextBulkCount);
        etMade = (EditText) findViewById(R.id.editTextCooked);
        etDate = (EditText) findViewById(R.id.editTextDate);
        etDate.setInputType(InputType.TYPE_NULL);

        dbHelper = new DBHelper(this);
        ArrayList<Integer> id = new ArrayList<Integer>();
        ArrayList<String> names = new ArrayList<String>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BULKAS,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                id.add(cursor.getInt(idIndex));
                names.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());

        }

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etName.setAdapter(adapt);
        if (id.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "Tabula ir tukša", Toast.LENGTH_LONG);
            toast.show();
            pievienot = (Button) findViewById(R.id.button2);
            pievienot.setVisibility(View.INVISIBLE);
            modificet = (Button) findViewById(R.id.button4);
            modificet.setVisibility(View.INVISIBLE);
        }
        else {
            etId.setText(Integer.toString(id.get(0)));
            activeId = id.get(0);
            active = etName.getSelectedItem().toString();
        }
        cursor.close();
        dbHelper.close();
      }

    public void addInfo(View View){
        if (etSold.getText().toString().equals("")|| etMade.getText().toString().equals("")|| etDate.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Aizpildiet visus laukus", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            String salesname = etName.getSelectedItem().toString();
            int salessold = Integer.parseInt(etSold.getText().toString());
            int salescooked = Integer.parseInt(etMade.getText().toString());
            String salesdate = etDate.getText().toString();

            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.KEY_SBULKA, salesname);
            contentValues.put(DBHelper.KEY_SSOLD, salessold);
            contentValues.put(DBHelper.KEY_SCOOKED, salescooked);
            contentValues.put(DBHelper.KEY_SDATE, salesdate);

            database.insert(DBHelper.TABLE_SALES, null, contentValues);

            etMade.setText("");
            etSold.setText("");
            etDate.setText("");
        }
    }
    public void updInfo(View View){
        dbHelper.close();
        Intent intent = new Intent(SaleActivity.this, ModSaleActivity.class);
        startActivity(intent);
    }
    public void salesStop(View View){
        finish();
    }

    public void setDate(View v) {
        DatePickerDialog datePick;
        datePick = new DatePickerDialog(SaleActivity.this, d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePick.getDatePicker().setMaxDate(currentMillis);
        datePick.show();
    }

    private void setInitialDateTime() {
        etDate.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
}
