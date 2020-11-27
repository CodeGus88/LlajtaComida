package com.example.llajtacomida.interfaces;

import com.example.llajtacomida.models.rating.Rating;

import java.util.ArrayList;

public interface RatingInterface {

    interface ViewRating{
        void showRating(float rating);
        void showVotesList(ArrayList<Object> votesList);
    }

    interface PresenterRating{
        void showRating(Rating rating);
        void showVotesList(); // Hace referenccia a la lista de votantes con sus comentarios
        void saveVote(float userPunctuation, String userExperience); // object puede ser plato o restaurantes
        void updatePunctuationObject(boolean isSuccess, float punctuation); //
    }

    interface ModelRating{
        void searchRating();
        void saveVote(Rating rating); // armar el objeto
        void stopRealtimeDatabase();
    }
}
