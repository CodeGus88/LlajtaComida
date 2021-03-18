package com.appcocha.llajtacomida.presenters.restaurant;

import android.util.Log;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.restaurant.menu.Menu;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.menu.SetMenuListModel;

import java.util.ArrayList;

/**
 * Presentador, gestor de menú
 */
public class SetMenuListPresenter implements RestaurantInterface.PresenterSetMenuList {

    private RestaurantInterface.ViewSetMenuList viewSetMenuList;
    private RestaurantInterface.ModelSetMenuList modelSetMenuList;

    /**
     * Constructor, establece viewSetMenuList
     * @param viewSetMenuList
     */
    public SetMenuListPresenter(RestaurantInterface.ViewSetMenuList viewSetMenuList){
        this.viewSetMenuList = viewSetMenuList;
        modelSetMenuList = new SetMenuListModel(this);
    }

    @Override
    public void showSetMenuList(ArrayList<Plate> plateList, Menu menu) {
        viewSetMenuList.showSetMenuList(plateList, menu);
    }

    @Override
    public void searchSetMenuList(String restaurantId) {
        modelSetMenuList.searchSetMenuList(restaurantId);
    }

    @Override
    public void saveMenuList(String restaurantId, Menu menu) { //Context context,
        modelSetMenuList.saveMenuList(restaurantId, menu);
    }

    @Override
    public void stopRealTimeDatabase() {
        modelSetMenuList.stopRealTimeDatabase();
    }
}
