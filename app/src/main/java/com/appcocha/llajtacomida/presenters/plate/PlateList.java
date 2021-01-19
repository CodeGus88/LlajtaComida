package com.appcocha.llajtacomida.presenters.plate;

import com.appcocha.llajtacomida.models.plate.Plate;
import java.util.ArrayList;

/**
 * Lista de platos (muestra la lista de platos en cualquier clase)
 */
public class PlateList {

    private static ArrayList<Plate> plateList;

    /**
     * Constructor, Establece la lista de platos
     * @param plateList
     */
    public  PlateList(ArrayList<Plate> plateList){
        if(plateList != null){
            this.plateList = plateList;
        }else{
            this.plateList = new ArrayList<Plate>();
        }
    }

    /**
     * Establece la lsiat de platos
     * @param plateListX
     */
    public static void setPlateList(ArrayList<Plate> plateListX){
        if(plateList != null){
            plateList.clear();
            plateList = plateListX;
        }else{
            plateList = new ArrayList<Plate>();
        }
    }

    /**
     * Obtiene la lista de platos
     * @return
     */
    public static ArrayList<Plate> getPlateList(){
        if(plateList == null){
            plateList = new ArrayList<Plate>();
        }
        return plateList;
    }
}
