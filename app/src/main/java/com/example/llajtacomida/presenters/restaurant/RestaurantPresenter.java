package com.example.llajtacomida.presenters.restaurant;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.models.restaurant.RestaurantDatabase;
import com.example.llajtacomida.models.restaurant.RestaurantModel;

import java.util.ArrayList;

public class RestaurantPresenter implements RestaurantInterface.PresenterRestaurant {

    private RestaurantInterface.ViewRestaurant viewRestaurant;
    private RestaurantInterface.ModelRestaurant modelRestaurant;

    public RestaurantPresenter(RestaurantInterface.ViewRestaurant viewRestaurant){
        this.viewRestaurant = viewRestaurant;
        modelRestaurant = new RestaurantModel(this);
    }

    @Override
    public void showRestaurant(Restaurant restaurant) {
        viewRestaurant.showRestaurant(restaurant);
    }

    @Override
    public void showRestaurantList(ArrayList<Restaurant> restaurantList) {
        viewRestaurant.showRestaurantList(restaurantList);
    }

    @Override
    public void searchRestaurant(String restaurantId) {
        modelRestaurant.searchRestaurant(restaurantId);
    }

    @Override
    public void loadRestaurantList() {
        modelRestaurant.loadRestaurantList();
    }

    @Override
    public void stopRealtimeDatabse() {
        modelRestaurant.stopRealtimeDatabse();
    }


    /**
     * Publica o despublica el restaurante
     * @param context
     * @param restaurant
     */
    public static void update(final Context context, final Restaurant restaurant){
        String message;
        if(restaurant.isPublic()){
            message = context.getString(R.string.confirm_message_publish_of);
        }else{
            message = context.getString(R.string.confirm_message_publish);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.btn_continue, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RestaurantDatabase restaurantDatabase = new RestaurantDatabase(context, restaurant);
                        restaurant.setPublic(!restaurant.isPublic());
                        restaurantDatabase.upDate();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }
}
