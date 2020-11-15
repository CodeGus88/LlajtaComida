package com.example.llajtacomida.presenters.restaurantsPresenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.MenuDB;
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.platesPresenter.ArrayAdapterPlate;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.example.llajtacomida.presenters.tools.ScreenSize;
import com.example.llajtacomida.views.restaurantsViews.RestaurantViewActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadMenuList  implements ValueEventListener{

    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private DatabaseReference databaseReference;
    private ArrayList<Plate> platesList;
    private ArrayAdapterPlate arrayAdapterPlate;
    private MenuDB menuDB;
    private String restaurantId;
    private final int ADAPTER_ELEMENT_LIST;

    public LoadMenuList(final Context context, String restaurantId){

        this.context = context;

        ADAPTER_ELEMENT_LIST = R.layout.adapter_element_list;
        this.restaurantId = restaurantId;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("App").child("plates");
        platesList = new ArrayList<Plate>();

        menuDB = new MenuDB(context, restaurantId);
        databaseReference.orderByChild("name").addValueEventListener(this);
    }

    private boolean existInMenu(String plateId, ArrayList<String> menuList){
        boolean exist = false;
        for (String id: menuList) {
            if(id.equals(plateId)){
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * para el actualizado automatico de la lista (para cuando se salga de la app)
     */
    public void stopOndataChange(){
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        platesList.clear();
        for (DataSnapshot p: snapshot.getChildren()) {
            Plate plate = (Plate) p.getValue(Plate.class);
            if(menuDB.getMenu() != null){
                if(existInMenu(plate.getId(), menuDB.getMenu().getMenuList())){
                    platesList.add(plate);
                }
            }
        }
        if(!platesList.isEmpty()){
            arrayAdapterPlate = new ArrayAdapterPlate(
                    context,
                    ADAPTER_ELEMENT_LIST
                    , platesList);
        }else{
            try {
                arrayAdapterPlate = new ArrayAdapterPlate(
                        context,
                        ADAPTER_ELEMENT_LIST,
                        platesList);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }
        }
        try {
            RestaurantViewActivity.setMenuList(arrayAdapterPlate);
            ScreenSize.setListViewHeightBasedOnChildren(RestaurantViewActivity.getListView());
            // Eventos de la lista
            RestaurantViewActivity.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PlatePresenter.showPlateView(context, platesList.get(position));
                }
            });
        }catch (Exception e){
            databaseReference.removeEventListener(this); // para la ejecución de la base de datos realTime
            Log.e("Error:", e.getMessage());
        }

    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
