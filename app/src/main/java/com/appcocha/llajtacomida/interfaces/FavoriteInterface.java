package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.models.image.Image;

import java.util.ArrayList;

public interface FavoriteInterface {

    interface ViewFavorite{
        public void showFavoriteList(ArrayList<Object> objectsList); // Carga la lista de los objetos favoritos del usuario
        public void showFavoriteIcon(boolean isFavorite); // pinta el ícono dependiendo de si es ono favorito
        public void successFul(boolean isSuccess);
    }

    interface PresenterFavorite{
        //        public PresenterFavorite(ViewFavorite viewFavorite, String nodeCollectionName); // El constructor debe pedir los argumentos
        public void showFavoriteList(ArrayList<Object> objectsList);
        public void showFavoriteObjectState(boolean isFavorite); // Si esnulo, no esta en favoritos, si tiene contenido si
        public void searchFavoriteObject(String objectId);
        public void searchObjectFavoriteList();
        public void stopRealtimeDatabase();
        public void saveObjectFavorite(String objectId); // agrega en favoritos
        public void removeObjectFavorite(String objectId); // Elimina de favoritos
        public void successFul(boolean isSuccess);

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
        public void saveObjectFavorite(String objectId); // agrega en favoritos
        public void removeObjectFavorite(String objectId); // Elimina de favoritos
        public void successFul(boolean isSuccess);
    }

}
