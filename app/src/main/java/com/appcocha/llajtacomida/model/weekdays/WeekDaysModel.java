package com.appcocha.llajtacomida.model.weekdays;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.SearchPlate;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WeekDaysModel implements SearchPlate.ModelFindPlate, ValueEventListener {

    private SearchPlate.PresenterPlateFound presenterPlateFound;
    private DatabaseReference databaseReference;

    public WeekDaysModel(SearchPlate.PresenterPlateFound presenterPlateFound){
        this.presenterPlateFound = presenterPlateFound;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void searchPlateName(String name) {
        Query query = databaseReference.child("App").child("plates").orderByChild("name").equalTo(name);
        query.addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Plate> plateList = new ArrayList<Plate>();
        for(DataSnapshot plate: snapshot.getChildren()){
            plateList.add(plate.getValue(Plate.class));
        }
        presenterPlateFound.showPlateList(plateList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
