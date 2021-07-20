package com.appcocha.llajtacomida.view.restaurants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterSetPromotions;
import com.appcocha.llajtacomida.presenter.restaurant.SetPromotionListPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;

import java.util.ArrayList;

public class SetPromotionsActivity extends AppCompatActivity implements RestaurantInterface.ViewSetPromotionList, View.OnClickListener {

    // Componentes
    private TextView tvTitle;
    private Switch switchShowAll;
    private ListView lvPlates;
    private EditText etSearch;
    private MenuItem iconSave;
    private Restaurant restaurant;
    private ArrayList<Plate> plateList;
    private AlertDialog alertDialog;
    private TextView tvAlertTitlePrice;
    private EditText etAlertTitle, etAlertDescription, etAlertPrice;
    private Button btnAlertCancel, btnAlertAdd;
    private int position;
    // Permisos
    private boolean isAdministrator, isAuthor;
    private ArrayAdapterSetPromotions arrayAdapterSetPromotions;

    // presentador
    private SetPromotionListPresenter setPromotionListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        // Configuración del boton atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plateList = new ArrayList<Plate>();
        // Premisos
        isAdministrator = true;
        isAuthor = true;

        Intent intent = getIntent();
        if(intent.hasExtra("restaurant")){
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
            getSupportActionBar().setTitle(restaurant.getName());
            getSupportActionBar().setSubtitle(R.string.promotion);
        }else{
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }

        initComponents();
        tvTitle.setText(getString(R.string.promotion_title) + " " + restaurant.getName());

        // Inicializar el presentador
        setPromotionListPresenter = new SetPromotionListPresenter(this);
        setPromotionListPresenter.searchSetPromotionList(restaurant.getId());
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        switchShowAll = (Switch) findViewById(R.id.switchShowAll);
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvPlates = (ListView) findViewById(R.id.lvPlates);

        View viewAlert = getLayoutInflater().inflate(R.layout.alert_set_promotion, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewAlert);
        tvAlertTitlePrice = (TextView) viewAlert.findViewById(R.id.tvAlertTitle);
        etAlertTitle = (EditText) viewAlert.findViewById(R.id.etAlertTitle);
        etAlertDescription = (EditText) viewAlert.findViewById(R.id.etAlertDescription);
        etAlertPrice = (EditText) viewAlert.findViewById(R.id.etAlertPrice);
        btnAlertCancel = (Button) viewAlert.findViewById(R.id.btnAlertCancel);
        btnAlertAdd = (Button) viewAlert.findViewById(R.id.btnAlertAdd);

        alertDialog = builder.create();
        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                SetPromotionsActivity.this.position = position;
                if(!arrayAdapterSetPromotions.existInPromotion(plateList.get(position).getId())){
                    alertDialog.show();
                    tvAlertTitlePrice.setText(plateList.get(position).getName());
                }else{
                    arrayAdapterSetPromotions.removePlate(plateList.get(position).getId());
                    try {
                        Sound.playThrow();
                    }catch (Exception e){
                        Log.e("Error: ", e.getMessage());
                    }
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
                    arrayAdapterSetPromotions.filter(ss.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAlertCancel.setOnClickListener(this);
        btnAlertAdd.setOnClickListener(this);
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
        if(isAdministrator || isAuthor){
            iconSave = (MenuItem)  menu.findItem(R.id.iconSave);
            iconSave.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Sound.playClick();
        switch (item.getItemId()){
            case R.id.iconSave:
                setPromotionListPresenter.savePromotionList(restaurant.getId(), arrayAdapterSetPromotions.getPromotion()); // this
                setPromotionListPresenter.stopRealTimeDatabase();
                onBackPressed();
                break;
            default:
                Log.d("Null", getString(R.string.message_invalid_option));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSetPromotionList(ArrayList<Plate> plateList, com.appcocha.llajtacomida.model.restaurant.menu.Menu menu, Promotion promotion) {
        this.plateList.clear();
        this.plateList.addAll(plateList);
        arrayAdapterSetPromotions = new ArrayAdapterSetPromotions(this, R.layout.adapter_element_restaurant_promotion, this.plateList, menu, promotion);
        lvPlates.setAdapter(arrayAdapterSetPromotions);
    }

    /**
     * Captura de acción del botón atrás <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        setPromotionListPresenter.stopRealTimeDatabase();
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    @Override
    public void onBackPressed() {
        setPromotionListPresenter.stopRealTimeDatabase();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Sound.playClick();
        if(v.getId() == R.id.btnAlertAdd){
            if(!etAlertTitle.equals("") && (!etAlertDescription.equals("") || !etAlertPrice.equals(""))){
                arrayAdapterSetPromotions.addPrice(plateList.get(position).getId(), etAlertTitle.getText().toString(), etAlertDescription.getText().toString(), etAlertPrice.getText().toString());
                alertDialog.dismiss();
            }else{
                Toast.makeText(this, getString(R.string.incomplete_fields), Toast.LENGTH_SHORT).show();
            }

        }else if(v.getId() == R.id.btnAlertCancel){
            alertDialog.dismiss();
        }
    }
}