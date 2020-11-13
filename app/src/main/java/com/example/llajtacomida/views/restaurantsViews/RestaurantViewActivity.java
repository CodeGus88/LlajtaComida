package com.example.llajtacomida.views.restaurantsViews;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Image;
import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.presenters.galeryPresenter.GaleryDatabase;
import com.example.llajtacomida.presenters.mapsPresenter.MapPresenter;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantDatabase;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantPresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.ArrayList;

public class RestaurantViewActivity extends AppCompatActivity implements View.OnClickListener {

    //database
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    public static String id;
    //iconos
    private MenuItem iconEdit, iconDelete, iconGalery;

    private boolean isAnAdministrator;
    private Restaurant restaurant;

    // components
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnMenuEdit;
    private Button btnVisit;
    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;
    private ZoomInImageView ivPhoto;
    private TextView tvName, tvOwnerName, tvPhone, tvAddress, tvOriginAndDescription;


    ArrayList<Image> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurantsTitle);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imagesList = new ArrayList<Image>();
        isAnAdministrator = true;

        // Para el tamaño de las imagenes
        Display display = getWindowManager().getDefaultDisplay();
        height = (int) (ScreenSize.getWidth(display)*0.6666667);
        width = ScreenSize.getWidth(display);


        initDatabase();
        id = getIntent().getStringExtra("id");
        showRestaurant();
        initComponents();
        loadImages();

    }



    //------------------------------>


    /**
     * Carga la presentacion de las imágenes
     */
    private void loadImages(){
        databaseReference.child("App").child("restaurants").child(id).child("images").addValueEventListener(new ValueEventListener() {
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
                        ImageView ivImg = new ImageView(RestaurantViewActivity.this);
                        Glide.with(RestaurantViewActivity.this).load(image.getUrl()).into(ivImg);
                        ivImg.setLayoutParams( // Tamaño de la imagen
                                new ViewGroup.LayoutParams((int) (width*0.89),(int) (height*0.89))
                        );
                        CardView cv = new CardView(RestaurantViewActivity.this);
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

    private void initDatabase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void showRestaurant(){
        databaseReference.child("App").child("restaurants").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurant = snapshot.getValue(Restaurant.class);
                try{
                    tvName.setText(restaurant.getName());
                    tvOwnerName.setText(restaurant.getOwnerName());
                    tvPhone.setText(restaurant.getPhone());
                    tvAddress.setText(restaurant.getAddress());
                    tvOriginAndDescription.setText(restaurant.getOriginAndDescription());
                    Glide.with(RestaurantViewActivity.this).load(restaurant.getUrl()).into(ivPhoto);
                }catch(Exception e){
                    Log.e("Error: " , e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RestaurantViewActivity.this, "Ocurrión un error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initComponents(){
        ivPhoto = (ZoomInImageView) findViewById(R.id.ziivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvOwnerName = (TextView) findViewById(R.id.tvOwner);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvOriginAndDescription = (TextView) findViewById(R.id.tvOriginAndDescription);
        ivPhoto.getLayoutParams().height = (int) (height*0.89); //por el espacio para los botones next previous
        ivPhoto.getLayoutParams().width = width; //(int) (width*0.89);//width;
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnMenuEdit = (ImageButton) findViewById(R.id.btnMenuEdit);
        btnVisit = (Button) findViewById(R.id.btnVisit);
        // Cuando es un Fracment no se puede asociar onClick desde el código xml, es necesario este tipo de solución
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnMenuEdit.setOnClickListener(this);
        btnVisit.setOnClickListener(this);
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

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // accion del boton atras del sistema operativo
        return false;
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
            case R.id.btnMenuEdit:
                RestaurantPresenter.showMenu(this, restaurant);
                break;
            case R.id.btnVisit:
                MapPresenter.showSetLocationMapActivity(this, restaurant);
                break;
            default:
                Toast.makeText(this, "Opción inválida", Toast.LENGTH_SHORT).show();
        }
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
                RestaurantPresenter.showEditRestaurantView(this, restaurant);
                break;
            case R.id.iconDelete:
                delete();
                break;
            case R.id.iconGalery:
                RestaurantPresenter.showGalery(this, restaurant.getId(),  restaurant.getName());
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
                RestaurantDatabase restaurantDataBase = new RestaurantDatabase(RestaurantViewActivity.this, restaurant);
                // Antes de eliminar el plato, debemos eliminar todos sus archivos
                GaleryDatabase galeryDatabase = new GaleryDatabase(RestaurantViewActivity.this, "restaurants", restaurant.getId());
                galeryDatabase.deleteAllData(); // es un metodo estatico
                restaurantDataBase.delete();
                onBackPressed();
            }
        });
        confirm.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //message to cancel
            }
        });
        confirm.show();
    }
}