package com.appcocha.llajtacomida.view.rating;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RatingInterface;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.rating.ArrayAdapterRating;
import com.appcocha.llajtacomida.presenter.rating.RatingPresenter;
import com.appcocha.llajtacomida.presenter.tools.ScreenSize;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Vista, maneja los votos del objeto (plato o restaurante)
 */
public class RatingRecordFragment extends Fragment implements View.OnClickListener, RatingInterface.ViewRating {

    // Components
    private ListView lvUserExperiences;
    private View view;
    private RatingBar rbStars;
    private EditText etExperience;
    private AlertDialog alertDialog;
    private Button btnCancel;
    private Button btnSave;

    // Data
    private String objectId;
    private String nodeCollectionName;
    private boolean valoratePermision;

    // Presentador
    private RatingPresenter ratingPresenter;

    //adaptador
    private ArrayAdapterRating arrayAdapterRating;

    private String loggedUser;

    public RatingRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_rating_record, container, false);
//        userId = FirebaseAuth.getInstance().getUid();
        loggedUser = FirebaseAuth.getInstance().getUid();
        objectId = this.getArguments().getString("objectId");
        nodeCollectionName = this.getArguments().getString("nodeCollectionName");
        initComponents();
        valoratePermision = false;
        if(nodeCollectionName.equals("plates")){
            valoratePermision = Permission.getAuthorize(AuthUser.user.getRole(), Permission.VALORATE_PLATE);
        }else if(nodeCollectionName.equals("restaurants")){
            valoratePermision = Permission.getAuthorize(AuthUser.user.getRole(), Permission.VALORATE_RESTAURANT);
        }
        return view;
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        rbStars = (RatingBar) view.findViewById(R.id.rbStars);
        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating > 0){
                    saveVote();
                }
            }
        });
        lvUserExperiences = (ListView) view.findViewById(R.id.lvUserExperiences);
        // alert de rating para votar
        View viewAlert = getLayoutInflater().inflate(R.layout.alert_vote, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewAlert);
        alertDialog = builder.create();
        // botones del alert
        btnCancel = (Button) viewAlert.findViewById(R.id.btnCancel);
        btnSave = (Button) viewAlert.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        // campo de texto del dialog
        etExperience = (EditText) viewAlert.findViewById(R.id.etExperience);

    }

    /**
     * Solicita guardar una votación
     */
    private void saveVote(){
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        Sound.playClick();
        switch (v.getId()){
            case R.id.btnCancel:
                rbStars.setRating(0);
                alertDialog.dismiss();
                break;
            case R.id.btnSave:
                if(valoratePermision){
                    ratingPresenter.saveVote(rbStars.getRating(), etExperience.getText().toString());
                    Toast.makeText(getContext(), getString(R.string.message_establish), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    etExperience.setText(null);
                }else Toast.makeText(getContext(), getContext().getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), getString(R.string.message_invalid_option), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showRating(ArrayList<Object> voteList, ArrayList<User> userList) {
        // mostrar con el presentador arrayadaoter
        try {
            arrayAdapterRating = new ArrayAdapterRating(getContext(), R.layout.adapter_element_users_rating_list, voteList, userList, nodeCollectionName, objectId);
            lvUserExperiences.setAdapter(arrayAdapterRating);
            ScreenSize.setListViewHeightBasedOnChildren(lvUserExperiences);
            rbStars.setEnabled(!userVoteAlready(userList));
            loadVoterPermission(); // Permisos de votar
        }catch (Exception e){
            Log.e("Error", "----------------------------------------------------------> " + e.getMessage());
        }
    }

    /**
     * permisos de los iconos
     */
    private void loadVoterPermission(){
        if(valoratePermision && rbStars.isEnabled())
            rbStars.setVisibility(View.VISIBLE);
        else if(rbStars.getVisibility() != View.GONE) rbStars.setVisibility(View.GONE);
    }

    /**
     * Verifica si un usuario ya votó por el objeto
     * @param userList
     * @return userVoteAlready
     */
    private boolean userVoteAlready(ArrayList<User> userList){
        boolean userVoteAlready = false;
        for(User user : userList){
            if(user.getId().equals(loggedUser)){
                userVoteAlready = true;
                break;
            }
        }
        if(userVoteAlready){
            rbStars.setVisibility(View.GONE);
        }else{
            rbStars.setVisibility(View.VISIBLE);
            rbStars.setRating(0);
        }
        return userVoteAlready;
    }


    /**
     * Detiene la BD
     */
    public void stopRealtimeDatabase(){
        ratingPresenter.stopRealtimeDatabase();
    }


    // Ciclos de vida para el reinicio de los presentadores

    @Override
    public void onResume() {
        super.onResume();
        // iniciar presentador
        ratingPresenter = new RatingPresenter(this,  nodeCollectionName, objectId);
        ratingPresenter.loadRating();
        Log.d("cicleLive", "RatingRecord onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        ratingPresenter.stopRealtimeDatabase();
        Log.d("cicleLive", "RatingRecord onPause");
    }
}