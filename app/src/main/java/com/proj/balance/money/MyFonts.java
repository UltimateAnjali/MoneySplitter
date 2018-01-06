package com.proj.balance.money;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by anjali desai on 08-10-2017.
 */

public class MyFonts {

    Context context;
    Typeface myType;

    public MyFonts(Context mContext){
        context = mContext;
    }

    public Typeface getMerri(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Merriweather-Regular.ttf");
        return myType;
    }

    public Typeface getMont(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Regular.ttf");
        return myType;
    }

    public Typeface getVolk(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Volkhov-Regular.ttf");
        return myType;
    }
}
