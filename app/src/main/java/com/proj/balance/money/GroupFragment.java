package com.proj.balance.money;

import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<GroupData> groupDataList;
    private List<String> grpKeys = new ArrayList<>();
    private GroupFragmentAdapter adapter;
    private TextView nogrp;
    public UserData userData;
    private static final String TAG = "--Group Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    public GroupFragment() {
    }

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupDataList = new ArrayList<>();
        adapter = new GroupFragmentAdapter(getContext(),groupDataList);

        prepareGroupData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.grpRecyclerView);
        nogrp = (TextView)view.findViewById(R.id.no_groups_text);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void prepareGroupData() {
        Query query = dbref.child("moneySplit").child("users").child(UserData.firebaseUid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("groups")){

                    nogrp.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    userData = dataSnapshot.getValue(UserData.class);
                    //Toast.makeText(getContext(),"Has"+UserData.groups,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"No",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*for(int loopCount = 0; loopCount < userData.getGroups().size(); loopCount++){
            grpKeys.add(userData.getGroups().toString());
            Log.i(TAG,"JKJKJKJKJK"+grpKeys.get(loopCount));
        }*/
    }
}
