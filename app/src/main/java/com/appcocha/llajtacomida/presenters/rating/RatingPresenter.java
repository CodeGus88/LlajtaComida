package com.appcocha.llajtacomida.presenters.rating;

import com.appcocha.llajtacomida.interfaces.RatingInterface;
import com.appcocha.llajtacomida.models.rating.Rating;
import com.appcocha.llajtacomida.models.rating.RatingModel;
import com.appcocha.llajtacomida.models.user.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Presentador, rating
 */
public class RatingPresenter implements RatingInterface.PresenterRating {

    private RatingInterface.ViewRating viewRating;
    private RatingInterface.ModelRating modelRating;
    private Rating rating;
    private ArrayList<Object> objectList;

    /**
     * Contructor, inicializa viewRating, nodeCollectionName, objectId
     * @param viewRating
     * @param nodeCollectionName
     * @param objectId
     */
    public RatingPresenter(RatingInterface.ViewRating viewRating, String nodeCollectionName, String objectId){
        this.viewRating = viewRating;
        modelRating = new RatingModel(this, nodeCollectionName, objectId);
    }

    /**
     * Constructor, inicializa nodeCollectionName, objectId
     * @param nodeCollectionName
     * @param objectId
     */
    public RatingPresenter(String nodeCollectionName, String objectId){
        viewRating = null;
        modelRating = new RatingModel(this, nodeCollectionName, objectId);
    }

    @Override
    public void loadRating() {
        modelRating.searchRating();
    }

    /**
     * inicializar carga
     * @param rating
     */
    @Override
    public void showRating(Rating rating, ArrayList<User> userList) {
        this.rating = rating;
        ArrayList<Object> voteList = new ArrayList<Object>();
        for(String key : rating.getVotesList().keySet()){
            voteList.add(rating.getVotesList().get(key));
        }
        viewRating.showRating(voteList, userList);
    }


    @Override
    public void saveVote(float userPunctuation, String userExperience) {
        // construir el objeto rating y mandarselo al modelo para que lo guarde
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = dt.format(newDate);
        rating.putPunctuation(FirebaseAuth.getInstance().getUid(), userPunctuation, userExperience, stringDate);
        modelRating.saveVote(rating);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelRating.stopRealtimeDatabase();
    }

}
