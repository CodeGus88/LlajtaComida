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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.ImageInterface;
import com.example.llajtacomida.interfaces.PlateInterface;
import com.example.llajtacomida.models.image.Image;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.presenters.image.GaleryDatabase;
import com.example.llajtacomida.presenters.image.ImagePresenter;
import com.example.llajtacomida.models.plate.PlateGestorDB;
import com.example.llajtacomida.presenters.plate.PlateNavegation;
import com.example.llajtacomida.presenters.plate.PlatePresenter;
import com.example.llajtacomida.presenters.plate.PlateRestListPresenter;
import com.example.llajtacomida.presenters.restaurant.ArrayAdapterRestaurant;
import com.example.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.example.llajtacomida.views.rating.RatingRecordFragment;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.ArrayList;

public class PlateViewActivity extends AppCompatActivity
    implements View.OnClickListener, PlateInterface.ViewPlate, ImageInterface.ViewImage, PlateInterface.ViewRestlist {

    private ZoomInImageView ivPhoto;
    private TextView tvName, tvIngredients, tvOrigin;

    public static String id;
    private MenuItem iconEdit, iconDelete, iconGalery;

    private boolean isAnAdministrator;
    private Plate plate;

    private static final int TIME_ANIMATION = 2000;

    // bootones
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;

    // Componentes to restaurant list
    private ArrayAdapterRestaurant arrayAdapterRestaurant;
    private ListView lvRestaurants;
    private ArrayList<Restaurant> restaurantList;

    //  Presentadores del plato y de la lista de restaurantes
    private PlateInterface.presenterRestList restListPresenter;
    private PlatePresenter platePresenter;
    private ImagePresenter imagePresenter;

    // Fragmento de rating
    private RatingRecordFragment ratingRecordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_view);
        // edit title toolbar
        getSupportActionBar().setTitle(R.string.platesTitle);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        //Configiración del boton atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantList = new ArrayList<Restaurant>();
        isAnAdministrator = true;

        // Para el tamaño de las imagenes
        Display display = getWindowManager().getDefaultDisplay();
        height = (int) (ScreenSize.getWidth(display)*0.6666667);
        width = ScreenSize.getWidth(display);

        id = getIntent().getStringExtra("id");

        initComponents();
        initPresenters();

        // Agregar fragmento fragment
        initRatingFragment();
    }

    private void initRatingFragment() {
        ratingRecordFragment = new RatingRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("objectId", id);
        bundle.putString("nodeCollectionName", "plates");
        ratingRecordFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flRating, ratingRecordFragment).commit(); // muestra el fragmento rating en la actividad
    }

    private void initPresenters(){
        // iniciando presentadores
        restListPresenter = new PlateRestListPresenter(this);
        restListPresenter.filterRestaurantListWithPlate(id);
        platePresenter = new PlatePresenter(this);
        platePresenter.searchPlate(id);
        imagePresenter = new ImagePresenter(this);
        final String NODE_COLLECTION_NAME = "plates";
        imagePresenter.searchImages(NODE_COLLECTION_NAME, id);
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        stopRealtimeDatabse();
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

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        viewFlipper = (ViewFlipper) findViewById(R.id.vfCarrucel);
        // lista de restaruantes
        lvRestaurants = (ListView) findViewById(R.id.lvRestaurantList);
        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestaurantNavegation.showRestaurantView(PlateViewActivity.this, restaurantList.get(position).getId());
            }
        });
    }

    private void initAnimation(){
        viewFlipper.setDisplayedChild(0);
        viewFlipper.setFlipInterval(TIME_ANIMATION);
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
                PlateNavegation.showEditPlateView(this, plate);
                break;
            case R.id.iconDelete:
                delete();
                break;
            case R.id.iconGalery:
                PlateNavegation.showGalery(this, id,  plate.getName());
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
                PlateGestorDB platesDataBase = new PlateGestorDB(PlateViewActivity.this, plate);
                // Antes de eliminar el plato, debemos eliminar todos sus archivos
                GaleryDatabase galeryDatabase = new GaleryDatabase(PlateViewActivity.this, "plates", plate.getId());
                galeryDatabase.deleteAllData(); // es un metodo estatico
                platesDataBase.delete();
                stopRealtimeDatabse();
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
                viewFlipper.setFlipInterval(TIME_ANIMATION);//Para reiniciar tiempo
                viewFlipper.showNext();
                break;
            case R.id.btnPrevious:
                viewFlipper.setInAnimation(this, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                viewFlipper.setFlipInterval(TIME_ANIMATION); // Para reiniciar tiempo
                viewFlipper.showPrevious();
                break;
            default:
                Toast.makeText(this, "Opción inválida", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interface para llenar la lista de resturantes con el plato
     * @param list
     */
    @Override
    public void showRestList(ArrayList<Restaurant> list) {
        try {
            restaurantList = list;
            arrayAdapterRestaurant = new ArrayAdapterRestaurant(this, R.layout.adapter_element_list, list);
            lvRestaurants.setAdapter(arrayAdapterRestaurant);
            ScreenSize.setListViewHeightBasedOnChildren(lvRestaurants);
        }catch (Exception e){
            Toast.makeText(this, "Error:", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getMessage());
        }

    }

    /**
     * Interface para mostrar el plato
     * @param plate
     */
    @Override
    public void showPlate(Plate plate) {
        try{ // Por si la actividad no está visible
            this.plate = plate;
            tvName.setText(plate.getName());
            tvIngredients.setText(plate.getIngredients());
            tvOrigin.setText(plate.getOrigin());
            Glide.with(PlateViewActivity.this).load(plate.getUrl()).into(ivPhoto);
        }catch(Exception e){
            Log.e("Error: " , e.getMessage());
        }
    }

    @Override
    public void showPlateList(ArrayList<Plate> plateList) {
        // no se usa para esta vista
    }

    /**
     * Interaface para mostrar las imágenes de de la galería del plato
     * @param imagesList
     */
    @Override
    public void showImages(ArrayList<Image> imagesList) {
        try { // Por si la actividad no está visible
            if(viewFlipper != null){
                viewFlipper.clearAnimation();
                viewFlipper.clearAnimation();
                for(int i = 1; i < viewFlipper.getChildCount(); i ++){
                    viewFlipper.removeViewAt(i);
                }
                viewFlipper.stopFlipping();
            }
            for (Image image:imagesList) {
                try {
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
                    Log.e("Error:", e.getMessage() );
                }
            }

        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    private void stopRealtimeDatabse(){
        platePresenter.stopRealtimeDatabase();
        restListPresenter.stopRealtimeDatabase();
        imagePresenter.stopRealtimeDatabse();
    }
}