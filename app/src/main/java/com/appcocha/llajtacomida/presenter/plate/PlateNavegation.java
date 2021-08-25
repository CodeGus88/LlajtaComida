package com.appcocha.llajtacomida.presenter.plate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.appcocha.llajtacomida.view.images.ImagesActivity;
import com.appcocha.llajtacomida.view.plates.CreatePlateActivity;
import com.appcocha.llajtacomida.view.plates.EditPlateActivity;
import com.appcocha.llajtacomida.view.plates.PlateViewActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Navegación de los platos
 */
public class PlateNavegation {

    /**
     * Muestra el formulario para crear un nuevo plato
     * @param context
     */
    public static void showCreatedPlateView(Context context) {
        Intent intent = new Intent(context, CreatePlateActivity.class);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el formulario para editar un plato
     * @param context
     * @param plate
     */
    public static void showEditPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, EditPlateActivity.class);
        intent.putExtra("plate", plate);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra un plato
     * @param context
     * @param plate
     */
    public static void showPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, PlateViewActivity.class);
        intent.putExtra("id", plate.getId());
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el selecctor y recortador de imágenes
     * @param context
     */
    public static void showCropImage(AppCompatActivity context) {
        CropImage.activity()
                .setActivityTitle(context.getString(R.string.plate_image_crop_title))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(1023, 700)
                .setAspectRatio(3, 2)
                .start(context);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Comparte el contenido en las redes sociales
     * @param context
     * @param plate
     * @param other
     */
    public static void showShare(Context context, Plate plate, String other, ArrayList<Restaurant> restList, Hashtable priceList){
        try{
            ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
            restaurantList.addAll(restList);
            restaurantList = Validation.getRestaurantsOrderByPunctuation(restaurantList);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String restaurants = "";
            String content = "*"+plate.getName().toUpperCase()+"*";
            if(restaurantList.size()<=5) content += "\n\n"+context.getString(R.string.tv_ingredients_plate_view).toUpperCase()+"\n\n"+
                    plate.getIngredients()+"\n\n"+
                    context.getString(R.string.tv_origin_plate_view).toUpperCase() +"\n\n"+
                    plate.getOrigin();
            if(restaurantList.size()>0){
                content += "\n\n"+context.getString(R.string.tv_rest_with_this_plate)+":";
                for (Restaurant restaurant: restaurantList){
                    String price = "";
                    if(!priceList.get(restaurant.getId()).toString().isEmpty()) price = priceList.get(restaurant.getId()).toString().replace("_", "|");
                    else price = StringValues.getDefaultPrice();
                    price += " "+context.getString(R.string.type_currency);
                    restaurants += "\n\n''" + restaurant.getName() +"''\n("+restaurant.getPhone()
                            .replace(",", " - ")
                            .replace(".", " - ")
                            .replace("-", " - ")
                            .replace(";", " - ")
                            .replace(":", " - ")+")\n"
                            +restaurant.getAddress()+"\n"
                            +plate.getName()+": *"+ price+"*\n"+
                            "http://maps.google.com/maps?daddr="+restaurant.getLatitude()+","+restaurant.getLongitude()+"&amp;ll=";
                }
                content += restaurants;
            }

            if(!other.isEmpty()) content += "\n\n"+other;
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(Intent.createChooser(intent, "Share"));
        }catch (Exception e){
            Log.e("Error5454", e.getMessage());
        }
    }

    /**
     * Muestra la galería de imágenes (gestor de imágenes)
     * @param context
     * @param parentId
     * @param parentName
     */
    public static void showGalery(Context context, String parentId,  String parentName){
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("nodeCollectionName", "plates"); // Para saber a que entidad pertenece
        intent.putExtra("parentName", parentName); // El nombre el plato
        intent.putExtra("parentId", parentId);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

}
