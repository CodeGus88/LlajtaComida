package com.appcocha.llajtacomida.view.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.main.MainNavigation;
import com.appcocha.llajtacomida.presenter.tools.Serializer;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.UserPresenter;
import com.appcocha.llajtacomida.presenter.user.UserRealTimePresenter;
import com.appcocha.llajtacomida.view.restaurants.AlertShowPromotion;
import com.appcocha.llajtacomida.view.restaurants.AlertShowRestaurantsWithPromotion;
import com.appcocha.llajtacomida.view.restaurants.RestaurantViewActivity;
import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Vista, Esta es la clase donde se cargn todos los fragmentos del menú
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        UserInterface.ViewUser,UserInterface.ViewUserRealTime, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    // Login silencioso
    private GoogleApiClient googleApiClient;
    private ProgressDialog progressDialog;
    private UserPresenter userPresenter;
    private UserRealTimePresenter userRealTimePresenter;
    private User user;
    private NavigationView navigationView;
    private String rol; // Para reiniciar la aplicación  si cambia el rol
    private AuthUser authUser;

    // Alert componentes
    private AlertDialog alertDialog;
    private ImageView ivAvatar;
    private TextView tvId;
    private TextView tvFulName;
    private TextView tvEmail;
    private TextView tvRole;
    private TextView btnSignOut, btnOk;
    private Switch switchSound;

    // Botones flotantes
    private FloatingActionButton fabRestaurant;

    // Efectos de sonido
    private Sound sound;
    private final String SOUND_STATE_NAME = "SOUND_STATE"; // nombre del archivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        getPregressDialog(getString(R.string.title_alert_init_title), getString(R.string.title_alert_init_message));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(getString(R.string.sub_title));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setVisibility(View.GONE); // inicia en oculto (menu lateral)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_plates, R.id.nav_restaurants,
            R.id.nav_favorit_plates, R.id.nav_favorit_restaurants,
            R.id.nav_about_us, R.id.nav_users)
            .setDrawerLayout(drawer)
            .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Login silencioso
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Solo si es administrador mostrará la lista de usuarios
        navigationView.getMenu().findItem(R.id.nav_users).setVisible(false); // de inicio debe estar oculto

        initComponents();

        // Efectos de sonido
        sound = new Sound(this);
        Sound.setVolumeOff( Serializer.readBooleanData(this, SOUND_STATE_NAME));
        Sound.playStart();
    } // End onCreate

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        View viewAlert = getLayoutInflater().inflate(R.layout.alert_user_data, null);
        ivAvatar = (ImageView) viewAlert.findViewById(R.id.ivAvatar);
        tvId = (TextView) viewAlert.findViewById(R.id.tvId);
        tvFulName = (TextView) viewAlert.findViewById(R.id.tvFulName);
        tvEmail = (TextView) viewAlert.findViewById(R.id.tvEmail);
        tvRole = (TextView) viewAlert.findViewById(R.id.tvRole);
        switchSound = (Switch) viewAlert.findViewById(R.id.switchSound);
        btnSignOut = (TextView) viewAlert.findViewById(R.id.tvSignOut);
        btnOk = (TextView) viewAlert.findViewById(R.id.tvOk);
        fabRestaurant = (FloatingActionButton) findViewById(R.id.fabRestaurant);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewAlert);
        alertDialog = builder.create();

        switchSound.setChecked(Serializer.readBooleanData(this, SOUND_STATE_NAME));
        switchSound.setOnClickListener(this);

        ivAvatar.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        fabRestaurant.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); // main es el menu de menu/main.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Sound.playClick();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getBaseContext(), getString(R.string.message_error), Toast.LENGTH_SHORT).show();
    }
    // Para guardar la sesion
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr =  Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSingInResult(result); // Este metodo garga los resultados
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                handleSingInResult(googleSignInResult);
                }
            });
        }
    }

    /**
     * Obtine los resultados del usuario
     * @param result
     */
    private void handleSingInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            final GoogleSignInAccount account = result.getSignInAccount();
            loadDataUser(account);
            NavigationView navigationView =  findViewById(R.id.nav_view);
            View header =  navigationView.getHeaderView(0);
            ImageView ivAvatar = header.findViewById(R.id.ivAvatar);
            TextView tvName = header.findViewById(R.id.tvName);
            TextView tvEmail = header.findViewById(R.id.tvEmail);
            if(account.getPhotoUrl() != null) Glide.with(this).load(account.getPhotoUrl()).into(ivAvatar);
            tvName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            ivAvatar.setOnClickListener(this);
        }else{
            getPregressDialog(getString(R.string.usersTitle).toUpperCase(), getString(R.string.message_clossing));
            MainNavigation.showLogin(MainActivity.this);
            progressDialog.dismiss();
        }
    }

    /**
     * Inicializa los datos del usuario cuando este se identifica (le da el rol de admin si es llajta.comida.app)
     * @param account
     */
    private void loadDataUser(GoogleSignInAccount account){
        try {
            String role;
            if(account.getId().equalsIgnoreCase(StringValues.getUserId())) role = "admin";
            else role =  "collaborator";
            user = new User(
                FirebaseAuth.getInstance().getUid(),
                account.getDisplayName(),
                account.getEmail(),
                account.getPhotoUrl().toString(),
                role);
            userPresenter = new UserPresenter(this);
            userPresenter.findUser(FirebaseAuth.getInstance().getUid());
            userRealTimePresenter = new UserRealTimePresenter(this);
        }catch(Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvSignOut) Sound.playLoad();
        else Sound.playClick();
        switch (v.getId()){
            case R.id.ivAvatar:
                Glide.with(MainActivity.this).load(user.getAvatarUrl()).into(ivAvatar);
                tvId.setText(user.getId());
                tvFulName.setText(user.getFulName());
                tvEmail.setText(user.getEmail());
                tvRole.setText(getTraslateRole(user.getRole()));
                alertDialog.show();
                break;
            case R.id.tvSignOut:
                signOut();
                break;
            case R.id.tvOk:
                alertDialog.dismiss();
                break;
            case R.id.switchSound:
                if(switchSound.isChecked()) {
                    sound.setVolumeOff(true);
                    Serializer.saveBooleanData(this, SOUND_STATE_NAME, true); // guarda el estado
                }else{
                    sound.setVolumeOff(false);
                    Serializer.saveBooleanData(this, SOUND_STATE_NAME, false); //guarda el estado
                }
                break;
            case R.id.fabRestaurant:
//                AlertShowPromotion alertShowPromotion = new AlertShowPromotion(RestaurantViewActivity.this);
                AlertShowRestaurantsWithPromotion alert = new AlertShowRestaurantsWithPromotion(MainActivity.this);
                break;
            default:
                Toast.makeText(this, getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Traduce los roles al idioma correspondiente (español)
     * @param role
     * @return role
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
     * Cierra sesión del usuario
     */
    private void signOut(){
        userRealTimePresenter.stopRealtimeDatabase();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            if(status.isSuccess()){
                getPregressDialog(getString(R.string.tv_user_title), getString(R.string.message_clossing));
                FirebaseAuth.getInstance().signOut();
                new Timer().schedule(new TimerTask() { // esperamos 2 seg  para que termine de cerrar correctamente antes de matar el proceso
                    @Override
                    public void run() {
                        MainNavigation.showLogin(MainActivity.this);
                        authUser.setUser(null);
                        authUser = null;
                        progressDialog.dismiss();
                        System.exit(0);
                    }
                }, 2000);
            }else{
                Toast.makeText(MainActivity.this, getString(R.string.message_error), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    /**
     * Muestra un diálogo de progreso
     * @param title
     * @param message
     */
    private void getPregressDialog(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void showUser(User user) {
        progressDialog.dismiss();
        // Cargar el usuario
        if (user != null) {
            try {
                if (user.getRole() != null) {
                    if (!user.getRole().equals("")) {
                        this.user.setRole(user.getRole()); // Mantiene el rol, y no vuelve a darle uno por defecto
                    }
                }
            } catch (Exception e) { // Si no existe el usuario no habrá rol que leer, debe cerrarse la sesión
                Log.e("Error", e.getMessage());
                signOut();
                stopRealtimeDatabase();
            }
        }
        rol = this.user.getRole(); // El rol puede cambiar
        userPresenter = new UserPresenter(this);
        userPresenter.storeUser(this.user);
        // inicia el presentador de usuario en tiempo real
        userRealTimePresenter.findUser(FirebaseAuth.getInstance().getUid());
    }

    @Override
    public void showUserList(ArrayList<User> userList) {
        // No se usa para este caso
    }

    @Override
    public void showUserRT(User user) {
        authUser = null;
        authUser = new AuthUser(user); // Sirve para identificar al usuario en la lista de comentarios por ejemplo
        if(user.getRole().equalsIgnoreCase("admin")) navigationView.getMenu().findItem(R.id.nav_users).setVisible(true);
        else navigationView.getMenu().findItem(R.id.nav_users).setVisible(false);

        if(rol.equalsIgnoreCase(user.getRole())){
            if(!user.getRole().equals("admin") //user.getRole().equals("none") ||
                    && !user.getRole().equals("collaborator")
                    && !user.getRole().equals("reader")
                    && !user.getRole().equals("voter")) {
                Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
                try {
                    signOut();
                } catch (Exception e) {
                    MainNavigation.showLogin(this);
                    Log.e("Error", e.getMessage());
                }
                stopRealtimeDatabase();
                finish();
            }
        }else{ // es usuario none
            stopRealtimeDatabase();
            MainNavigation.accessToApp(this);
            Toast.makeText(this, getString(R.string.restart_app_message), Toast.LENGTH_LONG).show();
            authUser = null;
            System.exit(0);
        }
        navigationView.setVisibility(View.VISIBLE); // Una ves que carga el usuario, ya puede muestrar el menu lateral
    }

    @Override
    public void showReport(String message) {
        if(message.equalsIgnoreCase("user not found")){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            signOut();
        }
    }

    /**
     * Detiene la base de datos
     */
    private void stopRealtimeDatabase(){
        userPresenter.stopRealtimeDatabase();
        userRealTimePresenter.stopRealtimeDatabase();
    }

    public void sss(View view){
        Toast.makeText(this, "Lona...............", Toast.LENGTH_SHORT).show();
    }
}