package com.teamprobulk.probulk.probulk;

/**
 * Created by Karina on 07-Mar-17.
 */

public class Bulka {
    private int ID;
    private String NAME;
    private double COST;
    private double PRICE;

    public Bulka(){
    }

    public Bulka(int id, String name, double cost, double price){
        this.ID = id;
        this.NAME = name;
        this.COST = cost;
        this.PRICE =price;
    }

    public int getId() {
        return ID;
    }
    public void setId(int id) {
        this.ID = id;
    }

    public String getName() {
        return NAME;
    }
    public void setName(String name) {
        this.NAME = name;
    }

    public double getCost() {
        return COST;
    }
    public void setCost(double cost) {
        this.COST = cost;
    }

    public double getPrice() {
        return PRICE;
    }
    public void setPrice(double price) {
        this.PRICE = price;
    }
}
