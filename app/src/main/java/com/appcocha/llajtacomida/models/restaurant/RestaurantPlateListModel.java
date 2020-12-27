package com.appcocha.llajtacomida.models.restaurant;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.menu.Menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantPlateListModel implements RestaurantInterface.ModelPlateList, ValueEventListener {

    private RestaurantInterface.PresenterPlateList presenterPlateList;
    private DatabaseReference databaseReference, databaseReferenceM;
    private ArrayList<Plate> plateListMenu, plateList;
    private Menu menu;

    public RestaurantPlateListModel(RestaurantInterface.PresenterPlateList presenterPlateList){
        this.presenterPlateList = presenterPlateList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceM = FirebaseDatabase.getInstance().getReference();
        plateListMenu = new ArrayList<Plate>();
        plateList = new ArrayList<Plate>();
    }


    @Override
    public void filterPlateListInMenu(String restaurantId) {
        databaseReference
                .child("App")
                .child("restaurants")
                .child(restaurantId)
                .child("menus")
                .child("local").addValueEventListener(this);
        databaseReferenceM.child("App").child("plates").orderByChild("name").addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabse() {
        databaseReference.removeEventListener(this);
        databaseReferenceM.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        plateListMenu.clear();
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/plates")){
            plateList.clear();
                for (DataSnapshot plate: snapshot.getChildren()) {
                    plateList.add(plate.getValue(Plate.class));
                }
        }else{
            menu = snapshot.getValue(Menu.class);
        }
        // Cargar menu
        if(menu != null){
            for (Plate plate: plateList) {
                if(menu.getMenuList().contains(plate.getId())){
                    plateListMenu.add(plate);
                }
            }
        }
        presenterPlateList.showPlateList(plateListMenu);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

}
