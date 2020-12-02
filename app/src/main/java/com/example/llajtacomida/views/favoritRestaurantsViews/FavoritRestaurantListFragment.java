package com.example.llajtacomida.views.favoritRestaurantsViews;

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

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.FavoriteInterface;
import com.example.llajtacomida.models.plate.Plate;
import com.example.llajtacomida.models.restaurant.Restaurant;
import com.example.llajtacomida.presenters.favorite.FavoritePresenter;
import com.example.llajtacomida.presenters.plate.ArrayAdapterPlate;
import com.example.llajtacomida.presenters.plate.PlateNavegation;
import com.example.llajtacomida.presenters.restaurant.ArrayAdapterRestaurant;
import com.example.llajtacomida.presenters.restaurant.RestaurantNavegation;

import java.util.ArrayList;

public class FavoritRestaurantListFragment extends Fragment implements FavoriteInterface.ViewFavorite {

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
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_favorit_restaurants_list, container, false);
        restaurantList = new ArrayList<Restaurant>();
        favoritePresenter = new FavoritePresenter(this, "restaurants");
        favoritePresenter.searchObjectFavoriteList();
        initComponents();
        return view;
    }

    private void initComponents(){
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvRestaurants = (ListView) view.findViewById(R.id.lvRestaurants);
        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        for(Object object: objectsList){
            restaurantList.add((Restaurant) object);
        }
        arrayAdapterRestaurant = new ArrayAdapterRestaurant(getContext(), R.layout.adapter_element_list, restaurantList);
        lvRestaurants.setAdapter(arrayAdapterRestaurant);
    }

    @Override
    public void showFavoriteIcon(boolean isFavorite) {
        // No se usa en esta vista
    }

    @Override
    public void successFul(boolean isSuccess) {
        // Esto no se usa en esta vista
    }
}