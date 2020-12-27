package com.appcocha.llajtacomida.presenters.main;
import android.content.Context;
import android.content.Intent;

import com.appcocha.llajtacomida.views.main.MainActivity;
import com.appcocha.llajtacomida.views.main.LoginActivity;

public class MainNavigation {

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

//    public static void restartAplication(Context context){
//        Intent i = context.getPackageManager()
//                .getLaunchIntentForPackage( context.getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//    }

}
