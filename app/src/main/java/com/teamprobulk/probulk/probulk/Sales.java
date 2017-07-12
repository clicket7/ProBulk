package com.teamprobulk.probulk.probulk;

import android.text.method.DateTimeKeyListener;

import java.util.Date;

/**
 * Created by Karina on 07-Mar-17.
 */

public class Sales {
    private int ID;
    private String BULKA;
    private String DATE;
    private int COOKED;
    private int SOLD;

    public Sales(){
    }

    public Sales(int id, String bulka, String date, int cooked, int sold){
        this.ID = id;
        this.BULKA = bulka;
        this.DATE = date;
        this.COOKED = cooked;
        this.SOLD = sold;

    }

    public int getId() {
        return ID;
    }
    public void setId(int id) {
        this.ID = id;
    }

    public String getBulka() {
        return BULKA;
    }
    public void setBulka(String bulka) {
        this.BULKA = bulka;
    }

    public String getDate() { return DATE; }
    public void setDate(String date) { this.DATE = date; }

    public int getCooked() { return COOKED; }
    public void setCooked(int cooked) { this.COOKED = cooked; }

    public int getSold() { return SOLD; }
    public void setSold(int sold) { this.SOLD = sold; }
}
