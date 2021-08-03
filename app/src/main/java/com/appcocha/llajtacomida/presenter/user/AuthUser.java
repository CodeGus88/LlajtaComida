package com.appcocha.llajtacomida.presenter.user;

import android.content.Context;

import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.tools.Serializer;

/**
 * Es el usuario (se lo utiliza para verificar los permisos)
 */
public class AuthUser {

    public static User user;
    public static final String USER_FILE_NAME = "USER_AUTH_FILE";

    /**
     * Constructor, establece user (escribe en la base de datos)
     * @param user
     * @param context
     */
    public AuthUser(Context context, User user){
//        this.user = user;
        this.user = new User();
        this.user.setAvatarUrl(user.getAvatarUrl());
        this.user.setEmail(user.getEmail());
        this.user.setFulName(user.getFulName());
        this.user.setId(user.getId());
        this.user.setRole(user.getRole());
        Serializer.saveUserData(context, USER_FILE_NAME, this.user);
    }

    /**
     * Constructor, establece user
     * @param user
     */
    public AuthUser(User user){
//        this.user = user;
        this.user = new User();
        this.user.setId(user.getId());
        this.user.setAvatarUrl(user.getAvatarUrl());
        this.user.setEmail(user.getEmail());
        this.user.setFulName(user.getFulName());
        this.user.setRole(user.getRole());
    }

    /**
     * Etablece user
     * @param userX
     */
    public static void setUser(Context context, User userX){
//        this.user = userX;
        user = new User();
        user.setId(userX.getId());
        user.setAvatarUrl(userX.getAvatarUrl());
        user.setEmail(userX.getEmail());
        user.setFulName(userX.getFulName());
        user.setRole(userX.getRole());
        Serializer.saveUserData(context, USER_FILE_NAME, user);
    }

    /**
     * Etablece user
     * @param userX
     */
    public static void setUser(User userX){
        if(userX != null){
            user = new User();
            user.setAvatarUrl(userX.getAvatarUrl());
            user.setEmail(userX.getEmail());
            user.setFulName(userX.getFulName());
            user.setId(userX.getId());
            user.setRole(userX.getRole());
        }else user = null;
    }

    /**
     * Obtiene el user
     * @return user
     */
    public static User getUser(){
        return user;
    }

    /**
     * Obtiene el user
     * @return user
     */
    public static User getUser(Context context){
        if(user == null) user = Serializer.readUserData(context, USER_FILE_NAME);
        return user;
    }

}
