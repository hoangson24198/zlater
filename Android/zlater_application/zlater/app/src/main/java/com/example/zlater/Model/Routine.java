package com.example.zlater.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hades on 30,October,2019
 **/
@Entity(tableName = "polyfit_routine")
public class Routine {
    @ColumnInfo(name = "step_count")
    @SerializedName("step_count")
    private int stepCount;
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @SerializedName("createdAt")
    @ColumnInfo(name = "createdAt")
    private String createdAt;
    @ColumnInfo(name = "updatedAt")
    @SerializedName("updatedAt")
    private String updatedAt;
    @ColumnInfo(name = "time_practice")
    @SerializedName("time_practice")
    private String timePractice;
    @ColumnInfo(name = "calories_consumed")
    @SerializedName("calories_consumed")
    private String caloriesConsumed;
    @ColumnInfo(name = "id_user")
    @SerializedName("id_user")
    private int polyfitUserId;
    @ColumnInfo(name = "create_date")
    @SerializedName("create_date")
    private String create_date;


    public Routine() {
    }


    @Ignore
    public Routine(int stepCount, String timePractice, String caloriesConsumed, int polyfitUserId) {
        this.stepCount = stepCount;
        this.timePractice = timePractice;
        this.caloriesConsumed = caloriesConsumed;
        this.polyfitUserId = polyfitUserId;
    }



    @Ignore
    public Routine(int stepCount, String createdAt, String updatedAt, String timePractice, String caloriesConsumed, int polyfitUserId) {
        this.stepCount = stepCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.timePractice = timePractice;
        this.caloriesConsumed = caloriesConsumed;
        this.polyfitUserId = polyfitUserId;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTimePractice() {
        return timePractice;
    }

    public void setTimePractice(String timePractice) {
        this.timePractice = timePractice;
    }

    public String getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public void setCaloriesConsumed(String caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public int getPolyfitUserId() {
        return polyfitUserId;
    }

    public void setPolyfitUserId(int polyfitUserId) {
        this.polyfitUserId = polyfitUserId;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}

