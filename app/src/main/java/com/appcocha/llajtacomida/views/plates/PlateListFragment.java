package com.appcocha.llajtacomida.views.plates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.plate.ArrayAdapterPlate;
import com.appcocha.llajtacomida.presenters.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenters.plate.PlatePresenter;
import com.appcocha.llajtacomida.presenters.plate.PlateList;
import com.appcocha.llajtacomida.presenters.user.AuthUser;

import java.util.ArrayList;

/**
 * Vista, Este fragmento muestra la lista de platos
 */
public class PlateListFragment extends Fragment implements PlateInterface.ViewPlate{

    // view que hará referencia a la actividad contenedora de este fragmento
    private View view;

    // Iconos
    private MenuItem iconAdd, iconSearch;

    // Components
    private EditText etSearch;
    private ListView lvPlates;

    // Para listar platos
    private ArrayList<Plate> plateList;
    private ArrayAdapterPlate arrayAdapterPlate;

    /**
     * Muestra la lista de platos
     */
    private PlateInterface.PresenterPlate presenterPlate;

    /**
     * Requiere de un constructor vacío
     */
    public PlateListFragment() {
        // Required empty public constructor
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents() {

        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvPlates = (ListView) view.findViewById(R.id.lvPlates);

        plateList = new ArrayList<Plate>();

        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlateNavegation.showPlateView(getContext(), plateList.get(position));
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() { // para buscar mientras se escribe
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterPlate.filter(s.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    /**
     * Inicializa la actividad a la que corresponde
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view = inflater.inflate(R.layout.fragment_plate_list, container, false);

        initComponents();
        presenterPlate = new PlatePresenter(this);
        presenterPlate.loadPlatesList();
        return view;
    }

    /**
     * Inicializa los íconos del del toolbar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        initIconsMenu(menu);
    }

    /**
     * Inicializa todos lo íconos
     * @param menu hace referenccia al menú de la actividad contenedora
     */
    private void initIconsMenu(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        for(int i = 0; i < menu.size(); i++){ // Ocultamos todo
            menu.getItem(i).setVisible(false);
        }
        iconSearch.setVisible(true);
        iconAdd = (MenuItem) menu.findItem(R.id.iconAdd);

        try{
            if(AuthUser.user.getRole().equalsIgnoreCase("admin")){ // solo si es admin se mostrara el icon ode usario
                iconAdd.setVisible(true);
            }else{
                iconAdd.setVisible(false);
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Could not connect", Toast.LENGTH_SHORT).show();
            Log.e("Error", "-------------------------------------------------> " + e.getMessage());
        }

    }

    /**
     * Este método es el oyente de las acciones de los íconos
     * @param item
     * @return
     */
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
                    // Para que vuelga a cargar la lista (0 es cualquier numero)
                    arrayAdapterPlate.filter("", 0);
                }
                break;
            case R.id.iconAdd:
                PlateNavegation.showCreatedPlateView(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPlate(Plate plate) {
        //     no se usará
    }

    /**
     * recibe los la lista de platos
     * @param plateList
     */
    @Override
    public void showPlateList(ArrayList<Plate> plateList) {
        try {
            this.plateList = plateList;
            arrayAdapterPlate = new ArrayAdapterPlate(getContext(), R.layout.adapter_element_list, this.plateList);
            lvPlates.setAdapter(arrayAdapterPlate);
            if(AuthUser.getUser().getRole().equalsIgnoreCase("admin")){ // Servira para la validacion de no repetir platos con el mis nonombre
                PlateList pl = new PlateList(plateList);
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }
}