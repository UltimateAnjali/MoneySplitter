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

    public Typeface getPlayfair(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/PlayfairDisplaySC-Regular.ttf");
        return myType;
    }

    public Typeface getMerri(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Merriweather-Regular.ttf");
        return myType;
    }

    public Typeface getOrbi(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Orbitron-Regular.ttf");
        return myType;
    }

    public Typeface getJosef(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/JosefinSans-Regular.ttf");
        return myType;
    }

    public Typeface getSlabo(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Slabo27px-Regular.ttf");
        return myType;
    }

    public Typeface getSource(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSerifPro-Regular.ttf");
        return myType;
    }

    public Typeface getVolk(){
        myType = Typeface.createFromAsset(context.getAssets(),"fonts/Volkhov-Regular.ttf");
        return myType;
    }
}
