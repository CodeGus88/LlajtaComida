package com.example.llajtacomida.views.platesViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Image;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.galeryPresenter.GaleryDatabase;
import com.example.llajtacomida.presenters.platesPresenter.PlateDatabase;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.ArrayList;

public class PlateViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ZoomInImageView ivPhoto;
    private TextView tvName, tvIngredients, tvOrigin;

    //Base de datos
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    public static String id;
    private MenuItem iconEdit, iconDelete, iconGalery;

    private boolean isAnAdministrator;
    private Plate plate;

    // bootones
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;


    ArrayList<Image> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_view);

        // edit title toolbar
        getSupportActionBar().setTitle(R.string.platesTitle);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        //Configiración del boton atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagesList = new ArrayList<Image>();
        isAnAdministrator = true;

        // Para el tamaño de las imagenes
        Display display = getWindowManager().getDefaultDisplay();
        height = (int) (ScreenSize.getWidth(display)*0.6666667);
        width = ScreenSize.getWidth(display);


        initDatabase();
        id = getIntent().getStringExtra("id");
        showPlate(); // Inicializa plate

        initComponents();

        loadImages();
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

        ivPhoto.getLayoutParams().height = (int) (height*0.89); //por el espacio para los botones next previous
        ivPhoto.getLayoutParams().width = width; //(int) (width*0.89);//width;

        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);

        // Cuando es un Fracment no se puede asociar onClick desde el código xml, es necesario este tipo de solución
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void initDatabase(){
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

    /**
     * Carga la presentacion de las imágenes
     */
    private void loadImages(){
        databaseReference.child("App").child("plates").child(id).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewFlipper = (ViewFlipper) findViewById(R.id.vfCarrucel);

                // Limpia toda la lista para que se actualice en tiempo real
                if(viewFlipper != null){
                    viewFlipper.clearAnimation();
                    viewFlipper.clearAnimation();
                    for(int i = 1; i < viewFlipper.getChildCount(); i ++){
                        viewFlipper.removeViewAt(i);
                    }
                    viewFlipper.stopFlipping();
                }
                for (DataSnapshot photo:snapshot.getChildren()) {
                    try {
                        Image image = photo.getValue(Image.class);
                        ImageView ivImg = new ImageView(PlateViewActivity.this);
                        Glide.with(PlateViewActivity.this).load(image.getUrl()).into(ivImg);
                        ivImg.setLayoutParams( // Tamaño de la imagen
                                new ViewGroup.LayoutParams((int) (width*0.89),(int) (height*0.89))
                        );
                        CardView cv = new CardView(PlateViewActivity.this);
                        cv.addView(ivImg);
                        cv.setRadius(35);
                        cv.setLayoutParams( // Tamaño de la imagen
                                new ViewGroup.LayoutParams((int) (width*0.89), (int) (height*0.89))
                        );
                        viewFlipper.addView(cv);
                        initAnimation();
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Algo salió mal");
            }
        });
    }

    private void initAnimation(){
            viewFlipper.setDisplayedChild(0);
            viewFlipper.setFlipInterval(5000);
            viewFlipper.startFlipping();
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
                PlatePresenter.showEditPlateView(this, plate);
                break;
            case R.id.iconDelete:
                delete();
                break;
            case R.id.iconGalery:
                PlatePresenter.showGalery(this, plate.getId(),  plate.getName());
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
                PlateDatabase platesDataBase = new PlateDatabase(PlateViewActivity.this, plate);
                // Antes de eliminar el plato, debemos eliminar todos sus archivos
                GaleryDatabase galeryDatabase = new GaleryDatabase(PlateViewActivity.this, "plates", plate.getId());
                galeryDatabase.deleteAllData(); // es un metodo estatico
                platesDataBase.delete();
                onBackPressed();
            }
        });
        confirm.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
//                Toast.makeText(PlateViewActivity.this, "Cancelar...", Toast.LENGTH_SHORT).show();
            }
        });
        confirm.show();
    }

        private void initIconMenu(Menu menu){
            if(isAnAdministrator){
                iconEdit = (MenuItem) menu.findItem(R.id.iconEdit);
                iconDelete = (MenuItem) menu.findItem(R.id.iconDelete);
                iconGalery = (MenuItem) menu.findItem(R.id.iconGalery);
                iconEdit.setVisible(true);
                iconDelete.setVisible(true);
                iconGalery.setVisible(true);
            }
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext:
                viewFlipper.setInAnimation(this, android.R.anim.slide_in_left); // slide_in_left agregado manualmente creando anim/slide_in_left.xml en res
                viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right); //slide_out_right agregado manualmente creando anim/slide_out_right.xml res
                viewFlipper.setFlipInterval(6000);//Para reiniciar tiempo
                viewFlipper.showNext();
                break;
            case R.id.btnPrevious:
                viewFlipper.setInAnimation(this, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                viewFlipper.setFlipInterval(6000); // Para reiniciar tiempo
                viewFlipper.showPrevious();
                break;
            default:
                Toast.makeText(this, "Opción inválida", Toast.LENGTH_SHORT).show();
        }
    }
}