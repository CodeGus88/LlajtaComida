package com.example.llajtacomida.interfaces;

import com.example.llajtacomida.models.image.Image;

import java.util.ArrayList;

public interface FavoriteInterface {

    interface ViewFavorite{
        public void showFavoriteList(ArrayList<Object> objectsList); // Carga la lista de los objetos favoritos del usuario
        public void showFavoriteIcon(boolean isFavorite); // pinta el ícono dependiendo de si es ono favorito
    }

    interface PresenterFavorite{
        //        public PresenterFavorite(ViewFavorite viewFavorite, String nodeCollectionName); // El constructor debe pedir los argumentos
        public void showFavoriteList(ArrayList<Object> objectsList);
        public void showFavoriteIcon(String objectId); // Si esnulo, no esta en favoritos, si tiene contenido si
        public void searchFavoriteObject(String objectId);
        public void searchObjectFavoriteList();
        public void stopRealtimeDatabase();
    }

    /**
     * modelId es el modelo al que se hace referencia (Plate Restaurant, etc)
     * model es el tipo de modelo (plate, restaurant)
     */
    interface ModelFavorite{
        //        public ModelFavorite(PresenterFavorite presenterFavorite, String userId, String nodeCollectionName); // El constructor debe pedir los argumentos
        public void searchFavoriteObject(String objectId);
        public void searchObjectFavoriteList();
        public void stopRealtimeDatabase();
    }

}
