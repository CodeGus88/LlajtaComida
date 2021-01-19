package com.appcocha.llajtacomida.models.plate;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.models.restaurant.menu.Menu;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Modelo, lee los restaurantes que tiene  un plato en su menú
 * Implementa PlateInterface.ModelRestList
 */
public class PlateModelRestList implements PlateInterface.ModelRestList,  ValueEventListener{

    private PlateInterface.presenterRestList restListPresenter;
    private ArrayList<Restaurant> restaurantList;
    private String plateId;
    private DatabaseReference databaseReference;

    /**
     * Contructor
     * @param restListPresenter
     */
    public PlateModelRestList(PlateInterface.presenterRestList restListPresenter){
        this.restListPresenter = restListPresenter;
        restaurantList = new ArrayList<Restaurant>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("App").child("restaurants").orderByChild("public").equalTo(true).addValueEventListener(this);
    }

    @Override
    public void filterRestaurantListWithPlate(String plateId) {
        this.plateId = plateId;
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        restaurantList.clear();
        for(DataSnapshot restaurnat: snapshot.getChildren()){
            Restaurant r = restaurnat.getValue(Restaurant.class);
            Menu menu = snapshot.child(r.getId()).child("menus").child("local").getValue(Menu.class);
            if(menu != null){
                if(menu.getMenuList().contains(plateId)){
                    restaurantList.add(r);
                }
            }
        }
        restListPresenter.showRestList(restaurantList);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {    }

}
