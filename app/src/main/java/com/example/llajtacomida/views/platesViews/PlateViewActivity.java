package com.example.llajtacomida.views.platesViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.platesPresenter.PlatesDataBase;
import com.example.llajtacomida.presenters.platesPresenter.PlatesPresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageView;

public class PlateViewActivity extends AppCompatActivity {

    private ZoomInImageView ivPhoto;
    private TextView tvName, tvIngredients, tvOrigin;

    //Base de datos
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private static String id;
    private MenuItem iconEdit, iconDelete;

    private boolean isAnAdministrator;
    private Plate plate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_view);

        //Configiración del boton atrás
        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        inicializarDataBase();
        id = getIntent().getStringExtra("id");
        showPlate();

        isAnAdministrator = true;
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    private void initComponents(){
        ivPhoto = (ZoomInImageView) findViewById(R.id.ziivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvIngredients = (TextView) findViewById(R.id.tvIngredients);
        tvOrigin = (TextView) findViewById(R.id.tvOrigin);

        Display display = getWindowManager().getDefaultDisplay();
        ivPhoto.getLayoutParams().height = (int) (ScreenSize.getWidth(display)*0.6666667);
        ivPhoto.getLayoutParams().width = ScreenSize.getWidth(display);
    }

    private void inicializarDataBase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void showPlate(){
        databaseReference.child("App").child("plates").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                plate = snapshot.getValue(Plate.class);
                try{
                    tvName.setText(plate.getName());
                    tvIngredients.setText(plate.getIngredients());
                    tvOrigin.setText(plate.getOrigin());
                    Glide.with(PlateViewActivity.this).load(plate.getUrl()).into(ivPhoto);
                }catch(Exception e){
                    Log.e("Error: " , e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlateViewActivity.this, "Ocurrión un error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIconMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.iconEdit:
                PlatesPresenter.showEditPlateView(this, plate);
                break;
            case R.id.iconDelete:
                delete();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle("Confimación");
        confirm.setMessage("¿Estás seguro de continuar? \n Se elemininará permanentemente");
        confirm.setCancelable(false);
        confirm.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                onBackPressed();
                PlatesDataBase platesDataBase = new PlatesDataBase(PlateViewActivity.this, plate);
                platesDataBase.delete();
//                Toast.makeText(PlateViewActivity.this, "" + plate.getId(), Toast.LENGTH_SHORT).show();
                if(platesDataBase.isSuccess()){
                    Toast.makeText(PlateViewActivity.this, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });

        confirm.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(PlateViewActivity.this, "Cancelar...", Toast.LENGTH_SHORT).show();
            }
        });
        confirm.show();
    }


    private void initIconMenu(Menu menu){
        if(isAnAdministrator){
            iconEdit = (MenuItem) menu.findItem(R.id.iconEdit);
            iconDelete = (MenuItem) menu.findItem(R.id.iconDelete);
            iconEdit.setVisible(true);
            iconDelete.setVisible(true);
        }
    }
}