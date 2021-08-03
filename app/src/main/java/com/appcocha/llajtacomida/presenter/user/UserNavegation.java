package com.appcocha.llajtacomida.presenter.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.appcocha.llajtacomida.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

/**
 * Navegación de usuario
 */
public class UserNavegation {

    /**
     * Abre Gmail para regactar un nuevo email
     * @param context
     * @param email
     */
    public static void openMail(Context context, String email){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));
        Animatoo.animateFade(context); //Animación al cambiar de actividad
    }

}
