package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.SelectGrpAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;


public class AddModifications extends Fragment {

    GroupData grp;
    UserData userData;
    EditText amt;
    TextView paidBy;
    Spinner payer;

    List<String> names = new ArrayList<>();

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

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
        getActivity().setTitle("Enter Details");

        View view = inflater.inflate(R.layout.fragment_add_modifications, container, false);
        amt = (EditText)view.findViewById(R.id.amountEdit);
        paidBy = (TextView)view.findViewById(R.id.paidByText);
        payer = (Spinner)view.findViewById(R.id.payerSpinner);

        Bundle bundle = getArguments();
        if(bundle!=null){
            grp = bundle.getParcelable("lol");
            names.add("You");
            for (String key : grp.getMembers().keySet()){
                getKeyUser(key);
            }
        }

        fillTheSpinner();

        payer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payer.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void fillTheSpinner() {
        if(names!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            payer.setAdapter(adapter);

            int pos = adapter.getPosition("You");
            payer.setSelection(pos);
        }
    }

    private void getKeyUser(String key) {
        Query query = dbref.child(getString(R.string.db_name)).child(getString(R.string.user_table)).child(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userData = dataSnapshot.getValue(UserData.class);
                    names.add(userData.getUserGivenName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
