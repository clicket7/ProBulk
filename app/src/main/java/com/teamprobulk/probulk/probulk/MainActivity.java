package com.teamprobulk.probulk.probulk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openBulkAdding(View View){
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    public void openBulkSales(View View){
        Intent intent = new Intent(MainActivity.this, SaleActivity.class);
        startActivity(intent);
    }

    public void openBulkForecast(View View){
        Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
        startActivity(intent);
    }

    public void openTable(View view) {
        Intent intent = new Intent(MainActivity.this, TableActivity.class);
        startActivity(intent);
    }
}
