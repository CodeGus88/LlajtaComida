package com.example.llajtacomida.views.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.UserInterface;
import com.example.llajtacomida.models.user.User;
import com.example.llajtacomida.presenters.tools.Validation;
import com.example.llajtacomida.presenters.user.UserPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UserInterface.ViewUser {

    private View root;
    private TextView tvWelcome;

    // Presenter
    private UserPresenter userPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        userPresenter = new UserPresenter(this);
        userPresenter.findUser(FirebaseAuth.getInstance().getUid());
        initComponets();
        return root;
    }

    private void initComponets(){
        tvWelcome = (TextView) root.findViewById(R.id.tvWelcome);
    }

    @Override
    public void showUser(User user) {
        tvWelcome.setText(tvWelcome.getText().toString() + " "+ Validation.getFirstName(user.getFulName()) +
                getString(R.string.tvWelcomeContinue));
    }

    @Override
    public void showUserList(ArrayList<User> userList) {

    }
}