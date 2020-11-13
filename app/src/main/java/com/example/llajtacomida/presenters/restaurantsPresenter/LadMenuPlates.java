package com.example.llajtacomida.presenters.restaurantsPresenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.llajtacomida.models.MenuDB;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.views.restaurantsViews.ArrayAdapterRestaurantMenu;
import com.example.llajtacomida.views.restaurantsViews.MenuActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class LadMenuPlates {
    // database
    Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Plate> platesList;
    private ArrayAdapterRestaurantMenu arrayAdapterRestaurantMenu;
    private MenuDB menu;
    private String restaurantId;
    private final int ADAPTER_ELEMENT_RESTAURANT_MENU;

    public LadMenuPlates(final Context context, String restaurantId){

        ADAPTER_ELEMENT_RESTAURANT_MENU = com.example.llajtacomida.R.layout.adapter_element_restaurant_menu;
        this.context = context;
        this.restaurantId = restaurantId;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("App").child("plates");
        platesList = new ArrayList<Plate>();

        menu = new MenuDB(context, restaurantId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                platesList.clear();
                for (DataSnapshot p: snapshot.getChildren()) {
                    Plate plate = (Plate) p.getValue(Plate.class);
                    platesList.add(plate);
                    arrayAdapterRestaurantMenu = new ArrayAdapterRestaurantMenu(
                            context,
                            ADAPTER_ELEMENT_RESTAURANT_MENU
                            , platesList, menu.getMenu());
                    MenuActivity.loadPlates(arrayAdapterRestaurantMenu);
                }
                if(platesList.isEmpty()){
                    try {
                        arrayAdapterRestaurantMenu = new ArrayAdapterRestaurantMenu(
                                context,
                                ADAPTER_ELEMENT_RESTAURANT_MENU,
                                platesList, menu.getMenu());
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                    MenuActivity.loadPlates(arrayAdapterRestaurantMenu);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public ArrayList<Plate> getPlatesList(){
        return platesList;
    }
}
