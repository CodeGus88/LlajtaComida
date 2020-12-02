package com.example.llajtacomida.views.restaurantsViews;

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
import android.widget.TextView;
import android.widget.Toast;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.presenters.map.MapPresenter;
import com.example.llajtacomida.models.restaurant.RestaurantGestorDB;
import com.example.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    // Components
    private EditText etName, etOwnerName, etAddress, etPhone, etOriginAndDescription;
    private TextView tvLatitude, tvLongitude;
    private ImageButton btnSetLocate;
    private Button btnSelectPhoto, btnCancel, btnStore;
    private ImageView ivPhoto;

    // object
    private Restaurant restaurant;

    // Image select
    private Uri uri;
    private Bitmap thumb_bitmap;
    private byte  [] thumb_byte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurantsTitle);
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponents();
        readObjectIntent();
        whriteForm();
        readObjectIntent();

    }


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

//        Display display = getWindowManager().getDefaultDisplay();
//        ivPhoto.getLayoutParams().height = (int) (ScreenSize.getWidth(display)*0.6666667);
//        ivPhoto.getLayoutParams().width = ScreenSize.getWidth(display);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSetLocate:
                whriteRestaurant();
                String path = "";
                if(uri != null) path = uri.getPath();
                MapPresenter.showGetLocationMapActivity(this, restaurant, "create", path);
                break;
            case R.id.btnSelectPhoto:
                RestaurantNavegation.showCropImage(this);
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnStore:
                storeRestaurant();
//                Toast.makeText(this, "Store", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Invalid option", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void storeRestaurant(){
        Toast.makeText(this, "Subiendo elemento...", Toast.LENGTH_LONG).show();
        whriteRestaurant();
        RestaurantGestorDB platesDataBase = new RestaurantGestorDB(this, restaurant, thumb_byte);
        platesDataBase.storeRestaurant();
        onBackPressed();
    }

    /**
     * Este método escribe el formulario en el objeto
     */
    private void whriteRestaurant() {
        restaurant.setName(etName.getText().toString());
        restaurant.setOwnerName(etOwnerName.getText().toString());
        restaurant.setAddress(etAddress.getText().toString());
        restaurant.setPhone(etPhone.getText().toString());
        restaurant.setOriginAndDescription(etOriginAndDescription.getText().toString());
        restaurant.setLatitude(tvLatitude.getText().toString());
        restaurant.setLongitude(tvLongitude.getText().toString());
        restaurant.setAuthor(FirebaseAuth.getInstance().getUid());
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
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }
}