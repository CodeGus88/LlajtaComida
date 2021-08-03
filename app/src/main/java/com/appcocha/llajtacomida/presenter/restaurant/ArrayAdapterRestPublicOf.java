package com.appcocha.llajtacomida.presenter.restaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.RestaurantGestorDB;
import com.appcocha.llajtacomida.presenter.map.MapNavegation;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.appcocha.llajtacomida.view.restaurants.RestaurantPublicOfActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Adaptador
 */
public class ArrayAdapterRestPublicOf  extends ArrayAdapter<Restaurant>{
    private ArrayList<Restaurant> restaurantList;
    private ArrayList<Restaurant> restaurantListCopy;
    private Context context;
    private int resource;

    /**
     * Constructor, inicializa context, resource, objects
     * @param context
     * @param resource
     * @param objects
     */
    public ArrayAdapterRestPublicOf(@NonNull Context context, int resource, @NonNull ArrayList <Restaurant> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.restaurantListCopy = new ArrayList<Restaurant>();
        this.restaurantList = objects;
        this.restaurantListCopy.addAll(objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        }
        try {
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
            TextView tvOwnerName = (TextView) view.findViewById(R.id.tvOwnerName);
            TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
            TextView tvDescription = (TextView) view.findViewById(R.id.tvRestInformation);
            TextView tvId = (TextView) view.findViewById(R.id.tvId);
            TextView tvAuthorId = (TextView) view.findViewById(R.id.tvAuthorId);

            // Botones
            Button btnPublish = (Button) view.findViewById(R.id.btnPublish);
            Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
            Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
            Button btnAuthor = (Button) view.findViewById(R.id.btnAuthor);
            // Cargar...
            if (!restaurantList.get(position).getName().isEmpty()) tvTitle.setText(restaurantList.get(position).getName());
            else tvTitle.setText(null);
            if (!restaurantList.get(position).getUrl().isEmpty()) Glide.with(context).load(restaurantList.get(position).getUrl()).into(ivImage);
            else Glide.with(context).load("null").into(ivImage);
            if (!restaurantList.get(position).getOwnerName().isEmpty()) tvOwnerName.setText(restaurantList.get(position).getOwnerName());
            else tvOwnerName.setText(null);
            if (!restaurantList.get(position).getAddress().isEmpty()) tvAddress.setText(restaurantList.get(position).getAddress());
            else tvAddress.setText(null);
            if (!restaurantList.get(position).getPhone().isEmpty()) tvPhone.setText(restaurantList.get(position).getPhone().replace(",", " - ").replace(".", " - ").replace("-", " - "));
            else tvPhone.setText(null);
            if (!restaurantList.get(position).getOriginAndDescription().isEmpty()) tvDescription.setText(restaurantList.get(position).getOriginAndDescription());
            else tvDescription.setText(null);
            if (!restaurantList.get(position).getId().isEmpty()) tvId.setText(restaurantList.get(position).getId());
            else tvId.setText(null);
            if (!restaurantList.get(position).getAuthor().isEmpty()) tvAuthorId.setText(context.getString(R.string.tv_author_id) + " " + restaurantList.get(position).getAuthor());
            else tvAuthorId.setText(null);
            // Cargar accion de los botones (es necesario que sean anónimas)
            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sound.playClick();
                    restaurantList.get(position).setPublic(true);
                    if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.PUBLISH_ON_OF_RESTAURANT))
                        publishRestaurant(restaurantList.get(position));
                    else Toast.makeText(context, context.getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sound.playClick();
                    deleteRestaurante(restaurantList.get(position));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sound.playClick();
                    RestaurantNavegation.showEditRestaurantView(context, restaurantList.get(position));
                }
            });
            tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sound.playClick();
                    MapNavegation.showSetLocationMapActivity(context, restaurantList.get(position));
                }
            });

            btnAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sound.playClick();
                    RestaurantPublicOfActivity.loadAuthor(restaurantList.get(position).getAuthor());
                }
            });
        }catch(Exception e){
            Log.e("Error: ", e.getMessage());
        }

        return view;
    }

    /**
     *  Filtra los datos del adaptador
     */
    public void filter(String texto, int previousLentg) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() == previousLentg){ // Si se presionó borrar
                restaurantList.clear();
                restaurantList.addAll(restaurantListCopy);
            }
            search(texto);
        }else if(restaurantList.size() != restaurantListCopy.size()){
            if(restaurantList.size() > 0){
                restaurantList.clear();
            }
            restaurantList.addAll(restaurantListCopy);
        }
        notifyDataSetChanged();
    }

    /**
     * Busca el texto en el toString del objeto
     * @param texto
     */
    public void search(String texto){
        int i = 0;
        while (i < restaurantList.size()) {
            String string = restaurantList.get(i).toString().toLowerCase();
            if (!string.contains(texto)) {
                restaurantList.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * Establece publicar o dejar de publicar un restaurante
     * @param restaurant
     */
    public void publishRestaurant(final Restaurant restaurant){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_message_publish)
                .setPositiveButton(R.string.btn_continue, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Sound.playClick();
//                        byte [] img = null;
//                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant, null);
                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant);
                        restaurantDatabase.upDate();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Sound.playClick();
                    }
                });
        builder.show();
    }

    /**
     * Elimina un restaurante "no" publicado
     * @param restaurant
     */
    public void deleteRestaurante(final Restaurant restaurant){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_message_restaurant_delete)
                .setPositiveButton(R.string.btn_continue, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Sound.playThrow();
//                        byte [] img = null;
//                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant, null);
                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant);
                        restaurantDatabase.delete();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Sound.playClick();
                    }
                });
        builder.show();
    }
}
