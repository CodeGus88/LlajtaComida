package com.example.llajtacomida.presenters.restaurantsPresenter;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;

public class ArrayAdapterSetMenu extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

    private final Context context;
    private final int resource;
    private ArrayList<Plate> platesList;
    private ArrayList<Plate> platesListCopy;
    private final Menu menu;

    public ArrayAdapterSetMenu(@NonNull Context context, int resource, ArrayList<Plate> platesList, final Menu menu) {
        super(context, resource, platesList);
        this.context = context;
        this.resource = resource;
        this.platesList = platesList;
        this.platesListCopy = new ArrayList<Plate>();
        this.platesListCopy.addAll(platesList);
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

        Glide.with(context).load(platesList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(platesList.get(position).getName());
        tvResumeItem.setText(platesList.get(position).getOrigin());
        cbSelectedItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbSelectedItem.isChecked()){
                    addPlate(platesList.get(position).getId());
                }else{
                    removePlate(platesList.get(position).getId());
                }
            }
        });
        cbSelectedItem.setChecked(existInList(platesList.get(position).getId(), menu.getMenuList()));
        notifyDataSetChanged();
        return view;
    }

    // Search
    /* Filtra los datos del adaptador */
    public void filter(String texto, int previousLentg) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() == previousLentg){ // Si se presionó borrar
                platesList.clear();
                platesList.addAll(platesListCopy);
            }
            search(texto);
        }else if(platesList.size() != platesListCopy.size()){
            if(platesList.size() > 0){
                platesList.clear();
            }
            platesList.addAll(platesListCopy);
        }
        notifyDataSetChanged();
    }

    public void search(String texto){
        int i = 0;
        while (i < platesList.size()) {
            String string = platesList.get(i).toString().toLowerCase();
            if (!string.contains(texto)) {
                platesList.remove(i);
            } else {
                i++;
            }
        }
    }




    /**
     * Para saber si el elemento existe en el menu
     * @param id id del plato
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

    public Menu getMenu(){
        Toast.makeText(context, "Elementos en lista: " + menu.getMenuList().size(), Toast.LENGTH_SHORT).show();
        return menu;
    }


}
