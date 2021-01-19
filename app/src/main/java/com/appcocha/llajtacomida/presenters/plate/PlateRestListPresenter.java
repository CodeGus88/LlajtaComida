package com.appcocha.llajtacomida.presenters.plate;

import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.models.plate.PlateModelRestList;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import java.util.ArrayList;

/**
 * Presentador, Lista de restaurantes con un determinado plato
 */
public class PlateRestListPresenter implements PlateInterface.presenterRestList {

    private PlateInterface.ViewRestlist viewRestlist;
    private PlateInterface.ModelRestList modelRestList;

    /**
     * Contructor, inicializa viewRestlist
     * @param viewRestlist
     */
    public PlateRestListPresenter(PlateInterface.ViewRestlist viewRestlist){
        this.viewRestlist = viewRestlist;
        modelRestList = new PlateModelRestList(this);
    }

    @Override
    public void showRestList(ArrayList<Restaurant> list) {
        viewRestlist.showRestList(list);
    }

    @Override
    public void filterRestaurantListWithPlate(String plateId) {
        modelRestList.filterRestaurantListWithPlate(plateId);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelRestList.stopRealtimeDatabase();
    }
}
