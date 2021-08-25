package com.appcocha.llajtacomida.model.restaurant.promotion;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.UUID;

public class Promotion {
    private String id;
    private boolean active;
    private ArrayList<PromotionElement> promotionList;

    /**
     * Constructor, genera un nuevo id
     */
    public Promotion(){
        this.id = UUID.randomUUID().toString();
        this.active = false;
        this.promotionList = new ArrayList<PromotionElement>();
    }

    public Promotion(String id, boolean active, ArrayList<PromotionElement> promotion) {
        this.id = id;
        this.active = active;
        this.promotionList = new ArrayList<PromotionElement>();
        this.promotionList.addAll(promotion);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<PromotionElement> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(ArrayList<PromotionElement> promotionList) {
        this.promotionList.clear();
        this.promotionList.addAll(promotionList);
    }

    /**
     * Devuelve una promoción
     * @param id
     * @return promotionElement
     */
    @Exclude
    public PromotionElement getPromotionElement(String id){
        PromotionElement promEl = null;
        for (PromotionElement promotionElement: promotionList) {
            if(promotionElement.getPlateId().equals(id)){
                promEl = promotionElement;
                break;
            }
        }
        return promEl;
    }


    /**
     * Agrega una promoción
     * @param promotionElement
     */
    @Exclude
    public void addPromotionElement(PromotionElement promotionElement){
        promotionList.add(promotionElement);
    }

    /**
     * Elimina una promoción
     * @param plateId
     */
    @Exclude
    public void removePromotionElement(String plateId){
        for(int i = 0; i<promotionList.size(); i++){
            if(promotionList.get(i).getPlateId().equals(plateId)){
                promotionList.remove(i);
            }
        }
    }

    /**
     * Verifica si un elemento promoción existe en la lista
     * @param plateId
     * @return
     */
    @Exclude
    public boolean existInList(String plateId){
        boolean exist = false;
        for (int i = 0; i < promotionList.size(); i++) {
            if(plateId.equals(promotionList.get(i).getPlateId())){
                exist = true;
                break;
            }
        }
        return exist;
    }
}
