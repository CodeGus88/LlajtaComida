package com.appcocha.llajtacomida.interfaces;

import com.appcocha.llajtacomida.model.rating.Rating;
import com.appcocha.llajtacomida.model.user.User;
import java.util.ArrayList;

/**
 * @Author: Gustavo Abasto Argote
 * Contiene las interfaces modelo, vista y presentador de rating
 */
public interface RatingInterface {

    /**
     * Vista
     */
    interface ViewRating{

        /**
         * Muestra el rating
         * @param votesList
         * @param userList
         */
        void showRating(ArrayList<Object> votesList, ArrayList<User> userList);
    }

    /**
     * Presentador
     */
    interface PresenterRating{

        /**
         * Determina cargar el rating
         */
        void loadRating();

        /**
         * Determina mostrar el rating
         * @param rating
         * @param users
         */
        void showRating(Rating rating, ArrayList<User> users);

        /**
         * Determina guardar un voto
         * @param userPunctuation
         * @param userExperience
         */
        void saveVote(float userPunctuation, String userExperience); // object puede ser plato o restaurantes

        /**
         * Determina detener la BD
         */
        void stopRealtimeDatabase();

    }

    /**
     * Modelo
     */
    interface ModelRating{

        /**
         * Busca el rating en la BD
         */
        void searchRating();

        /**
         * Guarda un voto en la BD
         * @param rating
         */
        void saveVote(Rating rating); // armar el objeto

        /**
         * Detiene la BD
         */
        void stopRealtimeDatabase();

        /**
         * Elimina un voto de la base de BD
         * @param voteId
         */
        void deleteVote(String voteId);
    }
}
