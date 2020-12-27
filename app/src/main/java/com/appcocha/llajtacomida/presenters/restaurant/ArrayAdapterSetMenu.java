package com.appcocha.llajtacomida.presenters.restaurant;

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
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.restaurant.menu.Menu;
import com.appcocha.llajtacomida.models.plate.Plate;

import java.util.ArrayList;

public class ArrayAdapterSetMenu extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

    private final Context context;
    private final int resource;
    private ArrayList<Plate> plateList;
    private ArrayList<Plate> plateListCopy;
    private final Menu menu;

    public ArrayAdapterSetMenu(@NonNull Context context, int resource, ArrayList<Plate> platesList, final Menu menu) {
        super(context, resource, platesList);
        this.context = context;
        this.resource = resource;
        this.plateList = platesList;
        this.plateListCopy = new ArrayList<Plate>();
        this.plateListCopy.addAll(platesList);
        if (menu == null) {
            this.menu = new Menu();
            this.menu.setName("Local");
        }else{
            this.menu = menu;
        }
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

        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin());
        cbSelectedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbSelectedItem.isChecked()){
                    addPlate(plateList.get(position).getId());
                }else{
                    removePlate(plateList.get(position).getId());
                }
            }
        });
        cbSelectedItem.setChecked(existInList(plateList.get(position).getId(), menu.getMenuList()));
        return view;
    }

    // Search
    /* Filtra los datos del adaptador */
    public void filter(String texto, int previousLentg) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() >= previousLentg){ //  antes ==, no funciona la interface solo para este caso
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

    public void search(String texto){
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

    /**
     * Para saber si el elemento existe en el menu
     * @param id  del plato
     * @param lista lista de ids en el menu
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

    private void addPlate(String plateId){
        if(!existInList(plateId, menu.getMenuList())){
            menu.getMenuList().add(plateId);
        }
    }

    private void removePlate(String plateId){
        for(int i = 0; i < menu.getMenuList().size(); i ++){
            if(menu.getMenuList().get(i).equals(plateId)){
                menu.getMenuList().remove(i);
            }
        }
    }

    private void clearNonExistentPlates(){
        ArrayList<String> platesListIdExistents = new ArrayList<String>();
        for(int i = 0; i< plateListCopy.size(); i++){
            platesListIdExistents.add(plateListCopy.get(i).getId());
        }
        int i = 0;
        while(i < menu.getMenuList().size()){
            String idPlateInMenu = menu.getMenuList().get(i);
            if(!platesListIdExistents.contains(idPlateInMenu)){ // Ojo genera un error posiblemente cuando el internet está lento
                menu.getMenuList().remove(idPlateInMenu);
            }else{
                i++;
            }
        }
    }

    public Menu getMenu(){
        clearNonExistentPlates();
        Toast.makeText(context, "Elementos en lista: " + menu.getMenuList().size(), Toast.LENGTH_SHORT).show();
        return menu;
    }
}
