package com.appcocha.llajtacomida.view.plates;

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
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.PlateInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.presenter.plate.ArrayAdapterPlate;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.plate.PlatePresenter;
import com.appcocha.llajtacomida.presenter.plate.PlateList;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.AuthUser;

import java.util.ArrayList;

/**
 * Vista, Este fragmento muestra la lista de platos
 */
public class PlateListFragment extends Fragment implements PlateInterface.ViewPlate, PopupMenu.OnMenuItemClickListener {

    // view que hará referencia a la actividad contenedora de este fragmento
    private View view;

    // Iconos
    private MenuItem iconAdd, iconSearch, iconReorder;

    // Components
    private EditText etSearch;
    private ListView lvPlates;
    private Space space;
    private PopupMenu popupMenu;

    // Para listar platos
    private ArrayList<Plate> plateList;
    private ArrayAdapterPlate arrayAdapterPlate;

    /**
     * Muestra la lista de platos
     */
    private PlateInterface.PresenterPlate platePresenter;

    /**
     * Requiere de un constructor vacío
     */
    public PlateListFragment() {
        // Required empty public constructor
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
        Sound.playClick();
        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view = inflater.inflate(R.layout.fragment_plate_list, container, false);
        initComponents();
        platePresenter = new PlatePresenter(this);
        platePresenter.loadPlatesList();
        return view;
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents() {
        space = (Space) view.findViewById(R.id.spaceForPopPup);
        popupMenu = new PopupMenu(getContext(), space);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_order_list);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvPlates = (ListView) view.findViewById(R.id.lvPlates);
        plateList = new ArrayList<Plate>();
        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
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
        iconReorder = (MenuItem) menu.findItem(R.id.iconReorder);
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        for(int i = 0; i < menu.size(); i++){ // Ocultamos todo
            menu.getItem(i).setVisible(false);
        }
        iconReorder.setVisible(true);
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
            case R.id.iconReorder:
                popupMenu.show();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_name_order:
                if(arrayAdapterPlate.reorderByName()) platePresenter.loadPlatesList();
                return true;
            case R.id.item_punctuation_order:
                if(arrayAdapterPlate.reorderByPunctuation()) platePresenter.loadPlatesList();
                return true;
            default:
                return false;
        }
    }
}