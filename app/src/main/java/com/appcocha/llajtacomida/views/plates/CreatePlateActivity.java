package com.appcocha.llajtacomida.views.plates;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.plate.PlateManagerPresenter;
import com.appcocha.llajtacomida.presenters.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenters.tools.ScreenSize;
import com.appcocha.llajtacomida.presenters.tools.Validation;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class CreatePlateActivity extends AppCompatActivity implements View.OnClickListener, PlateInterface.ViewPlateManager {

    // componetes
    private EditText etName, etIngredients, etOrigin;
    private ImageView ivPhoto;
    private Button btnSelectFoto, btnCancel, btnStore;
    private static Uri uri;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutIngredients;
    private TextInputLayout textInputLayoutOrigin;
    private TextInputLayout textInputLayoutImage;


    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;

    private ProgressDialog progressDialog;

    private PlateInterface.PresenterPlateManager presenterPlateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_plate);
        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.plates_title);
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setSubtitle(getString(R.string.title_creat_plate));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        thumb_bitmap = null;
        // Presentador
        presenterPlateManager = new PlateManagerPresenter(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSelectPhoto:
//                imageSelect();
                PlateNavegation.showCropImage(this);
                break;
            case R.id.btnStore:
                try {
                    storePlate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
        }
    }


    private void storePlate() throws InterruptedException {
        String name = etName.getText().toString();
        String ingredients = etIngredients.getText().toString();
        String origin = etOrigin.getText().toString();

        progressDialog.show();
        Plate plate = new Plate();
        plate.setName(name);
        plate.setIngredients(ingredients);
        plate.setOrigin(origin);
        presenterPlateManager.store(plate, thumb_byte);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Cargar y comprimir el archivo
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                //Segunda parte
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
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void initComponents(){
        btnSelectFoto = (Button) findViewById(R.id.btnSelectPhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnStore = (Button) findViewById(R.id.btnStore);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        etName = (EditText) findViewById(R.id.etName);
        etIngredients = (EditText) findViewById(R.id.etIngredients);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        initTextInputLayout();
        Display display = getWindowManager().getDefaultDisplay();
        int x= (int) (ScreenSize.getWidth(display)*0.855);
        int y = (int) (x*0.6666667);
        ivPhoto.getLayoutParams().width = x;
        ivPhoto.getLayoutParams().height = y;

        btnSelectFoto.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.message_uploading));
    }

    private void initTextInputLayout(){
        textInputLayoutName = (TextInputLayout) findViewById(R.id.tilName);
        textInputLayoutIngredients = (TextInputLayout) findViewById(R.id.tilIngredients);
        textInputLayoutOrigin = (TextInputLayout) findViewById(R.id.tilOriginAndDescription);
        textInputLayoutImage = (TextInputLayout) findViewById(R.id.tilImage);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getString(R.string.message_store_complete), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            onBackPressed();
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.message_store_incomplete), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void report(ArrayList<Integer> errors) {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.message_check_the_fileds), Toast.LENGTH_SHORT).show();
        textInputLayoutName.setError(null);
        textInputLayoutOrigin.setError(null);
        textInputLayoutIngredients.setError(null);
        textInputLayoutImage.setError(null);
        for(int error : errors){
            if(R.string.message_record_already_exists == error) textInputLayoutName.setError(getString(error));
            if (R.string.message_name_required == error) textInputLayoutName.setError(getString(error));
            if (R.string.message_description_required == error) textInputLayoutOrigin.setError(getString(error));
            if (R.string.message_ingredients_required == error) textInputLayoutIngredients.setError(getString(error));
            if (R.string.message_image_required == error) textInputLayoutImage.setError(getString(error));
        }
    }
}