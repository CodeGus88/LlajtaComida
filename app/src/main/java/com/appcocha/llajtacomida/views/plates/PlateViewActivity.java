 package com.appcocha.llajtacomida.views.plates;

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
import com.appcocha.llajtacomida.presenters.map.MapNavegation;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.ImageInterface;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.models.image.Image;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.image.GaleryDatabase;
import com.appcocha.llajtacomida.presenters.image.ImagePresenter;
import com.appcocha.llajtacomida.presenters.plate.PlateManagerPresenter;
import com.appcocha.llajtacomida.presenters.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenters.plate.PlatePresenter;
import com.appcocha.llajtacomida.presenters.plate.PlateRestListPresenter;
import com.appcocha.llajtacomida.presenters.restaurant.ArrayAdapterRestaurant;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenters.tools.ScreenSize;
import com.appcocha.llajtacomida.presenters.user.AuthUser;
import com.appcocha.llajtacomida.views.favorites.FavoriteObjectFragment;
import com.appcocha.llajtacomida.views.rating.RatingRecordFragment;
import com.zolad.zoominimageview.ZoomInImageView;
import java.text.DecimalFormat;
import java.util.ArrayList;

 /**
  * Esta clase es la vista de la sección ver plato
  */
 public class PlateViewActivity extends AppCompatActivity
    implements View.OnClickListener, PlateInterface.ViewPlate, ImageInterface.ViewImage,
        PlateInterface.ViewRestlist, PlateInterface.ViewPlateManager {

    private ZoomInImageView ivPhoto;
    private TextView tvName, tvIngredients, tvOrigin, tvTitleRestaurants, tvRestaurantsFound;


    public static String id;
    private MenuItem iconEdit, iconDelete, iconGalery;

    private Plate plate;

    private static final int TIME_ANIMATION = 2000;

    // bootones
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;

    // Components to restaurant list
    private ArrayAdapterRestaurant arrayAdapterRestaurant;
    private ListView lvRestaurants;
    private ArrayList<Restaurant> restaurantList;
    private TextView tvRating;
    private ImageButton btnMarkersView;
    // button of favorite
//    private ImageButton btnFavorite;

    //  Presentadores del plato y de la lista de restaurantes
    private PlateInterface.presenterRestList restListPresenter;
    private PlatePresenter platePresenter;
    private ImagePresenter imagePresenter;
    private PlateManagerPresenter plateManagerPresenter;

    // Fragments
    private RatingRecordFragment ratingRecordFragment;
    private FavoriteObjectFragment favoriteObjectFragment;

    // Toast personalizado (pause resume)
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_view);
        // edit title toolbar
        getSupportActionBar().setTitle(R.string.plates_title);
        // getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());

        //Configiración del boton atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantList = new ArrayList<Restaurant>();

        // Para el tamaño de las imagenes
        Display display = getWindowManager().getDefaultDisplay();
        height = (int) (ScreenSize.getWidth(display)*0.6666667);
        width = ScreenSize.getWidth(display);

        id = getIntent().getStringExtra("id");

        initComponents();
        initPresenters();

        // Agregar fragmento fragment
        initFragments();
    }

    private void initFragments() {
        // Inicializamos rating
        Bundle bundle = new Bundle();
        bundle.putString("objectId", id);
        bundle.putString("nodeCollectionName", "plates");
        ratingRecordFragment = new RatingRecordFragment();
        ratingRecordFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flRating, ratingRecordFragment).commit(); // muestra el fragmento rating en la actividad
        // Inicializamos favorite
        favoriteObjectFragment = new FavoriteObjectFragment();
        favoriteObjectFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flFavorite, favoriteObjectFragment).commit(); // Mmuestra el fragmento favorite
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
        plateManagerPresenter = new PlateManagerPresenter(this);
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
        tvRating = (TextView) findViewById(R.id.tvRating);
        btnMarkersView = (ImageButton) findViewById(R.id.btnMarkersView);

        tvTitleRestaurants = (TextView) findViewById(R.id.tvTitleRestaurants);
        tvRestaurantsFound = (TextView) findViewById(R.id.tvRestaurantsFound);

        ivPhoto.getLayoutParams().height = (int) (height*0.984); //por el espacio para los botones next previous
        ivPhoto.getLayoutParams().width = (int) (width*0.984);

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
        btnMarkersView.setOnClickListener(this);
        tvName.setOnClickListener(this);

        toast = new Toast(this);

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
        confirm.setTitle(getString(R.string.confirm_title));
        confirm.setMessage(getString(R.string.message_delete_question));
        confirm.setCancelable(false);
        confirm.setPositiveButton(getString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(PlateViewActivity.this, getString(R.string.message_deleting), Toast.LENGTH_SHORT).show();
                // Antes de eliminar el plato, debemos eliminar todos sus archivos
                GaleryDatabase galeryDatabase = new GaleryDatabase(PlateViewActivity.this, "plates", plate.getId());
                galeryDatabase.deleteAllData(); // es un metodo estatico
                plateManagerPresenter.delete(plate.getId());
                stopRealtimeDatabse();
                onBackPressed();
            }
        });
        confirm.setNegativeButton(getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
//                Toast.makeText(PlateViewActivity.this, "Cancelar...", Toast.LENGTH_SHORT).show();
            }
        });
        confirm.show();
    }

    private void initIconMenu(Menu menu){
        iconEdit = (MenuItem) menu.findItem(R.id.iconEdit);
        iconDelete = (MenuItem) menu.findItem(R.id.iconDelete);
        iconGalery = (MenuItem) menu.findItem(R.id.iconGalery);

        if(AuthUser.getUser().getRole().equalsIgnoreCase("admin")){
            iconGalery.setVisible(true);
            iconDelete.setVisible(true);
            iconEdit.setVisible(true);
        }else{
            iconGalery.setVisible(false);
            iconDelete.setVisible(false);
            iconEdit.setVisible(false);
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
            case R.id.btnMarkersView:
                if(restaurantList.size() > 0) MapNavegation.showSetAllLocationMapActivity(this, plate,restaurantList);
                else Toast.makeText(this, getString(R.string.message_not_found_restaurants), Toast.LENGTH_LONG).show();
                break;
            case R.id.tvName:
                pauseResume();
                break;
            default:
                Toast.makeText(this, getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseResume(){
        if(viewFlipper.isFlipping()){
            viewFlipper.stopFlipping();
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.mipmap.pause);
                toast.setView(imageView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
        }else{
            if(viewFlipper.getChildCount() > 1) {
                viewFlipper.startFlipping();
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.mipmap.play);
                    toast.setView(imageView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
            }else{
                Toast.makeText(this, getString(R.string.message__not_contain_images), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Interface para llenar la lista de resturantes con el plato
     * @param list
     */
    @Override
    public void showRestList(ArrayList<Restaurant> list) {
        try {
            restaurantList.clear();
            restaurantList = list;
            arrayAdapterRestaurant = new ArrayAdapterRestaurant(this, R.layout.adapter_element_list, list);
            lvRestaurants.setAdapter(arrayAdapterRestaurant);
            ScreenSize.setListViewHeightBasedOnChildren(lvRestaurants);
            tvRestaurantsFound.setText("("+restaurantList.size() + " " +getString(R.string.tv_restaurants_found)+")");
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
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            tvRating.setText(String.valueOf(decimalFormat.format(plate.getPunctuation())));
            Glide.with(PlateViewActivity.this).load(plate.getUrl()).into(ivPhoto);
            getSupportActionBar().setSubtitle(plate.getName()); // subtitulo del toolbar
            tvTitleRestaurants.setText(getString(R.string.tv_rest_with_plate_title) + " \"" + plate.getName() +"\"");
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
//                            new ViewGroup.LayoutParams((int) (width*0.89),(int) (height*0.89))
                            new ViewGroup.LayoutParams((int) (width*0.984),(int) (height*0.984))
                    );
                    CardView cv = new CardView(PlateViewActivity.this);
                    cv.addView(ivImg);
                    cv.setRadius(35);
                    cv.setLayoutParams( // Tamaño de la imagen
//                            new ViewGroup.LayoutParams((int) (width*0.89), (int) (height*0.89))
                            new ViewGroup.LayoutParams((int) (width*0.984), (int) (height*0.984))
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
        imagePresenter.stopRealtimeDatabase();
        ratingRecordFragment.stopRealtimeDatabase();
        favoriteObjectFragment.stopRealtimeDatabase();
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getString(R.string.message_delete_complete), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.message_delete_incomplete), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void report(ArrayList<Integer> errors) {
        // no se utiliza para este caso
    }
}