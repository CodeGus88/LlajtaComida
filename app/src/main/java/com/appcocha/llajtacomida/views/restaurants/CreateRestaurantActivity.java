package com.appcocha.llajtacomida.views.restaurants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.map.MapNavegation;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantManagerPresenter;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenters.tools.ScreenSize;
import com.appcocha.llajtacomida.presenters.tools.Sound;
import com.appcocha.llajtacomida.presenters.tools.Validation;
import com.appcocha.llajtacomida.presenters.user.AuthUser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Vista, crea un nuevo restaurnate
 */
public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener, RestaurantInterface.ViewRestaurantManager {

    // Components
    private EditText etName, etOwnerName, etAddress, etPhone, etOriginAndDescription;
    private TextView tvLatitude, tvLongitude;
    private ImageButton btnSetLocate;
    private Button btnSelectPhoto, btnCancel, btnStore;
    private ImageView ivPhoto;
    private CheckBox cbPublic;
    private ProgressDialog progressDialog;
    private TextInputLayout tilName;
    private TextInputLayout tilOwnerName;
    private TextInputLayout tilPhone;
    private TextInputLayout tilAddress;
    private TextInputLayout tilLatLon;
    private TextInputLayout tilDescription;
    private TextInputLayout tilImage;

    // object
    private Restaurant restaurant;

    // Image select
    private Uri uri;
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;
    private AlertDialog.Builder builder;

    // Presentadores
    private RestaurantManagerPresenter restaurantManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurants_title);
        getSupportActionBar().setSubtitle(getString(R.string.title_creat_restaurant));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponents();
        readObjectIntent();
        whriteForm();
        readObjectIntent();
        restaurantManagerPresenter = new RestaurantManagerPresenter(this);
    }

    /**
     * En caso de que se contenga un objeto de entrada (volver desde la actividad para establecer la ubicacion en el mapa)
     */
    private void readObjectIntent(){
        final Intent intent = this.getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
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
            restaurant = new Restaurant();
        }
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        etName = (EditText) findViewById(R.id.etName);
        etOwnerName = (EditText) findViewById(R.id.etOwnerName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etOriginAndDescription = (EditText) findViewById(R.id.etOriginAndDescriptiono);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        btnSetLocate = (ImageButton) findViewById(R.id.btnSetLocate);
        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnStore = (Button) findViewById(R.id.btnStore);
        cbPublic = (CheckBox) findViewById(R.id.cbPublic);
        initTextInputLayout();

        Display display = getWindowManager().getDefaultDisplay();
        int x= (int) (ScreenSize.getWidth(display)*0.855);
        int y = (int) (x*0.6666667);
        ivPhoto.getLayoutParams().width = x;
        ivPhoto.getLayoutParams().height = y;

        //acction buttons
        btnSetLocate.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        if(AuthUser.getUser().getRole().equalsIgnoreCase("admin")){
            cbPublic.setVisibility(View.VISIBLE);
            cbPublic.setChecked(true);
        }else{
            cbPublic.setVisibility(View.GONE);
            cbPublic.setChecked(false);
        }

        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_confirm_message);
        builder.setMessage(Validation.getFirstName(AuthUser.getUser().getFulName()) + ", " + getString(R.string.confirm_message));
        builder.setPositiveButton(R.string.btn_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                storeRestaurant();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.message_uploading));
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
        tilImage = (TextInputLayout) findViewById(R.id.tilImage);
    }

    @Override
    public void onClick(View view) {
        Sound.playClick();
        switch (view.getId()){
            case R.id.btnSetLocate:
                whriteRestaurant();
                String path = "";
                if(uri != null) path = uri.getPath();
                MapNavegation.showGetLocationMapActivity(this, restaurant, "create", path);
                break;
            case R.id.btnSelectPhoto:
                RestaurantNavegation.showCropImage(this);
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnStore:
                if(AuthUser.getUser().getRole().equalsIgnoreCase("admin")){
                    storeRestaurant();
                }else{
                    builder.show();
                }
//                Toast.makeText(this, "Store", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Invalid option", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Solicita almacenar el el objeto y su imagen en la base de datos
     */
    private void storeRestaurant(){
        progressDialog.show();
        whriteRestaurant();
        restaurantManagerPresenter.store(restaurant, thumb_byte);
    }

    /**
     * Este método escribe el formulario en el objeto
     */
    private void whriteRestaurant() {
//        restaurant.setName(etName.getText().toString());
        restaurant.setName(etName.getText().toString());
        restaurant.setOwnerName(etOwnerName.getText().toString());
        restaurant.setAddress(etAddress.getText().toString());
        restaurant.setPhone(etPhone.getText().toString());
        restaurant.setOriginAndDescription(etOriginAndDescription.getText().toString());
        restaurant.setLatitude(tvLatitude.getText().toString());
        restaurant.setLongitude(tvLongitude.getText().toString());
        restaurant.setAuthor(FirebaseAuth.getInstance().getUid());
        restaurant.setPublic(cbPublic.isChecked());
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
//        ivPhoto // su trado es distinto y se precargar dessde el getIntent en caso de que se vuelva del mapa
    }

    /**
     * Código de la vista de 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                //Segunda parte
                File file = new File(uri.getPath());
                processImage(file);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void processImage(File file){
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
        Sound.playError();
        progressDialog.dismiss();
        tilName.setError(null);
        tilOwnerName.setError(null);
        tilPhone.setError(null);
        tilAddress.setError(null);
        tilLatLon.setError(null);
        tilDescription.setError(null);
        tilImage.setError(null);
        Toast.makeText(this, getString(R.string.message_check_the_fileds), Toast.LENGTH_SHORT).show();
        for(int error : errors){
            if(error == R.string.message_name_required) tilName.setError(getString(error));
            if(error == R.string.message_owner_name_invalid) tilOwnerName.setError(getString(error));
            if(error == R.string.message_phone_required) tilPhone.setError(getString(error));
            if(error == R.string.message_phone_invalid) tilPhone.setError(getString(error));
            if(error == R.string.message_address_required) tilAddress.setError(getString(error));
            if(error == R.string.message_coordinates_required) tilLatLon.setError(getString(error));
            if(error == R.string.message_description_required) tilDescription.setError(getString(error));
            if(error == R.string.message_image_required) tilImage.setError(getString(error));
        }
    }
}