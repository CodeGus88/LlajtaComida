package com.appcocha.llajtacomida.model.favorite;

import android.util.Log;
import androidx.annotation.NonNull;
import com.appcocha.llajtacomida.interfaces.FavoriteInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Esta clase gestiona los elementos favoritos (platos, restaurantes) en la BD
 */
public class FavoriteModel implements FavoriteInterface.ModelFavorite, ValueEventListener {

    private final FavoriteInterface.PresenterFavorite presenterFavorite;
    private final String userId, nodeCollectionName, favoritesNode;
    private final DatabaseReference databaseReference; // es necesario varias referencias para actualizar cambios en las dos listas
    private final ArrayList<String> favoriteList;
    private final ArrayList<Object> favoriteObjectList;

    /**
     *
     * @param presenterFavorite
     * @param userId
     * @param nodeCollectionName
     */
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
        }else{
            favoritesNode = "undefined";
        }
        favoriteList = new ArrayList<String>();
        favoriteObjectList = new ArrayList<Object>();
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
        databaseReference.child("App").child(nodeCollectionName).orderByChild("name").addValueEventListener(this);
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
        databaseReference.child("App").child("users").child(userId).child(favoritesNode).child(objectId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
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
//        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/"+nodeCollectionName)){
        if(snapshot.getRef().toString().equals(StringValues.getDBURL() +"/App/"+nodeCollectionName)){
            favoriteObjectList.clear();
            for(DataSnapshot data : snapshot.getChildren()){
                if(nodeCollectionName.equals("plates")){
                    if(favoriteList.contains(data.getValue(Plate.class).getId())){
                        favoriteObjectList.add(data.getValue(Plate.class));
                    }
                }else if(nodeCollectionName.equals("restaurants")){
                    if(favoriteList.contains(data.getValue(Restaurant.class).getId())){
                        favoriteObjectList.add(data.getValue(Restaurant.class));
                    }
                }
            }
            presenterFavorite.showFavoriteList(favoriteObjectList);
//        }else if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users/"+userId+"/"+ favoritesNode)){
        }else if(snapshot.getRef().toString().equals(StringValues.getDBURL()+"/App/users/"+userId+"/"+ favoritesNode)){
            favoriteList.clear();
            for(DataSnapshot data : snapshot.getChildren()){
                favoriteList.add(data.getValue().toString());
            }
        }else if(snapshot.getRef().toString().equals(StringValues.getDBURL()+"/App/users/"+userId+"/"+ favoritesNode +"/"+objectId)){
            if(snapshot.getValue() != null){
                presenterFavorite.showFavoriteObjectState(true);
            }else{
                presenterFavorite.showFavoriteObjectState(false);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Cancel", "---------------------------> operation cancelled");
    }
}
