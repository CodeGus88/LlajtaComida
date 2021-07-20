 package com.appcocha.llajtacomida.presenter.restaurant;

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
import androidx.cardview.widget.CardView;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
import com.appcocha.llajtacomida.model.plate.Plate;

import java.util.ArrayList;

/**
 * Adaptador
 */
public class ArrayAdapterSetMenu extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

    private final Context context;
    private final int resource;
    private ArrayList<Plate> plateList;
    private ArrayList<Plate> plateListCopy;
    private final Menu menu;

    /**
     * Constructor, inicializa context, resource, platesList, menu
     * @param context
     * @param resource
     * @param platesList
     * @param menu
     */
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
        TextView tvResumeItem = (TextView) view.findViewById(R.id.tvPromotionDescription);
        CardView cvPrice = (CardView) view.findViewById(R.id.cvPrice);
        TextView tvPlatePrice = (TextView) view.findViewById(R.id.tvPlateNewPrice);

        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin());
        if(existInList(plateList.get(position).getId(), menu.getMenuList())) cvPrice.setVisibility(View.VISIBLE);
        else cvPrice.setVisibility(View.GONE);
        String price = Validation.getXWord(getText(plateList.get(position).getId() ), 2 );
        if(!price.equals("")) tvPlatePrice.setText(price + " " + context.getString(R.string.type_currency));
        else tvPlatePrice.setText(context.getString(R.string.default_price) + " " + context.getString(R.string.type_currency));
        return view;
    }

    public boolean existInMenu(String id){
        if(existInList(id, menu.getMenuList())){
            return true;
        }else{
            return false;
        }
    }
    public void addPrice(String id, String price){
        String addPrice = id + " " + price;
        addPlate(addPrice);
        notifyDataSetChanged();
    }

    /**
     * optiene el texto que contiene el id
     * @param id
     * @return
     */
    private String getText(String id){
        String text = "";
        for (String textAux: menu.getMenuList()) {
            if(textAux.contains(id)){
                text = textAux;
                break;
            }
        }
        return text;
    }

    /** Filtra los datos del adaptador */
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

    /**
     * Busca el texto en el  toString del objeto
     */
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
        for (int i = 0; i < lista.size(); i++) {
            if(Validation.getXWord(lista.get(i), 1).equals(id)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * Agrega un plato en la lista del menú
     * @param plateId
     */
    private void addPlate(String plateId){
        if(!existInList(plateId, menu.getMenuList())){
            menu.getMenuList().add(plateId);
        }
    }

    /**
     * Elimina el plato de la lista del restaurante
     * @param plateId
     */
    public void removePlate(String plateId){
        for(int i = 0; i < menu.getMenuList().size(); i ++){
            if(Validation.getXWord(menu.getMenuList().get(i), 1).equals(plateId)){
                menu.getMenuList().remove(i);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Elimina del menú los platos que no existen
     */
    private void clearNonExistentPlates(){
        ArrayList<String> platesListIdExistents = new ArrayList<String>();
        for(int i = 0; i< plateListCopy.size(); i++){
            platesListIdExistents.add(plateListCopy.get(i).getId());
        }
        int i = 0;
        while(i < menu.getMenuList().size()){
            String idPlateInMenu = Validation.getXWord(menu.getMenuList().get(i), 1);
            if(!platesListIdExistents.contains(idPlateInMenu)){ // Ojo genera un error posiblemente cuando el internet está lento
//                menu.getMenuList().remove(idPlateInMenu);
                for(int j = 0; i< menu.getMenuList().size(); i++){
                    if(menu.getMenuList().get(j) == idPlateInMenu){
                        menu.getMenuList().remove(j);
                        break;
                    }
                }
            }else{
                i++;
            }
        }
    }

    /**
     * Obtiene el menú
     * @return menu
     */
    public Menu getMenu(){
        clearNonExistentPlates();
        Toast.makeText(context, context.getString(R.string.menu_size) + " " + menu.getMenuList().size(), Toast.LENGTH_SHORT).show();
        return menu;
    }
}
