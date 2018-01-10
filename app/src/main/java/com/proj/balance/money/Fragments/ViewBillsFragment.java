package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.BillAdapter;
import com.proj.balance.money.DataModels.BillsData;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;

public class ViewBillsFragment extends Fragment {

    String groupKey;
    private RecyclerView recyclerView;
    List<BillsData> allBills = new ArrayList<>();
    List<String> billKeys;
    BillAdapter adapter;
    GroupData grpData;
    BillsData singleBill;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    int billCount = 0;

    public ViewBillsFragment() {
        // Required empty public constructor
    }

    public static ViewBillsFragment newInstance() {
        ViewBillsFragment fragment = new ViewBillsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            groupKey = bundle.getString("key");
            //Toast.makeText(getContext(),groupKey,Toast.LENGTH_SHORT).show();
        }

        adapter = new BillAdapter(getContext(),allBills);
        getGroupDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Bills");
        View view = inflater.inflate(R.layout.fragment_view_bills, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.billRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;

    }

    private void getGroupDetails() {
        Query query = dbref.child(getString(R.string.db_name))
                .child(getString(R.string.group_table))
                .child(groupKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   // Toast.makeText(getContext(),"Exists",Toast.LENGTH_SHORT).show();
                    grpData = dataSnapshot.getValue(GroupData.class);
                    billKeys = new ArrayList<String>(grpData.getBills().keySet());
                    getAllBillDetails();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAllBillDetails() {
        if(billKeys.get(billCount)!=null){
            Query query2 = dbref.child(getString(R.string.db_name))
                    .child(getString(R.string.bills_table))
                    .child(billKeys.get(billCount));
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        singleBill = dataSnapshot.getValue(BillsData.class);
                        allBills.add(singleBill);
                        adapter.notifyDataSetChanged();
                        if(billCount < billKeys.size()-1){
                            billCount++;
                            getAllBillDetails();
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
