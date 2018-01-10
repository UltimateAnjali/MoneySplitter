package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;


public class UnequalSplit extends Fragment {

    GroupData groupData;
    String description;
    String totalAmount;
    String payer;
    String splitType;

    public UnequalSplit() {
        // Required empty public constructor
    }

    public static UnequalSplit newInstance() {
        UnequalSplit fragment = new UnequalSplit();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            groupData = bundle.getParcelable("myGrp");
            description = bundle.getString("desc");
            totalAmount = bundle.getString("amt");
            payer = bundle.getString("payer");
            splitType = bundle.getString("splitType");
            Toast.makeText(getContext(),totalAmount,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Enter amounts");

        View view = inflater.inflate(R.layout.fragment_unequal_split, container, false);

        return view;
    }
}
