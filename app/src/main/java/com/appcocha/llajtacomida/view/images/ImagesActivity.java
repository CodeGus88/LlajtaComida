package com.appcocha.llajtacomida.view.images;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.ImageInterface;
import com.appcocha.llajtacomida.model.image.Image;
import com.appcocha.llajtacomida.presenter.image.ArrayAdapterImagesGalery;
import com.appcocha.llajtacomida.model.image.GaleryDatabase;
import com.appcocha.llajtacomida.presenter.image.ImagePresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenter.tools.ScreenSize;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Vista, gestor de imágenes
 */
public class ImagesActivity extends AppCompatActivity implements ImageInterface.ViewImage {

    // Datos de entrada
    private static String nodeCollectionName;
    private static String parentName;
    private static String parentId;

    // Para saber si se trata de la galería de platos o restaurantes
//    private String objectName; // Puede ser "plate" o "restaurant"
    private ArrayAdapterImagesGalery arrayAdapterImagesGalery;

    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte[] thumb_byte;
    private Uri uri;

    private GridView gvGalery;
    private TextView tvTitle;
    private MenuItem iconAdd;
    private boolean isAnAdministrator;

    protected Image image;
    private ImageInterface.PresenterImage presenterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_galery);

        // Recojer datos de entrada
        if(getIntent().hasExtra("nodeCollectionName")) nodeCollectionName = getIntent().getStringExtra("nodeCollectionName");
        if(getIntent().hasExtra("parentName")) parentName = getIntent().getStringExtra("parentName");
        if(getIntent().hasExtra("parentId")) parentId = getIntent().getStringExtra("parentId");
        getSupportActionBar().setTitle(parentName);
        getSupportActionBar().setSubtitle(getString(R.string.sub_title_images));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isAnAdministrator = true;

        initComponents();
//        if(nodeCollectionName.equals("plates")){
//            if(!Permissions.getAuthorize(AuthUser.user.getRole(), Permissions.SHOW_PLATE_GALERY)){
//                Toast.makeText(this, getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
//                onBackPressed();
//            }
//        }
    }

    /**
     * Captura de accion del boton atras <- de la barra superior
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // acción del botón atrás del sistema operativo
        return false;
    }

    /**
     * Inicializa ls componentes
     */
    private void initComponents(){

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        gvGalery= (GridView) findViewById(R.id.gvImages);
        tvTitle.setText(getString(R.string.images_title)+ " " + parentName);

        gvGalery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                image = arrayAdapterImagesGalery.getImage(position);
                addDeleteImage(getString(R.string.image), "Id: "+image.getId()
                        +"\nUrl: " + image.getUrl()
                        +"\n"+getString(R.string.remove_image), "delete", image, getString(R.string.message_remove));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initIconMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initIconMenu(Menu menu){
        if(isAnAdministrator) {
            iconAdd = (MenuItem) menu.findItem(R.id.iconAdd);
            iconAdd.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Sound.playClick();
        switch (item.getItemId()){
            case R.id.iconAdd:
                if(nodeCollectionName.equalsIgnoreCase("plates")) PlateNavegation.showCropImage(this);
                else if (nodeCollectionName.equalsIgnoreCase("restaurants")) RestaurantNavegation.showCropImage(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                // Segunda parte
                File file = new File(uri.getPath());
                try{
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(90)
                            .compressToBitmap(file);

                }catch (Exception e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream biByteArrayOutputStream  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, biByteArrayOutputStream);
                thumb_byte = biByteArrayOutputStream.toByteArray(); // Contiene la imagen comprimida
                // Fin del compresor
                if(thumb_bitmap != null){
                    image = new Image();
                    addDeleteImage(getString(R.string.add_image_title), getString(R.string.add_image_question), "add", image, getString(R.string.add_icon));
                }
                thumb_bitmap = null;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /**
     * Agrega e elimina imágenes
     * @param title
     * @param menssage
     * @param verbo
     * @param image
     * @param btn
     */
    private void addDeleteImage(final String title, final String menssage, final String verbo, final Image image, final String btn){
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle(title);
        confirm.setMessage(menssage);
        confirm.setCancelable(false);
        confirm.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                String resurceDestination = "images/"+nodeCollectionName+"/"+parentId+"/"+image.getId()+".jpg";
                GaleryDatabase galeryDatabase = new GaleryDatabase(ImagesActivity.this, nodeCollectionName, parentId, resurceDestination, image, thumb_byte);
                if(verbo.equalsIgnoreCase("add")){
                    Sound.playLoad();
                    Toast.makeText(ImagesActivity.this, getString(R.string.message_uploading), Toast.LENGTH_SHORT).show();
                    galeryDatabase.uploadData();
                }else if(verbo.equalsIgnoreCase("delete")){
                    Sound.playThrow();
                    Toast.makeText(ImagesActivity.this, getString(R.string.removing_image), Toast.LENGTH_SHORT).show();
                    galeryDatabase.deleteData();
                }
            }
        });
        confirm.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Sound.playClick();
            }
        });
        confirm.show();
    }

    @Override
    public void showImages(ArrayList<Image> imagesList) {
        // Tamaño de las imagenes de mosaico
        Display display = getWindowManager().getDefaultDisplay();
        int x= ((int) (ScreenSize.getWidth(display)*0.90)) / 3; //int x= ((int) (ScreenSize.getWidth(display)*0.855)) / 3;
        int y = (int) (x*0.6666667);
        try {
            arrayAdapterImagesGalery = new ArrayAdapterImagesGalery(ImagesActivity.this, imagesList, x, y);
            gvGalery.setAdapter(arrayAdapterImagesGalery);
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
        if(imagesList.isEmpty()){
            arrayAdapterImagesGalery = new ArrayAdapterImagesGalery(ImagesActivity.this, imagesList, x, y);
            gvGalery.setAdapter(arrayAdapterImagesGalery);
        }
    }

    /**
     * Los dos metodos sobreescritos que se vena continuación, recuperan los datos de la actividad
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("nodeCollectionName", nodeCollectionName);
        outState.putString("parentName", parentName);
        outState.putString("parentId", parentId);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nodeCollectionName = savedInstanceState.getString("nodeCollectionName");
        parentName = savedInstanceState.getString("parentName");
        parentId = savedInstanceState.getString("parentId");
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            Animatoo.animateFade(this); //Animación al cambiar de actividad
        }catch (Exception e){
            Log.e("Error:", e.getMessage());
        }
    }


    // Ciclos de vida para el reinicio de los presentadores

    @Override
    protected void onResume() {
        super.onResume();
        // Inicializar el presentador
        // inicializar presentador
        presenterImage = new ImagePresenter(this);
        presenterImage.searchImages(nodeCollectionName, parentId);
        Log.d("cicleLive", "SetPromotion onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterImage.stopRealtimeDatabase();
        Log.d("cicleLive", "SetPromotion onPause");
    }
}