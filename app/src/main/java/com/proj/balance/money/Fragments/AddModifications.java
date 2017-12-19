package com.proj.balance.money.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class AddModifications extends Fragment {

    GroupData grp;
    UserData userData;
    EditText amt, desc;
    TextView paidBy, splitAmtText;
    Spinner payer, splitSpin;
    FloatingActionButton right;
    ImageView currency;

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

        desc = (EditText)view.findViewById(R.id.enterDescEdit);
        currency = (ImageView)view.findViewById(R.id.currencyImage);
        amt = (EditText)view.findViewById(R.id.enterAmtEdit);
        paidBy = (TextView)view.findViewById(R.id.paidByText);
        payer = (Spinner)view.findViewById(R.id.payerSpinner);
        splitAmtText = (TextView)view.findViewById(R.id.splitText);
        splitSpin = (Spinner)view.findViewById(R.id.splitSpinner);
        right = (FloatingActionButton)view.findViewById(R.id.fabRightArrow);

        currency.setImageResource(R.drawable.canadian_dollar);

        Bundle bundle = getArguments();
        if(bundle!=null){
            grp = bundle.getParcelable("lol");
            names.add("You");
            for (String key : grp.getMembers().keySet()){
                getKeyUser(key);
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splitSpin.setAdapter(adapter);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descr = desc.getText().toString();
                Double value = Double.parseDouble(String.valueOf(amt.getText()));
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMaximumFractionDigits(2);
                formatter.setMinimumFractionDigits(2);
                String amount = formatter.format(value);

                String paidBy = payer.getSelectedItem().toString();
                String splitType = splitSpin.getSelectedItem().toString();
                Toast.makeText(getContext(),paidBy+" "+splitType+" "+amount,Toast.LENGTH_SHORT).show();
            }
        });

        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Canadian Dollar",Toast.LENGTH_SHORT).show();
            }
        });
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
