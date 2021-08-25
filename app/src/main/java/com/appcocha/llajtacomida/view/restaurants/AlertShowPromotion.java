package com.appcocha.llajtacomida.view.restaurants;

import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.restaurant.promotion.PromotionElement;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.bumptech.glide.Glide;

public class AlertShowPromotion implements View.OnClickListener {

    private final AlertDialog alertDialog;
    private final ImageView ivPlateImage;
    private final TextView tvPlateName,tvPromotionTitle, tvPromotionDescription, tvBeforePrice, tvNowPrice, tvOrigin, tvIngredients;
    private final Button btnClose, btnShowPlate;
    private final RestaurantViewActivity activity;
    private Plate plate;

    protected AlertShowPromotion(RestaurantViewActivity activity){
        this.activity = activity;
        View viewAlert = activity.getLayoutInflater().inflate(R.layout.alert_show_promotion, null);
        ivPlateImage = (ImageView) viewAlert.findViewById(R.id.ivPlateImage);
        tvPlateName = (TextView) viewAlert.findViewById(R.id.tvPlateName);
        tvPromotionTitle = (TextView) viewAlert.findViewById(R.id.tvPromotionTitle);
        tvPromotionDescription = (TextView) viewAlert.findViewById(R.id.tvDescription);
        tvBeforePrice = (TextView) viewAlert.findViewById(R.id.tvBeforePrice);
        tvNowPrice = (TextView) viewAlert.findViewById(R.id.tvNowPrice);
        tvOrigin = (TextView) viewAlert.findViewById(R.id.tvOrigin);
        tvIngredients = (TextView) viewAlert.findViewById(R.id.tvIngredients);
        btnClose = (Button) viewAlert.findViewById(R.id.btnClose);
        btnShowPlate = (Button) viewAlert.findViewById(R.id.btnShowPlate);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setView(viewAlert);
        alertDialog = builder.create();
        alertDialog.setView(viewAlert);

        btnClose.setOnClickListener(this);
        btnShowPlate.setOnClickListener(this);
        alertDialog.setCancelable(false);
    }

    protected void showPromotion(Plate plate, PromotionElement promotionElement, String oldPrice){
        try {
            this.plate = plate;
            Glide.with(activity).load(plate.getUrl()).into(ivPlateImage);
            tvPlateName.setText(plate.getName());
            tvPromotionTitle.setText(promotionElement.getTitle());
            tvPromotionDescription.setText(promotionElement.getDescription());

            if(!oldPrice.equals("") && promotionElement.isShowOldPrice())
                oldPrice = (oldPrice.replace("_", " " +activity.getString(R.string.type_currency) + ", ")) + " " + activity.getString(R.string.type_currency);
            else oldPrice = "-";
            tvBeforePrice.setText(oldPrice);

            String newPrice = String.valueOf(promotionElement.getPrice())
                    .replace("-1", "")
                    .replace(".0", "")
                    .replace("_", activity.getString(R.string.type_currency)+", ");
            if(!newPrice.equals("")) newPrice += " " + activity.getString(R.string.type_currency);
            else newPrice = "-";
            tvNowPrice.setText(newPrice);
            tvOrigin.setText(plate.getOrigin());
            tvIngredients.setText(plate.getIngredients());
            alertDialog.show();
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                alertDialog.dismiss();
                break;
            case R.id.btnShowPlate:
                alertDialog.dismiss();
                PlateNavegation.showPlateView(activity, plate);
                break;
        }
    }
}