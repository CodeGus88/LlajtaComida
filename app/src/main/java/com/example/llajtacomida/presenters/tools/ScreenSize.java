package com.example.llajtacomida.presenters.tools;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

public class ScreenSize {

    public static int getWidth(Display display){
        int width;
//        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {  // > API 12
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width   = display.getWidth();
        }
        return  width;
    }

    public static int getHeight(Display display){
        int height;
//        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {  // > API 12
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height  = display.getHeight();
        }
        return height;
    }
}
