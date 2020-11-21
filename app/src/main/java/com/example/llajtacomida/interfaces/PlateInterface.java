package com.example.llajtacomida.interfaces;

import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.Restaurant;

import java.util.ArrayList;

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
}
