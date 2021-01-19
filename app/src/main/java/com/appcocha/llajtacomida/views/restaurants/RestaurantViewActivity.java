package com.appcocha.llajtacomida.views.restaurants;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.ImageInterface;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.image.Image;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.restaurant.RestPlateListPresenter;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.image.GaleryDatabase;
import com.appcocha.llajtacomida.presenters.image.ImagePresenter;
import com.appcocha.llajtacomida.presenters.map.MapNavegation;
import com.appcocha.llajtacomida.presenters.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantManagerPresenter;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantPresenter;
import com.appcocha.llajtacomida.presenters.tools.ScreenSize;
import com.appcocha.llajtacomida.presenters.plate.ArrayAdapterPlate;
import com.appcocha.llajtacomida.presenters.user.AuthUser;
import com.appcocha.llajtacomida.views.favorites.FavoriteObjectFragment;
import com.appcocha.llajtacomida.views.rating.RatingRecordFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.zolad.zoominimageview.ZoomInImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RestaurantViewActivity extends AppCompatActivity implements View.OnClickListener,
        RestaurantInterface.ViewRestaurant, RestaurantInterface.ViewPlateList, ImageInterface.ViewImage,
        RestaurantInterface.ViewRestaurantManager{
    public String id;
    private static final int TIME_ANIMATION = 2000;
    //iconos
    private MenuItem iconEdit, iconDelete, iconGalery, iconMenuRestaurant, iconPublish;

    private Restaurant restaurant;
    // components
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnMenuEdit;
    private Button btnVisit;
    private ListView menuList;
    private TextView tvRating;
    // Favoritos

    // Visor de imagenes
    private ViewFlipper viewFlipper;
    private int height, width;
    private ZoomInImageView ivPhoto;
    private TextView tvName, tvOwnerName, tvPhone, tvAddress, tvOriginAndDescription;
    private LinearLayout llOwnerName;
    private ArrayAdapterPlate arrayAdapterPlate;
    private ArrayList<Plate> plateList;

    // Presenters
    private RestPlateListPresenter restPlateListPresenter;
    private RestaurantPresenter restaurantPresenter;
    private ImagePresenter imagePresenter;
    private RestaurantManagerPresenter restaurantManagerPresenter;

    // Fragments
    private RatingRecordFragment ratingRecordFragment;
    private FavoriteObjectFragment favoriteObjectFragment;

    // Toast personaliado
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        // Configuración del boton atrás
        getSupportActionBar().setTitle(R.string.restaurants_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Para el tamaño de las imagenes
        Display display = getWindowManager().getDefaultDisplay();
        height = (int) (ScreenSize.getWidth(display)*0.6666667);
        width = ScreenSize.getWidth(display);

        id = getIntent().getStringExtra("id");
        initComponents();
//        loadImages();
        plateList = new ArrayList<Plate>();
       initPresenters();
       initRatingFragment();
    }

    /**
     * Inicializa el fragmento rating en la actividad actual
     */
    private void initRatingFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("objectId", id);
        bundle.putString("nodeCollectionName", "restaurants");
        ratingRecordFragment = new RatingRecordFragment();
        ratingRecordFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flRating, ratingRecordFragment).commit(); // muestra el fragmento rating en la actividad
        favoriteObjectFragment = new FavoriteObjectFragment();
        favoriteObjectFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flFavorite, favoriteObjectFragment).commit(); // muestra el fragmento favorite
    }

    /**
     *Inicializa los presentadores
     */
    private void initPresenters() {
        restPlateListPresenter = new RestPlateListPresenter(this);
        restPlateListPresenter.filterPlateListInMenu(id);
        restaurantPresenter = new RestaurantPresenter(this);
        restaurantPresenter.searchRestaurant(id);
        imagePresenter = new ImagePresenter(this);
        final String NODE_COLECTION = "restaurants";
        imagePresenter.searchImages(NODE_COLECTION, id);
        restaurantManagerPresenter = new RestaurantManagerPresenter(this);
    }

    /**
     * Inicializa la animación automática
     */
    private void initAnimation(){
        viewFlipper.setDisplayedChild(0);
        viewFlipper.setFlipInterval(TIME_ANIMATION);
        viewFlipper.startFlipping();
    }


    /**
     * Inicializa los componentes de la vista
     */
    private void initComponents(){
        ivPhoto = (ZoomInImageView) findViewById(R.id.ziivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvOwnerName = (TextView) findViewById(R.id.tvOwner);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvOriginAndDescription = (TextView) findViewById(R.id.tvOriginAndDescription);
        llOwnerName = (LinearLayout) findViewById(R.id.llOwner);
        ivPhoto.getLayoutParams().height = (int) (height*0.984); //por el espacio para los botones next previous
        ivPhoto.getLayoutParams().width = (int) (width * 0.984); //(int) (width*0.89);//width;
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnMenuEdit = (ImageButton) findViewById(R.id.btnMenuEdit);
        btnVisit = (Button) findViewById(R.id.btnVisit);
        menuList = (ListView) findViewById(R.id.menuList);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlateNavegation.showPlateView(RestaurantViewActivity.this, plateList.get(position));
            }
        });
        tvRating = (TextView) findViewById(R.id.tvRating);
        // Cuando es un Fracment no se puede asociar onClick desde el código xml, es necesario este tipo de solución
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnMenuEdit.setOnClickListener(this);
        btnVisit.setOnClickListener(this);
        viewFlipper = (ViewFlipper) findViewById(R.id.vfCarrucel);
        tvName.setOnClickListener(this);
        toast = new Toast(this);
    }


    /**
     * Inicializa los íconos
     * @param menu
     */
    private void initIconMenu(Menu menu){
            iconEdit = (MenuItem) menu.findItem(R.id.iconEdit);
            iconDelete = (MenuItem) menu.findItem(R.id.iconDelete);
            iconGalery = (MenuItem) menu.findItem(R.id.iconGalery);
            iconMenuRestaurant = (MenuItem) menu.findItem(R.id.iconMenuRestaurant);
            iconPublish = (MenuItem) menu.findItem(R.id.iconPublish);
            changeIcon();
        if(AuthUser.getUser().getRole().equals("admin") || restaurant.getAuthor().equals(FirebaseAuth.getInstance().getUid())){
            iconEdit.setVisible(true);
            iconDelete.setVisible(true);
            iconGalery.setVisible(true);
            iconMenuRestaurant.setVisible(true);
        }else{
            iconEdit.setVisible(false);
            iconDelete.setVisible(false);
            iconGalery.setVisible(false);
            iconMenuRestaurant.setVisible(false);
        }
        if(AuthUser.getUser().getRole().equals("admin")) iconPublish.setVisible(true);
        else iconPublish.setVisible(false);
    }

    /**
     * Sirve para alternar el ícono de publicar o dejar de publicaar
     */
    private void changeIcon() {
        if(restaurant.isPublic()){
            iconPublish.setIcon(R.drawable.icon_public);
        }else{
            iconPublish.setIcon(R.drawable.icon_public_off);
        }
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        stopRealtimeDatabase();
        this.onBackPressed(); // accion del boton atras del sistema operativo
        return false;
    }

    @Override
    public void onBackPressed() {
        stopRealtimeDatabase();
        super.onBackPressed();
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
            case R.id.btnMenuEdit:
                RestaurantNavegation.showMenu(this, restaurant);
                break;
            case R.id.btnVisit:
                MapNavegation.showSetLocationMapActivity(this, restaurant);
                break;
            case R.id.tvName:
                pauseResume();
                break;
            default:
                Toast.makeText(this, getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Pausa o reanuda la presentación de las imágenes del restaurante
     */
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
                Toast.makeText(this, getString(R.string.message_not_contain_images), Toast.LENGTH_SHORT).show();
            }
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
                RestaurantNavegation.showEditRestaurantView(this, restaurant);
                break;
            case R.id.iconDelete:
                delete();
                break;
            case R.id.iconGalery:
                RestaurantNavegation.showGalery(this, restaurant.getId(),  restaurant.getName());
                break;
            case R.id.iconMenuRestaurant:
                RestaurantNavegation.showMenu(this, restaurant);
                break;
            case R.id.iconPublish:
                restaurantPresenter.update(this, restaurant); // publica o despublica
                break;
            default:
                Log.e("null", "Option invalid");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Solicita eliminar un registro
     */
    private void delete() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle(getString(R.string.confirm_title));
        confirm.setMessage(getString(R.string.message_delete_question));
        confirm.setCancelable(false);
        confirm.setPositiveButton(getString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            // Antes de eliminar el plato, debemos eliminar todos sus archivos
            GaleryDatabase galeryDatabase = new GaleryDatabase(RestaurantViewActivity.this, "restaurants", restaurant.getId());
            galeryDatabase.deleteAllData(); // es un metodo estatico
            Toast.makeText(RestaurantViewActivity.this, getString(R.string.message_deleting), Toast.LENGTH_SHORT).show();
            restaurantManagerPresenter.delete(restaurant.getId());
            stopRealtimeDatabase();
            onBackPressed();

            }
        });
        confirm.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        confirm.show();
    }

    /**
     * Carga la lista de platos de si menu
     * @param list
     */
    @Override
    public void showPlateList(ArrayList<Plate> list) {
       try {
           plateList.clear();
           plateList.addAll(list);
           arrayAdapterPlate = new ArrayAdapterPlate(this, R.layout.adapter_element_list, list);
           menuList.setAdapter(arrayAdapterPlate);
           ScreenSize.setListViewHeightBasedOnChildren(menuList);
       }catch (Exception e){
            Log.e("Error", e.getMessage());
       }
    }

    @Override
    public void showRestaurant(Restaurant restaurant) {
        try{
            this.restaurant = restaurant;
            if(!restaurant.getOwnerName().isEmpty()){
                llOwnerName.setVisibility(View.VISIBLE);
                tvOwnerName.setText(restaurant.getOwnerName());
            }else llOwnerName.setVisibility(View.GONE);
            tvName.setText(restaurant.getName());
            tvPhone.setText(restaurant.getPhone()
                    .replace(",", " - ")
                    .replace(".", " - ")
                    .replace("-", " - ")
                    .replace(";", " - ")
                    .replace(":", " - "));
            tvAddress.setText(restaurant.getAddress());
            tvOriginAndDescription.setText(restaurant.getOriginAndDescription());
            Glide.with(RestaurantViewActivity.this).load(restaurant.getUrl()).into(ivPhoto);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            tvRating.setText(String.valueOf(decimalFormat.format(restaurant.getPunctuation())));
            getSupportActionBar().setSubtitle(restaurant.getName());  // subtitulo del toolbar
            changeIcon();
        }catch(Exception e){
            Log.e("Error: " , e.getMessage());
        }
    }

    @Override
    public void showRestaurantList(ArrayList<Restaurant> restaurantList) {
        // no se usa en esta parte
    }

    @Override
    public void showImages(ArrayList<Image> imagesList) {
        try {
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
                    ImageView ivImg = new ImageView(RestaurantViewActivity.this);
                    Glide.with(RestaurantViewActivity.this).load(image.getUrl()).into(ivImg);
                    ivImg.setLayoutParams( // Tamaño de la imagen
                            new ViewGroup.LayoutParams((int) (width * 0.984), (int) (height * 0.984))
                    );
                    CardView cv = new CardView(RestaurantViewActivity.this);
                    cv.addView(ivImg);
                    cv.setRadius(35);
                    cv.setLayoutParams( // Tamaño de la imagen
                            new ViewGroup.LayoutParams((int) (width * 0.984), (int) (height * 0.984))
                    );
                    viewFlipper.addView(cv);
                    initAnimation();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * Detiene la base de datos
     */
    private void stopRealtimeDatabase(){
        restPlateListPresenter.stopRealtimeDatabse();
        imagePresenter.stopRealtimeDatabase();
        restaurantPresenter.stopRealtimeDatabse();
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
        // No se utiliza para esta vista
//        Toast.makeText(this, report, Toast.LENGTH_SHORT).show();
    }
}