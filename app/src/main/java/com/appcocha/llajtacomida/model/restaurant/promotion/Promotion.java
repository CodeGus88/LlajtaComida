package com.appcocha.llajtacomida.model.restaurant.promotion;

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
        this.active = true;
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

    public boolean isActive() {
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

//    /**
//     * Genera un mapa de datos del objeto (para editar)
//     * @return result
//     */
//    @Exclude
//    public Map<String, Object> toMap(){
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("id", id);
//        result.put("active", active);
//        result.put("promotionList",  promotionList);
//        return result;
//    }
}
