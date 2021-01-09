package com.appcocha.llajtacomida.views.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.models.user.User;
import com.appcocha.llajtacomida.presenters.tools.Validation;
import com.appcocha.llajtacomida.presenters.user.UserPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UserInterface.ViewUser {

    private View root;
    private TextView tvWelcome;
    private LinearLayout llfontTitle;
    // animation
    private ObjectAnimator animatorY;

    // Presenter
    private UserPresenter userPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        userPresenter = new UserPresenter(this);
        if(FirebaseAuth.getInstance().getUid() != null){
            userPresenter.findUser(FirebaseAuth.getInstance().getUid());
        }else{
            Log.d("Null", "----------------------------> user not fount");
        }
        initComponets();
        animation();
        return root;
    }

    private void initComponets(){
        tvWelcome = (TextView) root.findViewById(R.id.tvWelcome);
        llfontTitle = (LinearLayout) root.findViewById(R.id.llfontTitle);
    }

    private void animation(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorY = ObjectAnimator.ofFloat(llfontTitle, "y", -40F);
        animatorY.setDuration(2000);
        animatorSet.play(animatorY);
        animatorSet.start();
    }

    @Override
    public void showUser(User user) {
        try{
            String title;
            if(user.getFulName() != null){
                title = getString(R.string.tv_welcome) + " " + Validation.getFirstName(user.getFulName()) +
                        getString(R.string.tv_welcome_continue);

            }else{
                title = getString(R.string.tv_welcome) + " " + Validation.getFirstName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()) +
                        getString(R.string.tv_welcome_continue);
            }
            tvWelcome.setText(title);
        }catch (Exception e){
            Log.e("Error", "----------------------------------------------> " + e.getMessage());
        }
    }

    @Override
    public void showUserList(ArrayList<User> userList) {
        // not used
    }
}