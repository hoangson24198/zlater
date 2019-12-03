package com.example.zlater.Model;

/**
 * Created by Hoang Son on 03,November,2019
 **/
public class History {
    private int id;
    private float bmi;
    private int id_user;

    public History(int id, float bmi, int id_user) {
        this.id = id;
        this.bmi = bmi;
        this.id_user = id_user;
    }

    public History(float bmi, int id_user) {
        this.bmi = bmi;
        this.id_user = id_user;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
