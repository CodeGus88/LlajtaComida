package com.appcocha.llajtacomida.presenter.main;
import android.content.Context;
import android.content.Intent;

import com.appcocha.llajtacomida.view.main.MainActivity;
import com.appcocha.llajtacomida.view.users.LoginActivity;

/**
 * Navegación del Main
 */
public class MainNavigation {

    /**
     * Muestra la vista del logín para el inicio de sesión
     * @param context
     */
    public static void showLogin(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Muestra la vista del home en la aplicación (inicia sesión)
     * @param context
     */
    public static void accessToApp(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
