package com.appcocha.llajtacomida.model.restaurant.menu;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Modelo
 */
public class Menu {

    private String id;
    private String name;
    private ArrayList<String> menuList;

    /**
     * Constructor, establece el id
     * @param id
     */
    public Menu(String id) {
        this.id = id;
        menuList = new ArrayList<String>();
    }

    /**
     * Constructor, genera un nuevo id
     */
    public Menu(){
        id = UUID.randomUUID().toString();
        menuList = new ArrayList<String>();
    }

    /**
     * Obtiene el id
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Establece el id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre (local, nacional, internacional)
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Establece el nombre
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obbtiene la lista de platos en el menú
     * @return
     */
    public ArrayList<String> getMenuList() {
        return menuList;
    }

    /**
     * Establece la lista menú
     * @param menuList
     */
    public void setMenuList(ArrayList<String> menuList) {
        this.menuList.clear();
        this.menuList.addAll(menuList);
    }
}
