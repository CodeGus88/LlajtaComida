package com.example.llajtacomida.presenters.rating;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.llajtacomida.interfaces.RatingInterface;
import com.example.llajtacomida.models.plate.PlateGestorDB;
import com.example.llajtacomida.models.rating.Rating;
import com.example.llajtacomida.models.rating.RatingModel;
import com.example.llajtacomida.models.restaurant.RestaurantGestorDB;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatingPresenter implements RatingInterface.PresenterRating {

    private RatingInterface.ViewRating viewRating;
    private RatingInterface.ModelRating modelRating;
    private Rating rating;

    public RatingPresenter(RatingInterface.ViewRating viewRating, String nodeCollectionName, String objectId){
        this.viewRating = viewRating;
        modelRating = new RatingModel(this, nodeCollectionName, objectId);
    }

    public RatingPresenter(String nodeCollectionName, String objectId){
        viewRating = null;
        modelRating = new RatingModel(this, nodeCollectionName, objectId);
    }

    @Override
    public void showRating(Rating rating) {
        if(rating != null){
            this.rating = rating;
            Log.println(1, "tag :)", "---------------------------------------------------------> rating no es nulo");
        }else{
            this.rating = new Rating();
            Log.println(1, "tag :)", "--------------------------------------------------------->  rating es nulo");
        }
    }

    /**
     * Crea y hace mostarar un array de objetos
     */
    @Override
    public void showVotesList() {
        ArrayList<Object> onjectList = new ArrayList<Object>();
    }

    @Override
    public void saveVote(float userPunctuation, String userExperience) {
//        modelRating.searchRating();
        // construir el objeto rating y mandarselo al modelo para que lo guarde
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = dt.format(newDate);
        //final String userId, final float punctuation, final String experience, final String date
        rating.putPunctuation(FirebaseAuth.getInstance().getUid(), userPunctuation, userExperience, stringDate);
        modelRating.saveVote(rating);
    }

    @Override
    public void updatePunctuationObject(boolean isSuccess, float punctuation) {

    }
}
