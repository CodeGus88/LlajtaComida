package com.appcocha.llajtacomida.presenter.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.tools.Serializer;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Adaptador
 */
public class ArrayAdapterRestaurant extends ArrayAdapter<Restaurant> {

    private ArrayList<Restaurant> restaurantList, restaurantListCopy;
    private Context context;
    private int resource;
    private String orderBy;

    /**
     * Constructor, inicializa context, resource, objects
     * @param context
     * @param resource
     * @param restaurantList
     */
    public ArrayAdapterRestaurant(@NonNull Context context, int resource, @NonNull ArrayList <Restaurant> restaurantList) {
        super(context, resource, restaurantList);
        this.resource = resource;
        this.context = context;
        this.orderBy = Serializer.readStringData(context, "ORDER_LIST_STATE"); // Para saber el orden actual
        if(orderBy.equals("NAME")) {
            this.restaurantList = restaurantList;
            this.restaurantListCopy = new ArrayList<Restaurant>();
            this.restaurantListCopy.addAll(restaurantList);
        }else if(orderBy.equals("PUNCTUATION")){
            this.restaurantList = Validation.getRestaurantsOrderByPunctuation(restaurantList);
            this.restaurantListCopy = new ArrayList<Restaurant>();
//            this.restaurantListCopy.addAll(Validation.getRestaurantsOrderByPunctuation(restaurantList));
            this.restaurantListCopy.addAll(restaurantList);
        }
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
            TextView tvResumeItem = (TextView) view.findViewById(R.id.tvDescription);
            TextView tvRating = (TextView) view.findViewById(R.id.tvRating);
            DecimalFormat decimalFormat = new DecimalFormat("0.0"); // para que tenga solo un decimal
            tvRating.setText(String.valueOf(decimalFormat.format(restaurantList.get(position).getPunctuation())));
            Glide.with(context).load(restaurantList.get(position).getUrl()).into(ivPhotoItem);
            tvTitleItem.setText(restaurantList.get(position).getName());
            tvResumeItem.setText(restaurantList.get(position).getAddress() + "\n"+restaurantList.get(position).getOriginAndDescription().replace("\n", " "));
        }catch(Exception e){
            Log.e("Error: ", "------------------------------------------------> "+e.getMessage());
        }
        return view;
    }

    /**
     *  Filtra los datos del adaptador
     */
    public void filter(String texto, int previousLength) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() == previousLength){ // Si se presionó borrar
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

    /**
     * Guarda el estado de oeden de la lista (name)
     * @return success
     */
    public boolean  reorderByName(){
        if(!orderBy.equalsIgnoreCase("NAME")){
            Serializer.saveStringData(context, "ORDER_LIST_STATE", "NAME");
            return true;
        }else{
            Toast.makeText(context, context.getString(R.string.already_by_name), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Guarda el estado de oeden de la lista (punctuation)
     * @return success
     */
    public boolean  reorderByPunctuation(){
        if(!orderBy.equalsIgnoreCase("PUNCTUATION")){
            Serializer.saveStringData(context, "ORDER_LIST_STATE", "PUNCTUATION");
            return true;
        }else{
            Toast.makeText(context, context.getString(R.string.already_by_punctuation), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
