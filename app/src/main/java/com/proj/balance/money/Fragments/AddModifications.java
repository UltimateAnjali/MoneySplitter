package com.proj.balance.money.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.DataModels.BillsData;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddModifications extends Fragment {

    EditText amt, desc;
    TextView paidBy, splitAmtText, grpNameText;
    Spinner payerSpinner, splitSpinner;
    FloatingActionButton right;
    ImageView currency;

    GroupData grp;
    UserData userData;
    BillsData currentBill;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    HashMap<String,String> userNameKeys = new HashMap<>();
    HashMap<String,String> memberBillSplit = new HashMap<>();
    List<String> userKeys = new ArrayList<>();
    List<String> names = new ArrayList<>();
    ArrayAdapter<String> payerAdapter;

    String selectedPayer, selectedPayerKey, splitTypeValue, description, totalAmount;

    private  String TAG ="Modi------>>>>>>";

    int loopCount = 0;
    int userCount = 0;
    int userMemberCount = 0;

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

        //Get the bundle and set the current user as "You"
        Bundle bundle = getArguments();
        if(bundle!=null) {
            grp = bundle.getParcelable("selectedGrp");
            for(String key: grp.getMembers().keySet()){
                userKeys.add(key);
//                Toast.makeText(getContext(),key,Toast.LENGTH_SHORT).show();
            }
        }

        payerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, names);
        payerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void loopThroughKeys() {
        if(userKeys.get(loopCount)!=null){
            Query query = dbref
                    .child(getString(R.string.db_name))
                    .child(getString(R.string.user_table))
                    .child(userKeys.get(loopCount));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        userData = dataSnapshot.getValue(UserData.class);
//                        refData = new ReferenceUserData();
//                        refData.setUserName(userData.getUserGivenName());
//                        refData.setUserKey(userData.getFirebaseUid());
//                        Toast.makeText(getContext(),userData.getFirebaseUid(),Toast.LENGTH_SHORT).show();
                        userNameKeys.put(userData.getFirebaseUid(),userData.getUserGivenName());
                        names.add(userData.getUserGivenName());
//                        payerAdapter.notifyDataSetChanged();
                        BaseAdapter adapter = (BaseAdapter)payerSpinner.getAdapter();
                        adapter.notifyDataSetChanged();

                        if(loopCount < userKeys.size() - 1){
                            loopCount++;
                            loopThroughKeys();
                        }
                        //names.add(userData.getUserGivenName());
                        //finalList.add(refData);
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
        //Set the title
        getActivity().setTitle("Enter Details");

        //Set the view and map its components
        View view = inflater.inflate(R.layout.fragment_add_modifications, container, false);

        grpNameText = (TextView)view.findViewById(R.id.grpNameTextView);
        desc = (EditText)view.findViewById(R.id.enterDescEdit);
        currency = (ImageView)view.findViewById(R.id.currencyImage);
        amt = (EditText)view.findViewById(R.id.enterAmtEdit);
        paidBy = (TextView)view.findViewById(R.id.paidByText);
        payerSpinner = (Spinner)view.findViewById(R.id.payerSpinner);
        splitAmtText = (TextView)view.findViewById(R.id.splitText);
        splitSpinner = (Spinner)view.findViewById(R.id.splitSpinner);
        right = (FloatingActionButton)view.findViewById(R.id.fabRightArrow);

        //Setting the group name
        grpNameText.setText("Group: "+grp.getGrpName());
        //Setting Image resource for currency
        currency.setImageResource(R.drawable.canadian_dollar);

        ArrayAdapter<CharSequence> splitAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.split_type,android.R.layout.simple_spinner_item);
        splitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splitSpinner.setAdapter(splitAdapter);

        loopThroughKeys();

        payerSpinner.setAdapter(payerAdapter);

        payerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPayer = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getContext(),selectedPayer,Toast.LENGTH_SHORT).show();
                getTheKey(selectedPayer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        splitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                splitTypeValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheValues();
            }
        });
        return view;
    }

    private void getTheKey(String selectedPayer) {
        for (String myValue : userNameKeys.keySet()){
            if(userNameKeys.get(myValue).equals(selectedPayer)){
                selectedPayerKey = myValue;
                //Toast.makeText(getContext(),selectedPayerKey,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setTheValues() {
        try{
            description = desc.getText().toString();

            Double roundValue = Double.parseDouble(String.valueOf(amt.getText()));
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMaximumFractionDigits(2);
            formatter.setMinimumFractionDigits(2);

            totalAmount = formatter.format(roundValue);

            if (splitTypeValue.equals("Equally")) {
                Double totalAmountDoubleValue = Double.parseDouble(totalAmount);
                int numberOfMembers = userNameKeys.size();
                Double eachPersonShare = totalAmountDoubleValue/numberOfMembers;
                NumberFormat formatter2 = NumberFormat.getNumberInstance();
                formatter2.setMaximumFractionDigits(2);
                formatter2.setMinimumFractionDigits(2);
                final String eachPersonShareValue = formatter2.format(eachPersonShare);

                currentBill = new BillsData();
                currentBill.setTotalAmount(totalAmount);
                currentBill.setPayer(selectedPayerKey);
                currentBill.setCurrency(getString(R.string.current_currency));
                currentBill.setDescription(description);
                currentBill.setSplitType(splitTypeValue);
                currentBill.setGroupKey(grp.getGrpKey());

                DatabaseReference ref = dbref.child(getString(R.string.db_name)).child(getString(R.string.bills_table)).push();
                currentBill.setBillKey(ref.getRef().getKey());

                for (String thisUserKey : userNameKeys.keySet()){
                    memberBillSplit.put(thisUserKey,eachPersonShareValue);
                }

                currentBill.setMembers(memberBillSplit);
                ref.setValue(currentBill).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference ref2 = dbref.child(getString(R.string.db_name))
                                .child(getString(R.string.group_table))
                                .child(grp.getGrpKey())
                                .child(getString(R.string.bills_table))
                                .child(currentBill.getBillKey());
                        ref2.setValue(true);
                        setTheBillInUsers(eachPersonShareValue);
                    }
                });
            }

        }catch (Exception e){
            Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();
        }

    }

    private void setTheBillInUsers(String share) {
        Double temp = 0.00;
        for(String key : userNameKeys.keySet()){
            if(key.equals(selectedPayerKey)){

            }
            else {
                temp = temp + Double.parseDouble(share);
            }
        }
        NumberFormat formatter3 = NumberFormat.getNumberInstance();
        formatter3.setMinimumFractionDigits(2);
        formatter3.setMaximumFractionDigits(2);
        String payerShare = formatter3.format(temp);
        settleTheBillInUserData(payerShare,share);
    }

    private void settleTheBillInUserData(final String payerShare, final String othersShare) {
        //Toast.makeText(getContext(),payerShare+"->"+othersShare,Toast.LENGTH_SHORT).show();
        if(userKeys.get(userCount)!=null){
            if(userKeys.get(userCount).equals(selectedPayerKey)){
                try {
                    final DatabaseReference query = dbref
                            .child(getString(R.string.db_name))
                            .child(getString(R.string.user_table))
                            .child(userKeys.get(userCount))
                            .child(getString(R.string.group_table))
                            .child(grp.getGrpKey());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String value = dataSnapshot.getValue().toString();
                                if(value.equals("0.00")){
                                    query.setValue("+"+payerShare);
                                    Log.e(TAG,"+"+payerShare);
                                }
                                else if(value.substring(0,1).equals("+")){
                                    String value2 = value.substring(1);
                                    Double dbVal = Double.parseDouble(value2) + Double.parseDouble(payerShare);
                                    NumberFormat formatter4 = NumberFormat.getNumberInstance();
                                    formatter4.setMaximumFractionDigits(2);
                                    formatter4.setMinimumFractionDigits(2);
                                    String dbValString = formatter4.format(dbVal);
                                    if(dbVal > 0.00){
                                        query.setValue("+"+dbValString);
                                        Log.e(TAG,"+"+dbValString);
                                    }
                                    else if(dbVal < 0.00){
                                        query.setValue(dbValString);
                                        Log.e(TAG,dbValString);
                                    }
                                    else if(dbVal == 0.00){
                                        query.setValue("0.00");
                                        Log.e(TAG,dbValString);
                                    }
                                }
                                else if(value.substring(0,1).equals("-")){
                                    String value2 = value.substring(1);
                                    Double dbVal = - Double.parseDouble(value2) + Double.parseDouble(payerShare);
                                    NumberFormat formatter5 = NumberFormat.getNumberInstance();
                                    formatter5.setMaximumFractionDigits(2);
                                    formatter5.setMinimumFractionDigits(2);
                                    String dbValString = formatter5.format(dbVal);
                                    if(dbVal > 0.00){
                                        query.setValue("+"+dbValString);
                                        Log.e(TAG,"+"+dbValString);
                                    }
                                    else if(dbVal < 0.00){
                                        query.setValue(dbValString);
                                        Log.e(TAG,dbValString);
                                    }
                                    else if(dbVal == 0.00){
                                        query.setValue("0.00");
                                        Log.e(TAG,dbValString);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(),"Oops, Something went wrong!",Toast.LENGTH_SHORT).show();
                }


            }
            else{
                try{
                    final DatabaseReference reference2 = dbref
                            .child(getString(R.string.db_name))
                            .child(getString(R.string.user_table))
                            .child(userKeys.get(userCount))
                            .child(getString(R.string.group_table))
                            .child(grp.getGrpKey());

                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String value = dataSnapshot.getValue().toString();
                                if(value.equals("0.00")){
                                    reference2.setValue("-"+othersShare);
                                    Log.e(TAG,"-"+othersShare);
                                }
                                else if(value.substring(0,1).equals("+")){
                                    String value2 = value.substring(1);
                                    Double dbVal = Double.parseDouble(value2) - Double.parseDouble(othersShare);
                                    NumberFormat formatter4 = NumberFormat.getNumberInstance();
                                    formatter4.setMaximumFractionDigits(2);
                                    formatter4.setMinimumFractionDigits(2);
                                    String dbValString = formatter4.format(dbVal);
                                    if(dbVal > 0.00){
                                        reference2.setValue("+"+dbValString);
                                        Log.e(TAG,"+"+dbValString);
                                    }
                                    else if(dbVal < 0.00){
                                        reference2.setValue(dbValString);
                                        Log.e(TAG,dbValString);
                                    }
                                    else if(dbVal == 0.00){
                                        reference2.setValue("0.00");
                                        Log.e(TAG,dbValString);
                                    }
                                }
                                else if(value.substring(0,1).equals("-")){
                                    String value2 = value.substring(1);
                                    Double dbVal = - Double.parseDouble(value2) - Double.parseDouble(othersShare);
                                    NumberFormat formatter5 = NumberFormat.getNumberInstance();
                                    formatter5.setMaximumFractionDigits(2);
                                    formatter5.setMinimumFractionDigits(2);
                                    String dbValString = formatter5.format(dbVal);
                                    if(dbVal > 0.00){
                                        reference2.setValue("+"+dbValString);
                                        Log.e(TAG,"+"+dbValString);
                                    }
                                    else if(dbVal < 0.00){
                                        reference2.setValue(dbValString);
                                        Log.e(TAG,dbValString);
                                    }
                                    else if(dbVal == 0.00){
                                        reference2.setValue("0.00");
                                        Log.e(TAG,dbValString);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(),"Oops, Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }

            if(userCount < userKeys.size() - 1){
                userCount++;
                settleTheBillInUserData(payerShare,othersShare);
            }

            else {
                updateDataInUserMembers(payerShare,othersShare);
//                Fragment frag = GroupFragment.newInstance();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.detach(frag);
//                transaction.attach(frag);
////                transaction.replace(R.id.frame_layout, frag);
//                transaction.commit();
            }
        }
    }

    private void updateDataInUserMembers(final String payerShare, final String othersShare) {
        if(userKeys.get(userMemberCount)!=null) {
            if (userKeys.get(userMemberCount).equals(selectedPayerKey)) {
                try {
                    final DatabaseReference dbref5 = dbref
                            .child(getString(R.string.db_name))
                            .child(getString(R.string.user_members))
                            .child(userKeys.get(userMemberCount));
                    dbref5.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    if (userKeys.contains(snap.getKey())){
                                        //Toast.makeText(getContext(),""+snap.getKey()+snap.getValue().toString(),Toast.LENGTH_SHORT).show();
                                        String userAmount = snap.getValue().toString();
                                        if(userAmount.equals("0.00"))
                                        {
                                            dbref5.child(snap.getKey()).setValue("-"+othersShare);
                                        }else{
                                            String sign = userAmount.substring(0,1);
                                            String value = userAmount.substring(1);
                                            Double cal = 0.00;
                                            String calculatedValue;
                                            NumberFormat formatter8 = NumberFormat.getNumberInstance();
                                            formatter8.setMinimumFractionDigits(2);
                                            formatter8.setMaximumFractionDigits(2);
                                            if(sign.equals("+")){
                                                cal = Double.parseDouble(value) - Double.parseDouble(othersShare);
                                            }else if(sign.equals("-")){
                                                cal = - Double.parseDouble(value) - Double.parseDouble(othersShare);
                                            }
                                            calculatedValue = formatter8.format(cal);
                                            if (cal>0.00){
                                                dbref5.child(snap.getKey()).setValue("+"+calculatedValue);
                                            }
                                            else if(cal<0.00){
                                                dbref5.child(snap.getKey()).setValue(calculatedValue);
                                            }
                                            else if(cal == 0.00){
                                                dbref5.child(snap.getKey()).setValue("0.00");
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Oops, Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            else{
                try{
                    final DatabaseReference dbref9 = dbref
                            .child(getString(R.string.db_name))
                            .child(getString(R.string.user_members))
                            .child(userKeys.get(userMemberCount));
                    dbref9.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot snap : dataSnapshot.getChildren()){
                                    if(userKeys.contains(snap.getKey())){

                                        //If user is a payer
                                        if(snap.getKey().equals(selectedPayerKey)){
                                            String userAmount = snap.getValue().toString();
                                            if(userAmount.equals("0.00")){
                                                dbref9.child(snap.getKey()).setValue("+"+payerShare);
                                            }
                                            else{
                                                String sign = userAmount.substring(0,1);
                                                String amountValue = userAmount.substring(1);
                                                Double cal = 0.00;
                                                String calcValue;
                                                NumberFormat formatter9 = NumberFormat.getNumberInstance();
                                                formatter9.setMaximumFractionDigits(2);
                                                formatter9.setMinimumFractionDigits(2);
                                                if(sign.equals("+")){
                                                    cal = Double.parseDouble(amountValue) + Double.parseDouble(payerShare);
                                                }
                                                else if(sign.equals("-")){
                                                    cal = - Double.parseDouble(amountValue) + Double.parseDouble(payerShare);
                                                }

                                                calcValue = formatter9.format(cal);
                                                if (cal>0.00){
                                                    dbref9.child(snap.getKey()).setValue("+"+calcValue);
                                                }
                                                else if(cal<0.00){
                                                    dbref9.child(snap.getKey()).setValue(calcValue);
                                                }
                                                else if(cal == 0.00){
                                                    dbref9.child(snap.getKey()).setValue("0.00");
                                                }

                                            }
                                        }

                                        //If user is not the payer
                                        else{
                                            String userAmount = snap.getValue().toString();
                                            if(userAmount.equals("0.00")){
                                                dbref9.child(snap.getKey()).setValue("-"+othersShare);
                                            }
                                            else {
                                                String sign = userAmount.substring(0, 1);
                                                String amountValue = userAmount.substring(1);
                                                Double cal = 0.00;
                                                String calcValue;
                                                NumberFormat formatter9 = NumberFormat.getNumberInstance();
                                                formatter9.setMaximumFractionDigits(2);
                                                formatter9.setMinimumFractionDigits(2);
                                                if (sign.equals("+")) {
                                                    cal = Double.parseDouble(amountValue) - Double.parseDouble(othersShare);
                                                } else if (sign.equals("-")) {
                                                    cal = - Double.parseDouble(amountValue) - Double.parseDouble(othersShare);
                                                }

                                                calcValue = formatter9.format(cal);
                                                if (cal > 0.00) {
                                                    dbref9.child(snap.getKey()).setValue("+" + calcValue);
                                                } else if (cal < 0.00) {
                                                    dbref9.child(snap.getKey()).setValue(calcValue);
                                                } else if (cal == 0.00) {
                                                    dbref9.child(snap.getKey()).setValue("0.00");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(), "Oops, Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            if(userMemberCount < userKeys.size() -1){
                userMemberCount++;
                updateDataInUserMembers(payerShare,othersShare);
            }
        }
    }
}
