package com.proj.balance.money.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.proj.balance.money.Activities.AddExpenses;
import com.proj.balance.money.Adapters.PersonOwingsAdapter;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;

public class PersonOwingsFragment extends Fragment{

    private RecyclerView recyclerView;
    private List<UserData> userDataList = new ArrayList<>();
    private List<String> allMemberKeys = new ArrayList<>();
    private PersonOwingsAdapter adapter;
    private TextView noowings;
    private static final String TAG = "--Person Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton floatingActionButton;

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
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SelectGroupForExpenses.class);
                startActivity(intent);
            }
        });
        return  view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PersonOwingsAdapter(getContext(),userDataList);


        checkIfDataExists();

    }

//    public void navigateToExpenses(View view){
//        Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
//    }

    private void checkIfDataExists() {
        Query query = dbref.child(getString(R.string.db_name))
                .child(getString(R.string.user_members));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    noowings.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    getMembersData();
                }
                else {
                    noowings.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMembersData() {
        Query query2 = dbref
                .child(getString(R.string.db_name))
                .child(getString(R.string.user_members))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot memberSnapshot: dataSnapshot.getChildren()){
                        allMemberKeys.add(memberSnapshot.getKey());
                        getUserData(memberSnapshot.getKey());
                    }

                    //Toast.makeText(getContext(),"Exists",Toast.LENGTH_SHORT).show();
                }
                else {
                    //myref.setValue(members);
                    Toast.makeText(getContext(),"No members",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserData(String key) {
        Query query3 = dbref.child(getString(R.string.db_name))
                .child(getString(R.string.user_table)).child(key);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    userDataList.add(userData);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
