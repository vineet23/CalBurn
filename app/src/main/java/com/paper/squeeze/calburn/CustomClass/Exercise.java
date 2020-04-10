package com.paper.squeeze.calburn.CustomClass;

public class Exercise {

    private String name;
    private String sub;
    private int raw;

    public Exercise(String name, String sub, int raw) {
        this.name = name;
        this.sub = sub;
        this.raw = raw;
    }

    public String getName() {
        return name;
    }

    public String getSub() {
        return sub;
    }

    public int getRaw() {
        return raw;
    }
}
