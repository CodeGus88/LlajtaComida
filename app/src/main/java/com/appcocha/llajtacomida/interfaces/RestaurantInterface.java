package com.appcocha.llajtacomida.interfaces;

import android.content.Context;

import com.appcocha.llajtacomida.models.restaurant.menu.Menu;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;

import java.util.ArrayList;

public interface RestaurantInterface {
    /**
     * Las siguientes intefaces
     * Para la vista de un restaurante
     */

    interface ViewRestaurant{
        void showRestaurant(Restaurant restaurant);
        void showRestaurantList(ArrayList<Restaurant> restaurantList);
    }

    interface PresenterRestaurant{
        void showRestaurant(Restaurant restaurant);
        void showRestaurantList(ArrayList<Restaurant> restaurantList);
        void searchRestaurant(String restaurantId);
        void loadRestaurantList();
        void stopRealtimeDatabse();
    }

    interface ModelRestaurant{
        void searchRestaurant(String restaurantId);
        void loadRestaurantList();
        void stopRealtimeDatabse();
    }


    /**
     * las siguientes interfaces
     * Para la lista de platos del menu de un restaurante
     */

    interface ViewPlateList {
        void showPlateList(ArrayList<Plate> list);
    }

    interface PresenterPlateList {
        void showPlateList(ArrayList<Plate> list);
        void filterPlateListInMenu(String restaurantId);
        void stopRealtimeDatabse();
    }

    interface ModelPlateList {
        void filterPlateListInMenu(String restaurantId);
        void stopRealtimeDatabse();
    }


    /**
     * las siguientes interfaces son para el set del menu
     * Para obtener la lista de todos los platos
     */

    interface ViewSetMenuList{
        void showSetMenuList(ArrayList<Plate> plateList, Menu menu);
    }

    interface PresenterSetMenuList{
        void showSetMenuList(ArrayList<Plate> plateList, Menu menu);
        void searchSetMenuList(String restaurantId);
        void saveMenuList(String restaurantId, Menu menu); //Context context,
        void stopRealTimeDatabase();
    }

    interface  ModelSetMenuList{
        void searchSetMenuList(String restaurantId);
        void saveMenuList(String restaurantId, Menu menu); //Context context,
        void stopRealTimeDatabase();
    }


    /**
     * Las siguinetes interfaces son para publicar o dejar de publicar un restaurante
     */

    interface ViewRestPublicOf{
        void showRestPublicOf(ArrayList<Restaurant> restaruantList);
    }

    interface PresenterRestPublicOf {
        void showRestPublicOf(ArrayList<Restaurant> restaruantList);
        void filterRestPublicOf(boolean isPublic);
        void stopRealTimeDatabase();
    }

    interface ModelRestPublicOf {
        void filterRestPublicOf(boolean isPublic);
        void stopRealTimeDatabase();
    }



    /**
     * Para almacenar editar, registrar y eliminar restaurante
     */

    interface ViewRestaurantManager{
        void isSuccess(boolean isSuccess);
//        void report(int message);
        void report(ArrayList<Integer> errors);
    }

    interface PresenterRestaurantManager{
        void isSuccess(boolean isSuccess);
        void delete(String restaurantId);
        void store(Restaurant restaurant, final byte [] thumb_byte);
        void update(Restaurant restaurant, final byte [] thumb_byte);
    }

    interface ModelRestaurantManager{
        void delete(String restaurantId);
        void store(Restaurant restaurant, final byte [] thumb_byte);
        void update(Restaurant restaurant, final byte [] thumb_byte);
    }
}