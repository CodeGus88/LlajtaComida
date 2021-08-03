package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.model.plate.Plate;

import java.util.ArrayList;

/**
 * Las interfaces que se muestran a continuación buscan un elemeto mediante su nombre
 */
public interface SearchPlate {

    interface ViewPlateFound{
        void showPlateList(ArrayList<Plate> plateList);
    }

    interface PresenterPlateFound{
        void showPlateList(ArrayList<Plate> plateList);
        void searchPlateName(String name);
        void stopRealtimeDatabase();
    }

    interface ModelFindPlate{
        void searchPlateName(String name);
        void stopRealtimeDatabase();
    }

}
