package com.example.llajtacomida.views.restaurantsViews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.models.Restaurant;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantPresenter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Restaurant> restaurantsList;
    private ArrayAdapterRestaurant arrayAdapterRestaurant;

    // database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    // componentes
    private View view;
    private MenuItem iconSearch, iconAdd;
    private ListView lvRestaurants;
    private EditText etSearch;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantListFragment newInstance(String param1, String param2) {
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); // para cargar los iconos del toolBar
        view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        // Inflate the layout for this fragment

        initComponents();
        initDataBase();
        loadListRestaurants();
        return view;
    }


    // ----------------------------------->

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        initIconsMenu(menu);
    }

    private void initIconsMenu(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconSearch.setVisible(true);

        iconAdd = (MenuItem) menu.findItem(R.id.iconAdd);
        iconAdd.setVisible(true);
    }

    private void initComponents() {
        etSearch = (EditText) view.findViewById(R.id.searchView);
        lvRestaurants = (ListView) view.findViewById(R.id.lvRestaurants);
        restaurantsList = new ArrayList<Restaurant>();
        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestaurantPresenter.showRestaurantView(getContext(), restaurantsList.get(position));
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() { // para buscar mientras se escribe
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterRestaurant.filter(s.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initDataBase(){
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference().child("App").child("restaurants");
    }

    private void loadListRestaurants(){
        databaseReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurantsList.clear();
                for (DataSnapshot plate : snapshot.getChildren()){
                    try {
                        Restaurant r = plate.getValue(Restaurant.class); // Para el  uso de esta estrategia el contructor del objeto plato no debe recibir ningún parámetro
                        restaurantsList.add(r);
                        arrayAdapterRestaurant = new ArrayAdapterRestaurant(getContext(), R.layout.adapter_element_list, restaurantsList);
                        lvRestaurants.setAdapter(arrayAdapterRestaurant);
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
                if(restaurantsList.isEmpty()){
                    try {
                        arrayAdapterRestaurant = new ArrayAdapterRestaurant(getContext(), R.layout.adapter_element_list, restaurantsList);
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                    lvRestaurants.setAdapter(arrayAdapterRestaurant);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No se pudo cargar la lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSearch:
                    if(etSearch.getVisibility() == View.GONE){
                        etSearch.setVisibility(View.VISIBLE);
                        etSearch.setText(null);
                        etSearch.setFocusable(true);
                        etSearch.requestFocus();
                    }else{
                        etSearch.setVisibility(View.GONE);
                    }
                break;
            case R.id.iconAdd:
                RestaurantPresenter.showCreatedRestaurantView(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}