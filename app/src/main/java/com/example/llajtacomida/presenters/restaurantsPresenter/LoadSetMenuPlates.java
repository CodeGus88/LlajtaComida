package com.example.llajtacomida.presenters.restaurantsPresenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.llajtacomida.models.MenuDB;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.views.restaurantsViews.SetMenuActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class LoadSetMenuPlates {
    // database
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Plate> platesList;
    private ArrayAdapterSetMenu arrayAdapterSetMenu;
    private MenuDB menuDB;
    private String restaurantId;
    private final int ADAPTER_ELEMENT_RESTAURANT_MENU;

    public LoadSetMenuPlates(final Context context, String restaurantId){

        ADAPTER_ELEMENT_RESTAURANT_MENU = com.example.llajtacomida.R.layout.adapter_element_restaurant_menu;
        this.context = context;
        this.restaurantId = restaurantId;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("App").child("plates");
        platesList = new ArrayList<Plate>();

        menuDB = new MenuDB(context, restaurantId);
        databaseReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                platesList.clear();
                for (DataSnapshot p: snapshot.getChildren()) {
                    Plate plate = (Plate) p.getValue(Plate.class);
                    platesList.add(plate);
                }
                if(!platesList.isEmpty()){
                    arrayAdapterSetMenu = new ArrayAdapterSetMenu(
                            context,
                            ADAPTER_ELEMENT_RESTAURANT_MENU
                            , platesList, menuDB.getMenu());
                }else{
                    try {
                        arrayAdapterSetMenu = new ArrayAdapterSetMenu(
                                context,
                                ADAPTER_ELEMENT_RESTAURANT_MENU,
                                platesList, menuDB.getMenu());
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
                SetMenuActivity.loadPlates(arrayAdapterSetMenu);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        setSearch();
    }

    private void setSearch(){
        SetMenuActivity.getEtSearch().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterSetMenu.filter(s.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public ArrayList<Plate> getPlatesList(){
        return platesList;
    }

    public void saveMenu(){
        menuDB.saveMenu(arrayAdapterSetMenu.getMenu());
    }
}
