package com.example.zlater.Model;

public class Exercise {
    private int id;
    private String title;
    private String introduction;
    private String content;
    private String tips;
    private int sets;
    private int reps;
    private int rest;
    private String video_url;
    private String image_url;

    public Exercise(int id, String title, String introduction, String content, String tips, int sets, int reps, int rest, String video_url, String image_url) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.content = content;
        this.tips = tips;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
        this.video_url = video_url;
        this.image_url = image_url;
    }

    //This contructor use for loading excercise list
    public Exercise(String title, String image_url) {
        this.title = title;
        this.image_url = image_url;
    }

    //This contructor use for get data
    public Exercise(String title, String introduction, String content, String tips, int sets, int reps, int rest, String video_url, String image_url) {
        this.title = title;
        this.introduction = introduction;
        this.content = content;
        this.tips = tips;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
        this.video_url = video_url;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
