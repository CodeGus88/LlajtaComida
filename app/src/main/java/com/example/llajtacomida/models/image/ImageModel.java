package com.example.llajtacomida.models.image;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.ImageInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageModel implements ImageInterface.ModelImage, ValueEventListener {

    private ImageInterface.PresenterImage presenterImage;
    private DatabaseReference databaseReference;
    private ArrayList<Image> imagesList;

    public ImageModel(ImageInterface.PresenterImage presenterImage){
        this.presenterImage = presenterImage;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        imagesList = new ArrayList<Image>();
    }

    @Override
    public void searchImages(String nodeCollectionName, String modelId) {
        databaseReference.child("App").child(nodeCollectionName).child(modelId).child("images").addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabse() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        imagesList.clear();
        for (DataSnapshot image: snapshot.getChildren()) {
            imagesList.add(image.getValue(Image.class));
        }
        presenterImage.showImages(imagesList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error:", error.getMessage());
    }
}