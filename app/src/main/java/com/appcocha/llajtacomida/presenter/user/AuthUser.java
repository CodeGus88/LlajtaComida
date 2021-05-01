package com.appcocha.llajtacomida.presenter.user;

import com.appcocha.llajtacomida.model.user.User;

/**
 * Es el usuario (se lo utiliza para verificar los permisos)
 */
public class AuthUser {

    public static User user;

    /**
     * Constructor, establece user
     * @param user
     */
    public AuthUser(User user){
        this.user = user;
    }

    /**
     * Etablece user
     * @param userX
     */
    public static void setUser(User userX){ user = userX;}

    /**
     * Obtiene el user
     * @return
     */
    public static User getUser(){
        return user;
    }

}
