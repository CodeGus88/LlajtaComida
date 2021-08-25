package com.appcocha.llajtacomida.presenter.restaurant;

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
import com.appcocha.llajtacomida.view.restaurants.CreateRestaurantActivity;
import com.appcocha.llajtacomida.view.restaurants.EditRestaurantActivity;
import com.appcocha.llajtacomida.view.restaurants.RestaurantPublicOfActivity;
import com.appcocha.llajtacomida.view.restaurants.RestaurantViewActivity;
import com.appcocha.llajtacomida.view.restaurants.SetMenuActivity;
import com.appcocha.llajtacomida.view.restaurants.SetPromotionsActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

/**
 * Navegación de restaurante
 */
public class RestaurantNavegation {

    /**
     * Para crear un nuevo registro de restaurante
     * @param context
     */
    public static void showCreatedRestaurantView(Context context){
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
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
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el selector y recortador de imágenes
     * @param context
     */
    public static void showCropImage(AppCompatActivity context) {
        CropImage.activity()
            .setActivityTitle(context.getString(R.string.restaurant_image_crop_title))
            .setGuidelines(CropImageView.Guidelines.ON)
            .setRequestedSize(1023, 700)
            .setAspectRatio(3, 2)
            .start(context);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }


    /**
     * Comparte el contenido en las redes sociales
     * @param context
     * @param restaurant
     * @param other
     */
    public static void showShare(Context context, Restaurant restaurant, String other, ArrayList<Plate> plateList, ArrayList<String> menuPrice){
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String menu = context.getString(R.string.menu).toUpperCase()+"\n";
            for(Plate plate: plateList){
                String price = "";
                for(String data: menuPrice)
                    if(data.contains(plate.getId())){
                        price = Validation.getXWord(data, 2);
                        break;
                    }
                if(price.isEmpty()) price = StringValues.getDefaultPrice() + " " + context.getString(R.string.type_currency);
                else price += " " + context.getString(R.string.type_currency);
                menu += "\n - "+plate.getName() + " *" + price.replace("_", "|")+"*";
            }
            String content =
                    "http://maps.google.com/maps?daddr="+restaurant.getLatitude()+","+restaurant.getLongitude()+"&amp;ll="
                            //                "\n\n"+restaurant.getUrl()
                            +"\n\n*"+restaurant.getName().toUpperCase()+"*"
                            +"\n\n"+context.getString(R.string.tv_phone).toUpperCase()+" "+(restaurant.getPhone())
                            .replace(",", " - ")
                            .replace(".", " - ")
                            .replace("-", " - ")
                            .replace(";", " - ")
                            .replace(":", " - ")
                            +"\n\n"+context.getString(R.string.tv_address).toUpperCase()+" "+restaurant.getAddress()
                            +"\n\n" + menu
                            +"\n\n"+context.getString(R.string.tv_origin_desc).toUpperCase()
                            +"\n\n"+restaurant.getOriginAndDescription();
            if(!other.isEmpty()) content += "\n\n"+other;
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(Intent.createChooser(intent, "Share"));
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * Muestra la vista de un restaurante
     * @param context
     * @param restaurantId
     */
    public static void showRestaurantView(Context context, String restaurantId) {
        Intent intent = new Intent(context, RestaurantViewActivity.class);
        intent.putExtra("id", restaurantId);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
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
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra la vista para editar un restaurante
     * @param context
     * @param restaurant
     */
    public static void showEditRestaurantView(Context context, Restaurant restaurant) {
        Intent intent = new Intent(context, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Quitamos el mapa de la pila de actividades
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
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
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra la galería de imágenes de un restaurante
     * @param context
     * @param parentId
     * @param parentName
     */
    public static void showGalery(Context context, String parentId,  String parentName){
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("nodeCollectionName", "restaurants"); // Para saber a que entidad pertenece
        intent.putExtra("parentName", parentName); // El nombre de l restaurante
        intent.putExtra("parentId", parentId);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el gestor de menú de un restaurante
     * @param context
     * @param restaurant
     */
    public static void showMenu(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, SetMenuActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra el gestor de promociones de un restaurante
     * @param context
     * @param restaurant
     */
    public static void showPromotion(Context context, Restaurant restaurant){
        Intent intent = new Intent(context, SetPromotionsActivity.class);
        intent.putExtra("restaurant", restaurant);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

    /**
     * Muestra la lista de restaurantes no publicados
     * @param context
     */
    public static void showRestPublicOf(Context context){
        Intent intent = new Intent(context, RestaurantPublicOfActivity.class);
        context.startActivity(intent);
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }
}
