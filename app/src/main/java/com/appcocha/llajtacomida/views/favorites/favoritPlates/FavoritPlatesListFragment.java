package com.appcocha.llajtacomida.views.favorites.favoritPlates;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.FavoriteInterface;
import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.favorite.FavoritePresenter;
import com.appcocha.llajtacomida.presenters.plate.ArrayAdapterPlate;
import com.appcocha.llajtacomida.presenters.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenters.tools.Sound;

import java.util.ArrayList;

/**
 * Vista, platos favoritos
 */
public class FavoritPlatesListFragment extends Fragment implements FavoriteInterface.ViewFavorite {

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
        view = inflater.inflate(R.layout.fragment_favorit_plates_list, container, false);
        plateList = new ArrayList<Plate>();
        favoritePresenter = new FavoritePresenter(this, "plates");
        favoritePresenter.searchObjectFavoriteList();
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

    @Override
    public void onResume() {
        super.onResume();
        favoritePresenter.searchObjectFavoriteList();
    }
}