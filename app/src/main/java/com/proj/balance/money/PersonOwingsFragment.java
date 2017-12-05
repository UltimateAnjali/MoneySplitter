package com.proj.balance.money;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class PersonOwingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserData> fluserDataList = new ArrayList<>();
    private List<String> grpKeys = new ArrayList<>();
    private List<String> members = new ArrayList<>();
    private PersonOwingsAdapter adapter;
    private TextView noowings;
    public UserData userData;
    public GroupData groupData;
    private static final String TAG = "--Person Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    int loopControl = 0;
    int count = 0;

    public PersonOwingsFragment() {
    }

    public static PersonOwingsFragment newInstance() {
        PersonOwingsFragment fragment = new PersonOwingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PersonOwingsAdapter(getContext(),fluserDataList);
        prepareUserData();
    }

    private void prepareUserData() {
        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("groups")){

                    noowings.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    userData = dataSnapshot.getValue(UserData.class);
                    for(String key:userData.getGroups().keySet()){
                        grpKeys.add(key);
                    }
                    if(grpKeys.size()>=1){
                        getGroupData();
                    }
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

                        for(String groupMember:groupData.getMembers().keySet()){
                            if(!members.contains(groupMember)){
                                members.add(groupMember);
                            }
                        }
                        //adapter.notifyDataSetChanged();
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

        if(members.size()>=1){
            getGroupMembersData();
        }
    }

    private void getGroupMembersData() {
        if(members.get(count)!=null){
            //Toast.makeText(getContext(),"key: "+members.get(count).toString(),Toast.LENGTH_SHORT).show();
            Query mQueryy = dbref.child("moneySplit").child("users").child(members.get(count));
            mQueryy.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserData muserData = dataSnapshot.getValue(UserData.class);
                        //Toast.makeText(getContext(),"no: "+muserData.getUserGivenName(),Toast.LENGTH_SHORT).show();
                        fluserDataList.add(muserData);

                        if(count < members.size()-1){
                            count++;
                            getGroupMembersData();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_owings, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.persRecyclerView);
        noowings = (TextView)view.findViewById(R.id.no_owings_text);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return  view;
    }

}
