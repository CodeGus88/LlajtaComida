package com.example.llajtacomida.presenters.tools;

public class Validation {

    /**
     * verifica si un texto está o no vacio (Sin tomar en cuenta espacios)
     * @param text
     * @return
     */
    public static boolean isNotEmpty(String text){
        text = Validation.correctText(text);
        text = text.trim();
        if(text.isEmpty()){
            return false;
        }else{
            return true;
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

    public static boolean isPhone(){

        return true;
    }

}
