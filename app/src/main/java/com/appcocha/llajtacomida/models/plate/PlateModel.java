package com.appcocha.llajtacomida.models.plate;

import androidx.annotation.NonNull;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * Modelo, leer plato(s)
 * Implementa PlateInterface.ModelPlate
 */
public class PlateModel implements ValueEventListener, PlateInterface.ModelPlate {

    private PlateInterface.PresenterPlate presenterPlate;
    private DatabaseReference databaseReference;

    /**
     * Inicializa el presentador y la base de datos
     * @param presenterPlate
     */
    public PlateModel(PlateInterface.PresenterPlate presenterPlate){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.presenterPlate = presenterPlate;
    }

    @Override
    public void searchPlate(String plateId) {
        databaseReference.child("App").child("plates").child(plateId).addValueEventListener(this);
    }

    @Override
    public void loadPlatesList() {
        databaseReference.child("App").child("plates").orderByChild("name").addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/plates")){
            ArrayList<Plate> plateList = new ArrayList<Plate>();
            for (DataSnapshot plate: snapshot.getChildren()) {
                plateList.add(plate.getValue(Plate.class));
            }
            presenterPlate.showPlateList(plateList);
        }else{ //  entonces es un objeto plato
            Plate plate = snapshot.getValue(Plate.class);
            presenterPlate.showPlate(plate);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
