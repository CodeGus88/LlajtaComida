package com.appcocha.llajtacomida.model.restaurant;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.R;
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

/**
 * Esta clase no se reemplazó por la interface, porque tenia muchas dependencias
 */
public class RestaurantGestorDB {

    private final DatabaseReference databaseReference;
    private final StorageReference storageReference;
    private final Restaurant restaurant;
    private final Context context;
    private byte  [] thumb_byte;
    private Uri url;
    private static boolean isSuccess;

    public RestaurantGestorDB(final Context context, final Restaurant restaurant, final byte [] thumb_byte) {
        this.context = context;
        this.restaurant = restaurant;
        this.thumb_byte = thumb_byte; // es l aimagen comprimida
        this.isSuccess = false;
        url = null;
//        Configiración de la base de datos
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
    }

    public RestaurantGestorDB(Context context, Restaurant restaurant){
        this.context = context;
        this.restaurant = restaurant;
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("App")
                .child("restaurants").child(restaurant.getId());
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("images/restaurants/"+restaurant.getId()+"/"+restaurant.getId()+".jpg");
    }

    public void delete(){
        isSuccess = true;
        isSuccess = isSuccess && storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.removeValue();
//                Toast.makeText(context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, context.getString(R.string.message_delete_complete), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, context.getString(R.string.message_delete_incomplete)+"\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).isSuccessful();
    }

    public void upDate(){
        if(thumb_byte != null){
            update();
        }else{
            databaseReference.updateChildren(restaurant.toMap());
//            Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, context.getString(R.string.message_update_complete), Toast.LENGTH_SHORT).show();
        }
//        processComplete = true;
    }

    private void update(){
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        Task<Uri> uriTask =  uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    RestaurantGestorDB.isSuccess = true;
                    return storageReference.getDownloadUrl();
                }else{
                    RestaurantGestorDB.isSuccess = false;
                    throw Objects.requireNonNull(task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(isSuccess){ // Si se subió la imagen, procedemos al registro en la base de datos
                    url = task.getResult();
                    restaurant.setUrl(url.toString());
                    databaseReference.updateChildren(restaurant.toMap());
//                    Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, context.getString(R.string.message_update_complete), Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(context, "No se pudo actualizar el registro solicitado, intentelo más tarde", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, context.getString(R.string.message_update_incomplete), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isSuccess(){
        return isSuccess;
    }

}
