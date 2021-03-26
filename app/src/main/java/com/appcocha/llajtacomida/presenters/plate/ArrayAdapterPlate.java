package com.appcocha.llajtacomida.presenters.plate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appcocha.llajtacomida.presenters.tools.Serializer;
import com.appcocha.llajtacomida.presenters.tools.Validation;
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
    private String orderBy;

    /**
     * Contructor
     * @param context de la actividad
     * @param resource, es el item (xml)
     * @param plateList, lista de objetos plato
     */
    public ArrayAdapterPlate(@NonNull Context context, int resource, @NonNull ArrayList <Plate> plateList) {
        super(context, resource, plateList);
        this.resource = resource;
        this.context = context;
        this.orderBy = Serializer.readStringData(context, "ORDER_LIST_STATE"); // Para saber el orden actual
        if(orderBy.equals("NAME")) {
            this.plateList = plateList;
            this.plateListCopy = new ArrayList<Plate>();
            this.plateListCopy.addAll(plateList);
        }else if(orderBy.equals("PUNCTUATION")){
            this.plateList = Validation.getPlatesOrderByPunctuation(plateList);
            this.plateListCopy = new ArrayList<Plate>();
            this.plateListCopy.addAll(plateList);
        }
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
        DecimalFormat decimalFormat = new DecimalFormat("0.0"); // para que tenga solo un decimal
        tvRating.setText(String.valueOf(decimalFormat.format(plateList.get(position).getPunctuation())));
        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin().replace("\n", " "));
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

//    public void reorder() {
//        if(orderBy.equalsIgnoreCase("NAME")){
//            Serializer.saveOrderBy(context, "ORDER_LIST_STATE", "PUNCTUATION");
//            Toast.makeText(context, context.getString(R.string.order_by_punctuation), Toast.LENGTH_SHORT).show();
//        }else if(orderBy.equalsIgnoreCase("PUNCTUATION")){
//            Serializer.saveOrderBy(context, "ORDER_LIST_STATE", "NAME");
//            Toast.makeText(context, context.getString(R.string.order_by_name), Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Guarda el estado de oeden de la lista (name)
     * @return success
     */
    public boolean  reorderByName(){
        if(orderBy.equalsIgnoreCase("PUNCTUATION")){
            Serializer.saveStringData(context, "ORDER_LIST_STATE", "NAME");
            return true;
        }
        else{
            Toast.makeText(context, context.getString(R.string.already_by_name), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Guarda el estado de oeden de la lista (punctuation)
     * @return success
     */
    public boolean  reorderByPunctuation(){
        if(orderBy.equalsIgnoreCase("NAME")){
            Serializer.saveStringData(context, "ORDER_LIST_STATE", "PUNCTUATION");
            return true;
        }else{
            Toast.makeText(context, context.getString(R.string.already_by_punctuation), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
