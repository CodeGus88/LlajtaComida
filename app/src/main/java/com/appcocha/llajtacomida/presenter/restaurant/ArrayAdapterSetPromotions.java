 package com.appcocha.llajtacomida.presenter.restaurant;

 import android.content.Context;
 import android.graphics.Color;
 import android.graphics.Paint;
 import android.graphics.drawable.ColorDrawable;
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
 import androidx.cardview.widget.CardView;

 import com.appcocha.llajtacomida.R;
 import com.appcocha.llajtacomida.model.plate.Plate;
 import com.appcocha.llajtacomida.model.restaurant.menu.Menu;
 import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
 import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionElement;
 import com.appcocha.llajtacomida.presenter.tools.Validation;
 import com.bumptech.glide.Glide;

 import java.util.ArrayList;

 /**
  * Adaptador
  */
 public class ArrayAdapterSetPromotions extends ArrayAdapter<Plate>  { //implements CompoundButton.OnCheckedChangeListener

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
      * @param menu
      * @param promotion
      */
     public ArrayAdapterSetPromotions(@NonNull Context context, int resource, ArrayList<Plate> platesList, final Menu menu, Promotion promotion) {
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
         RelativeLayout rlItem = (RelativeLayout) view.findViewById(R.id.rlItem);
         ImageView ivPhotoItem = (ImageView) view.findViewById(R.id.ivPhotoItem);
         TextView tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
         TextView tvPromotionDescription = (TextView) view.findViewById(R.id.tvPromotionDescription);
         TextView tvPromotionTitle = (TextView) view.findViewById(R.id.tvPromotionTitle);
         CardView cvPrice = (CardView) view.findViewById(R.id.cvPrice);
         TextView tvPlateNewPrice = (TextView) view.findViewById(R.id.tvPlateNewPrice);
         TextView tvPlateOldPrice = (TextView) view.findViewById(R.id.tvPlateOldPrice);

         Glide.with(context).load(plateList.get(position).getUrl()).into(ivPhotoItem);
         tvTitleItem.setText(plateList.get(position).getName());
         if(existInMenu(plateList.get(position).getId())) rlItem.setBackgroundColor(context.getResources().getColor(R.color.colorItem));
         else rlItem.setBackgroundColor(Color.WHITE);

         if(existInPromotion(plateList.get(position).getId())){
             String newPrice = getNewAtribute(plateList.get(position).getId(), "price")
                     .replace(".0", "").replace("-1", "");
             String oldPrice = getOldPrice(plateList.get(position).getId());
             if(!newPrice.isEmpty()){
                 tvPlateNewPrice.setText(newPrice + " " + context.getString(R.string.type_currency));
                 tvPlateNewPrice.setVisibility(View.VISIBLE);
             }else tvPlateNewPrice.setVisibility(View.GONE);
             if(!oldPrice.isEmpty()){
                 tvPlateOldPrice.setText(oldPrice+ " " + context.getString(R.string.type_currency));
                 tvPlateOldPrice.setVisibility(View.VISIBLE);
                 if(!newPrice.isEmpty())
                     tvPlateOldPrice.setPaintFlags(tvPlateOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
             }else tvPlateOldPrice.setVisibility(View.GONE);
             tvPromotionTitle.setVisibility(View.VISIBLE);
             tvPromotionDescription.setVisibility(View.VISIBLE);
             tvPromotionTitle.setText(getNewAtribute(plateList.get(position).getId(), "title"));
             tvPromotionDescription.setText(getNewAtribute(plateList.get(position).getId(), "description"));
         }else{
             tvPromotionTitle.setText("");
             tvPromotionDescription.setText("");
             tvPlateNewPrice.setText("");
             tvPlateOldPrice.setText("");

             tvPromotionTitle.setVisibility(View.GONE);
             tvPromotionDescription.setVisibility(View.GONE);
             tvPlateNewPrice.setVisibility(View.GONE);
             tvPlateOldPrice.setVisibility(View.GONE);
         }

         return view;
     }

     /**
      * Verifica si un id se encuentra en la lista de promociones
      * @param id
      * @return exist
      */
     public boolean existInPromotion(String id){
         if(existInList(id, promotion.getPromotionList())){
             return true;
         }else{
             return false;
         }
     }

     /**
      * Verifica si un id se encuentra en la lista del menú
      * @param id
      * @return exist
      */
     public boolean existInMenu(String id){
         boolean exist = false;
         for(String menuItem: menu.getMenuList()){
            if(menuItem.contains(id)){
                exist = true;
                break;
            }
        }
         return exist;
     }


     /**
      * Agrega un precio y actualiza la lista
      * @param id
      * @param title
      * @param price
      */
     public boolean addPromotion(String id, String title, String description, String price){
         if(!id.isEmpty() && !title.isEmpty() && !description.isEmpty()){
             if(price.equals("")) price = "-1";
             addPlate(new PromotionElement(id, title, description, Float.parseFloat(price)));
             notifyDataSetChanged();
             return true;
         }else return false;
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

     /**
      * Agrega un plato en la lista de la promoción
      * @param promotionElement
      */
     private void addPlate(PromotionElement promotionElement){
         if(!existInList(promotionElement.getPlateId(), promotion.getPromotionList())){
             promotion.getPromotionList().add(promotionElement);
         }
     }

     /**
      * Elimina el plato de la lista del restaurante
      * @param plateId
      */
     public void removePlate(String plateId){
         for(int i = 0; i < promotion.getPromotionList().size(); i ++){
             if(promotion.getPromotionList().get(i).getPlateId().equals(plateId)){
                 promotion.getPromotionList().remove(i);
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
      * Obtiene la promoción
      * @return promotion
      */
     public Promotion getPromotion(){
         clearNonExistentPlates();
         return promotion;
     }
 }
