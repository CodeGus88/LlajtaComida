package com.example.llajtacomida.presenters.user;

import com.example.llajtacomida.models.user.User;

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
