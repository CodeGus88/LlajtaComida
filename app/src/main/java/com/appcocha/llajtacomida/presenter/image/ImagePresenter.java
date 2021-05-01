package com.appcocha.llajtacomida.presenter.image;

import com.appcocha.llajtacomida.interfaces.ImageInterface;
import com.appcocha.llajtacomida.model.image.Image;
import com.appcocha.llajtacomida.model.image.ImageModel;
import java.util.ArrayList;

/**
 * Modelo para imágenes
 */
public class ImagePresenter implements ImageInterface.PresenterImage {

    private ImageInterface.ViewImage viewImage;
    private ImageInterface.ModelImage modelImage;

    /**
     * Contructor, establece viewImage
     * @param viewImage
     */
    public ImagePresenter(ImageInterface.ViewImage viewImage){
        this.viewImage = viewImage;
        modelImage = new ImageModel(this);
    }

    @Override
    public void showImages(ArrayList<Image> imageList) {
        viewImage.showImages(imageList);
    }

    @Override
    public void searchImages(String nodeCollectionName, String modelId) {
        modelImage.searchImages(nodeCollectionName, modelId);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelImage.stopRealtimeDatabse();
    }
}
