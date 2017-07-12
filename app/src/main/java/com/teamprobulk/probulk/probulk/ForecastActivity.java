package com.teamprobulk.probulk.probulk;

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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kirill on 14.11.2016.
 */

public class ForecastActivity extends AppCompatActivity {


    DBHelper dbHelper;
    Integer i;
    Integer a;
    Spinner etName;
    EditText et1, et2, et3, et4, et5, et6, et7, etMail;
    TextView date1, date2, date3, date4, date5, date6, date7;
    String active;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<Integer> sales = new ArrayList<Integer>();
    ArrayList<Integer> cooked = new ArrayList<Integer>();
    ArrayList<Integer> forecast = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_forecast);

        etName = (Spinner) findViewById(R.id.spinner3);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText4);
        et5 = (EditText) findViewById(R.id.editText5);
        et6 = (EditText) findViewById(R.id.editText6);
        et7 = (EditText) findViewById(R.id.editText7);
        etMail = (EditText) findViewById(R.id.editText8);

        date1 = (TextView) findViewById(R.id.textViewDate1);
        date2 = (TextView) findViewById(R.id.textViewDate2);
        date3 = (TextView) findViewById(R.id.textViewDate3);
        date4 = (TextView) findViewById(R.id.textViewDate4);
        date5 = (TextView) findViewById(R.id.textViewDate5);
        date6 = (TextView) findViewById(R.id.textViewDate6);
        date7 = (TextView) findViewById(R.id.textViewDate7);

        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("s");
        int datums = currentDate.getDate()+1;
        formatter.applyPattern(datums+"/MM/yyyy");
        String dat = formatter.format(currentDate);
        date1.setText(dat);

        datums = currentDate.getDate()+2;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date2.setText(dat);

        datums = currentDate.getDate()+3;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date3.setText(dat);

        datums = currentDate.getDate()+4;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date4.setText(dat);

        datums = currentDate.getDate()+5;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date5.setText(dat);

        datums = currentDate.getDate()+6;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date6.setText(dat);

        datums = currentDate.getDate()+7;
        formatter.applyPattern(datums+"/MM/yyyy");
        dat = formatter.format(currentDate);
        date7.setText(dat);

        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_SALES, null, null, null, DBHelper.KEY_SBULKA, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_SBULKA);
            do {
                names.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());

            ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            etName.setAdapter(adapt);
            active = etName.getSelectedItem().toString();

            if (names.isEmpty()) {
                Button button = (Button) findViewById(R.id.button14);
                button.setVisibility(View.INVISIBLE);

                Toast toast = Toast.makeText(getApplicationContext(), "Tabula par pardošanam ir tukša", Toast.LENGTH_LONG);
                toast.show();
            }

            cursor.close();
            dbHelper.close();
        }
    }

    public void getForecast(View View){
        active = etName.getSelectedItem().toString();
        if (!(sales.isEmpty()))
        {
            sales.clear();
            forecast.clear();
            cooked.clear();
            date.clear();
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
            et6.setText("");
            et7.setText("");
        }
        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_SALES, null, "b_name = \""+active+"\"", null, null, null, DBHelper.KEY_SID);
        if (cursor.moveToFirst()){
            int salesIndex = cursor.getColumnIndex(DBHelper.KEY_SSOLD);
            int cookedIndex = cursor.getColumnIndex(DBHelper.KEY_SCOOKED);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_SDATE);
            do {
                sales.add(cursor.getInt(salesIndex));
                cooked.add(cursor.getInt(cookedIndex));
                date.add(cursor.getString(dateIndex));
            } while (cursor.moveToNext());
        }



        if (sales.size()<7) {
            Toast toast = Toast.makeText(getApplicationContext(), "Vajag vismaz 7 ieraksti tabulā Sales bulciņai", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            int[] day_koef = {3, 1, 1, 1, 1, 1, 2};

            a = 0;
            for (i=0; i<7; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<6; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[6];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<5; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[5];
            a += forecast.get(1)*day_koef[6];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<4; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[4];
            a += forecast.get(1)*day_koef[5];
            a += forecast.get(2)*day_koef[6];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<3; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[3];
            a += forecast.get(1)*day_koef[4];
            a += forecast.get(2)*day_koef[5];
            a += forecast.get(3)*day_koef[6];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<2; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[2];
            a += forecast.get(1)*day_koef[3];
            a += forecast.get(2)*day_koef[4];
            a += forecast.get(3)*day_koef[5];
            a += forecast.get(4)*day_koef[6];
            a/=10;
            forecast.add(a);

            a = 0;
            for (i=0; i<1; i++) a += sales.get(sales.size()-i-1)*day_koef[i];
            a += forecast.get(0)*day_koef[1];
            a += forecast.get(1)*day_koef[2];
            a += forecast.get(2)*day_koef[3];
            a += forecast.get(3)*day_koef[4];
            a += forecast.get(4)*day_koef[5];
            a += forecast.get(5)*day_koef[6];
            a/=10;
            forecast.add(a);


            et1.setText(Integer.toString(forecast.get(0)));
            et2.setText(Integer.toString(forecast.get(1)));
            et3.setText(Integer.toString(forecast.get(2)));
            et4.setText(Integer.toString(forecast.get(3)));
            et5.setText(Integer.toString(forecast.get(4)));
            et6.setText(Integer.toString(forecast.get(5)));
            et7.setText(Integer.toString(forecast.get(6)));
        }

        cursor.close();
        dbHelper.close();
    }

    public void forecastStop(View View) {
        finish();
    }

    public void sendInformation(View View) {
        String to = etMail.getText().toString();
        String subject = "Atskaite un prognoze par bulciņu "+active;
        String message = "Informācija par pārdošanam:";
        i = 0;
        if (!sales.isEmpty()) {
            do {
                message += "\n Datums - " + date.get(i);
                message += "\n Pardošanas - " + sales.get(i);
                message += "\n Noceptas - " + cooked.get(i);
                i++;
            } while (i != sales.size());

            message += "\n Prognozēšana: ";
            for (i=0;i<7;i++) message += "\n"+forecast.get(i);
            Log.d("Message = ", message);
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);

            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Izvēlējiet E-Mail klientu :"));
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Jāizpilda prognozi pirms", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}