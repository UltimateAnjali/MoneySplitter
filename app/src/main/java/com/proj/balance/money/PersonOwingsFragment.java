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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PersonOwingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserData> userDataList = new ArrayList<>();
    private List<String> grpKeys = new ArrayList<>();
    private List<String> allMemberKeys = new ArrayList<>();
    private List<String> filteredKeys = new ArrayList<>();
    private PersonOwingsAdapter adapter;
    private TextView noowings;
    public UserData userData;
    public GroupData groupData;
    private static final String TAG = "--Person Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    int loopCount = 0;
    int filterCount = 0;

    public PersonOwingsFragment() {
    }

    public static PersonOwingsFragment newInstance() {
        PersonOwingsFragment fragment = new PersonOwingsFragment();
        return fragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PersonOwingsAdapter(getContext(),userDataList);
        getCurrentUserData();
    }

    private void getCurrentUserData() {
        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    noowings.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    UserData mUserData = dataSnapshot.getValue(UserData.class);
                    for(String key: mUserData.getGroups().keySet()){
                        grpKeys.add(key);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (grpKeys.size()>=1){
            getAllMembersFromGroupKeys();
        }
//        else{
//            noowings.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        }
    }

    private void getAllMembersFromGroupKeys() {
        if (grpKeys.get(loopCount)!=null){
            Query query2 = dbref.child(String.valueOf(R.string.db_name))
                    .child(String.valueOf(R.string.group_table))
                    .child(grpKeys.get(loopCount));
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        groupData = dataSnapshot.getValue(GroupData.class);
                        for (String memberKey: groupData.getMembers().keySet()){
                            allMemberKeys.add(memberKey);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(loopCount < grpKeys.size()-1){
            loopCount++;
            getAllMembersFromGroupKeys();
        }
        else{
            filterAllMemberKeys();
        }
    }

    private void filterAllMemberKeys() {
        for(int cnt=0; cnt < allMemberKeys.size(); cnt++){
            if(!filteredKeys.contains(allMemberKeys.get(cnt))){
                filteredKeys.add(allMemberKeys.get(cnt));
            }
        }
        Log.d(TAG,"NOT FIL: "+allMemberKeys.size());
        Log.d(TAG,"FIL: "+filteredKeys.size());
        getActualData();
    }

    private void getActualData() {
        if(filteredKeys.get(filterCount)!=null){
            Query query2 = dbref.child(String.valueOf(R.string.db_name))
                    .child(String.valueOf(R.string.user_table))
                    .child(filteredKeys.get(filterCount));
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        userData = dataSnapshot.getValue(UserData.class);
                        userDataList.add(userData);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(filterCount < filteredKeys.size()-1){
            filterCount++;
            getActualData();
        }

    }

}
