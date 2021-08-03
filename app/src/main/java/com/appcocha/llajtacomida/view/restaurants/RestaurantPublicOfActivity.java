package com.appcocha.llajtacomida.view.restaurants;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterRestPublicOf;
import com.appcocha.llajtacomida.presenter.restaurant.RestPublicOfPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.appcocha.llajtacomida.presenter.user.UserNavegation;
import com.appcocha.llajtacomida.presenter.user.UserPresenter;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Vista, muetra la lista de restaurantes no publicados
 */
public class RestaurantPublicOfActivity extends AppCompatActivity implements RestaurantInterface.ViewRestPublicOf, View.OnClickListener, UserInterface.ViewUser {

    private static UserInterface.PresenterUser presenterUser;
    private RestPublicOfPresenter restPublicOfPresenter;

    private ListView lvPublicOfRest;
    private ArrayAdapterRestPublicOf arrayAdapterRestPublicOf;
    private MenuItem iconSearch;
    private EditText etSearch;
    // user alert
    private static AlertDialog userAlertDialog;
    private ImageView ivAvatar;
    private TextView tvId;
    private TextView tvFulName;
    private TextView tvEmail;
    private TextView tvRole;
    private RadioButton rbIsAdmin;
    private RadioButton rbIsCollaborator;
    private RadioButton rbIsVoter;
    private RadioButton rbIsReader;
    private RadioButton rbIsNone;
    private Button btnSave;
    private Button btnCancel;
    private User userEdit; // usuario que se muestra en el alert para editar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_public_of);

        getSupportActionBar().setTitle(R.string.restaurants_title);
        getSupportActionBar().setSubtitle(R.string.sub_title_restaurant_public_of);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();

        if(!Permission.getAuthorize(AuthUser.user.getRole(), Permission.SHOW_RESTAURANT_PUBLIC_OF_LIST_)){
            Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Inicializa los componentes
     */
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
        initAlertDialog();
    }

    /**
     * Traduce los roles
     * @param role (en inglés)
     * @return role (idioma del dispositivo)
     */
    private String getTraslateRole(String role){
        if(role.equalsIgnoreCase("admin")){
            role = getString(R.string.role_admin);
        }else if(role.equalsIgnoreCase("collaborator")){
            role = getString(R.string.role_collaborator);
        }else if(role.equalsIgnoreCase("voter")){
            role = getString(R.string.role_voter);
        }else if(role.equalsIgnoreCase("reader")){
            role = getString(R.string.role_reader);
        }else if(role.equalsIgnoreCase("none")){
            role = getString(R.string.role_none);
        }
        return role;
    }

    /**
     * Iniciliza el alert de opciones del autor del autor n de un restaurante
     */
    private void initAlertDialog() {
        View viewAlert = getLayoutInflater().inflate(R.layout.alert_user_options, null);
        ivAvatar = (ImageView) viewAlert.findViewById(R.id.ivAvatar);
        tvId = (TextView) viewAlert.findViewById(R.id.tvId);
        tvFulName = (TextView) viewAlert.findViewById(R.id.tvFulName);
        tvEmail = (TextView) viewAlert.findViewById(R.id.tvEmail);
        tvRole = (TextView) viewAlert.findViewById(R.id.tvRole);
        rbIsAdmin = (RadioButton) viewAlert.findViewById(R.id.rbIsAdmin);
        rbIsCollaborator = (RadioButton) viewAlert.findViewById(R.id.rbIsCollaborator);
        rbIsVoter = (RadioButton) viewAlert.findViewById(R.id.rbIsVoter);
        rbIsReader = (RadioButton) viewAlert.findViewById(R.id.rbIsReader);
        rbIsNone = (RadioButton) viewAlert.findViewById(R.id.rbIsNone);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewAlert);
        userAlertDialog = builder.create();
        // botones del alert
        btnCancel = (Button) viewAlert.findViewById(R.id.btnCancel);
        btnSave = (Button) viewAlert.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvEmail.setOnClickListener(this);

        if(!Permission.getAuthorize(AuthUser.getUser(this).getRole(), Permission.SHOW_RESTAURANT_PUBLIC_OF_LIST_)){
            onBackPressed();
            Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showRestPublicOf(ArrayList<Restaurant> restaurantList) {
        arrayAdapterRestPublicOf = new ArrayAdapterRestPublicOf(this, R.layout.adapter_rest_public_of, restaurantList);
        lvPublicOfRest.setAdapter(arrayAdapterRestPublicOf);
    }

    @Override
    public void onBackPressed() {
        Sound.playClick();
//        restPublicOfPresenter.stopRealTimeDatabase();
        super.onBackPressed();
        Animatoo.animateFade(this); //Animación al cambiar de actividad
    }

    @Override
    public boolean onSupportNavigateUp() {
//        restPublicOfPresenter.stopRealTimeDatabase();
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIcon(menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Iniciliza los íconos
     * @param menu
     */
    private void initIcon(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconSearch.setVisible(true);
        iconSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Sound.playClick();
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

    @Override
    public void onClick(View v) {
        Sound.playClick();
        switch (v.getId()){
            case R.id.btnSave:
                if(userEdit != null){
//                    User user = new User(tvId.getText().toString());
//                    user.setFulName(tvFulName.getText().toString());
//                    user.setEmail(tvEmail.getText().toString());
//                    user.setRole(changeUserRole());
                    userEdit.setRole(changeUserRole());
                    if(Permission.getAuthorize(AuthUser.getUser().getRole(), Permission.UPDATE_USER)){
                        presenterUser.storeUser(userEdit);
                    }else Toast.makeText(this, getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnCancel:
                userAlertDialog.dismiss();
                break;
            case R.id.tvEmail:
                UserNavegation.openMail(this, tvEmail.getText().toString());
                break;
        }
    }

    /**
     * Carga un al autor de un restaurante
     * @param userId
     */
    public static void loadAuthor(String userId){
        try{
            presenterUser.findUser(userId);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * Selecciona el rol de un autor
     * @return role
     */
    private String changeUserRole(){
        if(rbIsAdmin.isChecked()){
            return ("admin");
        }else if(rbIsCollaborator.isChecked()){
            return ("collaborator");
        }else if(rbIsVoter.isChecked()){
            return ("voter");
        } else if(rbIsReader.isChecked()){
            return ("reader");
        }else if(rbIsNone.isChecked()){
            return ("none");
        }else{ // Si  no existe por defecto none
            return ("none");
        }
    }

    /**
     * Se usa para ver el autor de cada item
     * @param user
     */
    @Override
    public void showUser(User user) {
        try{
            this.userEdit = user;
            Glide.with(this).load(user.getAvatarUrl()).into(ivAvatar);
            tvId.setText(user.getId());
            tvFulName.setText(user.getFulName());
            tvEmail.setText(user.getEmail());
//            tvRole.setText(user.getRole());
            tvRole.setText(getTraslateRole(user.getRole()));
            if(user.getRole().equals("admin")){
                rbIsAdmin.setChecked(true);
            }else if(user.getRole().equals("collaborator")){
                rbIsCollaborator.setChecked(true);
            }else if(user.getRole().equals("voter")){
                rbIsVoter.setChecked(true);
            }else if(user.getRole().equals("reader")){
                rbIsReader.setChecked(true);
            }else{
                rbIsNone.setChecked(true);
            }
        }catch (Exception e){
            Log.e("Error", "-----------------------------------------> " + e.getMessage());
        }
        userAlertDialog.show();
    }

    @Override
    public void showUserList(ArrayList<User> userList) {
        // no se usa en este caso
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getString(R.string.message_processed_correct), Toast.LENGTH_SHORT).show();
            userAlertDialog.dismiss();
        }else Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_SHORT).show();
    }

    // Ciclos de vida para el reinicio de los presentadores

    @Override
    protected void onResume() {
        super.onResume();
        if(Permission.getAuthorize(AuthUser.getUser(this).getRole(), Permission.SHOW_RESTAURANT_PUBLIC_OF_LIST_)){
            presenterUser = new UserPresenter(this);
            restPublicOfPresenter = new RestPublicOfPresenter(this);
            restPublicOfPresenter.filterRestPublicOf(false);
        }

        Log.d("cicleLive", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        restPublicOfPresenter.stopRealTimeDatabase();
        presenterUser.stopRealtimeDatabase();
        Log.d("cicleLive", "onPause");
    }
}