package com.appcocha.llajtacomida.views.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.models.user.User;
import com.appcocha.llajtacomida.presenters.main.MainNavigation;
import com.appcocha.llajtacomida.presenters.user.AuthUser;
import com.appcocha.llajtacomida.presenters.user.UserPresenter;
import com.appcocha.llajtacomida.presenters.user.UserRealTimePresenter;
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

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        UserInterface.ViewUser,UserInterface.ViewUserRealTime {

    private AppBarConfiguration mAppBarConfiguration;
    // Login silencioso
    private GoogleApiClient googleApiClient;
    private final Hashtable<String, String> userDataList = new Hashtable<String, String>();
    private ProgressDialog progressDialog;
    private UserPresenter userPresenter;
    private UserRealTimePresenter userRealTimePresenter;
    private User user;
    private NavigationView navigationView;
    private String rol; // Para reinikciar la aplicación  si cambia el rol

    private AuthUser authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        getPregressDialog(getString(R.string.title_alert_init_title), getString(R.string.title_alert_init_message));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        getSupportActionBar().setIcon(R.mipmap.image_icon);
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

    } // End onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); // main es el menu de menu/main.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            userDataList.put("name", account.getGivenName());
            userDataList.put("family_name", account.getFamilyName());
            userDataList.put("email", account.getEmail());
//            userDataList.put("phone", account.get);
            userDataList.put("state", getString(R.string.state));
            if(account.getPhotoUrl() != null) Glide.with(this).load(account.getPhotoUrl()).into(ivAvatar);
            tvName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut(userDataList);
                }
            });
        }else{
            getPregressDialog(getString(R.string.usersTitle).toUpperCase(), getString(R.string.message_clossing));
            MainNavigation.showLogin(MainActivity.this);
            progressDialog.dismiss();
        }
    }

    private void loadDataUser(GoogleSignInAccount account){
        try {
            String role;
            if(account.getId().equalsIgnoreCase(getString(R.string.user_id_admin_default))) role = "admin";
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

    /**
     * Este método sirve para cerrar sesión
     */
    private void logOut(Hashtable<String, String> user){
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle(getString(R.string.user_information));
        confirmDialog.setMessage(
                            getString(R.string.tv_user_name)+" " + user.get("name")
                        + "\n" +getString(R.string.tv_user_fast_name)+" "+ user.get("family_name")
                        + "\n"+getString(R.string.tv_email)+ " "+ user.get("email")
                        + "\n"+getString(R.string.tv_state)+ " " + user.get("state"));
        confirmDialog.setCancelable(false);
        confirmDialog.setPositiveButton(getString(R.string.btn_sign_out), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                signOut();
            }
        });
        confirmDialog.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
//               Toast.makeText(MainActivity.this, "Cancelaste", Toast.LENGTH_SHORT).show();
            }
        });
        confirmDialog.show();
    }

    private void signOut(){
        userRealTimePresenter.stopRealtimeDatabase();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            if(status.isSuccess()){
                getPregressDialog(getString(R.string.tv_user_title), getString(R.string.message_clossing));
                MainNavigation.showLogin(MainActivity.this);
                authUser = null;
                progressDialog.dismiss();
                System.exit(0);
//                onDestroy();
            }else{
                Toast.makeText(MainActivity.this, getString(R.string.message_error), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

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
//            rol = user.getRole();
            Toast.makeText(this, getString(R.string.restart_app_message), Toast.LENGTH_LONG).show();
            authUser = null;
            System.exit(0);
//            this.onPause();
//            this.onStop();
//            this.finishAffinity();
//            this.onDestroy();
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

    private void stopRealtimeDatabase(){
        userPresenter.stopRealtimeDatabase();
        userRealTimePresenter.stopRealtimeDatabase();
    }
}