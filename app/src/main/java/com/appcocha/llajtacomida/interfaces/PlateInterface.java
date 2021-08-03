package com.appcocha.llajtacomida.interfaces;

import android.content.Context;

import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces modelo, vista y presentador de los platos
 */
public interface PlateInterface {

    /**
     * Vista
     * Las siguientes intefaces
     * Para la vista de un restaurante
     */
    interface ViewPlate{
        /**
         * Muestra un plato
         * @param plate
         */
        void showPlate(Plate plate);

        /**
         * Muestra la lista de platos
         * @param plateList
         */
        void showPlateList(ArrayList<Plate> plateList);
    }

    /**
     * Presentador
     */
    interface PresenterPlate{

        /**
         * Muestra el plato
         * @param plate
         */
        void showPlate(Plate plate);

        /**
         * determina buscar un plato
         * @param plateId
         */
        void searchPlate(String plateId);

        /**
         * Determina cargar la lista de platos
         */
        void loadPlatesList();

        /**
         * Determinda mostrar la lista de platos
         * @param plateList
         */
        void showPlateList(ArrayList<Plate> plateList);

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabase();
    }

    /**
     * Modelo
     */
    interface ModelPlate{

        /**
         * Busca el plato en Firebase
         * @param plateId
         */
        void searchPlate(String plateId);

        /**
         * Cargar la lista de platos de la BD
         */
        void loadPlatesList();

        /**
         * Detiene la base de datos en tiempo real de la BD
         */
        void stopRealtimeDatabase();
    }



    // Las siguientes interfaces sirven para mostrar una lista de restaurantes con un determinado plato en sus menús


    /**
     * Vista
     */
    interface ViewRestlist {

        /**
         * Muestra la lista de restaurantes con un determindo plato en sus menús
         * @param list
         */
//        void showRestList(ArrayList<Restaurant> list);
        void showRestList(ArrayList<Restaurant> list, Hashtable priceInRestaurantsList);
    }

    /**
     * Presentador
     */
    interface presenterRestList {

        /**
         * Determina mostrar la lista de restaurantes filtrada
         * @param list
         */
        void showRestList(ArrayList<Restaurant> list, Hashtable priceInRestaurantsList);

        /**
         * Determinda filtrar la lista de restaurantes con un determinado plato en sus menús
         * @param plateId
         */
        void filterRestaurantListWithPlate(String plateId);

        /**
         * Determina detener la base de datos en tiempo real
         */
        void stopRealtimeDatabase();
    }

    /**
     * Modelo
     */
    interface ModelRestList {

        /**
         * Filtra los restaurantes con  un determinado plato de la BD
         * @param plateId
         */
        void filterRestaurantListWithPlate(String plateId);

        /**
         * Detiene BD
         */
        void stopRealtimeDatabase();
    }


    // GESTOR DE PLATOS

    /**
     * Vista
     */
    interface ViewPlateManager{

        /**
         * Muestra el mensaje de la conclusión del proceso
         * @param isSuccess
         */
        void isSuccess(boolean isSuccess);

        /**
         * muestra el id del reporte de error
         * @param errors
         */
        void report(ArrayList<Integer> errors);
    }

    /**
     * Presentador
     */
    interface PresenterPlateManager{

        /**
         * Determinda mostrar el reporte de conclusión del proceso
         * @param isSuccess
         */
        void isSuccess(boolean isSuccess);

        /**
         * Determina eliminar un plato
         * @param plateId
         */
        void delete(String plateId);

        /**
         * Determinda almacenar un nuevo plato
         * @param plate
         * @param thumb_byte
         */
        void store(Plate plate, final byte [] thumb_byte, Context context);
//        void store(Plate plate, final byte [] thumb_byte);

        /**
         * Determinda actualizar un plato
         * @param plate
         * @param thumb_byte
         */
//        void update(Plate plate, final byte [] thumb_byte);
        void update(Plate plate, final byte [] thumb_byte, Context context);
    }

    interface ModelPlateManager{

        /**
         * Elimina un plato de la BD
         * @param plateId
         */
        void delete(String plateId);

        /**
         * Almacena un plato en la BD
         * @param plate
         * @param thumb_byte
         */
        void store(Plate plate, final byte [] thumb_byte);

        /**
         * Actualiza un plato de la BD
         * @param plate
         * @param thumb_byte
         */
        void update(Plate plate, final byte [] thumb_byte);
    }
}
