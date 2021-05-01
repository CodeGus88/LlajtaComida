package com.appcocha.llajtacomida.model.restaurant;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.appcocha.llajtacomida.presenter.tools.Validation;
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
    private ArrayList<String> menuList;
    private Menu menu;

    public RestaurantPlateListModel(RestaurantInterface.PresenterPlateList presenterPlateList){
        this.presenterPlateList = presenterPlateList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceM = FirebaseDatabase.getInstance().getReference();
        menuList = new ArrayList<String>();
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
//        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/plates")){
        if(snapshot.getRef().toString().equals(StringValues.getDBURL() +"/App/plates")){
            plateList.clear();
                for (DataSnapshot plate: snapshot.getChildren()) {
                    plateList.add(plate.getValue(Plate.class));
                }
        }else{
            menu = snapshot.getValue(Menu.class);

            // Agregado para los precios
            if(menuList.size()>0) menuList.clear();
            if (menu != null) {
                menuList.addAll(menu.getMenuList());
                for (int i = 0; i < menu.getMenuList().size(); i++) {
                    menu.getMenuList().set(i, Validation.getFirstWord(menu.getMenuList().get(i)));
                }
            }
        }
        // Cargar menu
        if(menu != null){
            for (Plate plate: plateList) {
                if(menu.getMenuList().contains(plate.getId())){
                    plateListMenu.add(plate);
                }
            }
        }
        presenterPlateList.showPlateList(plateListMenu, menuList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

}
