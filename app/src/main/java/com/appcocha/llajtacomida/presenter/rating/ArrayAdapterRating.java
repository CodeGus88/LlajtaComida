package com.appcocha.llajtacomida.presenter.rating;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.rating.RatingModel;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.presenter.user.AuthUser;
import com.appcocha.llajtacomida.presenter.user.Permission;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Adaptador, rating
 */
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
        final ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnAlertDelete);
        final Hashtable<String, String> row = (Hashtable<String, String>)votesList.get(position);

        try {
            // obtener la primera fila de objetos que es un array (user, arrayListVotos)
            User user = getUser(row.get("user_id")); // el primer elemento es el objeto de tipo User
            if (!user.getEmail().isEmpty()) {
                Glide.with(context).load(user.getAvatarUrl()).into(ivAvatar);
            }
            tvFulName.setText(user.getFulName());
            rbUserVote.setRating(Float.parseFloat(row.get("punctuation")));
            tvDate.setText(row.get("date"));
            tvExperience.setText(row.get("experience"));
//            if (user.getId().equals(FirebaseAuth.getInstance().getUid()) || AuthUser.getUser().getRole().equals("admin")) { // Solo si es el autor u el administrador
            boolean permission = false;
            if(nodeCollectionName.equals("plates"))
                permission = Permission.getAuthorize(AuthUser.user.getRole(), Permission.DELETE_VALORATE_PLATE, AuthUser.getUser().getId().equals(user.getId()));
            else if(nodeCollectionName.equals("restaurants"))
                permission = Permission.getAuthorize(AuthUser.user.getRole(), Permission.DELETE_VALORATE_RESTAURANT, AuthUser.getUser().getId().equals(user.getId()));
            if (permission){ // Solo si es el autor u el administrador
                btnDelete.setVisibility(View.VISIBLE);
                final boolean finalPermission = permission; // requiere que la variable no se cambiada final
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(finalPermission) // requiere que la variable no se cambiada
                            deleteVote(row.get("user_id"));
                        else Toast.makeText(context, context.getString(R.string.does_not_have_the_permission), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //************************************************************************************
            // init alert
            if (!row.get("experience").isEmpty()) {
                View viewAlert = LayoutInflater.from(context).inflate(R.layout.alert_user_vote_view, null);
                final AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(viewAlert);
                alertDialog = builder.create();
                final ImageView ivAvatarAlert = (ImageView) viewAlert.findViewById(R.id.ivAvatar);
                final TextView tvFulNameAlert = (TextView) viewAlert.findViewById(R.id.tvFulName);
                final RatingBar rbUserVoteAlert = (RatingBar) viewAlert.findViewById(R.id.rvUserVote);
                final TextView tvDateAlert = (TextView) viewAlert.findViewById(R.id.tvDate);
                final TextView tvExperienceAlert = (TextView) viewAlert.findViewById(R.id.tvExperience);
                final Button btnCloseAlert = (Button) viewAlert.findViewById(R.id.btnClose);

                /// alert con el texto completo
                if (!user.getEmail().isEmpty()) {
                    Glide.with(context).load(user.getAvatarUrl()).into(ivAvatarAlert);
                }
                tvFulNameAlert.setText(user.getFulName());
                rbUserVoteAlert.setRating(Float.parseFloat(row.get("punctuation")));
                tvDateAlert.setText(row.get("date"));
                tvExperienceAlert.setText(row.get("experience"));
                tvExperience.setOnClickListener(new View.OnClickListener() { // Muestra el mensaje
                    @Override
                    public void onClick(View v) {
                        alertDialog.show();
                    }
                });
                btnCloseAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
            //*********************************************************************
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
    private void deleteVote(final String voteId){
        androidx.appcompat.app.AlertDialog.Builder confirm = new AlertDialog.Builder(context);
        confirm.setTitle(context.getString(R.string.title_confirmation));
        confirm.setMessage(context.getString(R.string.message_delete_question));
        confirm.setCancelable(false);
        confirm.setPositiveButton(context.getString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(context, context.getString(R.string.message_remove), Toast.LENGTH_SHORT).show();
                RatingModel ratingModel = new RatingModel(nodeCollectionName, objectId);
                ratingModel.deleteVote(voteId);
            }
        });
        confirm.setNegativeButton(context.getString(R.string.btn_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        confirm.show();
    }
}