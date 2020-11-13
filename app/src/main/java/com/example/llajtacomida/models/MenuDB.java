package com.example.llajtacomida.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuDB {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Menu menu;

    public MenuDB(Context context, String restaurantId){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("App")
                .child("restaurants")
                .child(restaurantId)
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
    }

    public Menu getMenu(){
        return menu;
    }
}
