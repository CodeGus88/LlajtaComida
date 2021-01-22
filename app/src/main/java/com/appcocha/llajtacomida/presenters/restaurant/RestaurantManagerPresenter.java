package com.appcocha.llajtacomida.presenters.restaurant;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.models.restaurant.RestaurantManagerModel;
import com.appcocha.llajtacomida.presenters.tools.Sound;
import com.appcocha.llajtacomida.presenters.tools.Validation;
import java.util.ArrayList;

/**
 * Presentador, gestor de estaurante
 */
public class RestaurantManagerPresenter implements RestaurantInterface.PresenterRestaurantManager {

    private RestaurantInterface.ViewRestaurantManager viewRestaurantManager;
    private RestaurantInterface.ModelRestaurantManager modelRestaurantManager;

    /**
     * Constructor, establece viewRestaurantManager
     * @param viewRestaurantManager
     */
    public RestaurantManagerPresenter(RestaurantInterface.ViewRestaurantManager viewRestaurantManager){
        this.viewRestaurantManager = viewRestaurantManager;
        modelRestaurantManager = new RestaurantManagerModel(this);
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        viewRestaurantManager.isSuccess(isSuccess);
    }

    @Override
    public void delete(String restaurantId) {
        modelRestaurantManager.delete(restaurantId);
    }

    @Override
    public void store(Restaurant restaurant, byte[] thumb_byte) {
        restaurant.setName(Validation.correctText(restaurant.getName()));
        restaurant.setOwnerName(Validation.correctText(restaurant.getOwnerName()));
        if(!restaurant.getName().isEmpty()
                && (restaurant.getOwnerName().isEmpty() || (!restaurant.getAuthor().isEmpty() && Validation.isPersonName(restaurant.getOwnerName())))
                && !restaurant.getPhone().isEmpty() && Validation.isPhone(restaurant.getPhone())
                && !restaurant.getAddress().isEmpty() && !restaurant.getOriginAndDescription().isEmpty()
                && (!restaurant.getLatitude().isEmpty() && !restaurant.getLongitude().isEmpty())
                && thumb_byte != null){
            Sound.playLoad();
            modelRestaurantManager.store(restaurant, thumb_byte);
        }else{
            ArrayList<Integer> errors = new ArrayList<Integer>();
            if(restaurant.getName().isEmpty()) errors.add(R.string.message_name_required);
            if(!restaurant.getOwnerName().isEmpty()) if(!Validation.isPersonName(restaurant.getOwnerName())) errors.add(R.string.message_owner_name_invalid);
            if(restaurant.getPhone().isEmpty())  errors.add(R.string.message_phone_required);
            if(!restaurant.getPhone().isEmpty()) if(!Validation.isPhone(restaurant.getPhone())) errors.add(R.string.message_phone_invalid);
            if(restaurant.getAddress().isEmpty()) errors.add(R.string.message_address_required);
            if(restaurant.getOriginAndDescription().isEmpty()) errors.add(R.string.message_description_required);
            if(restaurant.getLatitude().isEmpty() || restaurant.getLongitude().isEmpty()) errors.add(R.string.message_coordinates_required);
            if(thumb_byte == null) errors.add(R.string.message_image_required);
            viewRestaurantManager.report(errors);
        }
    }

    @Override
    public void update(Restaurant restaurant, final byte [] thumb_byte) {
        restaurant.setName(Validation.correctText(restaurant.getName()));
        restaurant.setOwnerName(Validation.correctText(restaurant.getOwnerName()));
        if(!restaurant.getName().isEmpty()
                && (restaurant.getOwnerName().isEmpty() || (!restaurant.getAuthor().isEmpty() && Validation.isPersonName(restaurant.getOwnerName())) )
                && !restaurant.getPhone().isEmpty() && Validation.isPhone(restaurant.getPhone())
                && !restaurant.getAddress().isEmpty() && !restaurant.getOriginAndDescription().isEmpty()
                && (!restaurant.getLatitude().isEmpty() && !restaurant.getLongitude().isEmpty())){
            Sound.playLoad();
            modelRestaurantManager.update(restaurant, thumb_byte);
        }else{
            ArrayList<Integer> errors = new ArrayList<Integer>();
            if(restaurant.getName().isEmpty()) errors.add(R.string.message_name_required);
            if(!restaurant.getOwnerName().isEmpty()) if(!Validation.isPersonName(restaurant.getOwnerName())) errors.add(R.string.message_owner_name_invalid);
            if(restaurant.getPhone().isEmpty())  errors.add(R.string.message_phone_required);
            if(!restaurant.getPhone().isEmpty()) if(!Validation.isPhone(restaurant.getPhone())) errors.add(R.string.message_phone_invalid);
            if(restaurant.getAddress().isEmpty()) errors.add(R.string.message_address_required);
            if(restaurant.getOriginAndDescription().isEmpty()) errors.add(R.string.message_description_required);
            if(restaurant.getLatitude().isEmpty() || restaurant.getLongitude().isEmpty()) errors.add(R.string.message_coordinates_required);
            viewRestaurantManager.report(errors);
        }
    }
}
