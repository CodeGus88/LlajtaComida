package com.example.llajtacomida.interfaces;

//import android.content.Context; // CONTEXTO

import com.example.llajtacomida.models.user.User;

public interface UserInterface {

    interface ViewUser {
        void showUser(User user);
    }

    interface PresenterUser {
        void showUser(User user);
        void findUser(String id);
        void storeUser(User user);
        void stopRealtimeDatabase();
    }

    interface ModelUser {
        void findUser(String id);
        void storeUser(User user);
        void stopRealtimeDatabase();
    }
}
