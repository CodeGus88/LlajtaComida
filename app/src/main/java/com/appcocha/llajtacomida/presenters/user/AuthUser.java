package com.appcocha.llajtacomida.presenters.user;

import com.appcocha.llajtacomida.models.user.User;

public class AuthUser {

    public static User user;

    public AuthUser(User user){
        this.user = user;
    }

    public static void setUser(User userX){ user = userX;}

    public static User getUser(){
        return user;
    }

}
