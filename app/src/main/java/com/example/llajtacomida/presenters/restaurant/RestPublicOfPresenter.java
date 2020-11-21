package com.example.llajtacomida.presenters.restaurant;

import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.example.llajtacomida.models.restaurant.RestPublicOfModel;
import com.example.llajtacomida.models.restaurant.Restaurant;

import java.util.ArrayList;

public class RestPublicOfPresenter implements RestaurantInterface.PresenterRestPublicOf {

    private RestaurantInterface.ModelRestPublicOf modelRestPublicOf;
    private RestaurantInterface.ViewRestPublicOf viewRestPublicOf;

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
