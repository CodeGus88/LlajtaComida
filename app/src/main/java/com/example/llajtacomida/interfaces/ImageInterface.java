package com.example.llajtacomida.interfaces;

import com.example.llajtacomida.models.image.Image;

import java.util.ArrayList;

/**
 * Estas interaces muestran todas las imágenes de un elemento
 */
public interface ImageInterface {

    interface ViewImage{
        public void showImages(ArrayList<Image> imagesList);
    }

    interface PresenterImage{
        public void showImages(ArrayList<Image> imageList);
        public void searchImages(String model, String modelId);
        public void stopRealtimeDatabase();
    }

    /**
     * modelId es el modelo al que se hace referencia (Plate Restaurant, etc)
     * model es el tipo de modelo (plate, restaurant)
     */
    interface ModelImage{
        public void searchImages(String nodeCollectionName, String modelId);
        public void stopRealtimeDatabse();
    }
}
