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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;

public class GaleryDatabase {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Context context;
    private  Image image;
    private byte thumb_byte [];
    private boolean isComplete;
    private static boolean isSuccess;
    private Uri url;
    private String objectParentType;
    private String parentId;

    private String dir;

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
        this.parentId = parentId;
        this.objectParentType = objectParentType;
        isComplete = false;
        isSuccess = false;
        initDatabase(objectParentType, parentId, resurceDestination);
    }

    /**
     * solo para eliminar todo
     * @param context
     * @param objectParentType
     * @param parentId
     */
    public GaleryDatabase(Context context, String objectParentType, String parentId){
        this.context = context;
        this.parentId = parentId;
        this.objectParentType = objectParentType;
        isComplete = false;
        isSuccess = false;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

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
        isSuccess = storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void deleteAllData() {

        databaseReference.child("App").child(objectParentType).child(parentId).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i:snapshot.getChildren()) {
                    try {
                        image = i.getValue(Image.class);
                        String toUrl = "images/"+ objectParentType +"/"+ parentId+"/"+image.getFileName();
                        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child(toUrl);
                        storageReference2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Error:", "Se eliminó la imagen");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("Error:", exception.getMessage());
//                                Toast.makeText(context, "Oh no, algo salió mal\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Algo salió mal");
            }
        });
    }

    public boolean isComplete(){
        return isComplete;
    }

    public boolean isSuccess(){
        return isSuccess;
    }

}
