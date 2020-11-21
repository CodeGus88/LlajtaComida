package com.example.llajtacomida.views.imagesViews;

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

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.ImageInterface;
import com.example.llajtacomida.models.image.Image;
import com.example.llajtacomida.presenters.image.GaleryDatabase;
import com.example.llajtacomida.presenters.image.ImagePresenter;
import com.example.llajtacomida.presenters.plate.PlateNavegation;
import com.example.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class ImagesActivity extends AppCompatActivity implements ImageInterface.ViewImage {

    // Datos de entrada
    private static String nodeCollectionName;
    private static String parentName;
    private static String parentId;

    // Para saber si se trata de la galería de platos o restaurantes
    private String objectName; // Puede ser "plate" o "restaurant"
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
        nodeCollectionName = getIntent().getStringExtra("nodeCollectionName");
        parentName = getIntent().getStringExtra("parentName");
        parentId = getIntent().getStringExtra("parentId");

        //Configiración del titlo del toolbar y boton atrás
        getSupportActionBar().setTitle(nodeCollectionName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isAnAdministrator = true;

        initComponents();
        // inicializar presentador
        presenterImage = new ImagePresenter(this);
        presenterImage.searchImages(nodeCollectionName, parentId);
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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        gvGalery= (GridView) findViewById(R.id.gvImages);
        tvTitle.setText("Imágenes de " + parentName);

        gvGalery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                image = arrayAdapterImagesGalery.getImage(position);
                addDeleteImage("Imagen", "Id: "+image.getId()
                        +"\nUrl: " + image.getUrl()
                        +"\n¿Quitar esta imagen?", "delete", image, "Quitar");
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
                    addDeleteImage("Agregar", "¿Quiere agregar la imagen recortada?", "add", image, "Agregar");
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
//                String resurceDestination = "images/plates/"+parentId+"/"+image.getId()+".jpg";
                String resurceDestination = "images/"+nodeCollectionName+"/"+parentId+"/"+image.getId()+".jpg";
                GaleryDatabase galeryDatabase = new GaleryDatabase(ImagesActivity.this, nodeCollectionName, parentId, resurceDestination, image, thumb_byte);
                if(verbo.equalsIgnoreCase("add")){
                    Toast.makeText(ImagesActivity.this, "Subiendo...", Toast.LENGTH_SHORT).show();
                    galeryDatabase.uploadData();
                }else if(verbo.equalsIgnoreCase("delete")){
                    Toast.makeText(ImagesActivity.this, "Quitando imagen...", Toast.LENGTH_SHORT).show();
                    galeryDatabase.deleteData();
                }
            }
        });
        confirm.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
//                Toast.makeText(GaleryActivity.this, "Cancelar...", Toast.LENGTH_SHORT).show();
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
            Log.e("Error", e.getMessage());
        }
        if(imagesList.isEmpty()){
            arrayAdapterImagesGalery = new ArrayAdapterImagesGalery(ImagesActivity.this, imagesList, x, y);
            gvGalery.setAdapter(arrayAdapterImagesGalery);
        }
    }
}