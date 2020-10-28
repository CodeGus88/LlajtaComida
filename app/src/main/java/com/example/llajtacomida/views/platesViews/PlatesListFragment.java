package com.example.llajtacomida.views.platesViews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.llajtacomida.MainActivity;
import com.example.llajtacomida.R;
import com.example.llajtacomida.presenters.mainPresenter.MainPresenter;
import com.example.llajtacomida.presenters.platesPresenter.PlatesPresenter;

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

    private void setComponets() {
        etSearch = (EditText) view.findViewById(R.id.etSerach);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view = inflater.inflate(R.layout.fragment_plates_list, container, false);

        setComponets();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        setIcons(menu);
    }

    private void setIcons(Menu menu) {
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconAdd = (MenuItem) menu.findItem(R.id.iconAdd);
        for(int i = 0; i < menu.size(); i++){ // Ocultamos todo
            menu.getItem(i).setVisible(false);
        }

        iconSearch.setVisible(true);
        if("Es administrador".equalsIgnoreCase("Es administrador")){
            iconAdd.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getContext(), "se presionó un ícono", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.iconSearch:
                if(etSearch.getVisibility() == View.GONE){
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.setText(null);
                }else{
                    etSearch.setVisibility(View.GONE);
                }
                break;
            case R.id.iconAdd:
                PlatesPresenter.showCreatedPlateView(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}