package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
import com.appcocha.llajtacomida.model.restaurant.promotion.RestaurantWithPromotionModel;

import java.util.ArrayList;

public class RestaurantWithPromotionPresenter implements RestaurantInterface.PresenterRestaurantWithPromotion {
    public RestaurantWithPromotionPresenter(){
        RestaurantWithPromotionModel restaurantWithPromotionModel = new RestaurantWithPromotionModel(this);
        restaurantWithPromotionModel.filterRestaurantWithPromotion();
    }

    @Override
    public void showRestaurantWithPromotion(ArrayList<Restaurant> restaurantList, ArrayList<Promotion> promotionList) {

    }

    @Override
    public void filterRestaurantWithPromotion() {

    }
}
