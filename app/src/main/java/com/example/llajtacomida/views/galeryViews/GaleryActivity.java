package com.example.llajtacomida.views.galeryViews;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Image;
import com.example.llajtacomida.presenters.galeryPresenter.GaleryDatabase;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.example.llajtacomida.views.ArrayAdapterImagesGalery;
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

public class GaleryActivity extends AppCompatActivity {

    // Datos de entrada
    private static String nodeCollectionName;
    private static String parentName;
    private static String parentId;

    // Para saber si se trata de la galería de platos o restaurantes
    private String objectName; // Puede ser "plate" o "restaurant"
    private ArrayList<Image> imageList;
    private ArrayAdapterImagesGalery arrayAdapterImagesGalery;

    // database
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    // Comprimir foto
    private Bitmap thumb_bitmap;
    private byte[] thumb_byte;
    private Uri uri;

    private GridView gvGalery;
    private TextView tvTitle;
    private MenuItem iconAdd;
    private boolean isAnAdministrator;

    private static String idImageSelected;
    protected static Image image;

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
//        getSupportActionBar().setTitle(getSupportActionBar().getTitle().toString().toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isAnAdministrator = true;
        imageList = new ArrayList<Image>();

        initComponents();
        initDataBase();
        loadImagesList();
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

    private void initDataBase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void loadImagesList(){
        databaseReference.child("App").child(nodeCollectionName).child(parentId).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList.clear();
                for (DataSnapshot photo:snapshot.getChildren()) {
                    try {
                        Image p = photo.getValue(Image.class);
                        imageList.add(p);
                        arrayAdapterImagesGalery = new ArrayAdapterImagesGalery(GaleryActivity.this, imageList);
                        gvGalery.setAdapter(arrayAdapterImagesGalery);
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
                if(imageList.isEmpty()){
                    arrayAdapterImagesGalery = new ArrayAdapterImagesGalery(GaleryActivity.this, imageList);
                    gvGalery.setAdapter(arrayAdapterImagesGalery);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Algo salió mal");
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
                PlatePresenter.showCropImage(this);
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
//                                Log.i("EROOR", e.getMessage());
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
//                public GaleryDatabase(Context context, String objectParentType, String parentId, String resurceDestination , Image image, byte [] thumb_byte){
                String resurceDestination = "images/plates/"+parentId+"/"+image.getId()+".jpg";
                GaleryDatabase galeryDatabase = new GaleryDatabase(GaleryActivity.this, nodeCollectionName, parentId, resurceDestination, image, thumb_byte);
                if(verbo.equalsIgnoreCase("add")){
                    Toast.makeText(GaleryActivity.this, "Subiendo...", Toast.LENGTH_SHORT).show();
                    galeryDatabase.uploadData();
                }else if(verbo.equalsIgnoreCase("delete")){
                    Toast.makeText(GaleryActivity.this, "Quitando imagen...", Toast.LENGTH_SHORT).show();
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
}