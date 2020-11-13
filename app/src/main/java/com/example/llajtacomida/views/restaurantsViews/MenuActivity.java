package com.example.llajtacomida.views.restaurantsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.MenuDB;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.example.llajtacomida.presenters.restaurantsPresenter.LadMenuPlates;
import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantPresenter;

public class MenuActivity extends AppCompatActivity {

    // Componentes
    private TextView tvTitle;
    private static  ListView lvPlates;
    private ArrayAdapterRestaurantMenu arrayAdapterRestaurantMenu;

    private Restaurant restaurant;

    LadMenuPlates plates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        }else{
            Toast.makeText(this, "No se encontró la informacción necesaria", Toast.LENGTH_SHORT).show();
        }

        initComponents();
        tvTitle.setText("Menu de " + restaurant.getName());
        plates = new LadMenuPlates(this, restaurant.getId());
    }

    private void initComponents(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvPlates = (ListView) findViewById(R.id.lvPlates);
        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MenuActivity.this, "Loala~~~~~~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void loadPlates(ArrayAdapterRestaurantMenu arrayAdapterRestaurantMenu){
        lvPlates.setAdapter(arrayAdapterRestaurantMenu);
    }
}