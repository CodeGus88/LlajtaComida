package com.example.llajtacomida.views.restaurantsViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Restaurant;
import java.util.ArrayList;


public class ArrayAdapterRestaurant extends ArrayAdapter<Restaurant> {

    private ArrayList<Restaurant> restaurantList;
    private ArrayList<Restaurant> restaurantListCopy;
    private Context context;
    private int resource;

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
        ImageView ivPhotoItem = (ImageView) view.findViewById(R.id.ivPhotoItem);
        TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
        TextView tvResumeItem = (TextView) view.findViewById(R.id.tvResumeItem);
        Glide.with(context).load(restaurantList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(restaurantList.get(position).getName());
        tvResumeItem.setText(restaurantList.get(position).getAddress() + "\n"+restaurantList.get(position).getOriginAndDescription());
        return view;
    }

    /* Filtra los datos del adaptador */
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
