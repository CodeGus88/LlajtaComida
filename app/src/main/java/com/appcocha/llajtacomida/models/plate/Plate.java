package com.appcocha.llajtacomida.models.plate;
import com.appcocha.llajtacomida.models.ObjectParent;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta es la clase es el modelo para los objetos Plato
 */
@IgnoreExtraProperties
public class Plate extends ObjectParent {

    private String ingredients;
    private String origin;

    /**
     * Inicializa una nueva instancia según la clase padre (ObjectParent), definiendo el id
     * @param id
     */
    public Plate(String id){
        super(id);
    }

    /**
     * Inicializa una nueva instancia según la clase padre (ObjectParent), donde el id es autogenerado
     */
    public Plate(){
        super();
    }

    /**
     * Obtiene los ingredientes del plato
     * @return ingredients
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Establece los ingredientes del plato
     * @param ingredients
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Obtiene el origen del plato
     * @return origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Establece el origen del plato
     * @param origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Retorna referencia (Se usa para el buscador)
     * @return String
     */
    @Override
    public String toString() {
        return name + " " + ingredients + " " + origin + " " + id + " " + punctuation;
    }

    /**
     * Crea un mapa de referencia del objeto (Sirve para actualizar en la BD)
     * @return Map
     */
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name",  name);
        result.put("url",  url);
        result.put("ingredients",  ingredients);
        result.put("origin",  origin);
        result.put("punctuation",  punctuation);
        return result;
    }

}
