package com.example.llajtacomida.models.restaurant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantModel implements RestaurantInterface.ModelRestaurant, ValueEventListener {

    private RestaurantInterface.PresenterRestaurant presenterRestaurant;
    private DatabaseReference databaseReference;

    public RestaurantModel(RestaurantInterface.PresenterRestaurant presenterRestaurant){
        this.presenterRestaurant = presenterRestaurant;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void searchRestaurant(String restaurantId) {
        databaseReference.child("App").child("restaurants").child(restaurantId).addValueEventListener(this);
    }

    @Override
    public void loadRestaurantList() {
//        Query query = databaseReference.child("App").child("restaurants").orderByChild("public").equalTo(true); // problemas al ordenar los restaaurantes
        Query query = databaseReference.child("App").child("restaurants");
        query.addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabse() {
        databaseReference.removeEventListener(this);
    }

    /**
     * Los escuchadores de la base de datos
     * @param snapshot
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/restaurants")){
            ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
            for (DataSnapshot restaurant: snapshot.getChildren()) {
                if(restaurant.getValue(Restaurant.class).isPublic()){ // se usa este método y no el de la consulta al tener problemas de ordenamiento de la lista
                    restaurantList.add(restaurant.getValue(Restaurant.class));
                }
            }
            presenterRestaurant.showRestaurantList(restaurantList);
        }else{
            presenterRestaurant.showRestaurant(snapshot.getValue(Restaurant.class));
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", error.getMessage());
    }
}
