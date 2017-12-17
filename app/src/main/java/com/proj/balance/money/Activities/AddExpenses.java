package com.proj.balance.money.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.R;

import java.util.ArrayList;
import java.util.List;

public class AddExpenses extends AppCompatActivity{

    EditText desc, amt, payer;
    AutoCompleteTextView grpName;
    Button split;
    String names[] = {"A","B","C","D"};
    private List<String> grpKeys = new ArrayList<>();
    private List<GroupData> grpData = new ArrayList<>();
    private List<String> grpNames = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    int loopCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        grpName = (AutoCompleteTextView) findViewById(R.id.selectGroupEdit);
        desc = (EditText)findViewById(R.id.descEdit);
        amt = (EditText)findViewById(R.id.amountEdit);
        payer = (EditText)findViewById(R.id.payerEdit);
        split = (Button)findViewById(R.id.splitBtn);

        Query query = ref.child(getString(R.string.db_name))
                .child(getString(R.string.user_table))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getString(R.string.group_table))){
                    UserData userData = dataSnapshot.getValue(UserData.class);
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

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, grpData.getClass().);

       // grpName.setAdapter(adapter);

        payer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddExpenses.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.choose_payer, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Payer");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        alertDialog.show();
    }

    private void getGroupData() {
        if(grpKeys.get(loopCount)!=null){
            Query mQuery = ref.child(getString(R.string.db_name))
                    .child(getString(R.string.group_table))
                    .child(grpKeys.get(loopCount));
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        GroupData groupData = dataSnapshot.getValue(GroupData.class);
                        grpData.add(groupData);
                       // grpNames.add(groupData.getGrpName());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

}
