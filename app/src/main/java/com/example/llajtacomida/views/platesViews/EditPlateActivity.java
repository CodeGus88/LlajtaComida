package com.example.llajtacomida.views.platesViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.platesPresenter.PlateDatabase;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.example.llajtacomida.presenters.tools.Validation;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

public class EditPlateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etIngredients, etOrigin;
    private ImageButton btnResetPhoto;
    private Button btnCancel, btnUpdate, btnSelectPhoto;
    private ImageView ivPhoto;
    private Plate plate;

    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plate);

        //Configiración del boton atrás
        getSupportActionBar().setTitle(R.string.platesTitle);
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumb_bitmap = null;

        initComponents();
        loadPlate();
    }

    private void loadPlate(){
        plate = (Plate) getIntent().getSerializableExtra("plate");
        etName.setText(plate.getName());
        etIngredients.setText(plate.getIngredients());
        etOrigin.setText(plate.getOrigin());
        Glide.with(this).load(plate.getUrl()).into(ivPhoto);
    }

    private void initComponents(){
        etName = (EditText) findViewById(R.id.etName);
        etIngredients = (EditText) findViewById(R.id.etIngredients);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        btnResetPhoto = (ImageButton) findViewById(R.id.btnResetPhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

//        Display display = getWindowManager().getDefaultDisplay();
//        ivPhoto.getLayoutParams().height = (int) (ScreenSize.getWidth(display)*0.6666667);
//        ivPhoto.getLayoutParams().width = ScreenSize.getWidth(display);
        Display display = getWindowManager().getDefaultDisplay();
        int x= (int) (ScreenSize.getWidth(display)*0.855);
        int y = (int) (x*0.6666667);
        ivPhoto.getLayoutParams().width = x;
        ivPhoto.getLayoutParams().height = y;

        btnSelectPhoto.setOnClickListener(this);
        btnResetPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
     }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSelectPhoto:
//                imageSelect();
                PlatePresenter.showCropImage(this);
                break;
            case R.id.btnResetPhoto:
                thumb_byte = null;
                Glide.with(this).load(plate.getUrl()).into(ivPhoto);
                break;
            case R.id.btnUpdate:
                updatePlate();
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Cargar y comprimir el archivo
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();

                // Segunda parte
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
                    e.printStackTrace();
                }
                ByteArrayOutputStream biByteArrayOutputStream  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, biByteArrayOutputStream);
                thumb_byte = biByteArrayOutputStream.toByteArray(); // Contiene la imagen comprimida
                // Fin del compresor
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void updatePlate(){

        String name = etName.getText().toString();
        String ingredients = etIngredients.getText().toString();
        String origin = etOrigin.getText().toString();

        TextInputLayout textInputLayoutName = (TextInputLayout) findViewById(R.id.tilName);
        TextInputLayout textInputLayoutIngredients = (TextInputLayout) findViewById(R.id.tilIngredients);
        TextInputLayout textInputLayoutOrigin = (TextInputLayout) findViewById(R.id.tilOriginAndDescription);

        if(Validation.isNotEmpty(name) ){
            textInputLayoutName.setError(null);
            if(Validation.isNotEmpty(ingredients)){
                textInputLayoutIngredients.setError(null);
                if(Validation.isNotEmpty(origin)){
                    textInputLayoutOrigin.setError(null);
                        Toast.makeText(this, "Actualizando el elemento...", Toast.LENGTH_LONG).show();
//                        Plate plate = this.plate;
                        plate.setName(name);
                        plate.setIngredients(ingredients);
                        plate.setOrigin(origin);
                        PlateDatabase platesDataBase = new PlateDatabase(this, plate, thumb_byte);
                        platesDataBase.upDate();
                        onBackPressed();
                }else textInputLayoutOrigin.setError("El campo origen es obligatorio"); // etOrigin.setError("El campo origen es obligatorio");// Toast.makeText(this, "El campo origen es obligatorio", Toast.LENGTH_SHORT).show();
            } else  textInputLayoutIngredients.setError("El campo ingredientes es obligatorio"); // etIngredients.setError("El campo ingredientes es obligatorio"); // Toast.makeText(this, "El campo ingredientes es obligatorio", Toast.LENGTH_SHORT).show();
        }else textInputLayoutName.setError("El campo nombre es obligatorio");
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }
}