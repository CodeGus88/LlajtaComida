package com.appcocha.llajtacomida.model.plate;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.presenter.tools.Validation;
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
 * Modelo
 * Gestiona la información del platos
 */
public class PlateManagerModel implements PlateInterface.ModelPlateManager {

    private  DatabaseReference databaseReference;
    private StorageReference storageReference;
    private PlateInterface.PresenterPlateManager presenterPlateManager;

    /**
     * Modelo para gestos de plato
     * @param presenterPlateManager
     */
    public PlateManagerModel(PlateInterface.PresenterPlateManager presenterPlateManager) { // Context context
//        Configiración de la base de datos
        this.presenterPlateManager = presenterPlateManager;
    }

    @Override
    public void delete(String plateId){
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
//        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//            databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    presenterPlateManager.isSuccess(task.isSuccessful());
//                }
//            });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                presenterPlateManager.isSuccess(false);
//            }
//        });

//      Nueva forma: Tomando en cuenta las reglas de firebase (realtimedatabase)
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
//                            Log.d("xxxxx28", "---> " +true);
                            presenterPlateManager.isSuccess(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.d("xxxxx28", "---> " +false);
                            Log.e("Error", e.getMessage());
                            presenterPlateManager.isSuccess(false);
                        }
                    });
                }else{
//                    Log.d("xxxxx28", "---> " +false);
                    presenterPlateManager.isSuccess(false);
                }
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
        }
    }

    /**
     * Método interno, almacena un plato en la BD
     * @param plate
     * @param thumb_byte
     */
    private void uploadData(final Plate plate, final byte [] thumb_byte){
//        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if(task.isSuccessful()){
//                    return storageReference.getDownloadUrl();
//                }else{
//                    throw Objects.requireNonNull(task.getException());
//                }
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
//                    plate.setUrl(task.getResult().toString());
//                    databaseReference.setValue(plate).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            presenterPlateManager.isSuccess(true);
//                        }
//                    });
//                }else{
//                    presenterPlateManager.isSuccess(false);
//                }
//            }
//        });

// Nueva forma: Tomando en cuenta las reglas de firebase (realtimedatabase)
        databaseReference.setValue(plate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // cargar imagen
                if(task.isSuccessful()){
//                    Log.d("xxxxx28", "---> " +true);
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
                            if(task.isSuccessful()){
                                plate.setUrl(task.getResult().toString());
                                update(plate, null); // agrega la url despues de que termina de cargar el archivo
//                                presenterPlateManager.isSuccess(true);
                            }else{
                                presenterPlateManager.isSuccess(false);
                            }
                        }
                    });
                }else{
//                    Log.d("xxxxx28", "---> " + false);
                    presenterPlateManager.isSuccess(false);
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
//        UploadTask uploadTask = storageReference.putBytes(thumb_byte);
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if(task.isSuccessful()){
//                    return storageReference.getDownloadUrl();
//                }else{
//                    throw Objects.requireNonNull(task.getException());
//                }
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if(task.isSuccessful()){ // Si se subió la imagen, procedemos al registro en la base de datos
//                    plate.setUrl(task.getResult().toString());
//                    databaseReference.updateChildren(plate.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            presenterPlateManager.isSuccess(true);
//                        }
//                    });
//                }else{
//                    presenterPlateManager.isSuccess(false);
//                }
//            }
//        });

//  Nueva forma: Tomando en cuenta las reglas de firebase (realtimedatabase)
        databaseReference.updateChildren(plate.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    Log.d("xxxxx28", "---> " +true);
//                    presenterPlateManager.isSuccess(true);
                    // subir imagen
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
                            if(task.isSuccessful()){
                                plate.setUrl(task.getResult().toString());
                                databaseReference.updateChildren(plate.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        presenterPlateManager.isSuccess(task.isSuccessful());
                                    }
                                });
                            }
                        }
                    });
                }else {
//                    Log.d("xxxxx28", "---> " +true);
                    presenterPlateManager.isSuccess(false);
                }
            }
        });
    }
}
