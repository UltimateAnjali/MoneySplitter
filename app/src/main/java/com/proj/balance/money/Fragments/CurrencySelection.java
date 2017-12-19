package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.proj.balance.money.R;

public class CurrencySelection extends Fragment {

    public CurrencySelection() {

    }

    public static CurrencySelection newInstance() {
        CurrencySelection fragment = new CurrencySelection();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Select Currency");
        View view = inflater.inflate(R.layout.fragment_currency_selection, container, false);
        return view;
    }

}
