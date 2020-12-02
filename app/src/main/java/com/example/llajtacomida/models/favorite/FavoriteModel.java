package com.example.llajtacomida.models.favorite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.FavoriteInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteModel implements FavoriteInterface.ModelFavorite, ValueEventListener {

    private FavoriteInterface.PresenterFavorite presenterFavorite;
    private String userId, nodeCollectionName, favoritesNode;
    private DatabaseReference databaseReference;
    private ArrayList<String> favoriteList;
    private ArrayList<Object> favoritObjectList;

    public FavoriteModel(FavoriteInterface.PresenterFavorite presenterFavorite, String userId, String nodeCollectionName){
        this.presenterFavorite = presenterFavorite;
        this.userId = userId;
        this.nodeCollectionName = nodeCollectionName;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // será necesario saber el nodo de favoritos para listas los platos
        if(nodeCollectionName.equals("plates")){
            favoritesNode = "favorite_plates";
        }else if(nodeCollectionName.equals("restaurants")){
            favoritesNode = "favorite_rest";
        }
        favoriteList = new ArrayList<String>();
        favoritObjectList = new ArrayList<Object>();
    }


    private String objectId;
    @Override
    public void searchFavoriteObject(String objectId) { // Revisar en la lista si el objeto se encuenntra en favoritos
        this.objectId = objectId;
        databaseReference.child("App").child("users").child(userId).child(favoritesNode).child(objectId).addValueEventListener(this);
    }
    @Override
    public void searchObjectFavoriteList() {
        databaseReference.child("App").child("users").child(userId).child(favoritesNode).addValueEventListener(this);
        databaseReference.child("App").child(nodeCollectionName).addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void saveObjectFavorite(String objectId) {
        databaseReference.child("App").child("users").child(userId).child(favoritesNode).child(objectId).setValue(objectId)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successFul(task.isSuccessful());
            }
        });
    }

    @Override
    public void removeObjectFavorite(String objectId) {
        databaseReference.child("App").child("users").child(userId).child(favoritesNode).child(objectId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successFul(task.isSuccessful());
            }
        });
    }

    @Override
    public void successFul(boolean isSuccess) {
        presenterFavorite.successFul(isSuccess);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/"+nodeCollectionName)){
            for(DataSnapshot data : snapshot.getChildren()){
                if(nodeCollectionName.equals("plates")){
                    if(favoriteList.contains(data.getValue(Plate.class).getId())){
                        favoritObjectList.add(data.getValue(Plate.class));
                    }
                }else if(nodeCollectionName.equals("restaurants")){
                    if(favoriteList.contains(data.getValue(Restaurant.class).getId())){
                        favoritObjectList.add(data.getValue(Restaurant.class));
                    }
                }
            }
            presenterFavorite.showFavoriteList(favoritObjectList);
        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users/"+userId+"/"+ favoritesNode)){
            for(DataSnapshot data : snapshot.getChildren()){
                favoriteList.add(data.getValue().toString());
            }
        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users/"+userId+"/"+ favoritesNode +"/"+objectId)){
            if(snapshot.getValue() != null){
                presenterFavorite.showFavoriteObjectState(true);
            }else{
                presenterFavorite.showFavoriteObjectState(false);
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Cancel", "---------------------------> operation canceled");
    }
}
