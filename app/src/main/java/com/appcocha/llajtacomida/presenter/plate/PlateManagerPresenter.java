package com.appcocha.llajtacomida.presenter.plate;

import android.content.Context;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.plate.PlateManagerModel;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import java.util.ArrayList;

/**
 * Presentador, platos
 */
public class PlateManagerPresenter implements PlateInterface.PresenterPlateManager{

    private PlateInterface.ViewPlateManager viewPlateManager;
    private PlateInterface.ModelPlateManager modelPlateManager;

    /**
     * Constructor, establece viewPlateManager
     * @param viewPlateManager
     */
    public PlateManagerPresenter(PlateInterface.ViewPlateManager viewPlateManager){
        this.viewPlateManager = viewPlateManager;
        modelPlateManager = new PlateManagerModel(this);
    }


//    public void store(Plate plate, byte[] thumb_byte) {
    @Override
    public void store(Plate plate, byte[] thumb_byte, Context context) {
        plate.setName(Validation.correctText(plate.getName()));
        if(!Validation.existPlateName(plate.getName()) &&
                !plate.getName().isEmpty() && !plate.getOrigin().isEmpty() &&
                !plate.getIngredients().isEmpty() && thumb_byte != null
        ){
            Sound.playLoad();
            modelPlateManager.store(plate, thumb_byte);
            if(!Validation.isConeccted(context)){
                ArrayList<Integer> errors = new ArrayList<Integer>();
                errors.add(R.string.message_error_disconnected);
                viewPlateManager.report(errors);
            }
        }else{
            ArrayList<Integer> errors = new ArrayList<Integer>();
            if (!plate.getName().isEmpty()) if(Validation.existPlateName(plate.getName())) errors.add(R.string.message_record_already_exists);
            if(plate.getName().isEmpty()) errors.add(R.string.message_name_required);
            if(plate.getOrigin().isEmpty()) errors.add(R.string.message_description_required);
            if(plate.getIngredients().isEmpty()) errors.add(R.string.message_ingredients_required);
            if(thumb_byte == null) errors.add(R.string.message_image_required);
            viewPlateManager.report(errors);
        }
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        viewPlateManager.isSuccess(isSuccess);
    }

    @Override
    public void delete(String plateId) {
        modelPlateManager.delete(plateId);
    }

    //    public void update(Plate plate, final byte [] thumb_byte) {
    @Override
    public void update(Plate plate, final byte [] thumb_byte, Context context) {
        plate.setName(Validation.correctText(plate.getName()));
        if(!Validation.existPlateNameExcludePlateId(plate.getId(), plate.getName()) &&
                !plate.getName().isEmpty() && !plate.getOrigin().isEmpty() &&
                !plate.getIngredients().isEmpty()){
            Sound.playLoad();
            modelPlateManager.update(plate, thumb_byte);
            if(!Validation.isConeccted(context)){
                ArrayList<Integer> errors = new ArrayList<Integer>();
                errors.add(R.string.message_error_disconnected);
                viewPlateManager.report(errors);
            }
        }else{
            ArrayList<Integer> errors = new ArrayList<Integer>();
            if (!plate.getName().isEmpty()) if(Validation.existPlateNameExcludePlateId(plate.getId(), plate.getName())) errors.add(R.string.message_record_already_exists);
            if(plate.getName().isEmpty()) errors.add(R.string.message_name_required);
            if(plate.getOrigin().isEmpty()) errors.add(R.string.message_description_required);
            if(plate.getIngredients().isEmpty()) errors.add(R.string.message_ingredients_required);
            viewPlateManager.report(errors);
        }
    }
}
