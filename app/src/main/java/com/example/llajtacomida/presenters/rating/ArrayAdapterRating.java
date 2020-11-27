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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

public class ArrayAdapterRating extends ArrayAdapter<Object> {

    private final Context context;
    private final ArrayList<Object> objects;
    private final int resource;

    /**
     * @param context
     * @param resource
     * @param objects {/user/{user_id/puntuation/experience/date}/}
     */
    public ArrayAdapterRating(@NonNull Context context, int resource, @NonNull ArrayList <Object> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
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
        RatingBar rvUserVote = (RatingBar) view.findViewById(R.id.rvUserVote);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvExperience = (TextView) view.findViewById(R.id.tvExperience);

        // obtener la primera fila de objetos que es un array (user, arrayListVotos)
        ArrayList<Object> data = (ArrayList<Object>)objects.get(position);
        User user = (User) data.get(0); // el primer elemento es el objeto de tipo User
        Hashtable<String, String> userVote = (Hashtable<String, String>) data.get(1);

        Glide.with(context).load(user.getAvatarUrl());
//        {user_id/puntuation/experience/date}
        tvFulName.setText(user.getFulName());
        rvUserVote.setRating(Float.parseFloat(userVote.get("value")));
        tvDate.setText(userVote.get("date"));
        tvExperience.setText(userVote.get("experience"));

        return view; //super.getView(position, convertView, parent);
    }
}
