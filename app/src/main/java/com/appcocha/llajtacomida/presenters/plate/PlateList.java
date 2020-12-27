package com.appcocha.llajtacomida.presenters.plate;

import android.util.Log;

import com.appcocha.llajtacomida.models.plate.Plate;

import java.util.ArrayList;

public class PlateList {

    private static ArrayList<Plate> plateList;

    public  PlateList(ArrayList<Plate> plateList){
        if(plateList != null){
            this.plateList = plateList;
        }else{
            this.plateList = new ArrayList<Plate>();
        }
    }

    public static void setPlateList(ArrayList<Plate> plateListX){
        if(plateList != null){
            plateList.clear();
            plateList = plateListX;
        }else{
            plateList = new ArrayList<Plate>();
        }
    }

    public static ArrayList<Plate> getPlateList(){
        if(plateList == null){
            plateList = new ArrayList<Plate>();
        }
        return plateList;
    }
}
