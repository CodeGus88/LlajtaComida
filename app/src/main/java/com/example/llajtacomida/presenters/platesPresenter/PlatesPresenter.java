package com.example.llajtacomida.presenters.platesPresenter;

        import android.content.Context;
        import android.content.Intent;

        import com.example.llajtacomida.views.platesViews.CreatePlateActivity;

public class PlatesPresenter {

    public static void showCreatedPlateView(Context context){
        Intent intent = new Intent(context, CreatePlateActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
