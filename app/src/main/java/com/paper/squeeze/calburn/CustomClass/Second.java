package com.paper.squeeze.calburn.CustomClass;

import java.util.ArrayList;

public class Second {

    private String detail;
    private String description;
    private String image;
    private String title;
    private ArrayList<Third> list;

    public Second(String detail, String description, String image, String title, ArrayList<Third> list) {
        this.detail = detail;
        this.description = description;
        this.image = image;
        this.title = title;
        this.list = list;
    }

    public String getDetail() {
        return detail;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Third> getList() {
        return list;
    }
}

