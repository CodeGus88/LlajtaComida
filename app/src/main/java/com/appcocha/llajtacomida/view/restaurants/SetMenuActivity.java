package com.appcocha.llajtacomida.view.restaurants;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterSetMenu;
import com.appcocha.llajtacomida.presenter.restaurant.SetMenuListPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

/**
 * Vista, gestor de menú de platos de un restaurante
 */
public class SetMenuActivity extends AppCompatActivity implements RestaurantInterface.ViewSetMenuList, View.OnClickListener{

    // Componentes
    private TextView tvTitle;
    private ListView lvPlates;
    private EditText etSearch;
    private MenuItem iconSave;
    private Restaurant restaurant;
    private ArrayList<Plate> plateList;
    private com.appcocha.llajtacomida.model.restaurant.menu.Menu menu;
    private AlertDialog alertDialog;
    private TextView tvAlertTitlePrice;
    private EditText etAlertPrice;
    private Button btnAlertCancel, btnAlertAdd;
    private ImageButton btnAlertDelete;
    private int position;
//    private int textPosition;
    // Permisos
//    private boolean isAdministrator, isAuthor;
    private ArrayAdapterSetMenu arrayAdapterSetMenu;

    // presentador
    private SetMenuListPresenter setMenuListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Configuración del boton atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plateList = new ArrayList<Plate>();
        menu = new com.appcocha.llajtacomida.model.restaurant.menu.Menu();
        // Premisos
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

        if(!Permission.getAuthorize(AuthUser.user.getRole()
                , Permission.SHOW_SET_RESTAURANT_MENU
                , AuthUser.getUser().getId().equals(restaurant.getAuthor()))){
            Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvPlates = (ListView) findViewById(R.id.lvPlates);

        View viewAlert = getLayoutInflater().inflate(R.layout.alert_price, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewAlert);
        tvAlertTitlePrice = (TextView) viewAlert.findViewById(R.id.tvAlertTitle);
        etAlertPrice = (EditText) viewAlert.findViewById(R.id.etAlertPrice);
        btnAlertCancel = (Button) viewAlert.findViewById(R.id.btnAlertCancel);
        btnAlertAdd = (Button) viewAlert.findViewById(R.id.btnAlertAdd);
        btnAlertDelete = (ImageButton) viewAlert.findViewById(R.id.btnAlertDelete);

        alertDialog = builder.create();
        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                SetMenuActivity.this.position = position;

                // editado
                alertDialog.show();
                tvAlertTitlePrice.setText(plateList.get(position).getName());

                Plate plate = plateList.get(position);
                String id_price = "";
                for(String id_p : menu.getMenuList()){
                    if(id_p.contains(plate.getId())){
                        id_price = id_p;
                        break;
                    }
                }
                if(!id_price.isEmpty()){
                    etAlertPrice.setText(Validation.getXWord(id_price, 2).replace("_", " "));
                    btnAlertDelete.setVisibility(View.VISIBLE);
                }else{
                    btnAlertDelete.setVisibility(View.GONE);
                }
            }
        });

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
        btnAlertCancel.setOnClickListener(this);
        btnAlertAdd.setOnClickListener(this);
        btnAlertDelete.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIconMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Iniciliza los íconos
     * @param menu
     */
    private void initIconMenu(Menu menu){
            if(Permission.getAuthorize(AuthUser.user.getRole()
                , Permission.WRITE_RESTAURANT_MENU
                , AuthUser.getUser().getId().equals(restaurant.getAuthor()))){
                iconSave = (MenuItem)  menu.findItem(R.id.iconSave);
                iconSave.setVisible(true);
            }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Sound.playClick();
        switch (item.getItemId()){
            case R.id.iconSave:
                if(Permission.getAuthorize(AuthUser.user.getRole()
                        , Permission.WRITE_RESTAURANT_MENU
                        , AuthUser.getUser().getId().equals(restaurant.getAuthor()))){
                    setMenuListPresenter.saveMenuList(restaurant.getId(), arrayAdapterSetMenu.getMenu()); // this
                    onBackPressed();
                }else Toast.makeText(this, getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d("Null", getString(R.string.message_invalid_option));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSetMenuList(ArrayList<Plate> plateList, com.appcocha.llajtacomida.model.restaurant.menu.Menu menu) {
        this.plateList.clear();
        this.plateList.addAll(plateList);
        this.menu = menu;
        arrayAdapterSetMenu = new ArrayAdapterSetMenu(this, R.layout.adapter_element_restaurant_menu, this.plateList, menu);
        lvPlates.setAdapter(arrayAdapterSetMenu);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
//        setMenuListPresenter.stopRealTimeDatabase();
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this); //Animación al cambiar de actividad
    }

    @Override
    public void onClick(View v) {
        Sound.playClick();
        if(v.getId() == R.id.btnAlertAdd){
            int requestCode = Validation.validateNumbers(etAlertPrice.getText().toString());
            if(requestCode == 0){
                String prices = Validation.correctText(etAlertPrice.getText().toString()).replace(" ", "_");
                prices = Validation.correctNumbers(prices, "_");
                arrayAdapterSetMenu.addPrice(plateList.get(position).getId(), prices);
                Toast.makeText(this, getString(R.string.added_element), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }else{
                Toast.makeText(this, getString(requestCode), Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.btnAlertCancel){
            alertDialog.dismiss();
        }else if(v.getId() == R.id.btnAlertDelete){
            try {
                arrayAdapterSetMenu.removePlate(plateList.get(position).getId(), true);
                Toast.makeText(this, getString(R.string.message_remove), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Sound.playThrow();
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }
        }
    }

    // Ciclos de vida para el reinicio de los presentadores

    @Override
    protected void onResume() {
        super.onResume();
        // Inicializar el presentador
        setMenuListPresenter = new SetMenuListPresenter(this);
        setMenuListPresenter.searchSetMenuList(restaurant.getId());
        Log.d("cicleLive", "SetMenu onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setMenuListPresenter.stopRealTimeDatabase();
        Log.d("cicleLive", "SetMenu onPause");
    }
}