package com.appcocha.llajtacomida.presenter.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.appcocha.llajtacomida.model.image.Image;
import java.util.ArrayList;

/**
 * Presentador, gestor de imágenes
 */
public class ArrayAdapterImagesGalery extends BaseAdapter {

    private Context context;
    private ArrayList<Image> galery;
    private int x, y;

    public ArrayAdapterImagesGalery(Context context, ArrayList<Image> galery, int x, int y){
        this.context = context;
        this.galery = galery;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getCount() {
        return galery.size();
    }

    @Override
    public Object getItem(int position) {
        return galery.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getItemIdImage(int position){
        return galery.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img = new ImageView(context);
        Glide.with(context).load(galery.get(position).getUrl()).into(img);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams( // Tamaño de la imagen
                new ViewGroup.LayoutParams(x, y)
        );
        return img;
    }

    public Image getImage(int position) {
        return galery.get(position);
    }
}
