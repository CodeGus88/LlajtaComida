package com.appcocha.llajtacomida.models.plate;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.PlateInterface;
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

public class PlateManagerModel implements PlateInterface.ModelPlateManager {

    private  DatabaseReference databaseReference;
    private StorageReference storageReference;
    private PlateInterface.PresenterPlateManager presenterPlateManager;

    public PlateManagerModel(PlateInterface.PresenterPlateManager presenterPlateManager) { // Context context
//        Configiración de la base de datos
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        storageReference = FirebaseStorage.getInstance().getReference();
        this.presenterPlateManager = presenterPlateManager;
    }

    @Override
    public void delete(String plateId){
//        storageReference.child("images/plates/"+plateId+"/"+plateId+".jpg");
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("plates")
                .child(plateId);
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("plates")
                .child(plateId)
                .child(plateId+".jpg");
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenterPlateManager.isSuccess(task.isSuccessful());
                }
            });
//                Toast.makeText(context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context, "Oh no, algo salió mal\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                presenterPlateManager.isSuccess(false);
            }
        });
    }


    @Override
    public void store(final Plate plate, final byte [] thumb_byte) {
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("plates")
                .child(plate.getId());
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("plates")
                .child(plate.getId())
                .child(plate.getId()+".jpg");

        if (thumb_byte != null) {
            uploadData(plate, thumb_byte);
        }else{
            Log.e("Error: ", "Image null");
//            Toast.makeText(context, "No se detectó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(final Plate plate, final byte [] thumb_byte){
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference().child("App")
                .child("plates")
                .child(plate.getId());
        storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("images")
                .child("plates")
                .child(plate.getId())
                .child(plate.getId()+".jpg");
        if(thumb_byte != null){
            updatePlate(plate, thumb_byte);
        }else{
            databaseReference.updateChildren(plate.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenterPlateManager.isSuccess(task.isSuccessful());
                }
            });
//            Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    // Almacena nuevo
    private void uploadData(final Plate plate, final byte [] thumb_byte){
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
//                    PlateGestorDB.isSuccess = true;
                    return storageReference.getDownloadUrl();
                }else{
//                    PlateGestorDB.isSuccess = false;
                    throw Objects.requireNonNull(task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
//                    url = task.getResult();
                    plate.setUrl(task.getResult().toString());
                    databaseReference.setValue(plate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            presenterPlateManager.isSuccess(true);
                        }
                    });
//                    Toast.makeText(context, "Se procesó correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    presenterPlateManager.isSuccess(false);
//                    Toast.makeText(context, "No se pudo subir el registro solicitado, intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Actualiza un plato
     * @param plate
     * @param thumb_byte
     */
    private void updatePlate(final Plate plate, final byte [] thumb_byte){
        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
//                    PlateGestorDB.isSuccess = true;
                    return storageReference.getDownloadUrl();
                }else{
//                    PlateGestorDB.isSuccess = false;
                    throw Objects.requireNonNull(task.getException());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
    //                url = task.getResult();
                    plate.setUrl(task.getResult().toString());
                    databaseReference.updateChildren(plate.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            presenterPlateManager.isSuccess(true);
                        }
                    });
    //                    Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    presenterPlateManager.isSuccess(false);
    //                    Toast.makeText(context, "No se pudo actualizar el registro solicitado, intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
