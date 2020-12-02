package com.example.llajtacomida.models.favorite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.FavoriteInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriteModel implements FavoriteInterface.ModelFavorite, ValueEventListener {

    private FavoriteInterface.PresenterFavorite presenterFavorite;
    private String userId, nodeCollectionName, favoriteNode;
    private DatabaseReference databaseReference;

    public FavoriteModel(FavoriteInterface.PresenterFavorite presenterFavorite, String userId, String nodeCollectionName{
        this.presenterFavorite = presenterFavorite;
        this.userId = userId;
        this.nodeCollectionName = nodeCollectionName;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // será necesario saber el nodo de favoritos para listas los platos
        if(nodeCollectionName.equals("plates")){
            favoriteNode = "favorite_plates";
        }else if(nodeCollectionName.equals("restaurants")){
            favoriteNode = "favorite_rest";
        }
    }


    private String objectId;
    @Override
    public void searchFavoriteObject(String objectId) { // Revisar en la lista si el objeto se encuenntra en favoritos
        this.objectId = objectId;
        databaseReference.child("App").child("users").child(userId).child(favoriteNode).child(objectId);
    }
    @Override
    public void searchObjectFavoriteList() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void stopRealtimeDatabase() {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // base de datos
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/"+nodeCollectionName)){
            for(DataSnapshot data : snapshot.getChildren()){
                if(){

                }
            }
        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users/"+userId+"/"+favoriteNode)){

        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users/"+userId+"/"+favoriteNode+"/"+objectId)){
            presenterFavorite.showFavoriteIcon(snapshot.getValue().toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Cancel", "---------------------------> operation canceled");
    }
}
