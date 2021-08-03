package com.appcocha.llajtacomida.view.favorites.favoritPlates;

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

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.FavoriteInterface;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.presenter.favorite.FavoritePresenter;
import com.appcocha.llajtacomida.presenter.plate.ArrayAdapterPlate;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantPresenter;
import com.appcocha.llajtacomida.presenter.tools.Sound;

import java.util.ArrayList;

/**
 * Vista, platos favoritos
 */
public class FavoritPlatesListFragment extends Fragment implements FavoriteInterface.ViewFavorite {

    // Iconos
    private MenuItem iconSearch;

    private EditText etSearch;
    private ListView lvPlates;
    private View view;

    // complementos
    private ArrayAdapterPlate arrayAdapterPlate;
    private ArrayList<Plate> plateList;

    // Presentador
    private FavoritePresenter favoritePresenter;

    public FavoritPlatesListFragment() {
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
        view = inflater.inflate(R.layout.fragment_favorit_plates_list, container, false);
        plateList = new ArrayList<Plate>();
//        favoritePresenter = new FavoritePresenter(this, "plates");
//        favoritePresenter.searchObjectFavoriteList();
        initComponents();
        return view;
    }

    /**
     * Inicializa los componentes del fragmento
     */
    private void initComponents(){
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvPlates = (ListView) view.findViewById(R.id.lvPlates);
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
     * Carga la lista de los platos favoritos del usuarios conectado en el adaptador
     * @param objectsList
     */
    @Override
    public void showFavoriteList(ArrayList<Object> objectsList) {
        try{
            plateList.clear();
            for(Object object: objectsList){
                plateList.add((Plate) object);
            }
            arrayAdapterPlate=  new ArrayAdapterPlate(getContext(), R.layout.adapter_element_list, plateList);
            lvPlates.setAdapter(arrayAdapterPlate);
            if(!etSearch.getText().toString().isEmpty() && etSearch.getVisibility() == View.VISIBLE)
                arrayAdapterPlate.filter(etSearch.getText().toString(), etSearch.getText().toString().length()-1);
        }catch (Exception e){
            Log.e("Error", "-------------------------------------------> " + e.getMessage());
        }
    }

    @Override
    public void showFavoriteIcon(boolean isFavorite) {
        // No se usa
    }

    @Override
    public void successFul(boolean isSuccess) {
        // No se usa
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
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconSearch.setVisible(true);
    }

    /**
     * Este método es el oyente de las acciones de los íconos
     * @param item
     * @return onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSearch:
//                if (etSearch.getVisibility() == View.GONE && userList != null) {
                if (etSearch.getVisibility() == View.GONE && plateList != null) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.setText(null);
                    etSearch.setFocusable(true);
                    etSearch.requestFocus();
                }else {
                    etSearch.setVisibility(View.GONE);
                    // Para que vuelga a cargar la lista (0 es cualquier numero)
//                    if(arrayAdapterPlate != null) arrayAdapterPlate.filter("", 0);
                    if(arrayAdapterPlate != null) arrayAdapterPlate.filter("", 0);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Ciclos de vida para el reinicio de los presentadores

    @Override
    public void onResume() {
        super.onResume();
        favoritePresenter = new FavoritePresenter(this, "plates");
        favoritePresenter.searchObjectFavoriteList();
        Log.d("cicleLive", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        favoritePresenter.stopRealtimeDatabase();
        Log.d("cicleLive", "onPause");
    }
}