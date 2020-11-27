package com.example.llajtacomida.models.restaurant.menu;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.menu.Menu;
import com.example.llajtacomida.models.restaurant.menu.MenuDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SetMenuListModel implements RestaurantInterface.ModelSetMenuList, ValueEventListener {

    private RestaurantInterface.PresenterSetMenuList presenterSetMenuList;
    private DatabaseReference databaseReference, databaseReferenceM;
    private String restaurantId;
    private ArrayList<Plate> plateList;
    private Menu menu;

    public SetMenuListModel(RestaurantInterface.PresenterSetMenuList presenterSetMenuList){
        this.presenterSetMenuList = presenterSetMenuList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceM = FirebaseDatabase.getInstance().getReference();
        plateList = new ArrayList<Plate>();
    }

    @Override
    public void searchSetMenuList(String restaurantId) {
        this.restaurantId = restaurantId;
        databaseReference.child("App").child("plates").addValueEventListener(this);
        databaseReferenceM.child("App").child("restaurants").child(restaurantId)
                .child("menus").child("local").addValueEventListener(this);
    }

    @Override
    public void saveMenuList(Context context, String restaurantId, Menu menu) {
        // guardar en la base de datos
        MenuDB menuDB = new MenuDB(context, restaurantId);
        menuDB.saveMenu(menu, "local");
    }


    @Override
    public void stopRealTimeDatabase() {
        databaseReference.removeEventListener(this);
        databaseReferenceM.removeEventListener(this);
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/plates")){
            plateList.clear();
            for (DataSnapshot plate: snapshot.getChildren()) {
                plateList.add(plate.getValue(Plate.class));
            }
        }else{
            menu = snapshot.getValue(Menu.class);
        }
        presenterSetMenuList.showSetMenuList(plateList, menu);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", error.getMessage());
    }

}
