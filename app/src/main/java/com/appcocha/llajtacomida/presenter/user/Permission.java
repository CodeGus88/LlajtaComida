package com.appcocha.llajtacomida.presenter.user;
import java.util.ArrayList;

public class Permission {

    /**
     * ROLES DE USUARIO
     */
    public static final String ADMIN = "ADMIN";
    public static final String COLLABORATOR = "COLLABORATOR";
    public static final String VOTER = "VOTER";
    public static final String READER = "READER";
    public static final String NONE = "NONE";

    /**
     * 0 - 19
     * Permisos para plato
     */
    public static final byte SHOW_PLATE_LIST = 0;
    public static final byte UPDATE_PLATE = 1;
    public static final byte DELETE_PLATE = 2;
    public static final byte ADD_PLATE = 3;
    public static final byte SHOW_PLATE = 4;
    // imágenes de un plato
    public static final byte SHOW_PLATE_GALERY = 5;
    public static final byte WRITE_PLATE_GALERY = 6;

    /**
     * 20 - 49
     * Permisos para un restaurante
     */
    public static final byte SHOW_RESTAURANT_LIST = 20;
    public static final byte UPDATE_RESTAURANT = 21;
    public static final byte PUBLISH_ON_OF_RESTAURANT = 22;
    public static final byte DELETE_RESTAURANT = 23;
    public static final byte ADD_RESTAURANT = 24;
    public static final byte SHOW_RESTAURANT = 25;
    public static final byte SHOW_RESTAURANT_PUBLIC_OF_LIST_ = 26;
    // imágenes de un restaurante
    public static final byte SHOW_RESTAURANT_GALERY = 27;
    public static final byte WRITE_RESTAURANT_GALERY = 28;
    // menú de un restaurante
    public static final byte SHOW_SET_RESTAURANT_MENU = 29;
    public static final byte WRITE_RESTAURANT_MENU = 30;
    // promotion de un restaurante
    public static final byte SHOW_SET_RESTAURANT_PROMOTION = 31;
    public static final byte WRITE_RESTAURANT_PROMOTION = 32;

    /**
     * 50 - 59
     * Valoración
     */
    public static final byte VALORATE_RESTAURANT = 50;
    public static final byte DELETE_VALORATE_RESTAURANT = 51;
    public static final byte VALORATE_PLATE = 52;
    public static final byte DELETE_VALORATE_PLATE = 53;
    public static final byte ADD_PLATE_TO_FAVORITE_LIST = 54;
    public static final byte ADD_RESTAURANT_TO_FAVORITE_LIST = 55;

    /**
     * 60 - 100
     * Usuarios
     */
    public static final byte SHOW_USER_LIST = 60;
    public static final byte UPDATE_USER = 61;
    public static final byte UPDATE_USER_ROL = 62;

    /**
     * -50 - -1
     * Acces to App
     */
    public static final byte ACCES_TO_APP = -50;

    /**
     * Con argumento de es autor
     * @param role
     * @param requestPermision
     * @param isAuthor
     * @return authorize
     */
    public static boolean getAuthorize(String role, byte requestPermision, boolean isAuthor){
        boolean authorize = false;
        if(role.equalsIgnoreCase(Permission.ADMIN))
            authorize = getAdminPermisions(isAuthor).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.COLLABORATOR))
            authorize = getCollaboratorPermisions(isAuthor).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.VOTER))
            authorize = getVoterPermisions(isAuthor).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.READER))
            authorize = getReaderPermisions(isAuthor).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.NONE))
            authorize = getNonePermisions(isAuthor).contains(requestPermision);
        return authorize;
    }


    /**
     * Para consultas que no requieren de comprobación de autoría
     * @param role
     * @param requestPermision
     * @return authorize
     */
    public static boolean getAuthorize(String role, byte requestPermision){
        boolean authorize = false;
        if(role.equalsIgnoreCase(Permission.ADMIN))
            authorize = getAdminPermisions(false).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.COLLABORATOR))
            authorize = getCollaboratorPermisions(false).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.VOTER))
            authorize = getVoterPermisions(false).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.READER))
            authorize = getReaderPermisions(false).contains(requestPermision);
        else if(role.equalsIgnoreCase(Permission.NONE))
            authorize = getNonePermisions(false).contains(requestPermision);
        return authorize;
    }

    /**
     * Retorna los permisos del administrador
     * @return permisions
     */
    public static ArrayList<Byte> getAdminPermisions(boolean isAuthor){
        ArrayList<Byte> permisions = new ArrayList<Byte>();
        permisions.add(Permission.SHOW_PLATE_LIST);
        permisions.add(Permission.UPDATE_PLATE);
        permisions.add(Permission.DELETE_PLATE);
        permisions.add(Permission.ADD_PLATE);
        permisions.add(Permission.SHOW_PLATE);
        permisions.add(Permission.WRITE_PLATE_GALERY);
        permisions.add(Permission.SHOW_RESTAURANT_LIST);
        permisions.add(Permission.UPDATE_RESTAURANT);
        permisions.add(Permission.PUBLISH_ON_OF_RESTAURANT);
        permisions.add(Permission.DELETE_RESTAURANT);
        permisions.add(Permission.ADD_RESTAURANT);
        permisions.add(Permission.SHOW_RESTAURANT);
        permisions.add(Permission.SHOW_RESTAURANT_PUBLIC_OF_LIST_);
        permisions.add(Permission.WRITE_RESTAURANT_MENU);
        permisions.add(Permission.WRITE_RESTAURANT_PROMOTION);
        permisions.add(Permission.VALORATE_RESTAURANT);
        permisions.add(Permission.VALORATE_PLATE);
        permisions.add(Permission.DELETE_VALORATE_RESTAURANT);
        permisions.add(Permission.DELETE_VALORATE_PLATE);
        permisions.add(Permission.ADD_PLATE_TO_FAVORITE_LIST);
        permisions.add(Permission.ADD_RESTAURANT_TO_FAVORITE_LIST);
        permisions.add(Permission.SHOW_USER_LIST);
        permisions.add(Permission.UPDATE_USER);
        permisions.add(Permission.SHOW_PLATE_GALERY);
        permisions.add(Permission.SHOW_RESTAURANT_GALERY);
        permisions.add(Permission.SHOW_SET_RESTAURANT_MENU);
        permisions.add(Permission.SHOW_SET_RESTAURANT_PROMOTION);
        permisions.add(Permission.ACCES_TO_APP);
        permisions.add(Permission.UPDATE_USER_ROL);
        return permisions;
    }

    /**
     * Retorna los permisos del colaborador
     * @return permisions
     */
    public static ArrayList<Byte> getCollaboratorPermisions(boolean isAuthor){
        ArrayList<Byte> permisions = new ArrayList<Byte>();
        permisions.add(Permission.SHOW_PLATE_LIST);
        permisions.add(Permission.SHOW_PLATE);
        permisions.add(Permission.SHOW_RESTAURANT_LIST);
        if (isAuthor) permisions.add(Permission.UPDATE_RESTAURANT);
        if (isAuthor) permisions.add(Permission.DELETE_RESTAURANT);
        permisions.add(Permission.ADD_RESTAURANT);
        permisions.add(Permission.SHOW_RESTAURANT);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_GALERY);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_MENU);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_PROMOTION);
        permisions.add(Permission.VALORATE_RESTAURANT);
        permisions.add(Permission.VALORATE_PLATE);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_RESTAURANT);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_PLATE);
        permisions.add(Permission.ADD_PLATE_TO_FAVORITE_LIST);
        permisions.add(Permission.ADD_RESTAURANT_TO_FAVORITE_LIST);
        if(isAuthor) permisions.add(Permission.UPDATE_USER);
        if(isAuthor) permisions.add(Permission.SHOW_RESTAURANT_GALERY);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_MENU);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_PROMOTION);
        permisions.add(Permission.ACCES_TO_APP);
        return permisions;
    }

    /**
     * Retorna los permisos del votante
     * @return permisions
     */
    public static ArrayList<Byte> getVoterPermisions(boolean isAuthor){
        ArrayList<Byte> permisions = new ArrayList<Byte>();
        permisions.add(Permission.SHOW_PLATE_LIST);
        permisions.add(Permission.SHOW_PLATE);
        permisions.add(Permission.SHOW_RESTAURANT_LIST);
        if (isAuthor) permisions.add(Permission.UPDATE_RESTAURANT);
        if (isAuthor) permisions.add(Permission.DELETE_RESTAURANT);
        permisions.add(Permission.SHOW_RESTAURANT);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_GALERY);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_MENU);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_PROMOTION);
        permisions.add(Permission.VALORATE_RESTAURANT);
        permisions.add(Permission.VALORATE_PLATE);
        permisions.add(Permission.ADD_PLATE_TO_FAVORITE_LIST);
        permisions.add(Permission.ADD_RESTAURANT_TO_FAVORITE_LIST);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_RESTAURANT);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_PLATE);
        if(isAuthor) permisions.add(Permission.SHOW_RESTAURANT_GALERY);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_MENU);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_PROMOTION);
        permisions.add(Permission.ACCES_TO_APP);
        return permisions;
    }

    /**
     * Retorna los permisos del lector
     * @return permisions
     */
    public static ArrayList<Byte> getReaderPermisions(boolean isAuthor){
        ArrayList<Byte> permisions = new ArrayList<Byte>();
        permisions.add(Permission.SHOW_PLATE_LIST);
        permisions.add(Permission.SHOW_PLATE);
        permisions.add(Permission.SHOW_RESTAURANT_LIST);
        if (isAuthor) permisions.add(Permission.UPDATE_RESTAURANT);
        if (isAuthor) permisions.add(Permission.DELETE_RESTAURANT);
        permisions.add(Permission.SHOW_RESTAURANT);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_GALERY);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_MENU);
        if (isAuthor) permisions.add(Permission.WRITE_RESTAURANT_PROMOTION);
        permisions.add(Permission.ADD_PLATE_TO_FAVORITE_LIST);
        permisions.add(Permission.ADD_RESTAURANT_TO_FAVORITE_LIST);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_RESTAURANT);
        if(isAuthor) permisions.add(Permission.DELETE_VALORATE_PLATE);
        if(isAuthor) permisions.add(Permission.SHOW_RESTAURANT_GALERY);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_MENU);
        if(isAuthor) permisions.add(Permission.SHOW_SET_RESTAURANT_PROMOTION);
        permisions.add(Permission.ACCES_TO_APP);
        return permisions;
    }

    /**
     * Retorna los permisos del usuario bloqueado (ninguno)
     * @return permisions
     */
    public static ArrayList<Byte> getNonePermisions(boolean isAutor){
        ArrayList<Byte> permisions = new ArrayList<Byte>();
        return permisions;
    }

}
