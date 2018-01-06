package com.proj.balance.money.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.SelectGrpAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupForExpenses extends Fragment {

    private RecyclerView recyclerView;
    private SelectGrpAdapter adapter;
    private ArrayList<GroupData> groupDataList = new ArrayList<GroupData>();
    private ArrayList<GroupData> finalList = new ArrayList<GroupData>();
    private TextView nogrp, title;
    private static final String TAG = "--SELECT--";

    MyFonts fontFace;

    public SelectGroupForExpenses() {
        // Required empty public constructor
    }

    public static SelectGroupForExpenses newInstance() {
        SelectGroupForExpenses fragment = new SelectGroupForExpenses();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SelectGrpAdapter(getContext(),finalList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Select Group");

        View view = inflater.inflate(R.layout.fragment_select_group_for_expenses, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.selectGrpRecyclerView);
        nogrp = (TextView)view.findViewById(R.id.no_groups_textS);
        title = (TextView)view.findViewById(R.id.titleForSelectGrp);

        fontFace = new MyFonts(getContext());
        title.setTypeface(fontFace.getMont());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Bundle extras = getArguments();
        if(extras!=null){
            nogrp.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            groupDataList = extras.getParcelableArrayList("myArray");
            //adapter.notifyDataSetChanged();
            for(int i=0;i<groupDataList.size();i++){
                finalList.add(groupDataList.get(i));
                adapter.notifyDataSetChanged();
            }
        }
        else {
            nogrp.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
