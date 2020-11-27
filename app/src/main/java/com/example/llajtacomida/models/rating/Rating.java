package com.example.llajtacomida.models.rating;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class Rating {

    private String id;
    private Hashtable<String, Hashtable<String, String>> votesList; // /user_id/punctuation/experience/date
    private float punctuation;

    public Rating(){
        this.id = UUID.randomUUID().toString();
        votesList = new Hashtable<String, Hashtable<String, String>>();
    }

    public Rating(String id) {
        this.id = id;
        votesList = new Hashtable<String, Hashtable<String, String>>();
    }

    public String getId() {
        return id;
    }

//    public Hashtable<String, Hashtable<String, String>> getPunctuationList() {
    @Exclude // Exclude porque firebase tiene problemas al intentar leer desde la base de damtos esta lista
    public Hashtable<String, Hashtable<String, String>> getVotesList() {
        return  votesList;
    }

    public void setVotesList(Hashtable<String, Hashtable<String, String>> votesList) {
        this.votesList =  votesList;
    }

    /**
     *
     * @return Este método develve la puntuacion total
     */
    public float getPunctuation() {
        return punctuation;
    }

    private void calculateMedia(){
        punctuation = 0;
        for (Hashtable<String, String> vote : votesList.values()) {
            punctuation += Float.parseFloat(vote.get("punctuation"));
        }
        punctuation = punctuation / votesList.size(); // Calculo de la media
    }



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

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("votesList", votesList);
        result.put("punctuation",  punctuation);
        return result;
    }
}
