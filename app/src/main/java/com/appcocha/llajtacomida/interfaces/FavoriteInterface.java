package com.appcocha.llajtacomida.interfaces;

import java.util.ArrayList;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces para el modelo, vista y presentador de platos y restaurantes favoritos
 */
public interface FavoriteInterface {

    /**
     * Vista
     */
    interface ViewFavorite{

        /**
         * Muestra la lista de objetos favoritos (platos o restaurantes)
         * @param objectsList
         */
        public void showFavoriteList(ArrayList<Object> objectsList); // Carga la lista de los objetos favoritos del usuario

        /**
         * Muestra el estado del botón de "favorito" dentro de la vista de un determinado objeto
         * @param isFavorite
         */
        public void showFavoriteIcon(boolean isFavorite); // pinta el ícono dependiendo de si es ono favorito

        /**
         * Muestra ek resultado del proceso de agregar / quitar de favoritos
         * @param isSuccess
         */
        public void successFul(boolean isSuccess); // Devuelve true si se guarda con éxito y false si se presenta algún inconveniente
    }

    /**
     * Presentador
     */
    interface PresenterFavorite{

        /**
         * Determina mostrar la lista de objetos favoritos (platos o restaurantes)
         * @param objectsList
         */
        public void showFavoriteList(ArrayList<Object> objectsList);

        /**
         * Determinda mostrar el estado del boton de favoritos
         * @param isFavorite
         */
        public void showFavoriteObjectState(boolean isFavorite);

        /**
         * Determinda buscar si un objeto es o no favorito
         * @param objectId
         */
        public void searchFavoriteObject(String objectId);

        /**
         * Determina buscar la lista de objetos favoritos (platos o restaurantes)
         */
        public void searchObjectFavoriteList();

        /**
         * Determinda detener la BD
         */
        public void stopRealtimeDatabase();

        /**
         * Determinda guardar un objeto como favorito
         * @param objectId
         */
        public void saveObjectFavorite(String objectId); // agrega en favoritos

        /**
         * determina eliminar un idObjeto de favoritos
         * @param objectId
         */
        public void removeObjectFavorite(String objectId); // Elimina de favoritos


        /**
         * Determina mostrar la finalización del proceso
         * @param isSuccess
         */
        public void successFul(boolean isSuccess);
    }

    /**
     * modelId es el modelo al que se hace referencia (Plate Restaurant, etc)
     * model es el tipo de modelo (plate, restaurant)
     */
    interface ModelFavorite{

        /**
         * Busca un objeto favorito en la DB
         * @param objectId
         */
        public void searchFavoriteObject(String objectId);

        /**
         * Busca una lista de objetos favoritos (platos o restaurantes)
         */
        public void searchObjectFavoriteList();

        /**
         * Detiene la BD
         */
        public void stopRealtimeDatabase();

        /**
         * Guarda el id de un objeto en la lista de favoritos (platos o restaurantes)
         * @param objectId
         */
        public void saveObjectFavorite(String objectId); // agrega en favoritos

        /**
         * Elimina un objetoId de la lista de favoritos
         * @param objectId
         */
        public void removeObjectFavorite(String objectId); // Elimina de favoritos

        /**
         * Verifica el estado del proceso
         * @param isSuccess
         */
        public void successFul(boolean isSuccess);
    }

}
