package com.example.llajtacomida.presenters.favorite;

import android.util.Log;

import com.example.llajtacomida.interfaces.FavoriteInterface;
import com.example.llajtacomida.models.favorite.FavoriteModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoritePresenter implements FavoriteInterface.PresenterFavorite {

    private FavoriteInterface.ViewFavorite viewFavorite;
    private FavoriteInterface.ModelFavorite modelFavorite;

    public FavoritePresenter(FavoriteInterface.ViewFavorite viewFavorite, String nodeCollectionName){
        this.viewFavorite = viewFavorite;
        modelFavorite = new FavoriteModel(this, FirebaseAuth.getInstance().getUid(), nodeCollectionName);
    }

    @Override
    public void showFavoriteList(ArrayList<Object> objectsList) {
        viewFavorite.showFavoriteList(objectsList);
    }

    @Override
    public void showFavoriteObjectState(boolean isFavorite) {
        viewFavorite.showFavoriteIcon(isFavorite);
    }

    @Override
    public void searchFavoriteObject(String objectId) { // Solo es para verificar si existe o no un elemento en favoritos
        modelFavorite.searchFavoriteObject(objectId);
    }

    @Override
    public void searchObjectFavoriteList() {
        modelFavorite.searchObjectFavoriteList();
    }

    @Override
    public void stopRealtimeDatabase() {
        modelFavorite.stopRealtimeDatabase();
    }

    @Override
    public void saveObjectFavorite(String objectId) {
//        Log.println(150, "presentador proceso: ", "---------------------------------------------------------> estoy aquí: " + objectId);
        modelFavorite.saveObjectFavorite(objectId);
    }

    @Override
    public void removeObjectFavorite(String objectId) {
        modelFavorite.removeObjectFavorite(objectId);
    }

    @Override
    public void successFul(boolean isSuccess) {
        viewFavorite.successFul(isSuccess);
    }
}