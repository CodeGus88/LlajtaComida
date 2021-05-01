package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.model.user.User;

import java.util.ArrayList;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces modelo, vista y presentador de usuarios
 */
public interface UserInterface {

    /**
     * Vista
     */
    interface ViewUser {

        /**
         * Muestra a un usuario
         * @param user
         */
        void showUser(User user);

        /**
         * Muestra la lista de usuarios de la app
         * @param userList
         */
        void showUserList(ArrayList<User> userList);
    }

    /**
     * Presentador
     */
    interface PresenterUser {

        /**
         * Determina mostrar un usuario
         * @param user
         */
        void showUser(User user);

        /**
         * Determina buscar un usuario
         * @param id
         */
        void findUser(String id);

        /**
         * Determina guardar un usuario
         * @param user
         */
        void storeUser(User user);

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabase();

        /**
         * Determina mostrar la lista de usuarios
         * @param userList
         */
        void showUserList(ArrayList<User> userList);

        /**
         * Determina cargar la lista de usuarios
         */
        void loadUserList();
    }


    /**
     * Modelo
     */
    interface ModelUser {

        /**
         * Busca un usuario en la BD
         * @param id
         */
        void findUser(String id);

        /**
         * Almacena un usuario en la BD
         * @param user
         */
        void storeUser(User user);

        /**
         * Detiene la BD
         */
        void stopRealtimeDatabase();

        /**
         * Carga la lista de usuarios de la App
         */
        void loadUserList();
    }

     // Las siguientes interfaces son para tener actualizados los datos de un usuario (base de datos en timepo real)
     // (Uso de la presentacion de los ppermisos del usuario)

    /**
     * Vista
     */
    interface ViewUserRealTime{

        /**
         * Muestra el usuario (en tiempo real)
         * @param user
         */
        void showUserRT(User user);

        /**
         * Muestra un reporte
         * @param message
         */
        void showReport(String message);
    }

    /**
     * Presentador
     */
    interface PresenterUserRealTime{

        /**
         * Determina mostrar un usuario en tiempo real
         * @param user
         */
        void showUserRT(User user);

        /**
         * Determina buscar un usuario
         * @param userId
         */
        void findUser(String userId);

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabase();
    }

    /**
     * Modelo
     */
    interface ModelUserRealTime{

        /**
         * Busca al usuario en la BD
         * @param userId
         */
        void findUser(String userId);

        /**
         * Detiene la BD
         */
        void stopRealtimeDatabase();
    }
}
