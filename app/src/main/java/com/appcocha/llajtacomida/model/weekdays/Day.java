package com.appcocha.llajtacomida.model.weekdays;

import java.util.ArrayList;

public class Day {
    private String dayName;
    private ArrayList<String> plateListName;

    public Day (String nameDay){
        this.dayName = nameDay;
        plateListName = new ArrayList<String>();
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public ArrayList<String> getPlateListName() {
        return plateListName;
    }

    public void setPlateListName(ArrayList<String> namePlateList) {
        this.plateListName = namePlateList;
    }

    public void addPlateName(String name){
        plateListName.add(name);
    }
}
