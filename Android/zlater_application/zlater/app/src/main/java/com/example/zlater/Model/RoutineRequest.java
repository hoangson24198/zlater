package com.example.zlater.Model;

/**
 * Created by Hoang Son on 03,November,2019
 **/
public class RoutineRequest {
    private int id;
    private int step_count;
    private String create_date;
    private String updatedAt;
    private String time_practice;
    private String calories_consumed;
    private int id_user;

    public RoutineRequest(int id, int step_count, String create_date, String updatedAt, String time_practice, String calories_consumed, int id_user) {
        this.id = id;
        this.step_count = step_count;
        this.create_date = create_date;
        this.updatedAt = updatedAt;
        this.time_practice = time_practice;
        this.calories_consumed = calories_consumed;
        this.id_user = id_user;
    }

    public RoutineRequest(int step_count, String create_date, String time_practice, String calories_consumed, int id_user) {
        this.step_count = step_count;
        this.create_date = create_date;
        this.time_practice = time_practice;
        this.calories_consumed = calories_consumed;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStep_count() {
        return step_count;
    }

    public void setStep_count(int step_count) {
        this.step_count = step_count;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTime_practice() {
        return time_practice;
    }

    public void setTime_practice(String time_practice) {
        this.time_practice = time_practice;
    }

    public String getCalories_consumed() {
        return calories_consumed;
    }

    public void setCalories_consumed(String calories_consumed) {
        this.calories_consumed = calories_consumed;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
