package com.example.llajtacomida.presenters.platesPresenter;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;

        import com.example.llajtacomida.models.Plate;
        import com.example.llajtacomida.views.platesViews.CreatePlateActivity;
        import com.example.llajtacomida.views.platesViews.EditPlateActivity;
        import com.example.llajtacomida.views.platesViews.PlateViewActivity;
        import com.theartofdev.edmodo.cropper.CropImage;
        import com.theartofdev.edmodo.cropper.CropImageView;

public class PlatesPresenter {

    public static void showCreatedPlateView(Context context) {
        Intent intent = new Intent(context, CreatePlateActivity.class);
        context.startActivity(intent);
    }

    public static void showEditPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, EditPlateActivity.class);
        intent.putExtra("plate", plate);
        context.startActivity(intent);
    }

    public static void showPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, PlateViewActivity.class);
        intent.putExtra("id", plate.getId());
        context.startActivity(intent);
    }

    public static void showCropImage(Activity context) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(1023, 700)
//                .setRequestedSize(640, 640)
                .setAspectRatio(3, 2)
                .start(context);
    }
}
