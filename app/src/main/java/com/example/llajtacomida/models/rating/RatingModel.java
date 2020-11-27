package com.example.llajtacomida.models.rating;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.RatingInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class RatingModel implements RatingInterface.ModelRating, ValueEventListener {

    private RatingInterface.PresenterRating presenterRating;
    private DatabaseReference databaseReference, databaseReferenceU;
    private String nodeCollectionName, objectId;

    public RatingModel(RatingInterface.PresenterRating presenterRating, String nodeCollectionName, String objectId){
        this.presenterRating = presenterRating;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.nodeCollectionName = nodeCollectionName;
        this.objectId = objectId;
        searchRating();
    }

    @Override
    public void searchRating() {
        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("rating").addListenerForSingleValueEvent(this);
    }

    @Override
    public void saveVote(final Rating rating) {
        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("rating").updateChildren(rating.toMap())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Actualiza el la puntuacion del objeto
                databaseReference.child("App").child(nodeCollectionName).child(objectId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(nodeCollectionName.equals("plates")){
                            Plate plate = snapshot.getValue(Plate.class);
                            plate.setPunctuation(rating.getPunctuation());
                            databaseReference.child("App").child(nodeCollectionName).child(objectId).updateChildren(plate.toMap());
                        }else if(nodeCollectionName.equals("restaurants")){
                            Restaurant restaurant = snapshot.getValue(Restaurant.class);
                            restaurant.setPunctuation(rating.getPunctuation());
                            databaseReference.child("App").child(nodeCollectionName).child(objectId).updateChildren(restaurant.toMap());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                presenterRating.updatePunctuationObject(true, rating.getPunctuation());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenterRating.updatePunctuationObject(false, rating.getPunctuation());
            }
        });

    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.getValue() != null) {
                Rating rating = snapshot.getValue(Rating.class); // getVotesList es escruido a momento de copiar el objeto (porque se tuvo probrlemas con el cast)
                rating.setVotesList(getDataHashtable(snapshot));
                presenterRating.showRating(rating);
            }else{
                presenterRating.showRating(null);
            }
//        stopRealtimeDatabase();
    }

    /**
     * Solucion alternativa al fallo de cast de una lista Hastable
     * @param snapshot
     * @return
     */
    private Hashtable<String, Hashtable<String, String>> getDataHashtable(DataSnapshot snapshot){
        Hashtable<String, Hashtable<String, String>> table = new Hashtable<String, Hashtable<String, String>>();
        for (DataSnapshot data: snapshot.child("votesList").getChildren()) {
            Hashtable<String, String> dataRow = new Hashtable<String, String>();
            dataRow.put("user_id", data.child("user_id").getValue().toString());
            dataRow.put("punctuation", data.child("punctuation").getValue().toString());
            dataRow.put("experience", data.child("experience").getValue().toString());
            dataRow.put("date", data.child("date").getValue().toString());
            table.put(data.getKey(), dataRow);
        }
        return  table;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", error.getMessage());
    }
}
