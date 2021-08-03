package com.appcocha.llajtacomida.view.favorites.favoritRestaurants;

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
import com.appcocha.llajtacomida.model.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenter.favorite.FavoritePresenter;
import com.appcocha.llajtacomida.presenter.restaurant.ArrayAdapterRestaurant;
import com.appcocha.llajtacomida.presenter.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenter.tools.Sound;

import java.util.ArrayList;

public class FavoritRestaurantListFragment extends Fragment implements FavoriteInterface.ViewFavorite {

    // Iconos
    private MenuItem iconSearch;

    private EditText etSearch;
    private ListView lvRestaurants;
    private View view;

    // complementos
    private ArrayAdapterRestaurant arrayAdapterRestaurant;
    private ArrayList<Restaurant> restaurantList;

    // Presentador
    private FavoritePresenter favoritePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Sound.playClick();
        // Inflate the layout for this fragment
        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view= inflater.inflate(R.layout.fragment_favorit_restaurants_list, container, false);
        restaurantList = new ArrayList<Restaurant>();
//        favoritePresenter = new FavoritePresenter(this, "restaurants");
//        favoritePresenter.searchObjectFavoriteList();
        initComponents();
        return view;
    }

    private void initComponents(){
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvRestaurants = (ListView) view.findViewById(R.id.lvRestaurants);
        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound.playClick();
                RestaurantNavegation.showRestaurantView(getContext(), restaurantList.get(position).getId());
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

    @Override
    public void showFavoriteList(ArrayList<Object> objectsList) {
        try {
            restaurantList.clear();
            for(Object object: objectsList){
                restaurantList.add((Restaurant) object);
            }
            arrayAdapterRestaurant = new ArrayAdapterRestaurant(getContext(), R.layout.adapter_element_list, restaurantList);
            lvRestaurants.setAdapter(arrayAdapterRestaurant);
            if(!etSearch.getText().toString().isEmpty() && etSearch.getVisibility() == View.VISIBLE)
                arrayAdapterRestaurant.filter(etSearch.getText().toString(), etSearch.getText().toString().length()-1);
        }catch (Exception e){
            Log.e("Error", "----------------------------------------------------------------> " + e.getMessage());
        }
    }

    @Override
    public void showFavoriteIcon(boolean isFavorite) {
        // No se usa en esta vista
    }


    @Override
    public void successFul(boolean isSuccess) {
        // Esto no se usa en esta vista
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
                if (etSearch.getVisibility() == View.GONE && restaurantList != null) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.setText(null);
                    etSearch.setFocusable(true);
                    etSearch.requestFocus();
                }else {
                    etSearch.setVisibility(View.GONE);
                    // Para que vuelga a cargar la lista (0 es cualquier numero)
//                    if(arrayAdapterPlate != null) arrayAdapterPlate.filter("", 0);
                    if(arrayAdapterRestaurant != null) arrayAdapterRestaurant.filter("", 0);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Ciclos de vida para el reinicio de los presentadores

    @Override
    public void onResume() {
        super.onResume();
        favoritePresenter = new FavoritePresenter(this, "restaurants");
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