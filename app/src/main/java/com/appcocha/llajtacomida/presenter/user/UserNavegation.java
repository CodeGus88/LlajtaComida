package com.appcocha.llajtacomida.presenter.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
        context.startActivity(Intent.createChooser(intent, "Llajta Comida"));
    }

}
