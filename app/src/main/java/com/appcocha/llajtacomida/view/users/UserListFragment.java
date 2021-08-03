package com.appcocha.llajtacomida.view.users;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.user.ArrayAdapterUser;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.appcocha.llajtacomida.presenter.user.UserNavegation;
import com.appcocha.llajtacomida.presenter.user.UserPresenter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Vista, Muestra y gestioma roles de los usuairos
 */
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

    private MenuItem iconSearch;

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
        Sound.playClick();
        // Inflate the layout for this fragment
        setHasOptionsMenu(true); // para el funcionamiento de los iconos
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        userList = new ArrayList<User>();
//        userPresenter = new UserPresenter(this);
//        userPresenter.loadUserList();

        initComponents();
        return view;
    }

    /**
     * Inicializa los componentes
     */
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
        tvEmail.setOnClickListener(this);
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
                Sound.playClick();
                user = userList.get(position);
                Glide.with(getContext()).load(user.getAvatarUrl()).into(ivAvatar);
                tvId.setText(user.getId());
                tvFulName.setText(user.getFulName());
                tvEmail.setText(user.getEmail());
                tvRole.setText(getTraslateRole(user.getRole()));
                alertDialog.show();
                loadRoleInRadioButtons(user);
            }
        });
    }

    /**
     * Traduce los roles
     * @param role (en inglés)
     * @return role (idioma del dispositivo)
     */
    private String getTraslateRole(String role){
        if(role.equalsIgnoreCase("admin")){
            role = getString(R.string.role_admin);
        }else if(role.equalsIgnoreCase("collaborator")){
            role = getString(R.string.role_collaborator);
        }else if(role.equalsIgnoreCase("voter")){
            role = getString(R.string.role_voter);
        }else if(role.equalsIgnoreCase("reader")){
            role = getString(R.string.role_reader);
        }else if(role.equalsIgnoreCase("none")){
            role = getString(R.string.role_none);
        }
        return role;
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
            if(!etSearch.getText().toString().isEmpty() && etSearch.getVisibility() == View.VISIBLE)
                arrayAdapterUser.filter(etSearch.getText().toString(), etSearch.getText().toString().length()-1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        if(isSuccess)
            Toast.makeText(getContext(), getContext().getString(R.string.message_processed_correct), Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), getContext().getString(R.string.message_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Sound.playClick();
        switch (v.getId()){
            case R.id.btnCancel:
                alertDialog.dismiss();
                break;
            case R.id.btnSave:
                if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.UPDATE_USER_ROL)){
                    changeUser();
                    userPresenter.storeUser(user);
                    alertDialog.dismiss();
                }else Toast.makeText(getContext(), getContext().getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvEmail:
                UserNavegation.openMail(getContext(), tvEmail.getText().toString());
                break;
        }
    }

    /**
     * Cambia el rol en el objeot user
     */
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
    }

    /**
     * Carga el rol del usuario
     * @param user
     */
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


    /**
     * Inicializa los íconos del del toolbar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        iconSearch = (MenuItem) menu.findItem(R.id.iconSearch);
        iconSearch.setVisible(true);
    }

    /**
     * Este método es el oyente de las acciones de los íconos
     * @param item
     * @return onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconSearch:
                if (etSearch.getVisibility() == View.GONE && userList != null) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.setText(null);
                    etSearch.setFocusable(true);
                    etSearch.requestFocus();
                }else {
                    etSearch.setVisibility(View.GONE);
                    // Para que vuelga a cargar la lista (0 es cualquier numero)
//                    if(arrayAdapterPlate != null) arrayAdapterPlate.filter("", 0);
                    if(arrayAdapterUser != null) arrayAdapterUser.filter("", 0);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Ciclos de vida para el reinicio de los presentadores\

    @Override
    public void onResume() {
        super.onResume();
        userPresenter = new UserPresenter(this);
        if(Permission.getAuthorize(AuthUser.user.getRole(), Permission.SHOW_USER_LIST)) userPresenter.loadUserList();
        else Toast.makeText(getContext(), getContext().getString(R.string.access_denied_message), Toast.LENGTH_SHORT).show();
        Log.d("cicleLive", "onResume");
    }

    @Override
    public void onPause() {
        userPresenter.stopRealtimeDatabase();
        super.onPause();
    }
}