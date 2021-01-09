package com.appcocha.llajtacomida.presenters.map;

import android.content.Context;
import android.content.Intent;

import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.views.maps.GetLocationMapActivity;
import com.appcocha.llajtacomida.views.maps.SetAllLocationMapActivity;
import com.appcocha.llajtacomida.views.maps.SetLocationMapActivity;

import java.util.ArrayList;

public class MapNavegation {

    public static void showGetLocationMapActivity(Context context, Restaurant restaurant, String verb, String uri){
        Intent intent = new Intent(context, GetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("verb", verb);
        intent.putExtra("uri", uri);
        context.startActivity(intent);
    }

    public static void showSetLocationMapActivity(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, SetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
    }

    public static void showSetAllLocationMapActivity(Context context, Plate plate, ArrayList<Restaurant> restaurantList){
        Intent intent = new Intent(context, SetAllLocationMapActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("plate", plate);
        context.startActivity(intent);
    }

}
