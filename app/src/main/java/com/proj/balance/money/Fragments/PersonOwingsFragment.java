package com.proj.balance.money.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.proj.balance.money.Adapters.PersonOwingsAdapter;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.DataModels.UserMembersData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;

public class PersonOwingsFragment extends Fragment{

    private RecyclerView recyclerView;
    private List<UserMembersData> userDataList = new ArrayList<>();
    public UserMembersData userMembersData;
    private List<String> allMemberKeys = new ArrayList<>();
    private PersonOwingsAdapter adapter;
    private TextView noowings;
    private static final String TAG = "--Person Fragment--";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    public PersonOwingsFragment() {
    }

    public static PersonOwingsFragment newInstance() {
        //Creating a new instance of this Fragment
        PersonOwingsFragment fragment = new PersonOwingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the adapter
        adapter = new PersonOwingsAdapter(getContext(),userDataList);

        //Calling method to check if data exists in the database
        checkIfDataExists();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting up Title Name
        getActivity().setTitle("Members");

        //Setting the view
        View view = inflater.inflate(R.layout.fragment_person_owings, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.persRecyclerView);
        noowings = (TextView) view.findViewById(R.id.no_owings_text);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void checkIfDataExists() {

        //Querying the database to check if data exists
        Query query = dbref.child(getString(R.string.db_name))
                .child(getString(R.string.user_members));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    //Change the visibility of components
                    noowings.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    //Calling the method to get all members data who are licked to the current user
                    getMembersData();
                }
                else {
                    //Make sure the visibility is as it is
                    noowings.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMembersData() {

        //Querying the database to get all the members data who are linked to the current user
        final Query query2 = dbref
                .child(getString(R.string.db_name))
                .child(getString(R.string.user_members))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot memberSnapshot: dataSnapshot.getChildren()){
                        //Getting the keys of all the members and adding it to arraylist
                        allMemberKeys.add(memberSnapshot.getKey());
                        //Calling the method to get user data by passing the user key and its value
                        getUserData(memberSnapshot.getKey(),memberSnapshot.getValue().toString());
                    }
                }
                else {
                    Toast.makeText(getContext(),"No members",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserData(String key, final String valuer) {

        //Querying the database to get the user data with the given user key
        Query query3 = dbref.child(getString(R.string.db_name))
                .child(getString(R.string.user_table)).child(key);

        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserData userData = dataSnapshot.getValue(UserData.class);
                    //Setting all the values of POJO class
                    userMembersData = new UserMembersData();
                    userMembersData.setUserKey(userData.getFirebaseUid());
                    userMembersData.setUserName(userData.getUserGivenName());
                    userMembersData.setUserAmount(valuer);

                    //Adding the POJO class to arraylist
                    userDataList.add(userMembersData);

                    //Notifying the database that the arraylist has been updated
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
