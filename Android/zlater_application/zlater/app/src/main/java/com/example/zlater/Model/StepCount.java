package com.example.zlater.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Hades on 31,October,2019
 **/

@Entity(tableName = "polyfit_step_count")
public class StepCount {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "step")
    private int step;

    public StepCount() {
    }
    @Ignore
    public StepCount(int hour, int step) {
        this.hour = hour;
        this.step = step;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
