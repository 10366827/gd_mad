package com.garydty.modernartui;

import android.graphics.Color;
import android.widget.FrameLayout;

/**
 * Created by Gary Doherty on 30/08/2017.
 */
//Color color = Color.parse("#AARRGGBB");
//String hexValue = Integer.toHexString(255);

class EpicFrameLayout {
    private int initRed, finRed, initGreen, finGreen, initBlue, finBlue;
    private int initialColour, finalColour;
    private FrameLayout frameLayout;

    EpicFrameLayout(FrameLayout _frameLayout, int _initialColour, int _finalColour) {
        frameLayout = _frameLayout;
//        if(frameLayout == null) {
//            Log.i("EpicFrameLayout", "layout passed to Epic was null");
//            return;
//        }
//
//        if(((ColorDrawable) frameLayout.getBackground()) == null){
//            Log.i("EpicFrameLayout", "ColorDrawable NULLLLLLLLLLL");
//            return;
//        }

//        Drawable background = frameLayout.getBackground();
//        initialColour = ((ColorDrawable) background).getColor();
        initialColour = _initialColour;
        finalColour = _finalColour;

        initRed = Color.red(initialColour);
        initGreen = Color.green(initialColour);
        initBlue = Color.blue(initialColour);

        finRed = Color.red(finalColour);
        finGreen = Color.green(finalColour);
        finBlue = Color.blue(finalColour);

        frameLayout.setBackgroundColor(initialColour);
    }

    void updateColour(int percentage){
        int redVal = (int) (initRed + ((finRed - initRed) * (percentage / 100.0)));
        int greenVal = (int) (initGreen + ((finGreen - initGreen ) * (percentage / 100.0)));
        int blueVal = (int) (initBlue + ((finBlue - initBlue) * (percentage / 100.0)));

        frameLayout.setBackgroundColor(Color.rgb(redVal, greenVal, blueVal));
    }
}
