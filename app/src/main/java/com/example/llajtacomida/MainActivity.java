package com.example.llajtacomida;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.presenters.mainPresenter.MainPresenter;
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

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private AppBarConfiguration mAppBarConfiguration;
    // Loginnsilencioso
    private GoogleApiClient googleApiClient;
    private Hashtable<String, String> user = new Hashtable<String, String>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        progressDialog = new ProgressDialog(this);
        
    } // End onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); // main es el menu de menu/main.xml

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(MainActivity.this, "Icono de menu", Toast.LENGTH_SHORT).show();
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

    // Para guardar ala sesion
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
            NavigationView navigationView =  findViewById(R.id.nav_view);
            View header =  navigationView.getHeaderView(0);
            ImageView imageView = header.findViewById(R.id.iVAvatar);
            TextView tvName = header.findViewById(R.id.tvName);
            TextView tvEmail = header.findViewById(R.id.tvEmail);

            if(account.getPhotoUrl() != null){
                Glide.with(this).load(account.getPhotoUrl()).into(imageView);
            }

            user.put("name", account.getGivenName());
            user.put("family_name", account.getFamilyName());
            user.put("email", account.getEmail());
            user.put("id", account.getId());
            user.put("phone", "");
            user.put("estado", "Conectado");

            tvName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut(user);
                }
            });

        }else{
            getPregressDialog("USUARIO5", "Cerrando...");
            MainPresenter.showLogin(MainActivity.this);
            progressDialog.dismiss();
        }
    }

    /**
     * Este método sirve para cerrar sesión
     */
    private void logOut(Hashtable<String, String> user){

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("INFORMACIÓN DEL USUARIO");
//        confirmDialog.setMessage("¿Estás seguro de cerrar sesión?");
        confirmDialog.setMessage(
                "Nombre: " + user.get("name")
                        + "\nApellidos: " + user.get("family_name")
                        + "\nEmail: " + user.get("email")
                        + "\nID: " + user.get("id")
                        + "\nEstado: " + user.get("estado"));
        confirmDialog.setCancelable(false);
        confirmDialog.setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            getPregressDialog("USUARIO", "Cerrando...");
                            MainPresenter.showLogin(MainActivity.this);
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
}