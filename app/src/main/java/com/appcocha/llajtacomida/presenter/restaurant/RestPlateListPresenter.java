package com.appcocha.llajtacomida.presenter.restaurant;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.RestaurantPlateListModel;

import java.util.ArrayList;

/**
 * Presentador, menú de un restaurante
 */
public class RestPlateListPresenter implements RestaurantInterface.PresenterPlateList {

    private RestaurantInterface.ModelPlateList modelPlateList;
    private RestaurantInterface.ViewPlateList viewPlateList;

    /**
     * Constructor, establece viewPlateList
     * @param viewPlateList
     */
    public RestPlateListPresenter(RestaurantInterface.ViewPlateList viewPlateList){
        this.viewPlateList = viewPlateList;
        modelPlateList = new RestaurantPlateListModel(this);
    }

    @Override
    public void showPlateList(ArrayList<Plate> list, ArrayList<String> priceList) {
        viewPlateList.showPlateList(list, priceList);
    }

    @Override
    public void filterPlateListInMenu(String restaurantId) {
        modelPlateList.filterPlateListInMenu(restaurantId);
    }
    @Override
    public void stopRealtimeDatabse(){
        modelPlateList.stopRealtimeDatabse();
    }
}
