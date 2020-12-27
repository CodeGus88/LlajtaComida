package com.appcocha.llajtacomida.models.rating;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RatingInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.models.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class RatingModel implements RatingInterface.ModelRating, ValueEventListener {

    private RatingInterface.PresenterRating presenterRating;
    private DatabaseReference databaseReference;
    private String nodeCollectionName, objectId;
    private Rating rating;
    private ArrayList<User> userList;

    public RatingModel(RatingInterface.PresenterRating presenterRating, String nodeCollectionName, String objectId){
        this.presenterRating = presenterRating;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.nodeCollectionName = nodeCollectionName;
        this.objectId = objectId;
        userList = new ArrayList<User>();

    }

    public RatingModel(String nodeCollectionName, String objectId){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.nodeCollectionName = nodeCollectionName;
        this.objectId = objectId;
        userList = new ArrayList<User>();
    }

    @Override
    public void searchRating() {
        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("rating").orderByChild("date")
                .addValueEventListener(this);
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
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void deleteVote(String voteId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("App").child(nodeCollectionName).child(objectId)
                .child("rating").child("votesList").child(voteId).removeValue();
        updatePunctuation(); // Como seelimina de forma manual, se deben actualizar las variables manualmente
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/" + nodeCollectionName + "/" + objectId +"/rating" )){
            if(snapshot.getValue() != null) {
                rating = snapshot.getValue(Rating.class); // getVotesList es escruido a momento de copiar el objeto (porque se tuvo probrlemas con el cast)
                rating.setVotesList(getDataHashtable(snapshot));
                // load userList
            }else{
                rating = new Rating();
            }
            initSearchUsers();
        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users")){
            userList.clear();
            for(DataSnapshot usr : snapshot.getChildren()){
                User user = usr.getValue(User.class);
                if(rating.getVotesList().keySet().contains(user.getId())){
                    userList.add(usr.getValue(User.class));
                }
            }
            // una ves  de que ya se tenga el reting y la lista de usuarios, mostramoss
            presenterRating.showRating(rating, userList);
        }
    }

    /**
     * iniciara despues de buscar el rating del objeto
     */
    private void initSearchUsers(){
        databaseReference.child("App").child("users").addValueEventListener(this);
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


    /**
     * Actualiza la punctuacion del atributo punctuation de los objetos rating y objecto (plate o restaurant)
     */
    private void updatePunctuation(){
        final float punctuation [] = new float[1]; // por necesitar ser constante, evadimos la regla si lo metemos dentro de un arreglo
        punctuation[0] = 0;
        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("rating").child("votesList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot row: snapshot.getChildren()) {
                            punctuation[0] += Float.parseFloat(row.child("punctuation").getValue().toString());
                            count ++;
                        }
                        if(count > 0){
                            punctuation[0] /= snapshot.getChildrenCount(); // saca le promedio
                        }
                        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("rating").child("punctuation").setValue(punctuation[0]);
                        databaseReference.child("App").child(nodeCollectionName).child(objectId).child("punctuation").setValue(punctuation[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
