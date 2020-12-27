package com.appcocha.llajtacomida.interfaces;

//import android.content.Context; // CONTEXTO

import com.appcocha.llajtacomida.models.user.User;

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

    /**
     * Las siguientes interfaces son para tener actualizados los datos de un usuario (base de datos en timepo real)
     * (Uso de la presentacion de los ppermisos del usuario)
     */

    interface ViewUserRealTime{
        void showUserRT(User user);
        void showReport(String message);
    }

    interface PresenterUserRealTime{
        void showUserRT(User user);
        void findUser(String userId);
        void stopRealtimeDatabase();
    }

    interface ModelUserRealTime{
        void findUser(String userId);
        void stopRealtimeDatabase();
    }
}
