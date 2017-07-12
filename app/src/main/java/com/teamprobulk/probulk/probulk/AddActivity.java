package com.teamprobulk.probulk.probulk;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sun.mail.imap.Utility;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;

public class AddActivity extends AppCompatActivity {

    EditText etName, etCost, etPrice, etOldPrice;
    DBHelper dbHelper;
    ImageView bulkaphoto;
    String imgBulka;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = (EditText) findViewById(R.id.editName);
        etCost = (EditText) findViewById(R.id.editCost);
        etPrice = (EditText) findViewById(R.id.editPrice);
        etOldPrice = (EditText) findViewById(R.id.editPriceOld);
        bulkaphoto = (ImageView) findViewById(R.id.imageBulka);

        dbHelper = new DBHelper(this);
    }

    public void getPicture(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bulkaphoto.setImageBitmap(photo);
            imgBulka = dbHelper.convertToBase64(photo);
        }

    }

    public void onClickAdd(View view) {
        if (etName.getText().toString().equals("") || etCost.getText().toString().equals("") || etPrice.getText().toString().equals("") || etOldPrice.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Aizpildiet visus laukus", Toast.LENGTH_LONG);
            toast.show();
        } else {
            String name = etName.getText().toString();
            double cost = Double.parseDouble(etCost.getText().toString());
            double price = Double.parseDouble(etPrice.getText().toString());
            double price_old = Double.parseDouble(etOldPrice.getText().toString());


            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.KEY_NAME, name);
            contentValues.put(DBHelper.KEY_COST, cost);
            contentValues.put(DBHelper.KEY_PRICE, price);
            contentValues.put(DBHelper.KEY_OLDPRICE, price_old);
            contentValues.put(DBHelper.KEY_IMAGE, imgBulka);

            database.insert(DBHelper.TABLE_BULKAS, null, contentValues);

            etName.setText("");
            etCost.setText("");
            etPrice.setText("");
            etOldPrice.setText("");
            bulkaphoto.setImageResource(0);
        }
    }

    public void modBulk(View View) {
        dbHelper.close();
        Intent intent = new Intent(AddActivity.this, ModActivity.class);
        startActivity(intent);
    }

    public void addStop(View View) {
        dbHelper.close();
        finish();
    }


}