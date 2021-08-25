package com.appcocha.llajtacomida.presenter.plate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.model.plate.Plate;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Este es un adaptador para la lista de platos con sus precios
 * Carga todos los platos
 */
public class ArrayAdapterPlatePrice extends ArrayAdapter<Plate> {

    private ArrayList<Plate> plateList;
    private ArrayList<Plate> plateListCopy;
    private ArrayList<String> priceList;
    private Context context;
    private int resource;

    /**
     * Contructor
     * @param context de la actividad
     * @param resource, es el item (xml)
     * @param objects, lista de objetos plato
     */
    public ArrayAdapterPlatePrice(@NonNull Context context, int resource, @NonNull ArrayList <Plate> objects,@NonNull ArrayList<String> priceList) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.plateListCopy = new ArrayList<Plate>();
        this.plateList = objects;
        this.plateListCopy.addAll(objects);
        this.priceList = priceList;
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
        TextView tvPlatePrice = (TextView) view.findViewById(R.id.tvPlateNewPrice);
        TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
        TextView tvResumeItem = (TextView) view.findViewById(R.id.tvDescription);
        TextView tvRating = (TextView) view.findViewById(R.id.tvRating);
        DecimalFormat decimalFormat = new DecimalFormat("0.0"); // para que tenga solo un decimal
        tvRating.setText(String.valueOf(decimalFormat.format(plateList.get(position).getPunctuation())));
        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin());
        String text = getText(plateList.get(position).getId());

        if(!text.equals("")) {
            String price = Validation.getXWord(text,2);
//            presentación de formato de precios
            if(!price.equals("")){
                tvPlatePrice.setText(Validation.getFormatPrice(price, context.getString(R.string.type_currency)));
            }else{
                tvPlatePrice.setText(context.getString(R.string.default_price) + " " + context.getString(R.string.type_currency));
            }
        }
        return view;
    }

    /**
     *
     * @param id
     * @return text
     */
    private String getText(String id){
        String text = "";
        for (String textAux: priceList) {
            if(textAux.contains(id)){
                text = textAux;
                break;
            }
        }
        return text;
    }
}

