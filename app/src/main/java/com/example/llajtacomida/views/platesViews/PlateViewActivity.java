package com.example.llajtacomida.views.platesViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Plate;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_view);
        initComponents();
        inicializarDataBase();
        id = getIntent().getStringExtra("id");
        showPlate();
    }


    private void initComponents(){
        ivPhoto = (ZoomInImageView) findViewById(R.id.ziivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvIngredients = (TextView) findViewById(R.id.tvIngredients);
        tvOrigin = (TextView) findViewById(R.id.tvOrigin);

        Display display = getWindowManager().getDefaultDisplay();

        //(height->1280, width->720) (widht 0.56)es lo que se usa ;para esta imagen
        ivPhoto.getLayoutParams().height = (int) (ScreenSize.getWidth(display)*0.5625);
        ivPhoto.getLayoutParams().width = ScreenSize.getWidth(display);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenSize.getWidth(display), ScreenSize.getHeight(display));
//        ivPhoto.setLayoutParams(params);


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
                Plate p = snapshot.getValue(Plate.class);
                tvName.setText(p.getName());
                tvIngredients.setText(p.getIngredients());
                tvOrigin.setText(p.getOrigin());
                Glide.with(PlateViewActivity.this).load(p.getUrl()).into(ivPhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlateViewActivity.this, "Ocurrión un error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}