package com.paper.squeeze.calburn.CustomClass;

import java.util.ArrayList;

public class First {

    private String name;
    private ArrayList<Second> data;

    public First(String name, ArrayList<Second> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Second> getData() {
        return data;
    }
}
