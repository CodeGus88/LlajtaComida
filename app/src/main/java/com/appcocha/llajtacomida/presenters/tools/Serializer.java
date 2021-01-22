package com.appcocha.llajtacomida.presenters.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Esta clase guarda el estado del volumen
 */
public class Serializer {

    /**
     * Guarda el estado del volumen con el nombre NAME
     * @param context
     * @param NAME
     * @param activate
     */
    public static void saveData(Context context, final String NAME, boolean activate){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NAME, activate);
        editor.commit();
    }

    /**
     * Lee el archivo NAME serializado
     * @param context
     * @param NAME
     * @return
     */
    public static boolean readData(Context context, final String NAME){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NAME, true);
    }
}
