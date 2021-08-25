package com.appcocha.llajtacomida.model.restaurant.promotion;


public class PromotionElement {

    private String plateId, title, description;
    private boolean showOldPrice;
    private float price;

    public PromotionElement(){
        plateId = "";
        title = "";
        description = "";
        price = -1;
        showOldPrice = false;
    }

    public PromotionElement(String plateId, String title, String description, float price, boolean showOldPrice) {
        this.plateId = plateId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.showOldPrice = showOldPrice;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPlateId() {
        return plateId;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowOldPrice() {
        return showOldPrice;
    }

    public void setShowOldPrice(boolean showOldPrice) {
        this.showOldPrice = showOldPrice;
    }

//    /**
//     * Genera un mapa de datos del objeto (para editar)
//     * @return result
//     */
//    @Exclude
//    public Map<String, Object> toMap(){
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("plateId", plateId);
//        result.put("title", title);
//        result.put("description", description);
//        result.put("price",  price);
//        return result;
//    }
}
