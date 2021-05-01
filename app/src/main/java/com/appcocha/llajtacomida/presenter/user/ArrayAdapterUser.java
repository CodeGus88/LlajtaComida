package com.appcocha.llajtacomida.presenter.user;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.user.User;
import java.util.ArrayList;

/**
 * Adaptador, usuarios
 */
public class ArrayAdapterUser extends ArrayAdapter<User> {

    private final int RESOURCE;
    private final Context CONTEXT;
    private ArrayList<User> userList;
    private ArrayList<User> userListCopy;

    /**
     * Constructor, inicializa context, resource y userList
     * @param context
     * @param resource
     * @param userList
     */
    public ArrayAdapterUser(@NonNull Context context, int resource, ArrayList<User> userList) {
        super(context, resource, userList);
        CONTEXT = context;
        this.RESOURCE = resource;
        this.userList = userList;
        userListCopy = new ArrayList<User>();
        userListCopy.addAll(userList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(CONTEXT).inflate(RESOURCE, null);
        }
        try {
            ImageView ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            TextView tvFulName = (TextView) view.findViewById(R.id.tvFulName);
            TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            TextView tvRole = (TextView) view.findViewById(R.id.tvRole);
            TextView tvId = (TextView) view.findViewById(R.id.tvId);
            Glide.with(CONTEXT).load(userList.get(position).getAvatarUrl()).into(ivAvatar);
            tvFulName.setText(userList.get(position).getFulName());
            tvEmail.setText(userList.get(position).getEmail());
            tvRole.setText(getTraslateRole(userList.get(position).getRole()));
            tvId.setText(userList.get(position).getId());
            if(userList.get(position).getRole().equalsIgnoreCase("admin")){
                tvRole.setTextColor(Color.YELLOW);
            }else if(userList.get(position).getRole().equalsIgnoreCase("collaborator")){
                tvRole.setTextColor(Color.GREEN);
            }else if(userList.get(position).getRole().equalsIgnoreCase("voter")){
                tvRole.setTextColor(Color.CYAN);
            }else if(userList.get(position).getRole().equalsIgnoreCase("reader")){
                tvRole.setTextColor(Color.LTGRAY);
            }else if(userList.get(position).getRole().equalsIgnoreCase("none")){
                tvRole.setTextColor(Color.RED);
            }

        }catch(Exception e){
            Log.e("Error: ", e.getMessage());
        }
        return view;
    }

    /** Filtra los datos del adaptador */
    public void filter(String texto, int previousLentg) {
        texto = texto.toLowerCase();
        if(!texto.isEmpty()) {
            if(texto.length() == previousLentg){ // Si se presionó borrar
                userList.clear();
                userList.addAll(userListCopy);
            }
            search(texto);
        }else if(userList.size() != userListCopy.size()){
            if(userList.size() > 0){
                userList.clear();
            }
            userList.addAll(userListCopy);
        }
        notifyDataSetChanged();
    }

    /**Filtra el texto en el toString del objeto n*/
    public void search(String texto){
        int i = 0;
        while (i < userList.size()) {
            String string = getTraslateRole(userList.get(i).getRole()).toLowerCase() +  " " + userList.get(i).toString().toLowerCase();
            if (!string.contains(texto)) {
                userList.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * traduce los terminos de los permisos para mostrar
     * @param role
     * @return role
     */
    private String getTraslateRole(String role){
        if(role.equalsIgnoreCase("admin")){
            role = CONTEXT.getString(R.string.role_admin);
        }else if(role.equalsIgnoreCase("collaborator")){
            role = CONTEXT.getString(R.string.role_collaborator);
        }else if(role.equalsIgnoreCase("voter")){
            role = CONTEXT.getString(R.string.role_voter);
        }else if(role.equalsIgnoreCase("reader")){
            role = CONTEXT.getString(R.string.role_reader);
        }else if(role.equalsIgnoreCase("none")){
            role = CONTEXT.getString(R.string.role_none);
        }
        return  role;
    }
}
