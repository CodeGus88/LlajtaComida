package com.example.llajtacomida.models.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * La clase solo virve para obtener os datos del usuario y mostrarlos en comentarios por ejemplo
 */
public class UserModel implements UserInterface.ModelUser, ValueEventListener {

    private UserInterface.PresenterUser presenterUser;
    private String verb;
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
        verb = "find";
        databaseReference.child("App").child("users").child(id).addListenerForSingleValueEvent(this);
    }

    /**
     * Este método guarda o actualiza los datos del usuario en firebase
     * @param user
     */
    @Override
    public void storeUser(User user) {
        verb = "store";
        this.user = user;
        databaseReference.child("App").child("users").child(this.user.getId());
        databaseReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void loadUserList() {
        databaseReference.child("App").child("users").addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getRef().toString().equals("https://llajtacomida-f137b.firebaseio.com/App/users")){
            userList.clear();
            for(DataSnapshot data : snapshot.getChildren()){
                userList.add(data.getValue(User.class));
            }
            presenterUser.showUserList(userList);
        }else{
            if(verb.equals("store")){
                databaseReference.child("App").child("users").child(user.getId()).updateChildren(user.toMap());
            }else if(verb.equals("find")){
                if(snapshot.getValue() != null){
                    user = snapshot.getValue(User.class);
                }else{
                    user = new User();
                }
                presenterUser.showUser(user);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", "--------------------------------------------------------> "+error.getMessage());
    }

}
