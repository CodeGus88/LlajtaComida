package com.appcocha.llajtacomida.presenters.restaurant;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.models.restaurant.RestaurantGestorDB;
import com.appcocha.llajtacomida.presenters.map.MapNavegation;
import com.appcocha.llajtacomida.views.restaurants.RestaurantPublicOfActivity;

import java.util.ArrayList;

/**
 * Adaptador
 */
public class ArrayAdapterRestPublicOf  extends ArrayAdapter<Restaurant> {
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
            if (!restaurantList.get(position).getUrl().isEmpty()) Glide.with(context).load(restaurantList.get(position).getUrl()).into(ivImage);
            if (!restaurantList.get(position).getOwnerName().isEmpty()) tvOwnerName.setText(restaurantList.get(position).getOwnerName());
            if (!restaurantList.get(position).getAddress().isEmpty()) tvAddress.setText(restaurantList.get(position).getAddress());
            if (!restaurantList.get(position).getPhone().isEmpty())tvPhone.setText(restaurantList.get(position).getPhone().replace(",", " - ").replace(".", " - ").replace("-", " - "));
            if (!restaurantList.get(position).getOriginAndDescription().isEmpty()) tvDescription.setText(restaurantList.get(position).getOriginAndDescription());
            if (!restaurantList.get(position).getId().isEmpty()) tvId.setText(restaurantList.get(position).getId());
            if (!restaurantList.get(position).getAuthor().isEmpty()) tvAuthorId.setText(context.getString(R.string.tv_author_id) + " " + restaurantList.get(position).getAuthor());
            // Cargar accion de los botones
            btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restaurantList.get(position).setPublic(true);
                    publishRestaurant(restaurantList.get(position));
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRestaurante(restaurantList.get(position));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestaurantNavegation.showEditRestaurantView(context, restaurantList.get(position));
                }
            });
            tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapNavegation.showSetLocationMapActivity(context, restaurantList.get(position));
                }
            });

            btnAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        byte [] img = null;
                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant, img);
                        restaurantDatabase.upDate();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
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
                        byte [] img = null;
                        RestaurantGestorDB restaurantDatabase = new RestaurantGestorDB(context, restaurant, img);
                        restaurantDatabase.delete();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
    }
}
