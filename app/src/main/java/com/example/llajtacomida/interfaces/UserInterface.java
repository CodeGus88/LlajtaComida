package com.example.llajtacomida.interfaces;

//import android.content.Context; // CONTEXTO

import com.example.llajtacomida.models.user.User;

import java.util.ArrayList;

public interface UserInterface {

    interface ViewUser {
        void showUser(User user);
        void showUserList(ArrayList<User> userList);
    }

    interface PresenterUser {
        void showUser(User user);
        void findUser(String id);
        void storeUser(User user);
        void stopRealtimeDatabase();
        void showUserList(ArrayList<User> userList);
        void loadUserList();
    }

    interface ModelUser {
        void findUser(String id);
        void storeUser(User user);
        void stopRealtimeDatabase();
        void loadUserList();
    }
}
