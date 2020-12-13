package com.example.llajtacomida.views.users;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.interfaces.UserInterface;
import com.example.llajtacomida.models.user.User;
import com.example.llajtacomida.presenters.user.ArrayAdapterUser;
import com.example.llajtacomida.presenters.user.UserPresenter;

import java.util.ArrayList;

public class UserListFragment extends Fragment implements UserInterface.ViewUser, View.OnClickListener {

    View view;

    // Components
    private EditText etSearch;
    private ListView lvUserList;
    // alert
    private AlertDialog alertDialog;
    private ImageView ivAvatar;
    private TextView tvId;
    private TextView tvFulName;
    private TextView tvEmail;
    private TextView tvRole;
    private RadioButton rbIsAdmin;
    private RadioButton rbIsCollaborator;
    private RadioButton rbIsVoter;
    private RadioButton rbIsReader;
    private RadioButton rbIsNone;
    private Button btnSave;
    private Button btnCancel;

    // Adaptador
    private ArrayAdapterUser arrayAdapterUser;
    private ArrayList<User> userList;

    // presenter
    private UserPresenter userPresenter;

    private User user; // user for save changes

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        userList = new ArrayList<User>();
        userPresenter = new UserPresenter(this);
        userPresenter.loadUserList();

        initComponents();
        return view;
    }

    private void initComponents(){
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvUserList = (ListView) view.findViewById(R.id.lvUserList);

        View viewAlert = getLayoutInflater().inflate(R.layout.alert_user_options, null);

        ivAvatar = (ImageView) viewAlert.findViewById(R.id.ivAvatar);
        tvId = (TextView) viewAlert.findViewById(R.id.tvId);
        tvFulName = (TextView) viewAlert.findViewById(R.id.tvFulName);
        tvEmail = (TextView) viewAlert.findViewById(R.id.tvEmail);
        tvRole = (TextView) viewAlert.findViewById(R.id.tvRole);
        rbIsAdmin = (RadioButton) viewAlert.findViewById(R.id.rbIsAdmin);
        rbIsCollaborator = (RadioButton) viewAlert.findViewById(R.id.rbIsCollaborator);
        rbIsVoter = (RadioButton) viewAlert.findViewById(R.id.rbIsVoter);
        rbIsReader = (RadioButton) viewAlert.findViewById(R.id.rbIsReader);
        rbIsNone = (RadioButton) viewAlert.findViewById(R.id.rbIsNone);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewAlert);
        alertDialog = builder.create();
        // botones del alert
        btnCancel = (Button) viewAlert.findViewById(R.id.btnCancel);
        btnSave = (Button) viewAlert.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);


        etSearch.addTextChangedListener(new TextWatcher() { // para buscar mientras se escribe
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    arrayAdapterUser.filter(s.toString(), start);
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user = userList.get(position);
                Glide.with(getContext()).load(user.getAvatarUrl()).into(ivAvatar);
                tvId.setText(user.getId());
                tvFulName.setText(user.getFulName());
                tvEmail.setText(user.getEmail());
                tvRole.setText(user.getRole());
                alertDialog.show();
                loadRoleInRadioButtons(user);
            }
        });
    }

    @Override
    public void showUser(User user) {
        // no se usa en este caso
    }

    @Override
    public void showUserList(ArrayList<User> userList) {
        try {
            this.userList = userList;
            arrayAdapterUser = new ArrayAdapterUser(getContext(), R.layout.adapter_user_list, userList);
            lvUserList.setAdapter(arrayAdapterUser);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                alertDialog.dismiss();
                break;
            case R.id.btnSave:
                changeUser();
                userPresenter.storeUser(user);
                alertDialog.dismiss();
                break;
        }
    }

    private void changeUser(){
        if(rbIsAdmin.isChecked()){
            user.setRole("admin");
        }else if(rbIsCollaborator.isChecked()){
            user.setRole("collaborator");
        }else if(rbIsVoter.isChecked()){
            user.setRole("voter");
        } else if(rbIsReader.isChecked()){
            user.setRole("reader");
        }else if(rbIsNone.isChecked()){
            user.setRole("none");
        }else{ // Si  no existe por defecto none
            user.setRole("none");
        }
//        return user;
    }

    private void loadRoleInRadioButtons(User user){
        if(user.getRole().equalsIgnoreCase("admin") && !rbIsAdmin.isChecked()){
            rbIsAdmin.setChecked(true);
        }else if(user.getRole().equalsIgnoreCase("collaborator") && !rbIsCollaborator.isChecked()){
            rbIsCollaborator.setChecked(true);
        }else if(user.getRole().equalsIgnoreCase("voter") && !rbIsVoter.isChecked()){
            rbIsVoter.setChecked(true);
        }else if(user.getRole().equalsIgnoreCase("reader") && !rbIsReader.isChecked()){
            rbIsReader.setChecked(true);
        }else if(user.getRole().equalsIgnoreCase("none") && !rbIsNone.isChecked()){
            rbIsNone.setChecked(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        userPresenter.stopRealtimeDatabase();
    }
}