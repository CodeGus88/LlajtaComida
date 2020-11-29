package com.example.llajtacomida.presenters.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.llajtacomida.R;
import com.example.llajtacomida.models.user.User;

import java.util.ArrayList;
import java.util.Hashtable;

public class ArrayAdapterRating extends ArrayAdapter<Object> {

    private final Context context;
    private final ArrayList<Object> votesList;
    private final int resource;
    private final ArrayList<User> userList;

    /**
     * @param context
     * @param resource
     * @param voteList {/user/{user_id/puntuation/experience/date}/}
     */
    public ArrayAdapterRating(@NonNull Context context, @NonNull int resource, @NonNull ArrayList<Object> voteList, ArrayList<User> userList) { //@NonNull ArrayList<Hashtable<String, String>> votesList, ArrayList<User> userList) {
        super(context, resource,  voteList);
        this.context = context;
        this.votesList = voteList;
        this.resource = resource;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(resource, null);
        }

        ImageView ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        TextView tvFulName = (TextView) view.findViewById(R.id.tvFulName);
        RatingBar rbUserVote = (RatingBar) view.findViewById(R.id.rvUserVote);
        final TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        final TextView tvExperience = (TextView) view.findViewById(R.id.tvExperience);

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
}
