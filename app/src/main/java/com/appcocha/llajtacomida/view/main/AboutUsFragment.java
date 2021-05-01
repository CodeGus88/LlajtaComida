package com.appcocha.llajtacomida.view.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.UserNavegation;

/**
 * Vista, Sobre nosotros
 */
public class AboutUsFragment extends Fragment {

    private ImageView ivIconImage;
    private View view;
    private TextView tvEmail;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Sound.playClick();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initComponents();
        return view;
    }

    /**
     * Inicializa los componentes
     */
    private void initComponents(){
        ivIconImage = (ImageView) view.findViewById(R.id.ivIconImage);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.95);
        ivIconImage.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sound.playClick();
                UserNavegation.openMail(getContext(), getString(R.string.mail).trim());
            }
        });
    }
}