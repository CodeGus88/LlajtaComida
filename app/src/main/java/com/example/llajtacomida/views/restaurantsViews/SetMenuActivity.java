package com.example.llajtacomida.views.restaurantsViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.presenters.restaurantsPresenter.ArrayAdapterSetMenu;
import com.example.llajtacomida.presenters.restaurantsPresenter.LoadSetMenuPlates;
import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantPresenter;

public class SetMenuActivity extends AppCompatActivity {

    // Componentes
    private TextView tvTitle;
    private static  ListView lvPlates;
    private static EditText etSearch;
//    private ArrayAdapterRestaurantMenu arrayAdapterRestaurantMenu;

    private MenuItem iconSave;

    private Restaurant restaurant;

    // Permisis
    private boolean isAdministrator, isAuthor;

    LoadSetMenuPlates loadSetMenuPlates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurantsTitle);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Premisos
        isAdministrator = true;
        isAuthor = true;

        Intent intent = getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        }else{
            Toast.makeText(this, "No se encontró la informacción necesaria", Toast.LENGTH_SHORT).show();
        }

        initComponents();
        tvTitle.setText("Menu de " + restaurant.getName());
        loadSetMenuPlates = new LoadSetMenuPlates(this, restaurant.getId());
    }

    private void initComponents(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvPlates = (ListView) findViewById(R.id.lvPlates);
        etSearch = (EditText) findViewById(R.id.etSearch);
    }

    /**
     * El listView se actualiza desde otra clase mendiante este método
     * @param arrayAdapterSetMenu
     */
    public static void loadPlates(ArrayAdapterSetMenu arrayAdapterSetMenu){
        lvPlates.setAdapter(arrayAdapterSetMenu);
    }

    public static EditText getEtSearch(){
      return etSearch;
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
                loadSetMenuPlates.saveMenu();
                RestaurantPresenter.showRestaurantViewFromSetMenu(this, restaurant);
                break;
            default:
                Log.d("Null", "Ícono desconocido");
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finishActivity(0);
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

}