package com.teamprobulk.probulk.probulk;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 07.03.2017.
 */

public class TableActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Spinner tabulas;
    String active;
    ListView datalist;
    ArrayList<String> ArrayofBulka = new ArrayList<String>();
    ArrayList<String>ArrayofSales = new ArrayList<String>();
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        tabulas = (Spinner) findViewById(R.id.spinnertables);
        datalist = (ListView) findViewById(R.id.datalist);

        ArrayList<String> tablenames = new ArrayList<String>();
        tablenames.add(dbHelper.TABLE_BULKAS);
        tablenames.add(dbHelper.TABLE_SALES);

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tablenames);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tabulas.setAdapter(adapt);
        active = tabulas.getSelectedItem().toString();
    }

    public void loadData(View view){
        ArrayofBulka.clear();
        ArrayofSales.clear();
        String st = tabulas.getSelectedItem().toString();
        if (st.equals(dbHelper.TABLE_BULKAS)) {
            List<Bulka> bulkalist = dbHelper.getAllBulkas();
            for (Bulka b : bulkalist) {
                String log = "ID: " + b.getId() + "\n   Name: " + b.getName() + "\n   Cost: "
                        + b.getCost() + "\n   Price: " + b.getPrice();
                Log.d("Name: ", log);
                ArrayofBulka.add(log);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ArrayofBulka);
            datalist.setAdapter(adapter);
        }
        if (st.equals(dbHelper.TABLE_SALES)) {
            List<Sales> saleslist = dbHelper.getAllSales();
            for (Sales s : saleslist) {
                String log = "ID: " + s.getId() + "\n   Bulka: " + s.getBulka() +
                        "\n   Date: " + s.getDate() + "\n   Cooked: " + s.getCooked() +
                        "\n   Sold: " + s.getSold();
                Log.d("Name: ", log);
                ArrayofSales.add(log);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ArrayofSales);
            datalist.setAdapter(adapter);
        }
    }

    public void TableStop(View view) {
        dbHelper.close();
        finish();
    }

}
