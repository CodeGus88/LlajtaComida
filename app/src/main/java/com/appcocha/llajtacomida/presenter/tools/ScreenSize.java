package com.appcocha.llajtacomida.presenter.tools;

import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Funciona solo para listas que tienen un tamaño estandar de filas
 */
public class ScreenSize {

    /**
     * Obtiene el ancho de la pantalla
     * @param display
     * @return
     */
    public static int getWidth(Display display){
        int width;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {  // > API 12
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width   = display.getWidth();
        }
        return  width;
    }

    /**
     * obtiene el alto de la pantalla
     * @param display
     * @return
     */
    public static int getHeight(Display display){
        int height;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {  // > API 12
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height  = display.getHeight();
        }
        return height;
    }

    /**
     * Este método sirve para adaptar el listView al tamaño  de su contenido (Conflicto listView dentro de un scroll)
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
