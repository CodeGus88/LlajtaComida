package com.example.llajtacomida.views.restaurantsViews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.llajtacomida.R;
import com.example.llajtacomida.presenters.restaurantsPresenter.RestaurantPresenter;

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

    private View view;

    // componentes
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
        lvRestaurants = (ListView) view.findViewById(R.id.lvRestaurants);
        etSearch  =(EditText) view.findViewById(R.id.searchView);
        return view;
    }


    // ----------------------------------->s


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSearch:
//                if(!restaurantList.isEmpty()){
                    if(etSearch.getVisibility() == View.GONE){
                        etSearch.setVisibility(View.VISIBLE);
                        etSearch.setText(null);
                        etSearch.setFocusable(true);
                        etSearch.requestFocus();
                    }else{
                        etSearch.setVisibility(View.GONE);
                        // Para que vuelga a cargar la lista (0 es cualquier numero)
//                        arrayAdapterPlates.filter("", 0);
                    }
//                }else{
//                    Toast.makeText(getContext(), "¡Aún no se cargaron datos!", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.iconAdd:
                RestaurantPresenter.showCreatedRestaurantView(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}