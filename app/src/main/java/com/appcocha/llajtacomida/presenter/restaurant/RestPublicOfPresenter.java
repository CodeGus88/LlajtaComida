package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.RestPublicOfModel;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import java.util.ArrayList;

/**
 * Presenter, restaurantes no publicados
 */
public class RestPublicOfPresenter implements RestaurantInterface.PresenterRestPublicOf {

    private RestaurantInterface.ModelRestPublicOf modelRestPublicOf;
    private RestaurantInterface.ViewRestPublicOf viewRestPublicOf;

    /**
     * Constructor, inicializa viewRestPublicOf
     * @param viewRestPublicOf
     */
    public RestPublicOfPresenter(RestaurantInterface.ViewRestPublicOf viewRestPublicOf){
        this.viewRestPublicOf = viewRestPublicOf;
        modelRestPublicOf = new RestPublicOfModel(this);
    }

    @Override
    public void showRestPublicOf(ArrayList<Restaurant> restaruantList) {
        viewRestPublicOf.showRestPublicOf(restaruantList);
    }

    @Override
    public void filterRestPublicOf(boolean isPublic) {
        modelRestPublicOf.filterRestPublicOf(isPublic);
    }

    @Override
    public void stopRealTimeDatabase() {
        modelRestPublicOf.stopRealTimeDatabase();
    }
}
