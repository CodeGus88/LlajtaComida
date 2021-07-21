 package com.appcocha.llajtacomida.presenter.restaurant;

 import android.content.Context;
 import android.graphics.Color;
 import android.graphics.Paint;
 import android.graphics.drawable.Drawable;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ArrayAdapter;
 import android.widget.ImageView;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;

 import com.appcocha.llajtacomida.R;
 import com.appcocha.llajtacomida.model.plate.Plate;
 import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
 import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
 import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionElement;
 import com.appcocha.llajtacomida.presenter.tools.RandomColor;
 import com.appcocha.llajtacomida.presenter.tools.Validation;
 import com.bumptech.glide.Glide;

 import java.util.ArrayList;
 import java.util.Random;

 /**
  * Adaptador
  */
 public class ArrayAdapterPromotion extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

     private final Context context;
     private final int resource;
     private ArrayList<Plate> plateList;
     private ArrayList<Plate> plateListCopy;
     private final Menu menu;
     private final Promotion promotion;


     /**
      * Constructor, inicializa context, resource, platesList, menu
      * @param context
      * @param resource
      * @param platesList
      * @param promotion
      */
     public ArrayAdapterPromotion(@NonNull Context context, int resource, ArrayList<Plate> platesList, Menu menu, Promotion promotion) {
         super(context, resource, platesList);
         this.context = context;
         this.resource = resource;
         this.plateList = platesList;
         this.plateListCopy = new ArrayList<Plate>();
         this.plateListCopy.addAll(platesList);
         this.menu = menu;
         this.promotion = promotion;
     }

     @NonNull
     @Override
     public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View view = convertView;
         if(view == null) {
             view = LayoutInflater.from(context).inflate(resource, null);
         }
         RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rlItem);
         ImageView ivPhotoItem = (ImageView) view.findViewById(R.id.ivPhotoItem);
         TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
         TextView tvPromotionTitle = (TextView) view.findViewById(R.id.tvPromotionTitle);
         TextView tvPlateNewPrice = (TextView) view.findViewById(R.id.tvPlateNewPrice);
         TextView tvPlateOldPrice = (TextView) view.findViewById(R.id.tvPlateOldPrice);

         Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
         tvTitleItem.setText(plateList.get(position).getName());
         tvPromotionTitle.setText(getNewAtribute(plateList.get(position).getId(), "title"));
         String newPrice = getNewAtribute(plateList.get(position).getId(), "price")
                 .replace(".0", "")
                 .replace("-1", "");
         if(!getOldPrice(plateList.get(position).getId()).equals("")){
             tvPlateOldPrice.setVisibility(View.VISIBLE);
             tvPlateOldPrice.setText(getOldPrice(plateList.get(position).getId()) +" "+ context.getString(R.string.type_currency));
         }else tvPlateOldPrice.setVisibility(View.GONE);
         if (!newPrice.equals("")){
             tvPlateNewPrice.setVisibility(View.VISIBLE);
             tvPlateNewPrice.setText(newPrice + " " + context.getString(R.string.type_currency));
         } else tvPlateNewPrice.setVisibility(View.GONE);

         if(!newPrice.equals("")
                 && !getOldPrice(plateList.get(position).getId()).equals("")){
             tvPlateOldPrice.setPaintFlags(tvPlateOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         }

         return view;
     }


     /**
      * Devuelde el la descripción de la promoción
      * @param id
      * @return description
      */
     private String getNewAtribute(String id, String atribute){
         String data = "";
         for (PromotionElement promotionElement: promotion.getPromotionList()) {
             if(promotionElement.getPlateId().equals(id)){
                 if (atribute.equalsIgnoreCase("title")) data = promotionElement.getTitle();
                 else if (atribute.equalsIgnoreCase("description")) data = promotionElement.getDescription();
                 else if(atribute.equalsIgnoreCase("price")) data = String.valueOf(promotionElement.getPrice());
                 break;
             }
         }
         return data;
     }

     /**
      * Devuelde el precio en el menú
      * @param id
      * @return price
      */
     private String getOldPrice(String id){
         String price = "";
         for (String p: menu.getMenuList()) {
             if(p.contains(id)){
                 if(Validation.getXWord(p, 2).equals("")) price = context.getString(R.string.default_price);
                 else price = Validation.getXWord(p, 2);
                 break;
             }
         }
         return price;
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
      * Para saber si el elemento existe en la promoción
      * @param id  del plato
      * @param lista lista de ids en la promoción
      * @return exist
      */
     private boolean existInList(String id, ArrayList<PromotionElement> lista){
         boolean exist = false;
         for (int i = 0; i < lista.size(); i++) {
             if(Validation.getXWord(lista.get(i).getPlateId(), 1).equals(id)){
                 exist = true;
                 break;
             }
         }
         return exist;
     }

 }
