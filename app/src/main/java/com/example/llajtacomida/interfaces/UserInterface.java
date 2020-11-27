package com.example.llajtacomida.interfaces;

//import android.content.Context; // CONTEXTO

import com.example.llajtacomida.models.user.User;

public interface UserInterface {

    interface ViewInterface{
        void showUser(User user);
    }

    interface PresenterInterface{
        void showUser(User user);
        void findUser(String id);
        void storeUser(User user);
    }

    interface ModelInterface{
        void findUser(String id);
        void storeUser(User user);
    }
}
