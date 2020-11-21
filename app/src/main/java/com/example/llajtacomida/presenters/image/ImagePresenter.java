package com.example.llajtacomida.presenters.image;

import com.example.llajtacomida.interfaces.ImageInterface;
import com.example.llajtacomida.models.image.Image;
import com.example.llajtacomida.models.image.ImageModel;

import java.util.ArrayList;

public class ImagePresenter implements ImageInterface.PresenterImage {

    private ImageInterface.ViewImage viewImage;
    private ImageInterface.ModelImage modelImage;

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
    public void stopRealtimeDatabse() {
        modelImage.stopRealtimeDatabse();
    }
}
