package com.example.llajtacomida.views.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.FavoriteInterface;
import com.example.llajtacomida.presenters.favorite.FavoritePresenter;

import java.util.ArrayList;


public class FavoriteObjectFragment extends Fragment implements FavoriteInterface.ViewFavorite {

    private View view;
    private boolean isFavorite;

    private String nodeCollectionName, objectId, messageAdd, messageRemove;

    // components
    private ImageButton btnFavorite;

    // Presenters
    private FavoritePresenter favoritePresenter;

    public FavoriteObjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite_object, container, false);
        nodeCollectionName = this.getArguments().getString("nodeCollectionName");
        objectId = this.getArguments().getString("objectId");
        loadMessages();
        favoritePresenter = new FavoritePresenter(this, nodeCollectionName);
        favoritePresenter.searchFavoriteObject(objectId);
        initComponents();

        return view;
    }

    private void initComponents() {
        btnFavorite = (ImageButton) view.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite) favoritePresenter.removeObjectFavorite(objectId);
                else favoritePresenter.saveObjectFavorite(objectId);
            }
        });
    }

    private void loadMessages(){
        if(nodeCollectionName.equals("plates")){
            messageAdd = getString(R.string.addPlateToFavoriteList);
            messageRemove = getString(R.string.removeFromListFavoritePlates);
        }else if(nodeCollectionName.equals("restaurants")){
            messageAdd = getString(R.string.addRestaurantToFavoriteList);
            messageRemove = getString(R.string.removeFromListFavoriteRestaurants);
        }else{
            messageAdd = "... ( + )";
            messageRemove = "... ( - )";
        }
    }

    @Override
    public void showFavoriteList(ArrayList<Object> objectsList) {
        // no se usará para  esta vista
    }

    @Override
    public void showFavoriteIcon(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if(isFavorite){
            btnFavorite.setImageResource(R.mipmap.favorite_on);
        }else{
            btnFavorite.setImageResource(R.mipmap.favorite_of);
        }
    }

    @Override
    public void successFul(boolean isSuccess) {
        if(isSuccess){
            if(isFavorite){
                Toast.makeText(getContext(), messageAdd, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), messageRemove, Toast.LENGTH_SHORT).show();
            }
        }else Toast.makeText(getContext(), getString(R.string.messageIsFailed), Toast.LENGTH_SHORT).show();
    }

    public void stopRealtimeDatabase(){
        favoritePresenter.stopRealtimeDatabase();
    }
}