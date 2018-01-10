package com.proj.balance.money.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.GroupFragmentAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.GroupOwingsData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<GroupOwingsData> groupOwingDataList = new ArrayList<>();
    private ArrayList<GroupData> grpDataList = new ArrayList<>();
    private List<String> grpKeys = new ArrayList<>();
    private GroupFragmentAdapter adapter;
    private TextView nogrp;
    public UserData userData;
    public GroupData groupData;
    public GroupOwingsData grpOwings56;
    HashMap<String, String> tempHash = new HashMap<>();
    private static final String TAG = "--Group Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    int loopControl = 0;
    int count = 0;
    FloatingActionButton floatingActionButton;

    public GroupFragment() {
    }

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new GroupFragmentAdapter(getContext(),groupOwingDataList);

        prepareGroupData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Groups");

        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.grpRecyclerView);
        nogrp = (TextView)view.findViewById(R.id.no_groups_text);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = SelectGroupForExpenses.newInstance();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("myArray", grpDataList);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //transaction.add(GroupFragment.newInstance(),"groupFrag");
                //transaction.addToBackStack(fragment.getClass().toString());
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }
        });

        return view;
    }

    private void prepareGroupData() {
        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("groups")){

                    nogrp.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    userData = dataSnapshot.getValue(UserData.class);

                    for(String key:userData.getGroups().keySet()){
                        grpKeys.add(key);
                    }

                    if(grpKeys.size() >= 1){
                        getGroupData();
                    }
                }
                else{
                    nogrp.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                    //Toast.makeText(getContext(),"No",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getGroupData() {

        if(grpKeys.get(loopControl)!=null){
            Query gQuery = dbref.child("moneySplit").child("groups").child(grpKeys.get(loopControl));
            gQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        groupData = dataSnapshot.getValue(GroupData.class);
                        grpOwings56 = new GroupOwingsData();

                        grpOwings56.setGrpName(groupData.getGrpName());
                        grpOwings56.setGrpType(groupData.getGrpType());
                        grpOwings56.setGrpKey(groupData.getGrpKey());
                        grpOwings56.setGrpOwing(userData.getGroups().get(groupData.getGrpKey()));

                        groupOwingDataList.add(grpOwings56);
                        grpDataList.add(groupData);

                        adapter.notifyDataSetChanged();
                        if(loopControl < grpKeys.size()-1){
                            loopControl++;
                            getGroupData();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
