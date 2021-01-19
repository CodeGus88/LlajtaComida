package com.appcocha.llajtacomida.models.restaurant.menu;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Gestiona el meú en la base de datos
 */
public class MenuDB {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String RESTAURANT_ID;
    private Menu menu;

    /**
     * Contructor, establece el nodo (id) en la BD del restaurante al que corresponde
     * @param RESTAURANT_ID
     */
    public MenuDB(final String RESTAURANT_ID){ //final Context CONTEXT,
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

    /**
     * Obtiene el menú
     * @return menu
     */
    public Menu getMenu(){
        return menu;
    }

    /**
     * Almacena el menú del restaurante en la BD
     * @param menu
     * @param type
     */
    public void saveMenu(Menu menu, String type){
        databaseReference.child("App")
                .child("restaurants")
                .child(RESTAURANT_ID)
                .child("menus")
                .child(type).setValue(menu);
    }
}
