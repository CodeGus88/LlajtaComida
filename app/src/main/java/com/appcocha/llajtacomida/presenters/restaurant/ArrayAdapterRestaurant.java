package com.appcocha.llajtacomida.presenters.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Adaptador
 */
public class ArrayAdapterRestaurant extends ArrayAdapter<Restaurant> {

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
    public ArrayAdapterRestaurant(@NonNull Context context, int resource, @NonNull ArrayList <Restaurant> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.restaurantListCopy = new ArrayList<Restaurant>();
        this.restaurantList = objects;
        this.restaurantListCopy.addAll(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        }
        try {
            ImageView ivPhotoItem = (ImageView) view.findViewById(R.id.ivPhotoItem);
            TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
            TextView tvResumeItem = (TextView) view.findViewById(R.id.tvResumeItem);
            TextView tvRating = (TextView) view.findViewById(R.id.tvRating);
            DecimalFormat decimalFormat = new DecimalFormat("0.0"); // para que tenda solo un decimal
            tvRating.setText(String.valueOf(decimalFormat.format(restaurantList.get(position).getPunctuation())));
//            tvRating.setText(String.valueOf(restaurantList.get(position).getPunctuation()));
            Glide.with(context).load(restaurantList.get(position).getUrl()).into(ivPhotoItem);
            tvTitleItem.setText(restaurantList.get(position).getName());
            tvResumeItem.setText(restaurantList.get(position).getAddress() + "\n"+restaurantList.get(position).getOriginAndDescription());
        }catch(Exception e){
            Log.e("Error: ", "------------------------------------------------> "+e.getMessage());
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
     * Busca el texto en el toString del objeto correspondiente
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
}
