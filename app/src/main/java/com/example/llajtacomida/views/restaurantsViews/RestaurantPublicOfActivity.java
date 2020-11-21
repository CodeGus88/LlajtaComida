package com.example.llajtacomida.views.restaurantsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.RestaurantInterface;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.presenters.restaurant.ArrayAdapterRestPublicOf;
import com.example.llajtacomida.presenters.restaurant.RestPublicOfPresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;

import java.util.ArrayList;

public class RestaurantPublicOfActivity extends AppCompatActivity implements RestaurantInterface.ViewRestPublicOf {

    private ListView lvPublicOfRest;
    private ArrayAdapterRestPublicOf arrayAdapterRestPublicOf;

    private RestPublicOfPresenter restPublicOfPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_public_of);

        getSupportActionBar().setTitle(R.string.restaurantsTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        restPublicOfPresenter = new RestPublicOfPresenter(this);
        restPublicOfPresenter.filterRestPublicOf(false);
    }

    private void initComponents() {
        lvPublicOfRest = (ListView) findViewById(R.id.lvPublicOfRest);
    }

    @Override
    public void showRestPublicOf(ArrayList<Restaurant> restaurantList) {
        arrayAdapterRestPublicOf = new ArrayAdapterRestPublicOf(this, R.layout.adapter_rest_public_of, restaurantList);
        lvPublicOfRest.setAdapter(arrayAdapterRestPublicOf);
    }

    @Override
    public void onBackPressed() {
        restPublicOfPresenter.stopRealTimeDatabase();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        restPublicOfPresenter.stopRealTimeDatabase();
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}