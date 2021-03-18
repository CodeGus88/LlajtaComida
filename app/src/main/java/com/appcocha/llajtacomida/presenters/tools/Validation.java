package com.appcocha.llajtacomida.presenters.tools;
import android.util.Log;

import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.plate.PlateList;

import java.util.ArrayList;

/**
 * Validaciones de registros y actualizaciones
 */
public class Validation {

    /**
     * verifica si un texto está o no vacio (Sin tomar en cuenta los espacios)
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
     * Quita los espacios demás
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
     * @return x word
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

    /**
     * Verifica que el el nombre no tenga caracteres que no correspondan
     * @param name
     * @return
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
}