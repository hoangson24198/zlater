package com.example.zlater.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "zlater_reminder")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "minute")
    private int minute;
    @ColumnInfo(name = "monday")
    private int monday;
    @ColumnInfo(name = "tuesday")
    private int tuesday;
    @ColumnInfo(name = "wednesday")
    private int wednesday;
    @ColumnInfo(name = "thursday")
    private int thursday;
    @ColumnInfo(name = "friday")
    private int friday;
    @ColumnInfo(name = "saturday")
    private int saturday;
    @ColumnInfo(name = "sunday")
    private int sunday;
    @ColumnInfo(name = "turnOn")
    private int turnOn;
    @ColumnInfo(name = "note")
    private String note;


    public Reminder() {
    }

    @Ignore
    public Reminder(int hour, int minute, String note) { //Insert
        this.hour = hour;
        this.minute = minute;
        this.note = note;
    }

    @Ignore
    public Reminder(int id, int hour, int minute, String note) { //Get
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.note = note;
    }

    @Ignore
    public Reminder(int id, int hour, int minute, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, String note,int turnOn) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.note = note;
        this.turnOn=turnOn;
    }

    @Ignore
    public Reminder(int hour, int minute, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, String note,int turnOn) {
        this.hour = hour;
        this.minute = minute;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.note = note;
        this.turnOn=turnOn;
    }


    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTurnOn() {
        return turnOn;
    }

    public void setTurnOn(int turnOn) {
        this.turnOn = turnOn;
    }
}
