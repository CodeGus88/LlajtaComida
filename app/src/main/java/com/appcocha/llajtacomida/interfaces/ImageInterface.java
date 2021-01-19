package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.models.image.Image;

import java.util.ArrayList;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces modelo, vista y presentador de las imágernes
 */
public interface ImageInterface {

    /**
     * Vista
     */
    interface ViewImage{

        /**
         * Muestra la lista de imágenes
         * @param imagesList
         */
        public void showImages(ArrayList<Image> imagesList);
    }

    /**
     * Presentador
     */
    interface PresenterImage{

        /**
         * Determina mostrar la lista de imágenes
         * @param imageList
         */
        public void showImages(ArrayList<Image> imageList);

        /**
         * Determina buscar una lista de imágenes
         * @param model
         * @param modelId
         */
        public void searchImages(String model, String modelId);

        /**
         * Determina detener la BD
         */
        public void stopRealtimeDatabase();
    }

    /**
     * Modelo
     */
    interface ModelImage{

        /**
         * Busca la lista de imágenes
         * @param nodeCollectionName
         * @param modelId
         */
        public void searchImages(String nodeCollectionName, String modelId);

        /**
         * Detiene la BD
         */
        public void stopRealtimeDatabse();
    }
}
