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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.restaurant.promotion.Promotion;
import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionElement;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterSetPromotions;
import com.appcocha.llajtacomida.presenter.restaurant.SetPromotionListPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class SetPromotionsActivity extends AppCompatActivity implements RestaurantInterface.ViewSetPromotionList, View.OnClickListener {

    // Componentes
    private TextView tvTitle;
    private Switch switchActivePromotion;
    private ListView lvPlates;
    private EditText etSearch;
    private MenuItem iconSave;
    private Restaurant restaurant;
    private ArrayList<Plate> plateList;
    private Promotion promotion;
    private AlertDialog alertDialog;
    private TextView tvAlertTitlePrice;
    private EditText etAlertTitle, etAlertDescription, etAlertPrice;
    private CheckBox cbAlertShowOldPrice;
    private Button btnAlertCancel, btnAlertAdd;
    private ImageButton btnDelete;
    private int position;
    // Permisos
//    private boolean isAdministrator, isAuthor;
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
        promotion = new Promotion();
        // Premisos
//        isAdministrator = true;
//        isAuthor = true;

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

        if(!Permission.getAuthorize(AuthUser.user.getRole()
                , Permission.SHOW_SET_RESTAURANT_PROMOTION
                , AuthUser.getUser().getId().equals(restaurant.getAuthor()))){
            Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        switchActivePromotion = (Switch) findViewById(R.id.switchActivePromotion);
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
        cbAlertShowOldPrice = (CheckBox) viewAlert.findViewById(R.id.cbAlertShowOldPrice);
        btnAlertCancel = (Button) viewAlert.findViewById(R.id.btnAlertCancel);
        btnAlertAdd = (Button) viewAlert.findViewById(R.id.btnAlertAdd);
        btnDelete = (ImageButton) viewAlert.findViewById(R.id.btnAlertDelete);
        alertDialog = builder.create();
        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                SetPromotionsActivity.this.position = position;
                // pruebas
                alertDialog.show();
                PromotionElement promElem = promotion.getPromotionElement(plateList.get(position).getId());
                tvAlertTitlePrice.setText(plateList.get(position).getName());
                if(promElem != null){
                    etAlertTitle.setText(promElem.getTitle());
                    etAlertDescription.setText(promElem.getDescription());
                    etAlertPrice.setText(String.valueOf(promElem.getPrice())
                            .replace(".0", "")
                            .replace("-1", ""));
                    cbAlertShowOldPrice.setChecked(promElem.isShowOldPrice());
                    btnDelete.setVisibility(View.VISIBLE);
                }else{
                    btnDelete.setVisibility(View.GONE);
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
        btnDelete.setOnClickListener(this);
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
            , Permission.WRITE_RESTAURANT_PROMOTION
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
                        , Permission.WRITE_RESTAURANT_PROMOTION
                        , AuthUser.getUser().getId().equals(restaurant.getAuthor()))) {
                    Promotion promotion = arrayAdapterSetPromotions.getPromotion();
                    promotion.setActive(switchActivePromotion.isChecked());
                    Toast.makeText(this, this.getString(R.string.menu_size) + " " + promotion.getPromotionList().size(), Toast.LENGTH_SHORT).show();
                    setPromotionListPresenter.savePromotionList(restaurant.getId(), promotion); // this
                    //                setPromotionListPresenter.stopRealTimeDatabase();
                    onBackPressed();
                }else Toast.makeText(this, getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
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
        this.promotion = promotion;

        arrayAdapterSetPromotions = new ArrayAdapterSetPromotions(this, R.layout.adapter_element_set_restaurant_promotion, this.plateList, menu, promotion);
        lvPlates.setAdapter(arrayAdapterSetPromotions);
        switchActivePromotion.setChecked(arrayAdapterSetPromotions.getPromotion().getActive());
    }

    /**
     * Captura de acción del botón atrás <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // acción del botón atrás del sistema operativo
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
            if(!etAlertTitle.equals("") && (!etAlertDescription.equals("") || !etAlertPrice.equals(""))){
                String price = Validation.correctNumbers(Validation.correctText(etAlertPrice.getText().toString()), " ");
                if (arrayAdapterSetPromotions.addPromotion(plateList.get(position).getId(), etAlertTitle.getText().toString()
                        , etAlertDescription.getText().toString(), price, cbAlertShowOldPrice.isChecked())){
                    Toast.makeText(this, getString(R.string.added_element), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else Toast.makeText(this, getString(R.string.incomplete_fields), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.incomplete_fields), Toast.LENGTH_SHORT).show();
            }

        }else if(v.getId() == R.id.btnAlertCancel){
            alertDialog.dismiss();
        }else if(v.getId() == R.id.btnAlertDelete){
            try {
                arrayAdapterSetPromotions.removePlate(plateList.get(position).getId());
                Toast.makeText(this, getString(R.string.message_remove), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Sound.playThrow();
            }catch (Exception e){
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    // Ciclos de vida para el reinicio de los presentadores

    @Override
    protected void onResume() {
        super.onResume();
        // Inicializar el presentador
        // Inicializar el presentador
        setPromotionListPresenter = new SetPromotionListPresenter(this);
        setPromotionListPresenter.searchSetPromotionList(restaurant.getId());
        Log.d("cicleLive", "SetPromotion onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPromotionListPresenter.stopRealTimeDatabase();
        Log.d("cicleLive", "SetPromotion onPause");
    }
}