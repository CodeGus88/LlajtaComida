package com.appcocha.llajtacomida.presenters.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class UserNavegation {

    public static void openMail(Context context, String email){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
        context.startActivity(Intent.createChooser(intent, "Llajta Comida"));
    }

}
