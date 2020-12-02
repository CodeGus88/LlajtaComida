package com.example.llajtacomida.presenters.rating;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.plate.PlateGestorDB;
import com.example.llajtacomida.models.rating.RatingModel;
import com.example.llajtacomida.models.user.User;
import com.example.llajtacomida.presenters.image.GaleryDatabase;
import com.example.llajtacomida.presenters.user.UserPresenter;
import com.example.llajtacomida.views.platesViews.PlateViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

public class ArrayAdapterRating extends ArrayAdapter<Object> {

    private final Context context;
    private final ArrayList<Object> votesList;
    private final int resource;
    private final ArrayList<User> userList;
    private final String nodeCollectionName, objectId;

    /**
     * @param context
     * @param resource
     * @param voteList {/user/{user_id/puntuation/experience/date}/}
     */
    public ArrayAdapterRating(@NonNull Context context, @NonNull int resource, @NonNull ArrayList<Object> voteList, ArrayList<User> userList,
                              String nodeCollectionName, String objectId) {
        super(context, resource,  voteList);
        this.context = context;
        this.votesList = voteList;
        this.resource = resource;
        this.userList = userList;
        this.nodeCollectionName = nodeCollectionName;
        this.objectId = objectId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(resource, null);
        }
        final ImageView ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        final TextView tvFulName = (TextView) view.findViewById(R.id.tvFulName);
        final RatingBar rbUserVote = (RatingBar) view.findViewById(R.id.rvUserVote);
        final TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        final TextView tvExperience = (TextView) view.findViewById(R.id.tvExperience);
        final ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        Hashtable<String, String> row = (Hashtable<String, String>)votesList.get(position);
        try {
            // obtener la primera fila de objetos que es un array (user, arrayListVotos)
            User user  =   getUser(row.get("user_id")); // el primer elemento es el objeto de tipo User
            if(!user.getEmail().isEmpty()){
                Glide.with(context).load(user.getAvatarUrl()).into(ivAvatar);
            }
            tvFulName.setText(user.getFulName());
            rbUserVote.setRating(Float.parseFloat(row.get("punctuation")));
            tvDate.setText(row.get("date"));
            tvExperience.setText(row.get("experience"));
            if(user.getId().equals(FirebaseAuth.getInstance().getUid()) && true){ // Solo si es el autor u el administrador
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteVote();
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return view; //super.getView(position, convertView, parent);
    }

    /**
     * Buscxa en la lista de usuarios
     * @param userId
     * @return
     */
    private User getUser(String userId){
        User user = new User(); // Usuario nuevo  temporal
        for (User usr: userList) {
            if(usr.getId().equals(userId)){
                user = usr;
                break;
            }
        }
        return user;
    }

    /**
     * elimina la fila de calificación del usuario
     */
    private void deleteVote(){
        androidx.appcompat.app.AlertDialog.Builder confirm = new AlertDialog.Builder(context);
        confirm.setTitle("Confimación");
        confirm.setMessage("¿Quitar la calificación?");
        confirm.setCancelable(false);
        confirm.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(context, "Quitar...", Toast.LENGTH_SHORT).show();
                RatingModel ratingModel = new RatingModel(nodeCollectionName, objectId);
                ratingModel.deleteVote(FirebaseAuth.getInstance().getUid());
            }
        });
        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        confirm.show();
    }
}
