package com.appcocha.llajtacomida.presenter.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appcocha.llajtacomida.model.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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



//    ////////////////////
    /**
     * Guarda el estado del orden de las listas con el nombre NAME
     * @param context
     * @param NAME
     * @param user
     */
    public static void saveUserData(Context context, final String NAME, final User user){
        // TODO: Convertir el objeto en json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(user);
        // TODO: Guardar el json en string
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, json); // guarda el string en forma de json
        editor.commit();
        Log.d("authUserSerializer", "Write: \n" + json);
    }

    /**
     * Lee el archivo NAME serializado
     * @param context
     * @param NAME
     * @return orderListState
     */
    public static User readUserData(Context context, final String NAME){
        // TODO: Leer el archivo NAME guardado
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, context.MODE_PRIVATE);
        String json = sharedPreferences.getString(NAME, "NAME");
        Log.d("authUserSerializer", "Read: \n" +json);
        // TODO: Convierte el json de string a objeto User
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }

}
