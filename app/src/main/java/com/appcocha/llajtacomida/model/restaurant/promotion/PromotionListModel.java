package com.appcocha.llajtacomida.model.restaurant.promotion;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PromotionListModel implements RestaurantInterface.ModelPromotionPlateList, ValueEventListener {

    private RestaurantInterface.PresenterPromotionPlateList presenterPromotionPlateList;
    private DatabaseReference databaseReference, databaseReferenceM, databaseReferenceP;
    private String restaurantId;
    private ArrayList<Plate> plateList;
    private Menu menu;
    private Promotion promotion;
    private byte processCounter;

    public PromotionListModel(RestaurantInterface.PresenterPromotionPlateList presenterPromotionPlateList){
        this.presenterPromotionPlateList = presenterPromotionPlateList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceM = FirebaseDatabase.getInstance().getReference();
        databaseReferenceP = FirebaseDatabase.getInstance().getReference();
        plateList = new ArrayList<Plate>();
    }

    @Override
    public void filterPlateListInPromotion(String restaurantId){
        processCounter = 0;
        this.restaurantId = restaurantId;
        databaseReference.child("App").child("plates").orderByChild("name").addValueEventListener(this);
        databaseReferenceM.child("App").child("restaurants").child(restaurantId)
                .child("menus").child("local").addValueEventListener(this);
        databaseReferenceP.child("App").child("restaurants").child(restaurantId)
                .child("promotion").addValueEventListener(this);

    }


    @Override
    public void stopRealTimeDatabase() {
        databaseReference.removeEventListener(this);
        databaseReferenceM.removeEventListener(this);
        databaseReferenceP.removeEventListener(this);
    }

    // BASE DE DATOS
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals(StringValues.getDBURL() +"/App/plates")){
            plateList.clear();
            for (DataSnapshot plate: snapshot.getChildren()) {
                plateList.add(plate.getValue(Plate.class));
            }
        }else if(snapshot.getRef().toString().equals(StringValues.getDBURL()+"/App/restaurants/"+restaurantId+"/menus/local")){
            menu = snapshot.getValue(Menu.class);
        }else if(snapshot.getRef().toString().equals(StringValues.getDBURL()+"/App/restaurants/"+restaurantId+"/promotion")){
            promotion = snapshot.getValue(Promotion.class);
        }
        processCounter++;
        if(processCounter >= 3){
            presenterPromotionPlateList.showPromotionPlateList(plateList, menu, promotion);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("ERROR onCancelled: ", error.getCode() + " " + error.getDetails() + " " + error.getMessage());
    }
}
