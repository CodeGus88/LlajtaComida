package com.appcocha.llajtacomida.views.restaurants;

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

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RestaurantInterface;
import com.appcocha.llajtacomida.models.restaurant.Restaurant;
import com.appcocha.llajtacomida.presenters.restaurant.ArrayAdapterRestaurant;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantNavegation;
import com.appcocha.llajtacomida.presenters.restaurant.RestaurantPresenter;
import com.appcocha.llajtacomida.presenters.tools.Sound;
import com.appcocha.llajtacomida.presenters.user.AuthUser;

import java.util.ArrayList;

/**
 * Vista, muestra la lista de restaurantes
 */
public class RestaurantListFragment extends Fragment implements RestaurantInterface.ViewRestaurant {

    private ArrayList<Restaurant> restaurantList;
    private ArrayAdapterRestaurant arrayAdapterRestaurant;

    // componentes
    private View view;
    private MenuItem iconSearch, iconAdd, iconRestPublicOf;
    private ListView lvRestaurants;
    private EditText etSearch;

    // Presentador
    private RestaurantPresenter restaurantPresenter;

    // Permisos
//    private boolean isAnAdministrator;// , isAuthor;;

    public RestaurantListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Sound.playClick();
        setHasOptionsMenu(true); // para cargar los iconos del toolBar
        view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        // Inflate the layout for this fragment
        initComponents();
        restaurantPresenter = new RestaurantPresenter(this);
        restaurantPresenter.loadRestaurantList();
        return view;
    }

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
        iconRestPublicOf = (MenuItem) menu.findItem(R.id.iconListPublicOf);
        try{
            if(AuthUser.getUser().getRole().equals("admin")) iconRestPublicOf.setVisible(true);
            else iconRestPublicOf.setVisible(false);
            if(AuthUser.getUser().getRole().equals("admin") || AuthUser.getUser().getRole().equals("collaborator")) iconAdd.setVisible(true);
            else iconAdd.setVisible(false);
        }catch (Exception e){
            Toast.makeText(getContext(), "Could not connect", Toast.LENGTH_SHORT).show();
            Log.e("Error", "-------------------------------------------------> " + e.getMessage());
        }
    }

    private void initComponents() {
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvRestaurants = (ListView) view.findViewById(R.id.lvRestaurants);
        restaurantList = new ArrayList<Restaurant>();
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
                        arrayAdapterRestaurant.filter("", 0);
                    }
                break;
            case R.id.iconAdd:
                RestaurantNavegation.showCreatedRestaurantView(getContext());
                break;
            case R.id.iconListPublicOf:
                RestaurantNavegation.showRestPublicOf(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showRestaurant(Restaurant restaurant) {
        // no se usa para la lista
    }

    @Override
    public void showRestaurantList(ArrayList<Restaurant> restaurantList) {
        try {
            this.restaurantList.clear();
            this.restaurantList.addAll(restaurantList);
            arrayAdapterRestaurant = new ArrayAdapterRestaurant(getContext(), R.layout.adapter_element_list, this.restaurantList);
            lvRestaurants.setAdapter(arrayAdapterRestaurant);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        restaurantPresenter.stopRealtimeDatabse();
    }
}