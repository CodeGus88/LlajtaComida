package com.example.llajtacomida.models.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.llajtacomida.interfaces.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRealTimeModel implements  UserInterface.ModelUserRealTime, ValueEventListener {

    private UserInterface.PresenterUserRealTime presenterUserRealTime;
    private DatabaseReference databaseReference;

    public UserRealTimeModel(UserInterface.PresenterUserRealTime presenterUserRealTime){
        this.presenterUserRealTime = presenterUserRealTime;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void findUser(String userId) {
        databaseReference.child("App").child("users").child(userId).addValueEventListener(this);
    }

    @Override
    public void stopRealtimeDatabase() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot != null){
            presenterUserRealTime.showUserRT(snapshot.getValue(User.class));
        }else{
            presenterUserRealTime.showUserRT(null);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.d("Cancel:", error.getMessage());
    }
}
