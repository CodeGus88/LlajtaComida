package com.appcocha.llajtacomida.views.maps;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.tools.RandomColor;
import com.appcocha.llajtacomida.presenters.tools.Sound;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;

/**
 * Vista, establece la ubicación de un restaurante en el mapa
 */
public class SetLocationMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    // Components
    private Spinner spTypesOfMaps;
    private Button btnBack;
    private LinearLayout llData;
    private TextView tvRating, tvRestaurantName, tvAddress, tvPhone;
    private ImageView ivRestaurantImage;
    private CardView cvImage;

    private Restaurant restaurant;

    private LatLng latLngOrigin, latLngDestination;

    // GPS
    private final int REQUEST_ACCESS = 1;
    private boolean requestPermission;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestPermission = false;
        Intent intent = getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        }else{
            restaurant = new Restaurant();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        initComponents();
        Sound.playLocation();
    }

    /**
     * Inicia todos los componentes
     */
    private void initComponents(){
        spTypesOfMaps = (Spinner) findViewById(R.id.spTypesOfMaps);
        btnBack = (Button) findViewById(R.id.btnBack);
        llData = (LinearLayout) findViewById(R.id.llData);
        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvRating = (TextView) findViewById(R.id.tvRating);
        cvImage = (CardView) findViewById(R.id.cvImage);
        ivRestaurantImage = (ImageView) findViewById(R.id.ivRestaurantImage);
        String [] options = {
                getString(R.string.item_normal),
                getString(R.string.item_hibrid),
                getString(R.string.item_satellite),
                getString(R.string.item_terrain)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_maps, options);
        spTypesOfMaps.setAdapter(adapter);
        spTypesOfMaps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Sound.playClick();
                if (adapterView.getItemAtPosition(i).equals(getString(R.string.item_normal))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.item_hibrid))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.item_satellite))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.item_terrain))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else {
                    Toast.makeText(SetLocationMapActivity.this, getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        tvRating.setText(getString(R.string.tv_rating)+": "+String.valueOf(decimalFormat.format(restaurant.getPunctuation()) + "✭"));
        tvRestaurantName.setText(restaurant.getName());
        tvAddress.setText(restaurant.getAddress());
        tvPhone.setText("☎: " + restaurant.getPhone()
                .replace(",", " - ")
                .replace(".", " - ")
                .replace("-", " - ")
                .replace(";", " - ")
                .replace(":", " - "));
        Glide.with(this).load(restaurant.getUrl()).into(ivRestaurantImage);
        spTypesOfMaps.setSelection(1);
        btnBack.setOnClickListener(this);
        llData.setOnClickListener(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getPermission();
        loadLocations();
    }

    /**
     * Carga la localización del restaurante
     */
    private void loadLocations() {
        latLngDestination = new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()));
        MarkerOptions markerOptions = new MarkerOptions().position(latLngDestination).title(restaurant.getName()).snippet(restaurant.getAddress());
        RandomColor randomColor = new RandomColor();
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(randomColor.getRandonColor()));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngDestination));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngDestination, 15.5f));
    }

    /**
     * Verifica si se le proporcionó el permiso de uso de GPS
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS) {
            requestPermission = true;
            getPermission();
        }
    }

    /**
     * Solicita permiso para el uso de GPS, en caso de no contar con este
     */
    private void getPermission(){
        if(!requestPermission) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS);
                return;
            }
        }
            try {
                mMap.setMyLocationEnabled(true);
            }catch (Exception e){
                // Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, getString(R.string.message_no_location) + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() { // Centrar en la ubicación actual
                @Override
                public void onSuccess(Location location) {
                try {
                    if(location != null){
                        latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 9f));
                        latLngOrigin = new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 12f));
                    }
                }catch (Exception e){
                    Log.e("Error GPS", e.getMessage());
                    Toast.makeText(SetLocationMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                }
            });
            // Activar GPS
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                startGPS();
            }
    }


    /**
     * Solicita activar el GPS
     */
    private void startGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_gps_activate))
                .setTitle(getString(R.string.confirm_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        Sound.playClick();
        switch (v.getId()){
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.llData:
                if(cvImage.getVisibility() == View.VISIBLE){
                    cvImage.setVisibility(View.GONE);
                }else cvImage.setVisibility(View.VISIBLE);
                break;
        }
    }
}