package com.example.llajtacomida.models;

import java.util.ArrayList;
import java.util.UUID;

public class Menu {

    private String id;
    private String name;
    private ArrayList<String> menuList;

    public Menu(String id) {
        this.id = id;
        menuList = new ArrayList<>();
    }

    public Menu(){
        id = UUID.randomUUID().toString();
        menuList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<String> menuList) {
        this.menuList = menuList;
    }
}
