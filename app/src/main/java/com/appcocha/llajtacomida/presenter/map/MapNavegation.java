package com.appcocha.llajtacomida.presenter.map;

import android.content.Context;
import android.content.Intent;

import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.view.maps.GetLocationMapActivity;
import com.appcocha.llajtacomida.view.maps.SetAllLocationMapActivity;
import com.appcocha.llajtacomida.view.maps.SetLocationMapActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

/**
 * Navegacjión del los mapas
 */
public class MapNavegation {

    /**
     * Muestra el mapa para establecer las coordenadas (restaurante) el mapa devuelve las coordenadas por eso get
     * @param context
     * @param restaurant
     * @param verb
     * @param uri
     */
    public static void showGetLocationMapActivity(Context context, Restaurant restaurant, String verb, String uri){
        Intent intent = new Intent(context, GetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("verb", verb);
        intent.putExtra("uri", uri);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el mapa para visualizar la ubicación de un restaurante en el mapa (Recibe coordenadas, por eso set)
     * Muestra la ubicación actual (GPS)
     * @param context
     * @param restaurant
     */
    public static void showSetLocationMapActivity(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, SetLocationMapActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el mapa con las ubicaciones de n restaurantes (restaurante con un determinado plato en su menú)
     * Muestra la ubicación actual (GPS)
     * @param context
     * @param plate
     * @param restaurantList
     */
    public static void showSetAllLocationMapActivity(Context context, Plate plate, ArrayList<Restaurant> restaurantList){
        Intent intent = new Intent(context, SetAllLocationMapActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("plate", plate);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

}
