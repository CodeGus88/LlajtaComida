package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public interface PlateInterface {

    /**
     * Las siguientes intefaces
     * Para la vista de un restaurante
     */
    interface ViewPlate{
        void showPlate(Plate plate);
        void showPlateList(ArrayList<Plate> plateList);
    }

    interface PresenterPlate{
        void showPlate(Plate plate);
        void searchPlate(String plateId);
        void loadPlatesList();
        void showPlateList(ArrayList<Plate> plateList);
        void stopRealtimeDatabase();
    }

    interface ModelPlate{
        void searchPlate(String plateId);
        void loadPlatesList();
        void stopRealtimeDatabase();
    }


    /**
     * las siguientes interfaces
     * Para la lista de restaurantes de un plato
     */
    interface ViewRestlist {
        void showRestList(ArrayList<Restaurant> list);
    }

    interface presenterRestList {
        void showRestList(ArrayList<Restaurant> list);
        void filterRestaurantListWithPlate(String plateId);
        void stopRealtimeDatabase();
    }

    interface ModelRestList {
        void filterRestaurantListWithPlate(String plateId);
        void stopRealtimeDatabase();
    }


    /**
     * Para almacenar editar, registrar y eliminar plato
     */

    interface ViewPlateManager{
        void isSuccess(boolean isSuccess);
//        void report(intmessage);
        void report(ArrayList<Integer> errors);
    }

    interface PresenterPlateManager{
        void isSuccess(boolean isSuccess);
        void delete(String plateId);
        void store(Plate plate, final byte [] thumb_byte);
        void update(Plate plate, final byte [] thumb_byte);
    }

    interface ModelPlateManager{
        void delete(String plateId);
        void store(Plate plate, final byte [] thumb_byte);
        void update(Plate plate, final byte [] thumb_byte);
    }
}
