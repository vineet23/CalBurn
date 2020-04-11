package com.paper.squeeze.calburn.CustomClass;

import java.io.Serializable;

public class Third implements Serializable {

    private String kcal;
    private String name;
    private String serving;

    public Third(String kcal, String name, String serving) {
        this.kcal = kcal;
        this.name = name;
        this.serving = serving;
    }

    public String getKcal() {
        return kcal;
    }

    public String getName() {
        return name;
    }

    public String getServing() {
        return serving;
    }

}
