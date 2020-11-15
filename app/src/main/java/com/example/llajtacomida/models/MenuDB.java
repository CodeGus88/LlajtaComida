package com.example.llajtacomida.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.llajtacomida.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuDB {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String RESTAURANT_ID;
    private Menu menu;
    private final Context CONTEXT;

    public MenuDB(final Context CONTEXT, final String RESTAURANT_ID){
        this.CONTEXT = CONTEXT;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("App")
                .child("restaurants")
                .child(RESTAURANT_ID)
                .child("menus")
                .child("local").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menu = snapshot.getValue(Menu.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               menu = new Menu();
            }
        });

        this.RESTAURANT_ID = RESTAURANT_ID;
    }

    public Menu getMenu(){
        return menu;
    }

    public void saveMenu(Menu menu){
        databaseReference.child("App")
                .child("restaurants")
                .child(RESTAURANT_ID)
                .child("menus")
                .child("local").setValue(menu);
        Toast.makeText(CONTEXT, "Save", Toast.LENGTH_SHORT).show();
    }

}
