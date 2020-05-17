package com.example.zlater.Model;

public class Tutorial {
    private String title;
    private String description;
    private Integer image;

    public Tutorial(String title, String description, Integer image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Tutorial(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
