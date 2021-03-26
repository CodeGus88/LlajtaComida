package com.appcocha.llajtacomida.presenters.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Esta clase guarda el estado del volumen y el estado de orden de las listas
 */
public class Serializer {

    /**
     * Guarda el estado del volumen con el nombre NAME
     * @param context
     * @param NAME
     * @param value
     */
    public static void saveBooleanData(Context context, final String NAME, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NAME, value);
        editor.commit();
    }

    /**
     * Lee el archivo NAME serializado
     * @param context
     * @param NAME
     * @return soundState
     */
    public static boolean readBooleanData(Context context, final String NAME){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NAME, true);
    }

    /**
     * Guarda el estado del orden de las listas con el nombre NAME
     * @param context
     * @param NAME
     * @param value
     */
    public static void saveStringData(Context context, final String NAME, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, value);
        editor.commit();
    }

    /**
     * Lee el archivo NAME serializado
     * @param context
     * @param NAME
     * @return orderListState
     */
    public static String readStringData(Context context, final String NAME){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME, "NAME");
    }

}
