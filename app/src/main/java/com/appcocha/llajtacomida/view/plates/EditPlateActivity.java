package com.appcocha.llajtacomida.view.plates;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.presenter.plate.PlateManagerPresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.tools.ScreenSize;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Vista, edita un plato
 */
public class EditPlateActivity extends AppCompatActivity implements View.OnClickListener, PlateInterface.ViewPlateManager {

    // components
    private EditText etName, etIngredients, etOrigin;
    private ImageButton btnResetPhoto;
    private Button btnCancel, btnUpdate, btnSelectPhoto;
    private ImageView ivPhoto;
    private Plate plate;
    private ProgressDialog progressDialog;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutIngredients;
    private TextInputLayout textInputLayoutOrigin;

    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;
    private Uri uri;

    // presentador
    private PlateManagerPresenter plateGestorPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plate);
        getSupportActionBar().setSubtitle(getString(R.string.title_edit_plate));

        //Configiración del boton atrás
        getSupportActionBar().setTitle(R.string.plates_title);
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumb_bitmap = null;

        initComponents();
        loadPlate();
        plateGestorPresenter = new PlateManagerPresenter(this);
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
        initTextInputLayout();

        Display display = getWindowManager().getDefaultDisplay();
        int x= (int) (ScreenSize.getWidth(display)*0.855);
        int y = (int) (x*0.6666667);
        ivPhoto.getLayoutParams().width = x;
        ivPhoto.getLayoutParams().height = y;

        btnSelectPhoto.setOnClickListener(this);
        btnResetPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.message_updating));
        progressDialog.setCancelable(false);
     }

     private void initTextInputLayout(){
        textInputLayoutName = (TextInputLayout) findViewById(R.id.tilName);
        textInputLayoutIngredients = (TextInputLayout) findViewById(R.id.tilIngredients);
        textInputLayoutOrigin = (TextInputLayout) findViewById(R.id.tilOrigin);
    }

    @Override
    public void onClick(View view) {
        Sound.playClick();
        switch (view.getId()){
            case R.id.btnSelectPhoto:
                PlateNavegation.showCropImage(this);
                break;
            case R.id.btnResetPhoto:
                thumb_byte = null;
                Glide.with(this).load(plate.getUrl()).into(ivPhoto);
                break;
            case R.id.btnUpdate:
                if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.UPDATE_PLATE)) updatePlate();
                else Toast.makeText(this, getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
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
//        progressDialog.show();
        progressDialog.show();
        plate.setName(name);
        plate.setIngredients(ingredients);
        plate.setOrigin(origin);
//        plateGestorPresenter.update(plate, thumb_byte);
        plateGestorPresenter.update(plate, thumb_byte, this);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // accion del boton atras del sistema operativo
        Sound.playClick();
        return false;
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        Sound.playSuccess();
        if(isSuccess){
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.message_update_complete), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.message_update_incomplete), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void report(ArrayList<Integer> errors) {
        Sound.playError();
        progressDialog.dismiss();
        if(!errors.contains(R.string.message_error_disconnected))
            Toast.makeText(this, getString(R.string.message_check_the_fileds), Toast.LENGTH_SHORT).show();
        textInputLayoutName.setError(null);
        textInputLayoutOrigin.setError(null);
        textInputLayoutIngredients.setError(null);
        for(int error : errors){
            if(R.string.message_record_already_exists == error) textInputLayoutName.setError(getString(error));
            if (R.string.message_name_required == error) textInputLayoutName.setError(getString(error));
            if (R.string.message_ingredients_required == error) textInputLayoutIngredients.setError(getString(error));
            if (R.string.message_description_required == error) textInputLayoutOrigin.setError(getString(error));
            if(R.string.message_error_disconnected == error){
                progressDialog.dismiss();
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed(){
        try{
            Animatoo.animateFade(this); //Animación al cambiar de actividad
            super.onBackPressed();
        }catch (Exception e){ // falla cuando vuelve a estar conectado
            Log.e("Error", e.getMessage());
        }
    }
}