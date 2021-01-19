package com.appcocha.llajtacomida.presenters.plate;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import com.appcocha.llajtacomida.R;
    import com.appcocha.llajtacomida.models.plate.Plate;
    import com.appcocha.llajtacomida.views.images.ImagesActivity;
    import com.appcocha.llajtacomida.views.plates.CreatePlateActivity;
    import com.appcocha.llajtacomida.views.plates.EditPlateActivity;
    import com.appcocha.llajtacomida.views.plates.PlateViewActivity;
    import com.theartofdev.edmodo.cropper.CropImage;
    import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Navegación de los platos
 */
public class PlateNavegation {

    /**
     * Muestra el formulario para crear un nuevo plato
     * @param context
     */
    public static void showCreatedPlateView(Context context) {
        Intent intent = new Intent(context, CreatePlateActivity.class);
        context.startActivity(intent);
    }

    /**
     * Muestra el formulario para editar un plato
     * @param context
     * @param plate
     */
    public static void showEditPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, EditPlateActivity.class);
        intent.putExtra("plate", plate);
        context.startActivity(intent);
    }

    /**
     * Muestra un plato
     * @param context
     * @param plate
     */
    public static void showPlateView(Context context, Plate plate) {
        Intent intent = new Intent(context, PlateViewActivity.class);
        intent.putExtra("id", plate.getId());
        context.startActivity(intent);
    }

    /**
     * Muestra el selecctor y recortador de imágenes
     * @param context
     */
    public static void showCropImage(Activity context) {
        CropImage.activity()
                .setActivityTitle(context.getString(R.string.plate_image_crop_title))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(1023, 700)
                .setAspectRatio(3, 2)
                .start(context);
    }

    /**
     * Muestra la galería de imágenes (gestor de imágenes)
     * @param context
     * @param parentId
     * @param parentName
     */
    public static void showGalery(Context context, String parentId,  String parentName){
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("nodeCollectionName", "plates"); // Para saber a que entidad pertenece
        intent.putExtra("parentName", parentName); // El nombre el plato
        intent.putExtra("parentId", parentId);
        context.startActivity(intent);
    }
}
