package com.appcocha.llajtacomida.presenters.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.views.images.ImagesActivity;
import com.appcocha.llajtacomida.views.restaurants.CreateRestaurantActivity;
import com.appcocha.llajtacomida.views.restaurants.EditRestaurantActivity;
import com.appcocha.llajtacomida.views.restaurants.RestaurantPublicOfActivity;
import com.appcocha.llajtacomida.views.restaurants.SetMenuActivity;
import com.appcocha.llajtacomida.views.restaurants.RestaurantViewActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RestaurantNavegation {

    /**
     * Para crear un nuevo registro de restaurante
     * @param context
     */
    public static void showCreatedRestaurantView(Context context){
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        context.startActivity(intent);
    }

    /**
     * Este método se usa para set del mapa
     * @param context
     * @param restaurant
     */
    public static void showCreatedRestaurantView(Context context, Restaurant restaurant, String uri){
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("uri", uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 0Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
    }

    public static void showCropImage(Activity context) {
        CropImage.activity()
            .setActivityTitle(context.getString(R.string.restaurant_image_crop_title))
            .setGuidelines(CropImageView.Guidelines.ON)
            .setRequestedSize(1023, 700)
            .setAspectRatio(3, 2)
            .start(context);
    }

    public static void showRestaurantView(Context context, String restaurantId) {
        Intent intent = new Intent(context, RestaurantViewActivity.class);
        intent.putExtra("id", restaurantId);
        context.startActivity(intent);
    }

    /**
     * Este método se usa para volver del setMenuActivity
     * @param context
     * @param restaurant
     */
    public static void showRestaurantViewFromSetMenu(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, RestaurantViewActivity.class);
        intent.putExtra("id", restaurant.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
    }


    public static void showEditRestaurantView(Context context, Restaurant restaurant) {
        Intent intent = new Intent(context, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
    }

    /**
     * Para volver desde el mapa (Si el formulario contenia una imagen caragda desde galeria)
     * @param context
     * @param restaurant
     * @param uri
     */
    public static void showEditRestaurantView(Context context, Restaurant restaurant, String uri) {
        Intent intent = new Intent(context, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("uri", uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
    }

    public static void showGalery(Context context, String parentId,  String parentName){
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("nodeCollectionName", "restaurants"); // Para saber a que entidad pertenece
        intent.putExtra("parentName", parentName); // El nombre de l restaurante
        intent.putExtra("parentId", parentId);
        context.startActivity(intent);
    }

    public static void showMenu(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, SetMenuActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
    }

    public static void showRestPublicOf(Context context){
        Intent intent = new Intent(context, RestaurantPublicOfActivity.class);
        context.startActivity(intent);
    }
}
