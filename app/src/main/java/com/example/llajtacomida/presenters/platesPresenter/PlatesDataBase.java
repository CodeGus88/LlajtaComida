package com.example.llajtacomida.presenters.platesPresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.llajtacomida.models.Plate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class PlatesDataBase {

    private final DatabaseReference databaseReference;
    private final StorageReference storageReference;
    private final Plate plate;
    private final Context context;
//    private final Uri uri; // es la imagen con la uri local del dispositivo
    private byte  [] thumb_byte;
    private static boolean success;
    private Uri url; // El url que tendrá la imagen cuando se suba
    private ProgressDialog progressDialog;

    public PlatesDataBase(Context context, Plate plate, final byte [] thumb_byte) {

        this.context = context;
        this.plate = plate;
        this.thumb_byte = thumb_byte; // es l aimagen comprimida
        this.success = false;
        url = null;

//        Configiración de la base de datos
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("plates").child(plate.getId());
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("Images")
                .child("plates")
                .child(plate.getId())
                .child(plate.getId()+".jpg");
    }

    public void storePlate(final ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if(task.isSuccessful()){
                    PlatesDataBase.success = true;
                    return storageReference.getDownloadUrl();
                }else{
                    PlatesDataBase.success = false;
                    throw Objects.requireNonNull(task.getException());
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(success){ // Si se subió la imagen, procedemos al registro en la base de datos
                    url = task.getResult();
                    plate.setUrl(url.toString());
                    databaseReference.setValue(plate);

                    Toast.makeText(context, "Se guradó correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "No se pudo subir el registro solicitado, intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public boolean update(){

        return true;
    }

}
