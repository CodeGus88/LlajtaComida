package com.example.llajtacomida.views.restaurantsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.presenters.mapsPresenter.MapToGetLocationPresenter;

public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    // Components
    private EditText etName, etOwnerName, etAddress, etPhone, etOriginDesc;
    private TextView tvLatitude, tvLongitude;
    private ImageButton btnSetLocate;
    private Button btnSelectPhoto, btnCancel, btnStore;
    private ImageView ivPhoto;

    // object
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurantsTitle);
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponents();
        // Precargar el objeto restaurant (En casod e tener datos por defecto)
        final Intent intent = this.getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            whriteForm();
        }else{
            restaurant = new Restaurant();
        }
    }

    private void initComponents(){
        etName = (EditText) findViewById(R.id.etName);
        etOwnerName = (EditText) findViewById(R.id.etOwnerName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etOriginDesc = (EditText) findViewById(R.id.etOriginDesc);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        btnSetLocate = (ImageButton) findViewById(R.id.btnSetLocate);
        btnSelectPhoto = (Button) findViewById(R.id.btnSelectPhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnStore = (Button) findViewById(R.id.btnStore);
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
                MapToGetLocationPresenter.showGetLocationMapActivity(this, restaurant);
                break;
            case R.id.btnSelectPhoto:
                Toast.makeText(this, "Select foto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnStore:
                Toast.makeText(this, "Store", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Invalid option", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Este método escribe el formulario en el objeto
     */
    private void whriteRestaurant() {
        String name = etName.getText().toString();
        String ownerName = etOwnerName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();
        String originDesc = etOriginDesc.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();

        // traducir al objeto
        restaurant.setName(name);
        restaurant.setOwnerName(ownerName);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setOriginAndDescription(originDesc);
        restaurant.setLatitude(latitude);
        restaurant.setLongitude(longitude);
    }

    /**
     * Este método escribe el objeto en el formulario
     */
    private void whriteForm(){
        //Traducir al formulario a partir del objeto
        etName.setText(restaurant.getName());
        etOwnerName.setText(restaurant.getOwnerName());
        etAddress.setText(restaurant.getAddress());
        etPhone.setText(restaurant.getPhone());
        etOriginDesc.setText(restaurant.getOriginAndDescription());
        tvLatitude.setText(restaurant.getLatitude());
        tvLongitude.setText(restaurant.getLongitude());
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