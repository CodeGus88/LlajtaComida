package com.appcocha.llajtacomida.presenter.tools;

/**
 * La clase contiene las cadenas de texto (métodos estáticos), tales como las rutas de conexión con la base de datos
  */
public class StringValues {

    /**
     * Retorna la url de la base de datos relatime database
     * @return DBurl
     */
    public static String getDBURL(){
        return "https://llajtacomida-f137b.firebaseio.com";
    }

    /**
     * Retorna el id del usuario administrador por defecto (id de Google)
     * @return id
     */
    public static String getUserId(){
        return "105708453298523886510";
    }

    /**
     * Retorna el precion por defecto
     * @return precio
     */
    public static String getDefaultPrice(){
        return "10";
    }

    /**
     * Establece la duración de presentación de las imágenes en platos y restaurantes
     * @return  presentacionTime
     */
    public static String getPresentationTime(){
        return "3000"; // milisegundos
    }
}
