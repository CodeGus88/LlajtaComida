package com.example.llajtacomida.views.platesViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.platesPresenter.PlatesDataBase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class CreatePlateActivity extends AppCompatActivity implements View.OnClickListener {

    // componetes
    private EditText etName, etIngredients, etOrigin;
    private ImageView ivPhoto;
    private Button btnSelectFoto, btnCancel, btnStore;
    private static Uri uri;
    private ProgressDialog progressDialog;

    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_plate);
        // Configuración del boton atrás
        //        getSupportActionBar().setTitle("NEW RESTAURANT");
        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setComponents();
        thumb_bitmap = null;
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSelectPhoto:
                imageSelect();
                break;
            case R.id.btnStore:
                storePlate();
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
        }
    }


    private void storePlate() {

        progressDialog.setTitle("GUARDAR");
        progressDialog.setMessage("Guardando, espere...");
        progressDialog.show();
        String id = UUID.randomUUID().toString();
        Plate plate = new Plate(id);
        plate.setName(etName.getText().toString());
        plate.setIngredients(etIngredients.getText().toString());
        plate.setOrigin(etOrigin.getText().toString());
        PlatesDataBase platesDataBase = new PlatesDataBase(this, plate, thumb_byte);
        platesDataBase.storePlate(progressDialog);


    }

    private void imageSelect(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(1280, 720)
//                .setRequestedSize(640, 480)
                .setAspectRatio(2, 1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Cargar y comprimir el archivo
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        File file = new File(uri.getPath());

        // cargamos al imagen al ivPhoto con picaso
//                Picasso.with(this).load(file).into(ivPhoto);
        ivPhoto.setImageURI(uri);
        //Comprimir imagen
        try{
            thumb_bitmap = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(90)
                    .compressToBitmap(file);

        }catch (Exception e){
//                                Log.i("EROOR", e.getMessage());
            e.printStackTrace();
        }

        ByteArrayOutputStream biByteArrayOutputStream  = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, biByteArrayOutputStream);
        thumb_byte = biByteArrayOutputStream.toByteArray(); // Contiene la imagen comprimida
        // Fin del compresor

    }


    private void setComponents(){

        btnSelectFoto = (Button) findViewById(R.id.btnSelectPhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnStore = (Button) findViewById(R.id.btnStore);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        etName = (EditText) findViewById(R.id.etName);
        etIngredients = (EditText) findViewById(R.id.etIngredients);
        etOrigin = (EditText) findViewById(R.id.etOrigin);

        btnSelectFoto.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

}