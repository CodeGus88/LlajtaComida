package com.example.llajtacomida.presenters.mapsPresenter;

import android.content.Context;
import android.content.Intent;

import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.views.mapsViews.GetLocationMapActivity;

public class MapToGetLocationPresenter {

    public static void showGetLocationMapActivity(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, GetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
    }

}
