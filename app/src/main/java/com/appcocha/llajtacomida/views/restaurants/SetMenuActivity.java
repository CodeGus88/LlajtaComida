package com.appcocha.llajtacomida.views.restaurants;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.restaurant.ArrayAdapterSetMenu;
import com.appcocha.llajtacomida.presenters.restaurant.SetMenuListPresenter;

import java.util.ArrayList;

public class SetMenuActivity extends AppCompatActivity implements RestaurantInterface.ViewSetMenuList {

    // Componentes
    private TextView tvTitle;
    private  ListView lvPlates;
    private EditText etSearch;
    private MenuItem iconSave;
    private Restaurant restaurant;
    // Permisos
    private boolean isAdministrator, isAuthor;
    private ArrayAdapterSetMenu arrayAdapterSetMenu;

    // presentador
    private SetMenuListPresenter setMenuListPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Configuración del boton atrás

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Premisos
        isAdministrator = true;
        isAuthor = true;

        Intent intent = getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
            getSupportActionBar().setTitle(restaurant.getName());
            getSupportActionBar().setSubtitle(R.string.menu);
        }else{
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }

        initComponents();
        tvTitle.setText(getString(R.string.menu_title) + " " + restaurant.getName());

        // Inicializar el presentador
        setMenuListPresenter = new SetMenuListPresenter(this);
        setMenuListPresenter.searchSetMenuList(restaurant.getId());
    }

    private void initComponents(){
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvPlates = (ListView) findViewById(R.id.lvPlates);
        etSearch.requestFocus();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                try {
                    arrayAdapterSetMenu.filter(ss.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIconMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initIconMenu(Menu menu){
        if(isAdministrator || isAuthor){
            iconSave = (MenuItem)  menu.findItem(R.id.iconSave);
            iconSave.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSave:
                setMenuListPresenter.saveMenuList(restaurant.getId(), arrayAdapterSetMenu.getMenu()); // this
                setMenuListPresenter.stopRealTimeDatabase();
                onBackPressed();
                break;
            default:
                Log.d("Null", getString(R.string.message_invalid_option));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showSetMenuList(ArrayList<Plate> plateList, com.appcocha.llajtacomida.models.restaurant.menu.Menu menu) {
        arrayAdapterSetMenu = new ArrayAdapterSetMenu(this, R.layout.adapter_element_restaurant_menu, plateList, menu);
        lvPlates.setAdapter(arrayAdapterSetMenu);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        setMenuListPresenter.stopRealTimeDatabase();
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    @Override
    public void onBackPressed() {
        setMenuListPresenter.stopRealTimeDatabase();
        super.onBackPressed();
    }
}