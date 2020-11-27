package com.example.llajtacomida.models.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.RatingInterface;
import com.example.llajtacomida.interfaces.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * La clase solo virve para obtener os datos del usuario y mostrarlos en comentarios por ejemplo
 */
public class UserModel implements UserInterface.ModelInterface, ValueEventListener {

    private UserInterface.PresenterInterface presenterInterface;
    private String verb;
    private DatabaseReference databaseReference;
    private User user;

    public UserModel(UserInterface.PresenterInterface presenterInterface){
        this.presenterInterface = presenterInterface;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void findUser(String id) {
        verb = "find";
        databaseReference.child("App").child("users").child(id);
        databaseReference.addListenerForSingleValueEvent(this);
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
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(verb.equals("store")){
            databaseReference.child("App").child("users").child(user.getId()).updateChildren(user.toMap());
        }else if(verb.equals("find")){
            presenterInterface.showUser(snapshot.getValue(User.class));
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("Error", "--------------------------------------------------------> "+error.getMessage());
    }

    public void stopRealtimedatabase(){
        databaseReference.removeEventListener(this);
    }
}
