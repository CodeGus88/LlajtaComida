 package com.appcocha.llajtacomida.view.plates;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.map.MapNavegation;
import com.appcocha.llajtacomida.presenter.plate.ArrayAdapterRestaurantPlatePrice;
import com.appcocha.llajtacomida.presenter.tools.Serializer;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.appcocha.llajtacomida.presenter.tools.Validation;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.interfaces.ImageInterface;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.image.Image;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.model.image.GaleryDatabase;
import com.appcocha.llajtacomida.presenter.image.ImagePresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateManagerPresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.plate.PlatePresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateRestListPresenter;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenter.tools.ScreenSize;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.view.favorites.FavoriteObjectFragment;
import com.appcocha.llajtacomida.view.rating.RatingRecordFragment;
import com.zolad.zoominimageview.ZoomInImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

 /**
  * Vista, Esta clase es la vista de la sección ver plato
  */
 public class PlateViewActivity extends AppCompatActivity
    implements View.OnClickListener, PlateInterface.ViewPlate, ImageInterface.ViewImage,
        PlateInterface.ViewRestlist, PlateInterface.ViewPlateManager {

    private ZoomInImageView ivPhoto;
    private TextView tvName, tvIngredients, tvOrigin, tvTitleRestaurants, tvRestaurantsFound, tvReadMoreLessIngredients, tvReadMoreLessOriginDescription;
    private LinearLayout llIngredients, llOriginDescription;

     private boolean sharePresed = false; // Bandera para saber cuando se abrió el diálogo para compartir (evita que reanude los presentadores en onResume)
     public static String id;
     private MenuItem iconEdit, iconDelete, iconGalery, iconShare;

    private Plate plate;

    private static final int TIME_ANIMATION = Integer.parseInt(StringValues.getPresentationTime());
    private final int MAX_LINES = 3;
     private final String IMAGES_ANIMATION_FILE = "IMAGES_ANIMATION_FILE";

    // bootones
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;

    // Components to restaurant list
    private ArrayAdapterRestaurantPlatePrice arrayAdapterRestaurantPlatePrice;
    private ListView lvRestaurants;
    private ArrayList<Restaurant> restaurantList;
    private Hashtable priceInRestaurantsList;
    private TextView tvRating;
    private ImageButton btnMarkersViewIcon, btnMarquersViewMap;

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

        if(getIntent().hasExtra("id")) id = getIntent().getStringExtra("id");

        initComponents();
//        initPresenters();

        // Agregar fragmento fragment
        initFragments();
//        if(!Permissions.getAuthorize(AuthUser.user.getRole(), Permissions.SHOW_PLATE)){
        if(!Permission.getAuthorize(AuthUser.getUser(this).getRole(), Permission.SHOW_PLATE)){
            Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

     /**
      * Inicialza los fragmentos
      */
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

     /**
      * Inicilaiza los presentadores
      */
    private void initPresenters(){
        // iniciando presentadores
        platePresenter = new PlatePresenter(this);
        platePresenter.searchPlate(id);
        imagePresenter = new ImagePresenter(this);
        final String NODE_COLLECTION_NAME = "plates";
        imagePresenter.searchImages(NODE_COLLECTION_NAME, id);
        plateManagerPresenter = new PlateManagerPresenter(this);

        restListPresenter = new PlateRestListPresenter(this);
        restListPresenter.filterRestaurantListWithPlate(id);
    }

    /**
     * Captura de acción del botón atrás <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp(){
//        stopRealtimeDatabse();
        onBackPressed(); // acción del boton atrás del sistema operativo
        Animatoo.animateFade(this); //Animación al cambiar de actividad
        return false;
    }

     /**
      * Inicializa los componentes de la vista
      */
    private void initComponents(){
        ivPhoto = (ZoomInImageView) findViewById(R.id.ziivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvIngredients = (TextView) findViewById(R.id.tvIngredients);
        tvOrigin = (TextView) findViewById(R.id.tvOrigin);
        tvRating = (TextView) findViewById(R.id.tvRating);
        btnMarkersViewIcon = (ImageButton) findViewById(R.id.btnMarkersViewAllIcon);
        btnMarquersViewMap = (ImageButton) findViewById(R.id.btnMarkersViewAllMap);

        // Leer más...
        tvReadMoreLessIngredients = (TextView) findViewById(R.id.tvReadMoreLessIngredients);
        tvReadMoreLessOriginDescription = (TextView) findViewById(R.id.tvReadMoreLessOriginDescription);
        llIngredients = (LinearLayout) findViewById(R.id.llIngredients);
        llOriginDescription = (LinearLayout) findViewById(R.id.llOriginDescription);

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
                Sound.playClick();
                RestaurantNavegation.showRestaurantView(PlateViewActivity.this, restaurantList.get(position).getId());
            }
        });
        btnMarkersViewIcon.setOnClickListener(this);
        btnMarquersViewMap.setOnClickListener(this);
        tvName.setOnClickListener(this);

        toast = new Toast(this);

        tvIngredients.setMaxLines(MAX_LINES);
        tvOrigin.setMaxLines(MAX_LINES);
        llIngredients.setOnClickListener(this);
        llOriginDescription.setOnClickListener(this);
    }

     /**
      * Iniciliza la presentación de las imágenes
      */
    private void initAnimation(int item){
        if(Serializer.readBooleanData(this, IMAGES_ANIMATION_FILE) && viewFlipper.getChildCount()>1 && !viewFlipper.isFlipping()){
            viewFlipper.setDisplayedChild(item);
            viewFlipper.setFlipInterval(TIME_ANIMATION);
            viewFlipper.startFlipping();
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
        Sound.playClick();
        switch (item.getItemId()){
            case R.id.iconEdit:
                if(Permission.getAuthorize(AuthUser.getUser().getRole(), Permission.UPDATE_PLATE))
                    PlateNavegation.showEditPlateView(this, plate);
                break;
            case R.id.iconDelete:
                if(Permission.getAuthorize(AuthUser.getUser().getRole(), Permission.DELETE_PLATE))
                delete();
                break;
            case R.id.iconGalery:
                if(Permission.getAuthorize(AuthUser.getUser().getRole(), Permission.SHOW_PLATE_GALERY))
                PlateNavegation.showGalery(this, id,  plate.getName());
                break;
            case R.id.iconShare:
                PlateNavegation.showShare(this, plate, "", restaurantList,priceInRestaurantsList);
                sharePresed = true;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

     /**
      * Elimina el elemento actual
      */
    private void delete() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle(getString(R.string.confirm_title));
        confirm.setMessage(getString(R.string.message_delete_question));
        confirm.setCancelable(false);
        confirm.setPositiveButton(getString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Sound.playThrow();
                Toast.makeText(PlateViewActivity.this, getString(R.string.message_deleting), Toast.LENGTH_SHORT).show();
                // Antes de eliminar el plato, debemos eliminar todos sus archivos
                GaleryDatabase galeryDatabase = new GaleryDatabase(PlateViewActivity.this, "plates", plate.getId());
                galeryDatabase.deleteAllData(); // es un metodo estatico
                plateManagerPresenter.delete(plate.getId());
//                stopRealtimeDatabse();
                onBackPressed();
            }
        });
        confirm.setNegativeButton(getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Sound.playClick();
            }
        });
        confirm.show();
    }

     /**
      * Inicializa  los íconos de esta sección
      * @param menu
      */
    private void initIconMenu(Menu menu){
        iconEdit = (MenuItem) menu.findItem(R.id.iconEdit);
        iconDelete = (MenuItem) menu.findItem(R.id.iconDelete);
        iconGalery = (MenuItem) menu.findItem(R.id.iconGalery);
        iconShare = (MenuItem) menu.findItem(R.id.iconShare);
        if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.SHOW_PLATE_GALERY))
            iconGalery.setVisible(true);
        else iconGalery.setVisible(false);
        if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.DELETE_PLATE))
            iconDelete.setVisible(true);
        else iconDelete.setVisible(false);
        if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.UPDATE_PLATE))
            iconEdit.setVisible(true);
        else iconEdit.setVisible(false);
        if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.SHOW_PLATE))
            iconShare.setVisible(true);
        else iconShare.setVisible(false);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llIngredients || v.getId() == R.id.llOriginDescription) Sound.playDrop();
        else Sound.playClick();
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
            case R.id.btnMarkersViewAllIcon:
            case R.id.btnMarkersViewAllMap: // Hace lo mismo que btnMarkersViewAllIcon
                if(restaurantList.size() > 0) MapNavegation.showSetAllLocationMapActivity(this, plate,restaurantList);
                else Toast.makeText(this, getString(R.string.message_not_found_restaurants), Toast.LENGTH_LONG).show();
                break;
            case R.id.tvName:
                if(Serializer.readBooleanData(this, IMAGES_ANIMATION_FILE))
                    Serializer.saveBooleanData(this, IMAGES_ANIMATION_FILE, false);
                else Serializer.saveBooleanData(this, IMAGES_ANIMATION_FILE, true);
                pauseResume();
                break;
            case R.id.llIngredients:
                if(tvIngredients.getMaxLines() == MAX_LINES){
                   tvIngredients.setMaxLines(1500);
                   tvReadMoreLessIngredients.setText(getString(R.string.read_less));
                }else{
                    tvIngredients.setMaxLines(MAX_LINES);
                    tvReadMoreLessIngredients.setText(getString(R.string.read_more));
                }
                break;
            case R.id.llOriginDescription:
                if(tvOrigin.getMaxLines() == MAX_LINES){
                    tvOrigin.setMaxLines(1500);
                    tvReadMoreLessOriginDescription.setText(getString(R.string.read_less));
                }else{
                    tvOrigin.setMaxLines(MAX_LINES);
                    tvReadMoreLessOriginDescription.setText(getString(R.string.read_more));
                }
                break;
            default:
                Toast.makeText(this, getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
        }
    }

     /**
      * Pausa, reanuda la reproducción de las imágenes del plato
      */
    private void pauseResume(){
        if(!Serializer.readBooleanData(this, IMAGES_ANIMATION_FILE)){
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
                Toast.makeText(this, getString(R.string.message_not_contain_images), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Interface para llenar la lista de resturantes con el plato
     * @param list
     */
    @Override
    public void showRestList(ArrayList<Restaurant> list, Hashtable priceInRestaurantsList) {
        try {
            restaurantList = list;
            this.priceInRestaurantsList = priceInRestaurantsList;
            arrayAdapterRestaurantPlatePrice = new ArrayAdapterRestaurantPlatePrice(this, R.layout.adapter_restaurant_plate_price_list, list, priceInRestaurantsList, plate.getName());
            lvRestaurants.setAdapter(arrayAdapterRestaurantPlatePrice);
            ScreenSize.setListViewHeightBasedOnChildren(lvRestaurants);
            tvRestaurantsFound.setText("("+restaurantList.size() + " " +getString(R.string.tv_restaurants_found)+")");
        }catch (Exception e){
            Toast.makeText(this, "Error:", Toast.LENGTH_SHORT).show();
            Log.e("Error: ", e.getMessage());
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
//                viewFlipper.clearAnimation();
                while(viewFlipper.getChildCount()>1)
                    viewFlipper.removeViewAt(1);
                viewFlipper.stopFlipping();
            }
            for (Image image:imagesList) {
                try {
                    ImageView ivImg = new ImageView(PlateViewActivity.this);
                    Glide.with(PlateViewActivity.this).load(image.getUrl()).into(ivImg);
                    ivImg.setLayoutParams( // Tamaño de la imagen
                            new ViewGroup.LayoutParams((int) (width*0.984),(int) (height*0.984))
                    );
                    CardView cv = new CardView(PlateViewActivity.this);
                    cv.addView(ivImg);
                    cv.setRadius(35);
                    cv.setLayoutParams( // Tamaño de la imagen
                            new ViewGroup.LayoutParams((int) (width*0.984), (int) (height*0.984))
                    );
                    viewFlipper.addView(cv);
                }catch (Exception e){
                    Log.e("Error:", e.getMessage() );
                }
            }
            // Verifica estado de presentación (activado o desactivado)
            int randomItem = 0;
            if(viewFlipper.getChildCount() > 1){
                Random random = new Random();
                randomItem = random.nextInt((viewFlipper.getChildCount()-1) + 0) + 0;
            }
            initAnimation(randomItem);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

     /**
      * Detiene la BD
      */
    private void stopRealtimeDatabase(){
        platePresenter.stopRealtimeDatabase();
        restListPresenter.stopRealtimeDatabase();
        imagePresenter.stopRealtimeDatabase();
        ratingRecordFragment.stopRealtimeDatabase();
        favoriteObjectFragment.stopRealtimeDatabase();
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        Sound.playSuccess();
        if(isSuccess)Toast.makeText(this, getString(R.string.message_delete_complete), Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, getString(R.string.message_delete_incomplete), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void report(ArrayList<Integer> errors) {
        // no se utiliza para este caso
    }

     @Override
     protected void onSaveInstanceState(@NonNull Bundle outState) {
         super.onSaveInstanceState(outState);
         outState.putString("id", id);
     }

     @Override
     protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
         super.onRestoreInstanceState(savedInstanceState);
         id = savedInstanceState.getString("id");
     }

     @Override
     public void onBackPressed() {
         try {
             super.onBackPressed();
             Animatoo.animateFade(this); //Animación al cambiar de actividad
         }catch (Exception e){
             Log.e("Error: ", e.getMessage());
         }
     }

     // Ciclos de vida para el reinicio de los presentadores
     @Override
     protected void onResume() {
         super.onResume();
         if(!sharePresed || !isInitAllPresenters()) initPresenters();
         else sharePresed = false;
         Log.d("cicleLive", "onResume");
     }

     /**
      * Verifica si todos los presentadores están inicializados
      * @return isAllInit
      */
     private boolean isInitAllPresenters(){
         boolean isAllInit = true;
         isAllInit &= platePresenter != null;
         isAllInit &= imagePresenter != null;
         isAllInit &= plateManagerPresenter != null;
         isAllInit &= restListPresenter != null;
         return isAllInit;
     }

     @Override
     protected void onPause() {
         super.onPause();
         stopRealtimeDatabase();
         Log.d("cicleLive", "onPause");
     }
 }