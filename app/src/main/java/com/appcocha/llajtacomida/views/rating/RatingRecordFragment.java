package com.appcocha.llajtacomida.views.rating;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.RatingInterface;
import com.appcocha.llajtacomida.models.user.User;
import com.appcocha.llajtacomida.presenters.rating.ArrayAdapterRating;
import com.appcocha.llajtacomida.presenters.rating.RatingPresenter;
import com.appcocha.llajtacomida.presenters.tools.ScreenSize;
import com.appcocha.llajtacomida.presenters.user.AuthUser;
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

        // iniciar presentador
        ratingPresenter = new RatingPresenter(this,  nodeCollectionName, objectId);
        ratingPresenter.loadRating();
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
        switch (v.getId()){
            case R.id.btnCancel:
                rbStars.setRating(0);
                alertDialog.dismiss();
                break;
            case R.id.btnSave:
                ratingPresenter.saveVote(rbStars.getRating(), etExperience.getText().toString());
                Toast.makeText(getContext(), getString(R.string.message_establish), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                etExperience.setText(null);
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
        if((AuthUser.getUser().getRole().equals("admin")
                || AuthUser.getUser().getRole().equals("collaborator")
                || AuthUser.getUser().getRole().equals("voter"))
                && rbStars.isEnabled()){
            rbStars.setVisibility(View.VISIBLE);
        }else if(rbStars.getVisibility() != View.GONE) rbStars.setVisibility(View.GONE);
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
}