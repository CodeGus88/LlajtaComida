package com.example.llajtacomida.views.rating;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.llajtacomida.R;
import com.example.llajtacomida.presenters.rating.RatingPresenter;
import com.google.firebase.auth.FirebaseAuth;

public class RatingRecordFragment extends Fragment implements View.OnClickListener {

    // Components
    private View view;
    private RatingBar rbStars;
    private EditText etExperience;
    private AlertDialog alertDialog;
    private Button btnCancel;
    private Button btnSave;

    // Data
//    private String userId;
    private String objectId;
    private String nodeCollectionName;

    // Presentador
    private RatingPresenter ratingPresenter;

    public RatingRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_rating_record, container, false);
//        userId = FirebaseAuth.getInstance().getUid();
        objectId = this.getArguments().getString("objectId");
        nodeCollectionName = this.getArguments().getString("nodeCollectionName");
        initComponents();

        // iniciar presentador
        ratingPresenter = new RatingPresenter(nodeCollectionName, objectId);
        return view;
    }

    private void initComponents(){
        rbStars = (RatingBar) view.findViewById(R.id.rbStars);
        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                saveVote();
            }
        });
        LayerDrawable stars = (LayerDrawable) rbStars.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP); // Estrellas
        stars.getDrawable(1).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP); // Sombras


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
                Toast.makeText(getContext(), getString(R.string.messageEstablish), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                break;
            default:
                Toast.makeText(getContext(), getString(R.string.messageInvalidOption), Toast.LENGTH_SHORT).show();
        }
    }
}