package com.example.llajtacomida.views.restaurantsViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Menu;
import com.example.llajtacomida.models.Plate;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class ArrayAdapterRestaurantMenu extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

    private final Context context;
    private final int resource;
    private ArrayList<Plate> platesList;
    private ArrayList<Plate> platesCopy;
    private Menu menu;
    private ArrayList<String> newMenuList;

    public ArrayAdapterRestaurantMenu(@NonNull Context context, int resource, ArrayList<Plate> platesList, Menu menu) {
        super(context, resource, platesList);
        this.context = context;
        this.resource = resource;
        this.platesList = platesList;
        this.platesCopy = new ArrayList<Plate>();
        this.platesCopy.addAll(platesList);
        this.menu = menu;
        newMenuList = new ArrayList<String>();
        this.newMenuList.addAll(menu.getMenuList());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        }
        ImageView ivPhotoItem = (ImageView) view.findViewById(R.id.ivPhotoItem);
        TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
        TextView tvResumeItem = (TextView) view.findViewById(R.id.tvResumeItem);
        final CheckBox cbSelectedItem = (CheckBox) view.findViewById(R.id.cbSelectItem);

        Glide.with(context).load(platesList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(platesList.get(position).getName());
        tvResumeItem.setText(platesList.get(position).getOrigin());

//        cbSelectedItem.setChecked(true);
        // Es necesario que sea una clase anónima al ser n objetos anónimos, no se tiene identidad
        cbSelectedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbSelectedItem.isChecked()){

                }else{

                }
            }
        });
        return view;
    }

    private void addPlate(String plateId){
        if(!existInList(plateId, newMenuList)){
            newMenuList.add(plateId);
        }
    }

    private void removePlate(String plateId){
        if(existInList(plateId, newMenuList)){

        }
    }

    /**
     * Para saber si el elemento existe en el menu
     * @param id
     * @param lista
     * @return
     */
    private boolean existInList(String id, ArrayList<String> lista){
        boolean exist = false;
        for (String plateId: lista) {
            if(plateId.equals(id)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    private Menu getMenu(){
        menu.setMenuList(newMenuList);
        return menu;
    }
}
