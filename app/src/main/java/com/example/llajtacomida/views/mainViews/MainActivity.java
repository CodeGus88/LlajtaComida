package com.example.llajtacomida.views.mainViews;

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
import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.UserInterface;
import com.example.llajtacomida.models.user.User;
import com.example.llajtacomida.presenters.main.MainNavigation;
import com.example.llajtacomida.presenters.user.UserPresenter;
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

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, UserInterface.ViewUser {

    private AppBarConfiguration mAppBarConfiguration;
    // Loginnsilencioso
    private GoogleApiClient googleApiClient;
    private Hashtable<String, String> userDataList = new Hashtable<String, String>();
    private ProgressDialog progressDialog;
    private UserPresenter userPresenter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_plates, R.id.nav_restaurants,
                R.id.nav_favorit_plates, R.id.nav_favorit_restaurants,
                R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Login silencioso
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        progressDialog = new ProgressDialog(this);

        // init presenter

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
        Toast.makeText(getBaseContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
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
            saveUser(account);
            NavigationView navigationView =  findViewById(R.id.nav_view);
            View header =  navigationView.getHeaderView(0);
            ImageView ivAvatar = header.findViewById(R.id.ivAvatar);
            TextView tvName = header.findViewById(R.id.tvName);
            TextView tvEmail = header.findViewById(R.id.tvEmail);

            if(account.getPhotoUrl() != null){
                Glide.with(this).load(account.getPhotoUrl()).into(ivAvatar);
            }

            userDataList.put("name", account.getGivenName());
            userDataList.put("family_name", account.getFamilyName());
            userDataList.put("email", account.getEmail());
            userDataList.put("phone", "");
            userDataList.put("state", "conectado");

            tvName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut(userDataList);
                }
            });

        }else{
            getPregressDialog("USUARIO5", "Cerrando...");
            MainNavigation.showLogin(MainActivity.this);
            progressDialog.dismiss();
        }
    }

    private void saveUser(GoogleSignInAccount account){
        try {
            user = new User(
                    FirebaseAuth.getInstance().getUid(),
                    account.getDisplayName(), account.getEmail(),
                    account.getPhotoUrl().toString(), "user");
            userPresenter = new UserPresenter(this);
//            userPresenter.findUser(user.getId());
            userPresenter.findUser(FirebaseAuth.getInstance().getUid());
//            UserPresenter userPresenter = new UserPresenter(this);
//            userPresenter.storeUser(user);

        }catch(Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * Este método sirve para cerrar sesión
     */
    private void logOut(Hashtable<String, String> user){
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("INFORMACIÓN DEL USUARIO");
        confirmDialog.setMessage(
                            "Nombre: " + user.get("name")
                        + "\nApellidos: " + user.get("family_name")
                        + "\nEmail: " + user.get("email")
                        + "\nEstado: " + user.get("state"));
        confirmDialog.setCancelable(false);
        confirmDialog.setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        getPregressDialog("USUARIO", "Cerrando...");
                        MainNavigation.showLogin(MainActivity.this);
                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(MainActivity.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });
        confirmDialog.setNegativeButton("Vale", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
//               Toast.makeText(MainActivity.this, "Cancelaste", Toast.LENGTH_SHORT).show();
            }
        });
        confirmDialog.show();
    }

    private void getPregressDialog(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void showUser(User user) {
        // Cargar el usuario
        if(user != null){
            if(user.getRole() != null && !user.getRole().equals("")){
                this.user.setRole(user.getRole()); // Mantiene el rol, y no vuelve a darle uno por defecto
            }
        }
        userPresenter.stopRealtimeDatabase(); // Es necesario parar para hacer el uso del storUser (De otro modo falla)
        userPresenter.storeUser(this.user);
    }
}