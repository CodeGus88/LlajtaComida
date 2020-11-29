package com.example.llajtacomida.interfaces;

import com.example.llajtacomida.models.rating.Rating;
import com.example.llajtacomida.models.user.User;

import java.util.ArrayList;
import java.util.Hashtable;

public interface RatingInterface {

    interface ViewRating{
        void showRating(ArrayList<Object> votesList, ArrayList<User> userList);
    }

    interface PresenterRating{
        void loadRating();
        void showRating(Rating rating, ArrayList<User> users);
        void saveVote(float userPunctuation, String userExperience); // object puede ser plato o restaurantes
        void stopRealtimeDatabase();

    }

    interface ModelRating{
        void searchRating();
        void saveVote(Rating rating); // armar el objeto
        void stopRealtimeDatabase();
    }
}
