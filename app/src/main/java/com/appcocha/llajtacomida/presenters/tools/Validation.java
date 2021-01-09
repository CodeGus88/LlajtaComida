package com.appcocha.llajtacomida.presenters.tools;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.plate.PlateList;

import java.util.ArrayList;

public class Validation {

    /**
     * verifica si un texto está o no vacio (Sin tomar en cuenta espacios)
     * @param text
     * @return
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
     * quitara los espacios demás
     * @param text
     * @return
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

    public static String getFirstName(String fulName){
        String firstName = "";
        for(int i = 0; i<fulName.length(); i++){
            if(fulName.charAt(i) == ' '){
                break;
            }
            firstName += fulName.charAt(i);
        }
        return firstName;
    }

    /**
     * Valida si es un registro es un nuevo registro
     * @param name
     * @return
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
     * @return
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

    public static boolean isPersonName(String name){
        boolean isName=  true;
        name = name.toLowerCase();
        for(int i = 0; i < name.length(); i ++){
            if(!((name.charAt(i) >= 97 && name.charAt(i) <= 122) || name.charAt(i) == 'ñ'
                    || name.charAt(i) == 'á' || name.charAt(i) == 'é' || name.charAt(i) == 'í'
                    || name.charAt(i) == 'ó' || name.charAt(i) == 'ú' || name.charAt(i) == ' ') ){
                isName = false;
                break;
            }
        }
        return isName;
    }
}