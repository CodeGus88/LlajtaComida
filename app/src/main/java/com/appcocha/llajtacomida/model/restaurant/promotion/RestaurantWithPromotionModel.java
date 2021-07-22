package com.appcocha.llajtacomida.model.restaurant.promotion;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RestaurantWithPromotionModel implements RestaurantInterface.ModelResraurantWithPromotion, ValueEventListener {

    private DatabaseReference databaseReference;
    private RestaurantInterface.PresenterRestaurantWithPromotion presenterRestaurantWithPromotion;
    private ArrayList<Restaurant> restaurantList;

    public RestaurantWithPromotionModel(RestaurantInterface.PresenterRestaurantWithPromotion presenterRestaurantWithPromotion){
        this.presenterRestaurantWithPromotion = presenterRestaurantWithPromotion;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        restaurantList = new ArrayList<Restaurant>();
    }

    @Override
    public void filterRestaurantWithPromotion() {
        Query query = databaseReference.child("App").child("restaurants").orderByChild("promotion/active").equalTo(true);
        query.addValueEventListener(this);
    }

    @Override
    public void stopRealTimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        restaurantList.clear();
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            restaurantList.add(dataSnapshot.getValue(Restaurant.class));
        }
        presenterRestaurantWithPromotion.showRestaurantWithPromotion(restaurantList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
