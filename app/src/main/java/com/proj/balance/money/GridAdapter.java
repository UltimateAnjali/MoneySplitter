package com.proj.balance.money;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PublicKey;

/**
 * Created by anjali desai on 08-11-2017.
 */

public class GridAdapter extends BaseAdapter {

    private Context context;
    public ImageView imagev;
    public TextView textv;

    public Integer[] thumbIds = {
            R.drawable.apartment,
            R.drawable.trip,
            R.drawable.social,
            R.drawable.party,
            R.drawable.friends,
            R.drawable.other};

    public String[] thumbTexts = {"Apartment","Trip","Social Gathering","Party","Friends","Other"};

    public GridAdapter(Context c){
        context = c;
    }
    @Override
    public int getCount() {
        return thumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return thumbTexts[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.select_group_type,null);
        }

        imagev = (ImageView)view.findViewById(R.id.select_icon);
        textv = (TextView)view.findViewById(R.id.select_text);
        //ImageView imageView = new ImageView(context);
        //imageView.setImageResource(thumbIds[i]);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new GridView.LayoutParams(500, 500));

        imagev.setImageResource(thumbIds[i]);
        imagev.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textv.setText(thumbTexts[i]);
        return view;
    }
}
