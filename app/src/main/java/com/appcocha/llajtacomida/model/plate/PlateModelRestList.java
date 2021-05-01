package com.appcocha.llajtacomida.model.plate;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

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

//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        restaurantList.clear();
//        for(DataSnapshot restaurnat: snapshot.getChildren()){
//            Restaurant r = restaurnat.getValue(Restaurant.class);
//            Menu menu = snapshot.child(r.getId()).child("menus").child("local").getValue(Menu.class);
//            if(menu != null){
//                if(menu.getMenuList().contains(plateId)){
//                    restaurantList.add(r);
//                }
//            }
//        }
//        restListPresenter.showRestList(restaurantList);
//    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        restaurantList.clear();
        Hashtable<String, String> priceInRestaurantsList; // acumulará los precios de plateId en los idRestaurant restaurante
        priceInRestaurantsList = new Hashtable<String, String>();
        for(DataSnapshot restaurant: snapshot.getChildren()){
            Restaurant r = restaurant.getValue(Restaurant.class);
            Menu menu = snapshot.child(r.getId()).child("menus").child("local").getValue(Menu.class);
            if(menu != null){
                for (String string: menu.getMenuList()){
                    if(string.contains(plateId)){
//                        if(!Validation.getXWord(string, 2).isEmpty()){
                            priceInRestaurantsList.put(r.getId(), Validation.getXWord(string, 2));
//                        }else{
//                            priceInRestaurantsList.put(r.getId(), "UNDEFINED");
//                        }
                        restaurantList.add(r);
                        break; // solo busca uno (el plato solo existe una ves en el menú del restaurante)
                    }
                }
            }
        }
        restListPresenter.showRestList(restaurantList, priceInRestaurantsList);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {    }

}
