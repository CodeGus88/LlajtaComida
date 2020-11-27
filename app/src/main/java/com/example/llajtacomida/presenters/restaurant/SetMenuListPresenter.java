package com.example.llajtacomida.presenters.restaurant;

import android.content.Context;

import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.example.llajtacomida.models.restaurant.menu.Menu;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.menu.SetMenuListModel;

import java.util.ArrayList;

public class SetMenuListPresenter implements RestaurantInterface.PresenterSetMenuList {

    private RestaurantInterface.ViewSetMenuList viewSetMenuList;
    private RestaurantInterface.ModelSetMenuList modelSetMenuList;

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
    public void saveMenuList(Context context, String restaurantId, Menu menu) {
        modelSetMenuList.saveMenuList(context, restaurantId, menu);
    }

    @Override
    public void stopRealTimeDatabase() {
        modelSetMenuList.stopRealTimeDatabase();
    }
}
