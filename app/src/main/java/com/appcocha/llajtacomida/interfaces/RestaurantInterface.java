package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;

import java.util.ArrayList;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces modelo, vista y presentador de los restaurantes
 */
public interface RestaurantInterface {

    /**
     * Vista
     */
    interface ViewRestaurant{

        /**
         * Muestra un restaurante
         * @param restaurant
         */
        void showRestaurant(Restaurant restaurant);

        /**
         * Muestra la lista de restaurantes
         * @param restaurantList
         */
        void showRestaurantList(ArrayList<Restaurant> restaurantList);
    }

    /**
     * Presentador
     */
    interface PresenterRestaurant{

        /**
         * Dtermina mostrar un restaurante
         * @param restaurant
         */
        void showRestaurant(Restaurant restaurant);

        /**
         * Determina mostrar la lista de restaurantes
         * @param restaurantList
         */
        void showRestaurantList(ArrayList<Restaurant> restaurantList);

        /**
         * Determina buscar un restaurante
         * @param restaurantId
         */
        void searchRestaurant(String restaurantId);

        /**
         * Determina cargar la lista de restaurantes
         */
        void loadRestaurantList();

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabse();
    }

    /**
     * Modelo
     */
    interface ModelRestaurant{

        /**
         * Busca un restaurante en la BD
         * @param restaurantId
         */
        void searchRestaurant(String restaurantId);

        /**
         * Carga la lista de restaurantes de la BD
         */
        void loadRestaurantList();

        /**
         * Detiene la BD
         */
        void stopRealtimeDatabse();
    }


    // Las siguientes interfaces
    // Para la lista de platos del menu de un restaurante

    /**
     * Vista
     */
    interface ViewPlateList {

        /**
         * Muestra la lista de platos en el menú de un restaurante
         * @param list
         */
        void showPlateList(ArrayList<Plate> list, ArrayList<String> menuPrice);
    }

    /**
     * Presentador
     */
    interface PresenterPlateList {

        /**
         * Determina mostrar la lista de platos
         * @param list
         */
        void showPlateList(ArrayList<Plate> list, ArrayList<String> priceList);

        /**
         * Determina filtrar la lista de platos según el menu de un restaurante
         * @param restaurantId
         */
        void filterPlateListInMenu(String restaurantId);

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabse();
    }

    /**
     * Modelo
     */
    interface ModelPlateList {

        /**
         * Filtra la lista de platos que contiene el menú de un restaurante de la BD
         * @param restaurantId
         */
        void filterPlateListInMenu(String restaurantId);

        /**
         * Detiene la base de datos
         */
        void stopRealtimeDatabse();
    }


     // Las siguientes interfaces son para el set del menu
     // Para obtener la lista de todos los platos

    /**
     * Vista
     */
    interface ViewSetMenuList{

        /**
         * Muestra la lista de platos (para modificar el menú de un restaurante)
         * @param plateList
         * @param menu
         */
        void showSetMenuList(ArrayList<Plate> plateList, Menu menu);
    }

    /**
     * Presentador
     */
    interface PresenterSetMenuList{

        /**
         * Determina mostrar la lista de platos (con los que exisen en el menú de un restaurate)
         * @param plateList
         * @param menu
         */
        void showSetMenuList(ArrayList<Plate> plateList, Menu menu);

        /**
         * Determina buscar la lista de platos en el menú de un restaurante
         * @param restaurantId
         */
        void searchSetMenuList(String restaurantId);

        /**
         * Determina guardar la lista de paltos del menú de un restaurante
         * @param restaurantId
         * @param menu
         */
        void saveMenuList(String restaurantId, Menu menu); //Context context,

        /**
         * Determina detener la BD
         */
        void stopRealTimeDatabase();
    }

    /**
     * Modelo
     */
    interface  ModelSetMenuList{

        /**
         * Genera una lista de platos y una lista de id' de platos
         * @param restaurantId
         */
        void searchSetMenuList(String restaurantId);

        /**
         * Guarda el menú de platos de un restaurante
         * @param restaurantId
         * @param menu
         */
        void saveMenuList(String restaurantId, Menu menu); //Context context,

        /**
         * Detiene la BD
         */
        void stopRealTimeDatabase();
    }




    // Las siguientes interfaces son para el set del promoción
    // Para obtener la lista de todos los platos

    /**
     * Vista
     */
    interface ViewSetPromotionList{

        /**
         * Muestra la lista de platos (para modificar el menú de un restaurante)
         * @param plateList
         * @param promotion
         */
        void showSetPromotionList(ArrayList<Plate> plateList, Menu menu, Promotion promotion);
    }

    /**
     * Presentador
     */
    interface PresenterSetPromotionList{

        /**
         * Determina mostrar la lista de platos (con los que exisen en el menú de un restaurate)
         * @param plateList
         * @param promotion
         */
        void showSetPromotionList(ArrayList<Plate> plateList, Menu menu, Promotion promotion);

        /**
         * Determina buscar la lista de platos en el menú de un restaurante
         * @param restaurantId
         */
        void searchSetPromotionList(String restaurantId);

        /**
         * Determina guardar la lista de paltos del menú de un restaurante
         * @param restaurantId
         * @param promotion
         */
        void savePromotionList(String restaurantId, Promotion promotion); //Context context,

        /**
         * Determina detener la BD
         */
        void stopRealTimeDatabase();
    }

    /**
     * Modelo
     */
    interface  ModelSetPromotionList{

        /**
         * Genera una lista de platos y una lista de id' de platos
         * @param restaurantId
         */
        void searchSetPromotionList(String restaurantId);

        /**
         * Guarda el menú de platos de un restaurante
         * @param restaurantId
         * @param promotion
         */
        void savePromotionList(String restaurantId, Promotion promotion); //Context context,

        /**
         * Detiene la BD
         */
        void stopRealTimeDatabase();
    }




     // Las siguinetes interfaces son para publicar o dejar de publicar un restaurante

    /**
     * Vista
     */
    interface ViewRestPublicOf{
        void showRestPublicOf(ArrayList<Restaurant> restaruantList);
    }

    /**
     * Presentador
     */
    interface PresenterRestPublicOf {

        /**
         * Determina mostrar la lista de restaurantes no publicados
         * @param restaruantList
         */
        void showRestPublicOf(ArrayList<Restaurant> restaruantList);

        /**
         * Determina filtrar la lista de restaurantes no publicados
         * @param isPublic
         */
        void filterRestPublicOf(boolean isPublic);

        /**
         * Determina detener la base de datos
         */
        void stopRealTimeDatabase();
    }

    /**
     * Modelo
     */
    interface ModelRestPublicOf {

        /**
         * Filtra en una lista los restaurantes no publicados
         * @param isPublic
         */
        void filterRestPublicOf(boolean isPublic);

        /**
         * Detiene la BD
         */
        void stopRealTimeDatabase();
    }



     // Para almacenar editar, registrar y eliminar restaurante

    /**
     * Vista
     */
    interface ViewRestaurantManager{

        /**
         * Muestra la conclusión del proceso
         * @param isSuccess
         */
        void isSuccess(boolean isSuccess);

        /**
         * Muestra el reporte en caso de fallo
         * @param errors
         */
        void report(ArrayList<Integer> errors);
    }

    /**
     * Presentador
     */
    interface PresenterRestaurantManager{

        /**
         * Determina mostrar la conclusión del proceso
         * @param isSuccess
         */
        void isSuccess(boolean isSuccess);

        /**
         * Determina eliminar un restaurnate
         * @param restaurantId
         */
        void delete(String restaurantId);

        /**
         * Determina almacenaar el registro de un restaurante en la BD
         * @param restaurant
         * @param thumb_byte
         */
        void store(Restaurant restaurant, final byte [] thumb_byte);

        /**
         * Determina actualizar un restaurante en la BD
         * @param restaurant
         * @param thumb_byte
         */
        void update(Restaurant restaurant, final byte [] thumb_byte);
    }

    /**
     * Modelo
     */
    interface ModelRestaurantManager{

        /**
         * Elimina un restaurante en la BD
         * @param restaurantId
         */
        void delete(String restaurantId);

        /**
         * Almacena un restaurante en la BD
         * @param restaurant
         * @param thumb_byte
         */
        void store(Restaurant restaurant, final byte [] thumb_byte);

        /**
         * Actualzia un plato en la BD
         * @param restaurant
         * @param thumb_byte
         */
        void update(Restaurant restaurant, final byte [] thumb_byte);
    }
}
