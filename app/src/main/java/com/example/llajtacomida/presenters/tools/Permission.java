package com.example.llajtacomida.presenters.tools;

import java.util.ArrayList;

/**
 * Lo spermisos son en terminos generales, es decir que cualuier plato, cualquier usuario, cualquier restaurante, etc
 * Todos los autores pueden hacer todo en sus publicaciones
 */
public class Permission {

    private ArrayList<String> rolePermissions;
    private final String ROLE;
    public Permission(final String ROLE){
        this.ROLE = ROLE;
        rolePermissions = new ArrayList<String>();
        switch(ROLE.toLowerCase()){
            case "admin":
                loadAdmin();
                break;
            case "collaborator":
                loadCollaborator();
                break;
            case "voter":

                break;
            case "reader":

                break;
            case "none":

                break;
            default:
        }
    }

    private void loadAdmin(){
        rolePermissions.add("log in");
        // plates
        rolePermissions.add("create plate");
        rolePermissions.add("edit plate");
        rolePermissions.add("remove plate");
        rolePermissions.add("manage images of a plate");
        rolePermissions.add("rate plate");
        rolePermissions.add("manage favorite dishes");
        //restaurants
        rolePermissions.add("create restaurant");
        rolePermissions.add("edit restaurant");
        rolePermissions.add("remove restaurant");
        rolePermissions.add("manage list of unpublished restaurants");
        rolePermissions.add("publish restaurant");
        rolePermissions.add("manage images of a restaurant");
        rolePermissions.add("rate restaurant");
        rolePermissions.add("remove restaurant");
        rolePermissions.add("manage restaurant menu");
        rolePermissions.add("manage users");
    }

    private void loadCollaborator(){
        rolePermissions.add("log in");
        // plates
        rolePermissions.add("rate plate");
        rolePermissions.add("manage favorite dishes");
        //restaurants
        rolePermissions.add("create restaurant");
        rolePermissions.add("edit restaurant");
        rolePermissions.add("remove restaurant");
        rolePermissions.add("manage list of unpublished restaurants");
        rolePermissions.add("publish restaurant");
        rolePermissions.add("manage images of a restaurant");
        rolePermissions.add("rate restaurant");
        rolePermissions.add("remove restaurant");
        rolePermissions.add("manage restaurant menu");
        rolePermissions.add("manage users");
    }

}
