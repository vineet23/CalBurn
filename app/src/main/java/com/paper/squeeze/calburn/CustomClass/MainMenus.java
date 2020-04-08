package com.paper.squeeze.calburn.CustomClass;

import android.graphics.drawable.Drawable;

public class MainMenus {

    private String title;
    private Drawable image;
    private int color;

    public MainMenus(String title, Drawable image, int color) {
        this.title = title;
        this.image = image;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getImage() {
        return image;
    }

    public int getColor() {
        return color;
    }
}
