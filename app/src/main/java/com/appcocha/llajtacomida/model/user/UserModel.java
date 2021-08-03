package com.appcocha.llajtacomida.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.presenter.tools.StringValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Modelo
 * La clase solo virve para obtener os datos del usuario y mostrarlos en comentarios por ejemplo
 */
public class UserModel implements UserInterface.ModelUser, ValueEventListener {

    private UserInterface.PresenterUser presenterUser;
    private DatabaseReference databaseReference;
    private User user;
    private ArrayList<User> userList;

    public UserModel(UserInterface.PresenterUser presenterUser){
        this.presenterUser = presenterUser;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userList = new ArrayList<User>();
    }

    @Override
    public void findUser(String id) {
        databaseReference.child("App").child("users").child(id).addListenerForSingleValueEvent(this);
    }

    /**
     * Este método guarda o actualiza los datos del usuario en firebase
     * @param user
     */
    @Override
    public void storeUser(User user) {
        try{
            if(!user.getId().equals("")){
                this.user = user;
                databaseReference.child("App").child("users").child(this.user.getId()).updateChildren(this.user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        presenterUser.isSuccess(task.isSuccessful());
                    }
                });
            }else{
                Log.e("Error", "Not found user id");
                presenterUser.isSuccess(false);
            }
        }catch(Exception e){
            Log.e("Error", e.getMessage());
            presenterUser.isSuccess(false);
        }
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void loadUserList() {
        databaseReference.child("App").child("users").orderByChild("fulName").addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users")){
        if(snapshot.getRef().toString().equals(StringValues.getDBURL() +"/App/users")){
            userList.clear();
            for(DataSnapshot data : snapshot.getChildren()){
                userList.add(data.getValue(User.class));
            }
            presenterUser.showUserList(userList);
        }else{ // si es buscar un usaurio
            if(snapshot.getValue() != null){
                user = snapshot.getValue(User.class);
            }else{
                user = new User();
            }
            presenterUser.showUser(user);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", "--------------------------------------------------------> "+error.getMessage());
    }
}
