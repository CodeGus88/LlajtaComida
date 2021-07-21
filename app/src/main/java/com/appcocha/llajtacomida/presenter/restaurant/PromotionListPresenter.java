package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionElement;
import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionListModel;

import java.util.ArrayList;

/**
 * Presentador, gestor de menú
 */
public class PromotionListPresenter implements RestaurantInterface.PresenterPromotionPlateList {

    private RestaurantInterface.ViewPromotionPlateList viewPromotionPlateList;
    private RestaurantInterface.ModelPromotionPlateList modelPromotionPlateList;

    /**
     * Constructor, establece viewSetMenuList
     * @param viewPromotionPlateList
     */
    public PromotionListPresenter(RestaurantInterface.ViewPromotionPlateList viewPromotionPlateList){
        this.viewPromotionPlateList = viewPromotionPlateList;
        modelPromotionPlateList = new PromotionListModel(this);
    }

    @Override
    public void showPromotionPlateList(ArrayList<Plate> list, Menu menu, Promotion promotion) {
        int count = 0;
        ArrayList<Plate> aux = new ArrayList<>();
        aux.addAll(list);
        while(count<aux.size()){
            if(!existInPromotion(aux.get(count).getId(), promotion)){
                aux.remove(count);
            }else{
                count ++;
            }
        }
        viewPromotionPlateList.showPromotionPlateList(aux, menu, promotion);
    }

    /**
     * Verifica si un plato de promoción existe en la lista de platos
     * @param id
     * @param promotion
     * @return exist
     */
    private boolean existInPromotion(String id, Promotion promotion){
        boolean exist = false;
        if(promotion!=null){
            for(PromotionElement element: promotion.getPromotionList()){
                if(element.getPlateId().equals(id)){
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    @Override
    public void filterPlateListInPromotion(String restaurantId) {
        modelPromotionPlateList.filterPlateListInPromotion(restaurantId);
    }

    @Override
    public void stopRealtimeDatabse() {
        modelPromotionPlateList.stopRealTimeDatabase();
    }
}
