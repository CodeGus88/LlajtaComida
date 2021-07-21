package com.appcocha.llajtacomida.model.restaurant.menu;

import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.google.firebase.database.Exclude;

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
     * Obtiene la lista de platos en el menú
     * @return menuList
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

    /**
     * Devuelve el precio del elemento con id
     * @param id
     * @return price
     */
    @Exclude
    public String getPrice(String id){
        String price = "";
        for(String s: menuList){
            if(s.contains(id)){
                if(Validation.getXWord(s, 2).equals(""))
                    price = StringValues.getDefaultPrice();
                else price = Validation.getXWord(s, 2);
            }
        }
        return price;
    }
}
