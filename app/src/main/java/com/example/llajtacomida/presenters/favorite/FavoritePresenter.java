package com.example.llajtacomida.presenters.favorite;

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

    }

    @Override
    public void showFavoriteIcon(String objectId) {
        if(!objectId.isEmpty()) viewFavorite.showFavoriteIcon(true);
        else viewFavorite.showFavoriteIcon(true);
    }

    @Override
    public void searchFavoriteObject(String objectId) {

    }

    @Override
    public void searchObjectFavoriteList() {

    }

    @Override
    public void stopRealtimeDatabase() {
        modelFavorite.stopRealtimeDatabase();
    }
}
