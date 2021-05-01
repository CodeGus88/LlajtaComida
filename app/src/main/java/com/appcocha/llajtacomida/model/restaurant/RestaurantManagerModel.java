package com.appcocha.llajtacomida.model.restaurant;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RestaurantManagerModel implements RestaurantInterface.ModelRestaurantManager{

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private RestaurantInterface.PresenterRestaurantManager presenterRestaurantManager;

    public RestaurantManagerModel(RestaurantInterface.PresenterRestaurantManager presenterRestaurantManager) { // Context context
        this.presenterRestaurantManager = presenterRestaurantManager;
    }

    @Override
    public void delete(String restaurantId){
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("restaurants")
                .child(restaurantId);
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("restaurants")
                .child(restaurantId)
                .child(restaurantId+".jpg");
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        presenterRestaurantManager.isSuccess(task.isSuccessful());
                    }
                });
//                Toast.makeText(context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context, "Oh no, algo salió mal\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                presenterRestaurantManager.isSuccess(false);
            }
        });
    }


    @Override
    public void store(final Restaurant restaurant, final byte [] thumb_byte) {
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("restaurants")
                .child(restaurant.getId());
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("restaurants")
                .child(restaurant.getId())
                .child(restaurant.getId()+".jpg");

        if (thumb_byte != null) {
            uploadData(restaurant, thumb_byte);
        }else{
            Log.e("Error: ", "Image null");
//            Toast.makeText(context, "No se detectó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(final Restaurant restaurant, final byte [] thumb_byte){
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("restaurants")
                .child(restaurant.getId());
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("restaurants")
                .child(restaurant.getId())
                .child(restaurant.getId()+".jpg");
        if(thumb_byte != null){
            updateRestaurant(restaurant, thumb_byte);
        }else{
            databaseReference.updateChildren(restaurant.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenterRestaurantManager.isSuccess(task.isSuccessful());
                }
            });
//            Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    // Almacena nuevo
    private void uploadData(final Restaurant restaurant, final byte [] thumb_byte){
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    return storageReference.getDownloadUrl();
                }else{
                    throw Objects.requireNonNull(task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
//                    url = task.getResult();
                    restaurant.setUrl(task.getResult().toString());
                    databaseReference.setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            presenterRestaurantManager.isSuccess(true);
                        }
                    });
//                    Toast.makeText(context, "Se procesó correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    presenterRestaurantManager.isSuccess(false);
//                    Toast.makeText(context, "No se pudo subir el registro solicitado, intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Actualiza un restaurante
     * @param restaurant
     * @param thumb_byte
     */
    private void updateRestaurant(final Restaurant restaurant, final byte [] thumb_byte){
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    return storageReference.getDownloadUrl();
                }else{
                    throw Objects.requireNonNull(task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
                    //                url = task.getResult();
                    restaurant.setUrl(task.getResult().toString());
                    databaseReference.updateChildren(restaurant.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            presenterRestaurantManager.isSuccess(true);
                        }
                    });
                }else{
                    presenterRestaurantManager.isSuccess(false);
                }
            }
        });
    }
}
