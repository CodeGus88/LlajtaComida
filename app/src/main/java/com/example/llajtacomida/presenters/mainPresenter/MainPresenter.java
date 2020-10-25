package com.example.llajtacomida.presenters.mainPresenter;

import android.content.Context;
import android.content.Intent;
import com.example.llajtacomida.MainActivity;
import com.example.llajtacomida.views.mainViews.LoginActivity;

public class MainPresenter {


    public static void showLogin(Context context){

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void accessToApp(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
