package com.appcocha.llajtacomida.view.restaurants;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.map.MapNavegation;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantManagerPresenter;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenter.tools.ScreenSize;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Vista, Edita un restaurante
 */
public class EditRestaurantActivity extends AppCompatActivity implements View.OnClickListener, RestaurantInterface.ViewRestaurantManager {

    // Components
    private EditText etName, etOwnerName, etAddress, etPhone, etOriginAndDescription;
    private TextView tvLatitude, tvLongitude;
    private ImageButton btnResetPhoto, btnSetLocate;
    private Button btnCancel, btnUpdate, btnSelectPhoto;
    private ImageView ivPhoto;
    private Restaurant restaurant;
    private ProgressDialog progressDialog;
    private TextInputLayout tilName;
    private TextInputLayout tilOwnerName;
    private TextInputLayout tilPhone;
    private TextInputLayout tilAddress;
    private TextInputLayout tilLatLon;
    private TextInputLayout tilDescription;

    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;
    private Uri uri;

    private RestaurantManagerPresenter restaurantManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        uri = null;
        //Configiración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurants_title);
        getSupportActionBar().setSubtitle(getString(R.string.title_edit_restaurant));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        readObjectData();
        whriteForm();

        restaurantManagerPresenter = new RestaurantManagerPresenter(this);
    }

    /**
     * En caso de que se contenga un objeto de entrada (modificado) (volver desde la actividad para establecer la ubicacion en el mapa)
     */
    private void readObjectData(){
        final Intent intent = this.getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            if(intent.hasExtra("uri")) { // Si tenia imagen al editar (volviendo desde mapa)
                // codificar
                String path = intent.getStringExtra("uri");
                if(path!=null){
                    File file = new File(path);
                    if(file.exists()){
                        uri = Uri.fromFile(file);
                        processImage(file);
                    }
                }
            } else thumb_bitmap = null;
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            onBackPressed();
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
                processImage(file);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /**
     * Muestra la imagen del restaurante (actual o modificado)
     * @param file
     */
    private void  processImage(File file){
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
    }

    /**
     * Inicializa los componetes
     */
    private void initComponents(){
        etName = (EditText) findViewById(R.id.etName);
        etOwnerName = (EditText) findViewById(R.id.etOwnerName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etOriginAndDescription = (EditText) findViewById(R.id.etOriginAndDescription);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        btnSetLocate = (ImageButton) findViewById(R.id.btnSetLocate);
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
        btnSetLocate.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnResetPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.message_updating));
        progressDialog.setCancelable(false);
    }

    /**
     * Inicializa los compoenentes TextInputLayout
     */
    public void initTextInputLayout(){
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        tilOwnerName = (TextInputLayout) findViewById(R.id.tilOwnerName);
        tilPhone = (TextInputLayout) findViewById(R.id.tilPhone);
        tilAddress = (TextInputLayout) findViewById(R.id.tilAddress);
        tilLatLon = (TextInputLayout) findViewById(R.id.tilLatLon);
        tilDescription = (TextInputLayout) findViewById(R.id.tilDescription);
    }

    @Override
    public void onClick(View view) {
        Sound.playClick();
        switch (view.getId()){
            case R.id.btnSetLocate:
                whriteRestaurant();
                String path = "";
                if(uri != null) path = uri.getPath(); // Apara salvar la imagen de fondo
                MapNavegation.showGetLocationMapActivity(this, restaurant, "edit", path);
                break;
            case R.id.btnSelectPhoto:
                RestaurantNavegation.showCropImage(this);
                break;
            case R.id.btnResetPhoto:
                thumb_byte = null;
                Glide.with(this).load(restaurant.getUrl()).into(ivPhoto);
                break;
            case R.id.btnUpdate:
                updateRestaurant();
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
        }
    }


    /**
     * Solicita actualizar el objeto
     */
    private void updateRestaurant(){
        whriteRestaurant();
        progressDialog.show();
        restaurantManagerPresenter.update(restaurant, thumb_byte);
    }

    /**
     * Este método escribe el formulario en el objeto
     */
    private void whriteRestaurant() {
//        restaurant.setName(etName.getText().toString());
        restaurant.setName(Validation.correctText(etName.getText().toString()));
        restaurant.setOwnerName(etOwnerName.getText().toString());
        restaurant.setAddress(etAddress.getText().toString());
        restaurant.setPhone(etPhone.getText().toString());
        restaurant.setOriginAndDescription(etOriginAndDescription.getText().toString());
        restaurant.setLatitude(tvLatitude.getText().toString());
        restaurant.setLongitude(tvLongitude.getText().toString());
    }

    /**
     * Este método escribe el objeto en el formulario
     */
    private void whriteForm(){
        etName.setText(restaurant.getName());
        etOwnerName.setText(restaurant.getOwnerName());
        etAddress.setText(restaurant.getAddress());
        etPhone.setText(restaurant.getPhone());
        etOriginAndDescription.setText(restaurant.getOriginAndDescription());
        tvLatitude.setText(restaurant.getLatitude());
        tvLongitude.setText(restaurant.getLongitude());
        if(thumb_bitmap == null){
            Glide.with(this).load(restaurant.getUrl()).into(ivPhoto);
        }else{
            ivPhoto.setImageBitmap(thumb_bitmap);
        }
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        Sound.playClick();
        onBackPressed(); // accion del boton atras del sistema operativo
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
        tilName.setError(null);
        tilOwnerName.setError(null);
        tilPhone.setError(null);
        tilAddress.setError(null);
        tilLatLon.setError(null);
        tilDescription.setError(null);
        Toast.makeText(this, getString(R.string.message_check_the_fileds), Toast.LENGTH_SHORT).show();
        for(int error : errors){
            if(error == R.string.message_name_required) tilName.setError(getString(error));
            if(error == R.string.message_owner_name_invalid) tilOwnerName.setError(getString(error));
            if(error == R.string.message_phone_required) tilPhone.setError(getString(error));
            if(error == R.string.message_phone_invalid) tilPhone.setError(getString(error));
            if(error == R.string.message_address_required) tilAddress.setError(getString(error));
            if(error == R.string.message_coordinates_required) tilLatLon.setError(getString(error));
            if(error == R.string.message_description_required) tilDescription.setError(getString(error));
        }
    }
}