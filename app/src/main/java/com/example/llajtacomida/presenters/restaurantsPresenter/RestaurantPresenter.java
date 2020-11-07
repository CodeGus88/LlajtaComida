package com.example.llajtacomida.presenters.restaurantsPresenter;

import android.content.Context;
import android.content.Intent;

import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.views.restaurantsViews.CreateRestaurantActivity;

public class RestaurantPresenter {

    public static void showCreatedRestaurantView(Context context){
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        context.startActivity(intent);
    }

    /**
     * Este método se usa para set del mapa
     * @param context
     * @param restaurant
     */
    public static void showCreatedRestaurantView(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
    }

}
