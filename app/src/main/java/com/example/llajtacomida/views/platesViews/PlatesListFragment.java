package com.example.llajtacomida.views.platesViews;

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
import com.example.llajtacomida.models.Plate;
import com.example.llajtacomida.presenters.platesPresenter.PlatePresenter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlatesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlatesListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    // Iconos
    private MenuItem iconAdd, iconSearch;

    // Components
    private EditText etSearch;
    private ListView lvPlates;

    // database
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    // Para listar platos
    private ArrayList<Plate> platesList;
    private ArrayAdapterPlate arrayAdapterPlates;

    private boolean isAnAdministrator;

    public PlatesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlatesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlatesListFragment newInstance(String param1, String param2) {
        PlatesListFragment fragment = new PlatesListFragment();
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

    private void initComponents() {

        etSearch = (EditText) view.findViewById(R.id.searchView);
        lvPlates = (ListView) view.findViewById(R.id.lvPlates);

        platesList = new ArrayList<Plate>();
//        platesListCopy = new ArrayList<Plate>();

        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlatePresenter.showPlateView(getContext(), platesList.get(position));
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() { // para buscar mientras se escribe
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterPlates.filter(s.toString(), start);
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
        databaseReference =  firebaseDatabase.getReference().child("App").child("plates");
    }

    private void loadListPlates(){

        databaseReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                platesList.clear();
                for (DataSnapshot plate : snapshot.getChildren()){
                    try {
                        Plate p = plate.getValue(Plate.class); // Para el  uso de esta estrategia el contructor del objeto plato no debe recibir ningún parámetro
                        platesList.add(p);
                        arrayAdapterPlates = new ArrayAdapterPlate(getContext(), R.layout.adapter_element_list, platesList);
                        lvPlates.setAdapter(arrayAdapterPlates);
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
                if(platesList.isEmpty()){
                    try {
                        arrayAdapterPlates = new ArrayAdapterPlate(getContext(), R.layout.adapter_element_list, platesList);
                    }catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                    lvPlates.setAdapter(arrayAdapterPlates);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No se pudo cargar la lista", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view = inflater.inflate(R.layout.fragment_plates_list, container, false);

        isAnAdministrator = true;

        initComponents();
        initDataBase();
        loadListPlates();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        initIconsMenu(menu);
    }

    private void initIconsMenu(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        for(int i = 0; i < menu.size(); i++){ // Ocultamos todo
            menu.getItem(i).setVisible(false);
        }
        iconSearch.setVisible(true);
        if(isAnAdministrator){
            iconAdd = (MenuItem) menu.findItem(R.id.iconAdd);
            iconAdd.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSearch:
                if(!platesList.isEmpty()){
                    if(etSearch.getVisibility() == View.GONE){
                        etSearch.setVisibility(View.VISIBLE);
                        etSearch.setText(null);
                        etSearch.setFocusable(true);
                        etSearch.requestFocus();
                    }else{
                        etSearch.setVisibility(View.GONE);
                        // Para que vuelga a cargar la lista (0 es cualquier numero)
                        arrayAdapterPlates.filter("", 0);
                    }
                }else{
                    Toast.makeText(getContext(), "¡Aún no se cargaron datos!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iconAdd:
                PlatePresenter.showCreatedPlateView(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}