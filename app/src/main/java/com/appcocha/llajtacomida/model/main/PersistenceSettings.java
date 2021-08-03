package com.appcocha.llajtacomida.model.main;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class PersistenceSettings {
    public PersistenceSettings(boolean persistence){
        // habilitar base de datos sin conexion
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(persistence);
            Log.d("setPersistenceEnabled", "------> true");
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }
}
