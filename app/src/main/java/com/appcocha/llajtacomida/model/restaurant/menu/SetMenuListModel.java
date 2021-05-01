package com.appcocha.llajtacomida.model.restaurant.menu;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Interactua la base de datos para el menú
 */
public class SetMenuListModel implements RestaurantInterface.ModelSetMenuList, ValueEventListener {

    private RestaurantInterface.PresenterSetMenuList presenterSetMenuList;
    private DatabaseReference databaseReference, databaseReferenceM;
    private ArrayList<Plate> plateList;
    private Menu menu;

    public SetMenuListModel(RestaurantInterface.PresenterSetMenuList presenterSetMenuList){
        this.presenterSetMenuList = presenterSetMenuList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceM = FirebaseDatabase.getInstance().getReference();
        plateList = new ArrayList<Plate>();
    }

    /**
     * Recupera la lista de platos
     * Recupera la lista de id's del menú de los platos
     * @param restaurantId
     */
    @Override
    public void searchSetMenuList(String restaurantId) {
        databaseReference.child("App").child("plates").orderByChild("name").addValueEventListener(this);
        databaseReferenceM.child("App").child("restaurants").child(restaurantId)
                .child("menus").child("local").addValueEventListener(this);
    }

    @Override
    public void saveMenuList(String restaurantId, Menu menu) { //Context context,
        // guardar en la base de datos
//        MenuDB menuDB = new MenuDB(context, restaurantId);
        MenuDB menuDB = new MenuDB(restaurantId);
        menuDB.saveMenu(menu, "local");
    }


    @Override
    public void stopRealTimeDatabase() {
        databaseReference.removeEventListener(this);
        databaseReferenceM.removeEventListener(this);
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/plates")){
        if(snapshot.getRef().toString().equals(StringValues.getDBURL() +"/App/plates")){
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
