package com.example.llajtacomida.presenters.mapsPresenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.views.mapsViews.GetLocationMapActivity;

public class MapPresenter {

    public static void showGetLocationMapActivity(Context context, Restaurant restaurant, String verb, String uri){
        Intent intent = new Intent(context, GetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("verb", verb);
        intent.putExtra("uri", uri);
        context.startActivity(intent);
    }

}
