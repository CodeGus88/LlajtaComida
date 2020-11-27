package com.example.llajtacomida.views.restaurantsViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    private MenuItem iconSearch;
    private RestPublicOfPresenter restPublicOfPresenter;
    private EditText etSearch;

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
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvPublicOfRest = (ListView) findViewById(R.id.lvPublicOfRest);

        etSearch.addTextChangedListener(new TextWatcher() { // para buscar mientras se escribe
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterRestPublicOf.filter(s.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIcon(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initIcon(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconSearch.setVisible(true);
        iconSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(etSearch.getVisibility() == View.GONE){
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }else{
                    etSearch.setText(null);
                    etSearch.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }
}