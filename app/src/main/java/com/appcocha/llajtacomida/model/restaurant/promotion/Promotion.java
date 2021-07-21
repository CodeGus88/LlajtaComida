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
        PromotionElement pe = new PromotionElement();
        for (PromotionElement promotionElement: promotionList) {
            if(promotionElement.getPlateId().equals(id)){
                pe = promotionElement;
                break;
            }
        }
        return pe;
    }
}
