package com.example.llajtacomida.views.mapsViews;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetLocationMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;

    // Components
    private Spinner spTypesOfMaps;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initComponents();
    }

    private void initComponents(){
        spTypesOfMaps = (Spinner) findViewById(R.id.spTypesOfMaps);
        btnBack = (Button) findViewById(R.id.btnBack);
        String [] options = {
                getString(R.string.itemNormal),
                getString(R.string.itemHibrid),
                getString(R.string.itemSatellite),
                getString(R.string.itemTerrain)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_maps, options);
        spTypesOfMaps.setAdapter(adapter);
        spTypesOfMaps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals(getString(R.string.itemNormal))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.itemHibrid))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.itemSatellite))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (adapterView.getItemAtPosition(i).equals(getString(R.string.itemTerrain))) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else {
                    Toast.makeText(SetLocationMapActivity.this, "Ivalid option", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        Intent intent = getIntent();
        Restaurant restaurant;
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        }else{
            restaurant = new Restaurant();
        }
        LatLng restaurantLocation = new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()));
        marker = mMap.addMarker(new MarkerOptions().position(restaurantLocation).title(restaurant.getName()).snippet(getString(R.string.tvPhone) + ": " + restaurant.getPhone()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15.5f));
        mMap.getUiSettings().setZoomControlsEnabled(true); // opciones de zoom del mapa
    }
}