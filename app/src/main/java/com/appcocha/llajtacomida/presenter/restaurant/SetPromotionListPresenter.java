package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
import com.appcocha.llajtacomida.model.restaurant.promotion.SetPromotionListModel;

import java.util.ArrayList;

/**
 * Presentador, gestor de menú
 */
public class SetPromotionListPresenter implements RestaurantInterface.PresenterSetPromotionList {

    private RestaurantInterface.ViewSetPromotionList viewSetPromotionList;
    private RestaurantInterface.ModelSetPromotionList modelSetPromotionList;

    /**
     * Constructor, establece viewSetMenuList
     * @param viewSetPromotionList
     */
    public SetPromotionListPresenter(RestaurantInterface.ViewSetPromotionList viewSetPromotionList){
        this.viewSetPromotionList = viewSetPromotionList;
        modelSetPromotionList = new SetPromotionListModel(this);
    }

    @Override
    public void showSetPromotionList(ArrayList<Plate> plateList, Menu menu, Promotion promotion){
        if (menu == null) {
            menu = new Menu();
            menu.setName("Local");
        }
        if(promotion == null) promotion = new Promotion();
        viewSetPromotionList.showSetPromotionList(plateList, menu, promotion);
    }

    @Override
    public void searchSetPromotionList(String restaurantId){
        modelSetPromotionList.searchSetPromotionList(restaurantId);
    }

    @Override
    public void savePromotionList(String restaurantId, Promotion promotion){
        if(promotion.getPromotionList().size() == 0) promotion.setActive(false);
        modelSetPromotionList.savePromotionList(restaurantId, promotion);
    }

    @Override
    public void stopRealTimeDatabase() {
        modelSetPromotionList.stopRealTimeDatabase();
    }
}
