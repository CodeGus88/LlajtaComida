package com.appcocha.llajtacomida.presenter.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.plate.PlateList;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Validaciones de registros y actualizaciones
 */
public class Validation {

    /**
     * verifica si un texto está o no vacio (Sin tomar en cuenta los espacios)
     * @param text
     * @return isEmpty
     */
    public static boolean isEmpty(String text){
        text = Validation.correctText(text);
        text = text.trim();
        if(text.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Quita los espacios demás
     * @param text
     * @return resultText
     */
    public static String correctText(String text){
        byte continuousSpaces = 0;
        text = text.trim();
        String resultText = "";
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == ' '){
                continuousSpaces ++;
            }else{
                continuousSpaces = 0;
            }
            if(continuousSpaces <= 1){
                resultText += text.charAt(i);
            }
        }
        return resultText;
    }


    /**
     * Verifica los espacios y caracteres que no sea números
     * @param text, números
     * @return request
     */
    public static int validateNumbers(String text){
        for(int i = 0; i< text.length(); i++){
//            Log.d("myxaa", text.charAt(i)+" ----> "+ (int) text.charAt(i));
            if(text.length()>20){
                return R.string.message_content_too_long;
            }else if(( (int) text.charAt(i) < 48 || (int) text.charAt(i)>57) && (int) text.charAt(i) != 32){
                return R.string.message_wrong_number_format;
            }
        }
        return 0;
    }

    /**
     * Elimina: "0", ",", "." como primieros dígitos de número considerando que todos los caracteres con números (0-9) o searadores
     * @param data
     * @param separator
     * @return data
     */
    public static String correctNumbers(String data, String separator){
        // Salvar posibles precios ceros válidos
        // Recuperar 0 válido del principio y del final
        if(data.length() >= 2){ // salvar el primer cero con *
            char[]chars = data.toCharArray();
            if(chars[0] == '0' && chars[1] == separator.charAt(0)){
                chars[0] = '*';
            }
            data = String.valueOf(chars);
        }
        if(data.length() >=2) { // salvar el últmo cero con *
            char[]chars = data.toCharArray();
            if(chars[chars.length-2] == separator.charAt(0) && chars[chars.length-1] == '0'){
                chars[chars.length-1] = '*';
            }
            data = String.valueOf(chars);
        }

        if(data.contains(separator+separator)){
            data = data.replace(separator+separator, separator+"*"+separator);
            return correctNumbers(data, separator);
        }
        if (data.length()>1)
//            if(!( (int) data.charAt(0) >= 49 && (int) data.charAt(0)<=57)){
            if(data.charAt(0) == '0'){
                data = data.substring(1, data.length());
                return correctNumbers(data, separator);
            }

            if(data.contains(separator+"0") || data.contains(separator+".") || data.contains(separator+",")
                    || data.contains(separator+separator)){
                data = data.replace(separator+"0", separator)
                        .replace(separator+".", separator)
                        .replace(separator+",", separator);
                return correctNumbers(data, separator);
            }else{
                data = data.replace("*", "0");
                return data;
            }
    }

    /**
     * Presentación de precios en menus
     * @param data
     * @param typeOfCurrency
     */
    public static String getFormatPrice(String data, String typeOfCurrency){
        for(int i = data.length() - 1; i>=0; i--){
            if(data.charAt(i) == '_'){
                char[] chars = data.toCharArray();
                chars[i] = 'y';
                data = String.valueOf(chars);
                break;
            }
        }
        return data.replace("_", " "+typeOfCurrency+" | ")
                .replace("y", " " + typeOfCurrency+" | ")
                + " " + typeOfCurrency;
    }

    /**
     * Verifica si es o son teléfonos los que están en el campo phones
     * @param phones
     * @return isPhone
     */
    public static boolean isPhone(String phones){
        ArrayList<String> phoneList = new ArrayList<String>();
        String phone = "";
        boolean isPhone = true;
        boolean isVerifiable = true;
        for(int i = 0; i< phones.length(); i++){
            if(!(phones.charAt(i) == ' ') && !(phones.charAt(i) == '-') && !(phones.charAt(i) == ',')
                    && !(phones.charAt(i) == '.') && !(phones.charAt(i) == ';') && !(phones.charAt(i) == ':')
                    && !(phones.charAt(i)>=48 && phones.charAt(i) <= 57)){
                isVerifiable = false;
                break;
            }
        }
        if(isVerifiable){
            // Separar telefonos
            for(int i = 0; i < phones.length(); i++){
                if(phones.charAt(i) == ' ' || phones.charAt(i) == '-' || phones.charAt(i) == ','
                        || phones.charAt(i) == '.' || phones.charAt(i) == ';' || phones.charAt(i) == ':'
                        || i == (phones.length() - 1)){
                    if(i == (phones.length() - 1)){ // Si es el último dígito tambiel lo agrega
                        phone+=String.valueOf(phones.charAt(i));
                    }
                    phoneList.add(phone);
                    phone = "";
                }else{
                    phone+=String.valueOf(phones.charAt(i));
                }
            }

            for(String number : phoneList){
                if(number.length() == 7){ // Si es teléfono 7 dígitos
                    if(number.charAt(0) == '4'){
                        isPhone &= true;
                    }else{
                        isPhone &= false;
                    }
                }else if(number.length() == 8){ // Si es celular u8 dígitos
                    if(number.charAt(0) == '7' || number.charAt(0) == '6'){
                        isPhone &= true;
                    }else{
                        isPhone &= false;
                    }
                }else{
                    isPhone &= false;
                }
            }
        }else{
            isPhone = false;
        }

        return isPhone;
    }

    /**
     * Obtiene la primera palabra de la cadena
     * @param fulText
     * @return firstName
     */
    public static String getFirstWord(String fulText){
        String firstName = "";
        for(int i = 0; i<fulText.length(); i++){
            if(fulText.charAt(i) == ' '){
                break;
            }
            firstName += fulText.charAt(i);
        }
        return firstName;
    }

    /**
     * Obtiene la palabra x del texto
     * @param fulText, x
     * @return xWord
     */
    public static String getXWord(String fulText, int x){
        fulText = correctText(fulText);
        ArrayList<String> wordList = new ArrayList<String>();
        String temporaryWord = "";
        for(int i = 0; i<fulText.length(); i++){
            if(fulText.charAt(i) != ' '){
                temporaryWord += fulText.charAt(i);
            }
            if(fulText.charAt(i) == ' ' || (i==fulText.length()-1)){
                wordList.add(temporaryWord);
                temporaryWord = "";
            }
        }
        if(x <= wordList.size() && x-1 >= 0) return wordList.get(x-1);
        else return "";
    }

    /**
     * Valida si es un registro es un nuevo registro
     * @param name
     * @return exist
     */
    public static boolean existPlateName(String name){
        boolean exist = false;
        for ( Plate plate: PlateList.getPlateList()) {
            if(plate.getName().equalsIgnoreCase(name)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * Valida si es la edición de un registro
     * @param name
     * @return exist
     */
    public static boolean existPlateNameExcludePlateId(String plateId, String name){
        boolean exist = false;
        for (Plate plate: PlateList.getPlateList()) {
            if(plate.getName().equalsIgnoreCase(name) && !plate.getId().equals(plateId)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * Verifica que el el nombre no tenga caracteres que no correspondan
     * @param name supuesto nombre de persona
     * @return isName
     */
    public static boolean isPersonName(String name){
        boolean isName = false;
        name = name.toLowerCase();
        for(int i = 0; i < name.length(); i ++){
            if((name.charAt(i) >= 97 && name.charAt(i) <= 122)
                    || name.charAt(i) == 225 || name.charAt(i) == 233 || name.charAt(i) == 237 || name.charAt(i) == 243 || name.charAt(i) == 250
                    || name.charAt(i) == 32 || name.charAt(i) == 241 || name.charAt(i) == 252){
                isName = true;
            }else{
                isName = false;
                break;
            }
        }
        return isName;
    }

    /**
     * Ordena la lista de objetos (restaurantes)  según su puntuación
     * Algoritno de ordenamiento por inserción
     * @return restaurantList
     */
    public static ArrayList<Restaurant> getRestaurantsOrderByPunctuation(ArrayList<Restaurant> restaurantList){
        int pos;
        Restaurant restaurant;
        for(int i = 0 ; i < restaurantList.size() ; i++){
            pos = i;
            restaurant = restaurantList.get(i);
            while (pos > 0 && restaurantList.get(pos-1).getPunctuation() < restaurant.getPunctuation()){
                restaurantList.set(pos, restaurantList.get(pos-1));
                pos--;
            }
            restaurantList.set(pos, restaurant);
        }
        return restaurantList;
    }

    /**
     * Ordena la lista de objetos (platos)  según su puntuación
     * Algoritno de ordenamiento por inserción
     * @param plateList
     * @return plateList
     */
    public static ArrayList<Plate> getPlatesOrderByPunctuation(ArrayList<Plate> plateList) {
        int pos;
        Plate plate;
        for(int i = 0 ; i < plateList.size() ; i++){
            pos = i;
            plate = plateList.get(i);
            while (pos > 0 && plateList.get(pos-1).getPunctuation() < plate.getPunctuation()){
                plateList.set(pos, plateList.get(pos-1));
                pos--;
            }
            plateList.set(pos, plate);
        }
        return plateList;
    }

    /**
     * Ordenar la lista de restaurantes según su precio
     * @param restaurantList
     * @param priceInRestaurantsList
     * @return restaurantList
     */
    public static ArrayList<Restaurant> getRestaurantListOrderByPrice(ArrayList<Restaurant> restaurantList, Hashtable priceInRestaurantsList){
        try {
            Hashtable<String, String> hastable = priceInRestaurantsList;
            for (int i = 0; i < restaurantList.size(); i++) {
                for (int j = i+1; j < restaurantList.size(); j++) {
                    String stringPriceI = Validation.getXWord(hastable.get(restaurantList.get(i).getId()).replace("_", " "), 1);
                    float priceI = 0;
                    if (!stringPriceI.equals("")) priceI = Float.parseFloat(stringPriceI);
                    else priceI = Float.parseFloat(StringValues.getDefaultPrice());

                    String stringPriceJ = Validation.getXWord(hastable.get(restaurantList.get(j).getId()).replace("_", " "), 1);
                    float priceJ;
                    if (!stringPriceJ.equals("")) priceJ = Float.parseFloat(stringPriceJ);
                    else priceJ = Float.parseFloat(StringValues.getDefaultPrice());
                    if (priceI > priceJ){
                        Restaurant aux = restaurantList.get(i);
                        restaurantList.set(i, restaurantList.get(j));
                        restaurantList.set(j, aux);
                    }
                }
            }
        }catch (Exception e){
            Log.e("Error: ", "------------------------------------------------> "+e.getMessage());
        }
        return restaurantList;
    }

    /**
     * Verifica la conexión a internet
     * @param context
     * @return isConnected
     */
    public static boolean isConeccted(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetWork != null;
        if(isConnected) isConnected &= activeNetWork.isConnectedOrConnecting();
        return isConnected;
    }

}