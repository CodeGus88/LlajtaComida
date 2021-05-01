package com.appcocha.llajtacomida.model.rating;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Modelo
 */
public class Rating {

    private String id;
    private Hashtable<String, Hashtable<String, String>> votesList; // /user_id/punctuation/experience/date
    private float punctuation;

    /**
     * Contructor
     * Genera un nuevo identificador (id)
     */
    public Rating(){
        this.id = UUID.randomUUID().toString();
        votesList = new Hashtable<String, Hashtable<String, String>>();
    }

    /**
     * Constructor
     * Establece el id
     * @param id
     */
    public Rating(String id) {
        this.id = id;
        votesList = new Hashtable<String, Hashtable<String, String>>();
    }

    /**
     * Obtiene el id del objeto
     * @return id
     */
    public String getId() {
        return this.id;
    }

    @Exclude // Exclude porque firebase tiene problemas al intentar leer desde la base de damtos esta lista
    public Hashtable<String, Hashtable<String, String>> getVotesList() {
        return  votesList;
    }

    public void setVotesList(Hashtable<String, Hashtable<String, String>> votesList) {
        this.votesList =  votesList;
    }

    /**
     * Obtiene la puntuación (rating) del objeto (plato o restaurante)
     * @return Este método develve la puntuacion total
     */
    public float getPunctuation() {
        return punctuation;
    }

    /**
     * Calcula la media de votaciones para el rating
     */
    private void calculateMedia(){
        punctuation = 0;
        for (Hashtable<String, String> vote : votesList.values()) {
            punctuation += Float.parseFloat(vote.get("punctuation"));
        }
        punctuation = punctuation / votesList.size(); // Calculo de la media
    }

    /**
     * Agrega un nuevo voto, lo agrega en la lista y recalcula el rating (puntuación)
     * @param userId
     * @param punctuation
     * @param experience
     * @param date
     */
    @Exclude
    public void putPunctuation(final String userId, final float punctuation, final String experience, final String date){
        Hashtable<String, String> punctuationData = new Hashtable<String, String>();
        punctuationData.put("user_id", userId);
        punctuationData.put("punctuation", String.valueOf(punctuation));
        punctuationData.put("experience", experience);
        punctuationData.put("date", date);
        votesList.put(userId, punctuationData);
        calculateMedia(); // Al ingresar un nuevo valor, se debe alcualizar su valor principal
    }

    /**
     * Genera un mapa de datos del objeto (para editar)
     * @return
     */
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("votesList", votesList);
        result.put("punctuation",  punctuation);
        return result;
    }
}
