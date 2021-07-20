package com.appcocha.llajtacomida.model.restaurant.promotion;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Gestiona el meú en la base de datos
 */
public class PromotionDB {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String RESTAURANT_ID;
    private Promotion promotion;

    /**
     * Contructor, establece el nodo (id) en la BD del restaurante al que corresponde
     * @param RESTAURANT_ID
     */
    public PromotionDB(final String RESTAURANT_ID){ //final Context CONTEXT,
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("App")
                .child("restaurants")
                .child(RESTAURANT_ID)
                .child("promotion")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promotion = snapshot.getValue(Promotion.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               promotion = new Promotion();
            }
        });
        this.RESTAURANT_ID = RESTAURANT_ID;
    }

    /**
     * Obtiene la promoción
     * @return promotion
     */
    public Promotion getPromotion(){
        return promotion;
    }

    /**
     * Almacena la promoción del restaurante en la BD
     * @param promotion
     */
    public void savePromotion(Promotion promotion){
        databaseReference.child("App")
                .child("restaurants")
                .child(RESTAURANT_ID)
                .child("promotion")
                .setValue(promotion);
    }
}
