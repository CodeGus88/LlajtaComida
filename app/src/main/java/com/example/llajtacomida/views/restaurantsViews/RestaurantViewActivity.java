package com.example.llajtacomida.views.restaurantsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.llajtacomida.R;

public class RestaurantViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurantsTitle);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}