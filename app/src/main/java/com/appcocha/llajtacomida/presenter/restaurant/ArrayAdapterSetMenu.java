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
        TextView tvResumeItem = (TextView) view.findViewById(R.id.tvDescription);
        CardView cvPrice = (CardView) view.findViewById(R.id.cvPrice);
        TextView tvPlatePrice = (TextView) view.findViewById(R.id.tvPlateNewPrice);

        Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
        tvTitleItem.setText(plateList.get(position).getName());
        tvResumeItem.setText(plateList.get(position).getOrigin());
        if(existInList(plateList.get(position).getId(), menu.getMenuList())) cvPrice.setVisibility(View.VISIBLE);
        else cvPrice.setVisibility(View.GONE);
        String price = Validation.getXWord(getText(plateList.get(position).getId() ), 2 );
        if(!price.equals("")) tvPlatePrice.setText(price.replace("_", ", ") + " " + context.getString(R.string.type_currency));
        else tvPlatePrice.setText(context.getString(R.string.default_price) + " " + context.getString(R.string.type_currency));
        return view;
    }

    /**
     * Agrega precio
     * @param id
     * @param price
     */
    public void addPrice(String id, String price){
        String idWithPrice = id + " " + price;
        addPlate(idWithPrice);
        notifyDataSetChanged();
    }

    /**
     * optiene el texto que contiene el id
     * @param id
     * @return text
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
     * @param list lista de ids en el menu
     * @return exist
     */
    private boolean existInList(String id, ArrayList<String> list){
        for (String data : list)
            if(Validation.getXWord(data, 1).equals(id))
                return true;
        return false;
    }

    /**
     * Agrega un plato en la lista del menú
     * @param plateWithPrice
     */
    private void addPlate(String plateWithPrice){
        if(!existInList(Validation.getXWord(plateWithPrice, 1), menu.getMenuList())){
            menu.getMenuList().add(plateWithPrice);
        }else{ // si ya existe en la lista
            removePlate(Validation.getXWord(plateWithPrice, 1), false);
            menu.getMenuList().add(plateWithPrice);
        }
        notifyDataSetChanged();
    }

    /**
     * Elimina el plato de la lista del restaurante (tambien duplicados)
     * @param plateId
     */
    public void removePlate(String plateId, boolean update){
        int i = 0;
        while(i<menu.getMenuList().size()){
            if(Validation.getXWord(menu.getMenuList().get(i), 1).equals(plateId)) menu.getMenuList().remove(i);
            else i++;
        }
        if(update) notifyDataSetChanged();
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
