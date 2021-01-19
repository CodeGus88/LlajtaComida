package com.appcocha.llajtacomida.presenters.plate;

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
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.plate.Plate;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Este es un adaptador para la lista de platos
 * Carga todos los platos
 */
public class ArrayAdapterPlate extends ArrayAdapter<Plate> {

    private ArrayList<Plate> plateList;
    private ArrayList<Plate> plateListCopy;
    private Context context;
    private int resource;

    /**
     * Contructor
     * @param context de la actividad
     * @param resource, es el item (xml)
     * @param objects, lista de objetos plato
     */
    public ArrayAdapterPlate(@NonNull Context context, int resource, @NonNull ArrayList <Plate> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.context = context;
            this.plateListCopy = new ArrayList<Plate>();
            this.plateList = objects;
            this.plateListCopy.addAll(objects);
    }

    /**
     * Carga la lista de platos
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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
        TextView tvRating = (TextView) view.findViewById(R.id.tvRating);
        DecimalFormat decimalFormat = new DecimalFormat("0.0"); // para que tenda solo un decimal
        tvRating.setText(String.valueOf(decimalFormat.format(plateList.get(position).getPunctuation())));
        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin());

        return view;
    }

    /**
     * Este método sirve para realizar búsquedas
     *  Filtra los datos del adaptador
     */
    public void filter(String texto, int previousLentg) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() == previousLentg){ // Si se presionó borrar
                plateList.clear();
                plateList.addAll(plateListCopy);
            }
            search(texto);
        }else if(plateList.size() != plateListCopy.size()){
            if(plateList.size() > 0){
                plateList.clear();
            }
            plateList.addAll(plateListCopy);
        }
        notifyDataSetChanged();
    }

    /**
     * Busca
     * @param texto, elemento buscado
     */
    public  void search(String texto){
        int i = 0;
        while (i < plateList.size()) {
            String string = plateList.get(i).toString().toLowerCase();
            if (!string.contains(texto)) {
                plateList.remove(i);
            } else {
                i++;
            }
        }
    }
}
