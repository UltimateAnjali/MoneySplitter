package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proj.balance.money.Adapters.SelectGrpAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;


public class AddModifications extends Fragment {

    GroupData grp;
    public AddModifications() {
        // Required empty public constructor
    }

    public static AddModifications newInstance() {
        AddModifications fragment = new AddModifications();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            grp = bundle.getParcelable("lol");
            Toast.makeText(getContext(),grp.grpName,Toast.LENGTH_SHORT).show();
        }
        return inflater.inflate(R.layout.fragment_add_modifications, container, false);
    }
}
