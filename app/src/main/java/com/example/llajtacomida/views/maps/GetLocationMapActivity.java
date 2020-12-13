package com.example.llajtacomida.views.maps;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GetLocationMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private Restaurant restaurant;
    private String uri;
    private String verb;
    // Components
    private SearchView searchView;
    private Button btnSet, btnBack;
    private TextView tvLatitude, tvLongitude;
    private Spinner spTypesOfMaps;

    //Maps
    private Marker marker;
    private LatLng latLng;
    private float zoom;
    private String title;
    private String message;
//    SupportMapFragment supportMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        message = getString(R.string.marker_search_description);
        title = getString(R.string.marker_title);

        // Rescatar los datos del objeto
        final Intent intent = this.getIntent();
        if(intent.hasExtra("restaurant") && intent.hasExtra("verb")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
            verb = getIntent().getStringExtra("verb");
            if(intent.hasExtra("uri")){
                uri = intent.getStringExtra("uri");
            }
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        //Definir estados
        zoom = 15.5F;
        initComponents();

    }

    private void initComponents() {
        searchView = (SearchView) findViewById(R.id.etSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAddress(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQuery(restaurant.getAddress(), false);
        searchView.requestFocus();

        btnSet = (Button) findViewById(R.id.btnSet);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnSet.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        spTypesOfMaps = (Spinner) findViewById(R.id.spTypesOfMaps);
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
                if (adapterView.getItemAtPosition(i).equals("Normal")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (adapterView.getItemAtPosition(i).equals("Híbrido")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (adapterView.getItemAtPosition(i).equals("Satélite")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (adapterView.getItemAtPosition(i).equals("Terreno")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else {
                    Toast.makeText(GetLocationMapActivity.this, "Opción inválida", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypesOfMaps.setSelection(1);
    }

    /**
     * Busca y dibuja el mardador a partir de una dirección query
     * @param query
     */
    private void searchAddress(String query) {
        ///// Efecto levantar camara
        Toast.makeText(this, "Buscando "+query+"...", Toast.LENGTH_SHORT).show();
        zoom = 6.5F;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        ////// fin efecto
        mMap.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName("Cochabamba: "+query, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null){
            title = query;
            latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title).draggable(true).snippet(message));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            zoom = 16.5F;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }else{
            Toast.makeText(this, "No se encontró la referencia", Toast.LENGTH_SHORT).show();
        }
        mMap.getUiSettings().setZoomControlsEnabled(true); // opciones de zoom
        updateLatLng();
    }

    private void updateLatLng(){
        tvLatitude.setText("Lat: " + marker.getPosition().latitude);
        tvLongitude.setText("Lon: " + marker.getPosition().longitude);
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
        latLng = null;
        // Localización por defecto
        Geocoder geocoder = new Geocoder(this);
        if(!restaurant.getLatitude().isEmpty() && !restaurant.getLongitude().isEmpty()){
            try {
                latLng = new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()));
            }catch (Exception e){
                e.printStackTrace();
                latLng = null;
            }
        }else if (!restaurant.getAddress().isEmpty()){
            try {
                List<Address> addresses = geocoder.getFromLocationName("Cochabamba: "+restaurant.getAddress(), 1);
                if(addresses.size() > 0){
                    latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    zoom = 15.5F;
                    if(!restaurant.getName().isEmpty()){
                        title = restaurant.getName();
                    }else{
                        title = restaurant.getAddress();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(latLng == null){
            zoom = 13.5F;
            title = "Cochabamba";
            latLng = new LatLng(-17.3937469,-66.156974); // https://www.google.com/maps/@-17.3937469,-66.156974,20.03z
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .draggable(true)
                .snippet(message));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.setOnMarkerDragListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        zoom = 15.5F;

        updateLatLng();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSet:
                restaurant.setLatitude(String.valueOf(marker.getPosition().latitude));
                restaurant.setLongitude(String.valueOf(marker.getPosition().longitude));
                if(verb.equalsIgnoreCase("create")){
                    RestaurantNavegation.showCreatedRestaurantView(this, restaurant, uri);
                }else if(verb.equalsIgnoreCase("edit")){
                    RestaurantNavegation.showEditRestaurantView(this, restaurant, uri);
                }
                Toast.makeText(this, getString(R.string.load_position), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
    // Métodos de eventos al mover el marcador
    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker){
        updateLatLng();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }
}