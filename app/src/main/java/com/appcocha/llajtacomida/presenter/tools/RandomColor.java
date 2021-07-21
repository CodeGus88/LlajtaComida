package com.appcocha.llajtacomida.presenter.tools;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RandomColor {

    private final ArrayList<Float> colorList, colorListCopy;

    /**
     * Esta clase proporciona dies colores al azar, sin repetir dentro del rango
     * Se usa para pintar los marcadores
     */
    public RandomColor(){
        colorList = new ArrayList<Float>();
        colorList.add(BitmapDescriptorFactory.HUE_AZURE);
        colorList.add(BitmapDescriptorFactory.HUE_BLUE);
        colorList.add(BitmapDescriptorFactory.HUE_CYAN);
        colorList.add(BitmapDescriptorFactory.HUE_GREEN);
        colorList.add(BitmapDescriptorFactory.HUE_MAGENTA);
        colorList.add(BitmapDescriptorFactory.HUE_ORANGE);
        colorList.add(BitmapDescriptorFactory.HUE_RED);
        colorList.add(BitmapDescriptorFactory.HUE_ROSE);
        colorList.add(BitmapDescriptorFactory.HUE_VIOLET);
        colorList.add(BitmapDescriptorFactory.HUE_YELLOW);
        colorListCopy = new ArrayList<Float>();
        colorListCopy.addAll(getColorList());
    }

    /**
     * Obtiene lista de colores
     * @return colorList
     */
    public ArrayList<Float> getColorList() {
        return colorList;
    }

    /**
     * Obtiene un color al azar
     * @return colorCode
     */
    public float getRandonColor(){
        int indexColor = ThreadLocalRandom.current().nextInt(0, colorListCopy.size());
        final float colorCode = colorListCopy.get(indexColor);
        colorListCopy.remove(indexColor);
        if(colorListCopy.isEmpty()){
            colorListCopy.addAll(colorList);
        }
        return colorCode;
    }
}
