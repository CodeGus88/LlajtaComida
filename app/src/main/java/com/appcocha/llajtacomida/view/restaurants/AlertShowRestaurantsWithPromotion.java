package com.appcocha.llajtacomida.view.restaurants;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterRestaurant;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantsWithPromotionPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.appcocha.llajtacomida.view.main.MainActivity;

import java.util.ArrayList;


public class AlertShowRestaurantsWithPromotion implements View.OnClickListener, RestaurantInterface.ViewRestaurantsWithPromotion {

    private AlertDialog alertDialog;
    private MainActivity activity;
    private ListView lvRestaurants;
    private Button btnClose;
    private ArrayAdapterRestaurant arrayAdapterRestaurant;
    private ArrayList<Restaurant> restaurantList;
    private RestaurantInterface.PresenterRestaurantWithPromotion presenterRestaurantWithPromotion;

    public AlertShowRestaurantsWithPromotion(MainActivity activity){
        this.activity = activity;
        View viewAlert = activity.getLayoutInflater().inflate(R.layout.alert_show_restaurant_promotion_list, null);
        lvRestaurants = (ListView) viewAlert.findViewById(R.id.lvRestaurants);
        btnClose = (Button) viewAlert.findViewById(R.id.btnClose);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(viewAlert);
        alertDialog = builder.create();
        alertDialog.setView(viewAlert);
        btnClose.setOnClickListener(this);
        restaurantList = new ArrayList<Restaurant>();
        // lv
        presenterRestaurantWithPromotion = new RestaurantsWithPromotionPresenter(this);
        presenterRestaurantWithPromotion.filterRestaurantWithPromotion();
        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                alertDialog.dismiss();
                RestaurantNavegation.showRestaurantView(activity, restaurantList.get(position).getId());
            }
        });
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                presenterRestaurantWithPromotion.stopRealTimeDatabase();
                alertDialog.dismiss();
                break;
        }
    }

    @Override
    public void showRestaurantsWithPromotion(ArrayList<Restaurant> restaurantList){
        restaurantList = Validation.getRestaurantsOrderByPunctuation(restaurantList);
        this.restaurantList = restaurantList;
        arrayAdapterRestaurant = new ArrayAdapterRestaurant(activity, R.layout.adapter_element_list, restaurantList);
        lvRestaurants.setAdapter(arrayAdapterRestaurant);
    }
}
