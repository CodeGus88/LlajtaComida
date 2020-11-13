package com.example.llajtacomida.presenters.mapsPresenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.views.mapsViews.GetLocationMapActivity;
import com.example.llajtacomida.views.mapsViews.SetLocationMapActivity;

public class MapPresenter {

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

}
