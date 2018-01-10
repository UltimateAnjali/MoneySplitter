package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.MemberDisplayAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.SingleGroupMembersData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupDetails extends Fragment {

    public String groupKey;
    RecyclerView myRecyclerView;
    TextView grpNameText, grpName, grpTypeText, memberText;
    Button viewBillsBtn, deleteBtn;
    GroupData groupData = new GroupData();
    UserData userData;
    SingleGroupMembersData singleMemberData;
    MemberDisplayAdapter adapter;
    List<SingleGroupMembersData> membersDataList = new ArrayList<>();
   // HashMap<String, String> memberWiseAmountsMap;
    Set<String> allKeys;
    List<String> userKeys;
    int userCount = 0;
    MyFonts fontFace;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    public GroupDetails() {
    }

    public static GroupDetails newInstance() {
        GroupDetails fragment = new GroupDetails();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            groupKey = bundle.getString("grpKey");
            //Toast.makeText(getContext(),groupKey,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Bundle null",Toast.LENGTH_SHORT).show();
        }

        adapter = new MemberDisplayAdapter(getContext(),membersDataList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Group Details");

        View view = inflater.inflate(R.layout.fragment_group_details, container, false);
        myRecyclerView = (RecyclerView)view.findViewById(R.id.dispGrpMembers);
        grpNameText = (TextView)view.findViewById(R.id.grpNameTextView2);
        grpName = (TextView)view.findViewById(R.id.grpNameDisplay2);
        grpTypeText = (TextView)view.findViewById(R.id.grpTypeTextView);
        viewBillsBtn = (Button) view.findViewById(R.id.view_bills_btn);
        deleteBtn = (Button)view.findViewById(R.id.delete_group_btn);
        memberText = (TextView)view.findViewById(R.id.memberTextView);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(adapter);

        fontFace = new MyFonts(getContext());

        grpNameText.setTypeface(fontFace.getMerri());
        grpName.setTypeface(fontFace.getMont());
        grpTypeText.setTypeface(fontFace.getMont());
        viewBillsBtn.setTypeface(fontFace.getMont());
        memberText.setTypeface(fontFace.getMerri());

        getGroupData(groupKey);

        viewBillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

//        if(groupData.getGrpAdmin().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//            deleteBtn.setVisibility(View.VISIBLE);
//        }

        return view;
    }

    private void getGroupData(String groupKey) {
        try {
            Query query = dbref.child(getString(R.string.db_name))
                    .child(getString(R.string.group_table))
                    .child(groupKey);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        groupData = dataSnapshot.getValue(GroupData.class);
                        //memberWiseAmountsMap = new HashMap<String, String>(groupData.getMembers());
                        allKeys = groupData.getMembers().keySet();
                        userKeys = new ArrayList<String>(allKeys);
                        grpName.setText(groupData.getGrpName());
                        grpTypeText.setText("~"+groupData.getGrpType());

                        getUserSpecificData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),"Oops! Something went wrong.",Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserSpecificData() {
        if(userKeys.get(userCount)!=null){
            Query query = dbref.child(getString(R.string.db_name))
                    .child(getString(R.string.user_table))
                    .child(userKeys.get(userCount));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        userData = dataSnapshot.getValue(UserData.class);
                        singleMemberData = new SingleGroupMembersData();
                        singleMemberData.setUsername(userData.getUserGivenName());
                        singleMemberData.setUserPhoto(userData.getUserPhoto());
                        singleMemberData.setUserAmount(groupData.getMembers().get(userKeys.get(userCount)));
                        if (userKeys.get(userCount).equals(groupData.getGrpAdmin())){
                            singleMemberData.setUserType("Admin");
                        }
                        else {
                            singleMemberData.setUserType("Member");
                        }
                        membersDataList.add(singleMemberData);
                        adapter.notifyDataSetChanged();
                        if(userCount < userKeys.size() - 1){
                            userCount++;
                            getUserSpecificData();
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
