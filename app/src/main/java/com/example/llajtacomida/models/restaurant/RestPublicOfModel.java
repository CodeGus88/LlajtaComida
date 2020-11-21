package com.example.llajtacomida.models.restaurant;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestPublicOfModel implements RestaurantInterface.ModelRestPublicOf, ValueEventListener {
    private RestaurantInterface.PresenterRestPublicOf presenterPublicOf;
    private DatabaseReference databaseReference;
    private ArrayList<Restaurant> restaurantList;

    public RestPublicOfModel(RestaurantInterface.PresenterRestPublicOf presenterPublicOf){
        this.presenterPublicOf = presenterPublicOf;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        restaurantList = new ArrayList<Restaurant>();
    }

    @Override
    public void filterRestPublicOf(boolean isPublic) {
        Query query = databaseReference.child("App").child("restaurants").orderByChild("public").equalTo(isPublic);
        query.addValueEventListener(this);
    }

    @Override
    public void stopRealTimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        restaurantList.clear();
        for(DataSnapshot restaurant: snapshot.getChildren()){
            restaurantList.add(restaurant.getValue(Restaurant.class));
        }
        presenterPublicOf.showRestPublicOf(restaurantList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
