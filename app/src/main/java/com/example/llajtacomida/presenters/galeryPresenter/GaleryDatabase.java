package com.example.llajtacomida.presenters.galeryPresenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.llajtacomida.models.Image;
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

public class GaleryDatabase {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private final Context context;
    private final Image image;
    private final byte thumb_byte [];
    private boolean isComplete;
    private static boolean isSuccess;
    private Uri url;

    /**
     *
     * @param context
     * @param image
     * @param thumb_byte
     */
    public GaleryDatabase(final Context context, final String objectParentType, final String parentId,
                          final String resurceDestination, final Image image, final byte [] thumb_byte){
        this.context = context;
        this.image = image;
        this.thumb_byte = thumb_byte;
        isComplete = false;
        isSuccess = false;
        initDatabase(objectParentType, parentId, resurceDestination);
    }

    public void initDatabase(String objectParentType, String parentId, String resurceDestination){
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("App")
                    .child(objectParentType)
                    .child(parentId)
                    .child("images")
                    .child(image.getId());
            storageReference = FirebaseStorage.getInstance().getReference().child(resurceDestination);
    }


    /**
     * Sube la imagen a storange de la basde de datos y se crea un objeto referente el la base de datos de tiempo real
     */
    public void uploadData(){
        if (thumb_byte != null) {
            UploadTask uploadTask = storageReference.putBytes(thumb_byte);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        GaleryDatabase.isSuccess = true;
                        return storageReference.getDownloadUrl();
                    } else {
                        GaleryDatabase.isSuccess = false;
                        Toast.makeText(context, "Ocurrió un error al subir la imagen", Toast.LENGTH_SHORT).show();
                        throw Objects.requireNonNull(task.getException());
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (isSuccess) {
                        url = task.getResult();
                        image.setUrl(url.toString());
                        databaseReference.setValue(image);
                        Toast.makeText(context, "Se procesó correctamente", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("Error ------------------>", task.getException().getMessage());
                    }
                }
            });
        }else{
            Toast.makeText(context, "No se detectó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(){
        isSuccess = true;
        isSuccess = isSuccess && storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.removeValue();
                Toast.makeText(context, "Se quitó la imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Oh no, algo salió mal\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).isSuccessful();
    }

    public boolean isComplete(){
        return isComplete;
    }

    public boolean isSuccess(){
        return isSuccess;
    }

}
