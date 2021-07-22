package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.promotion.RestaurantWithPromotionModel;

import java.util.ArrayList;

public class RestaurantsWithPromotionPresenter implements RestaurantInterface.PresenterRestaurantWithPromotion {

    private RestaurantInterface.ViewRestaurantsWithPromotion viewRestaurantsWithPromotion;
    private RestaurantWithPromotionModel restaurantWithPromotionModel;

    public RestaurantsWithPromotionPresenter(RestaurantInterface.ViewRestaurantsWithPromotion viewRestaurantsWithPromotion){
        this.viewRestaurantsWithPromotion = viewRestaurantsWithPromotion;
        restaurantWithPromotionModel = new RestaurantWithPromotionModel(this);
    }

    @Override
    public void showRestaurantWithPromotion(ArrayList<Restaurant> restaurantList) {
        viewRestaurantsWithPromotion.showRestaurantsWithPromotion(restaurantList);
    }

    @Override
    public void filterRestaurantWithPromotion() {
        restaurantWithPromotionModel.filterRestaurantWithPromotion();
    }

    @Override
    public void stopRealTimeDatabase() {
        restaurantWithPromotionModel.stopRealTimeDatabase();
    }
}
