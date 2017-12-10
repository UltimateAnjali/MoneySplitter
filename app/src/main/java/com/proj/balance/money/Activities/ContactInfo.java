package com.proj.balance.money.Activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.R;
import com.proj.balance.money.DataModels.UserData;

public class ContactInfo extends AppCompatActivity {

    CoordinatorLayout rel;
    EditText cont;
    Button bnext;
    String temp;
    private static final String TAG = "--Contact Info--";
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rel = (CoordinatorLayout) findViewById(R.id.contactinfo);
        cont = (EditText)findViewById(R.id.contact);
        bnext = (Button)findViewById(R.id.next);

        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(cont.getText().toString())){
                    Snackbar snackbar = Snackbar.make(rel,"Please enter phone number",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(cont.getText().toString().trim().length() < 10){
                    Snackbar snackbar = Snackbar.make(rel,"Phone number not valid",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    String contact = cont.getText().toString().trim();
                    temp = "(";
                    for(int i = 0; i < contact.length(); i++){
                        if (i==3){
                            temp = temp + contact.substring(0,3)+") ";
                        }
                        if(i==6){
                            temp = temp + contact.substring(3,6) + "-" + contact.substring(6);
                        }
                    }
                    userData.setUserContact(temp);
                    userData.setDataflag(true);
                    //UserData.userContact = temp;
                    //Toast.makeText(getApplicationContext(),"Length: "+contact.length()+"\nNumber: "+temp,Toast.LENGTH_LONG).show();
                    //UserData.dataflag = true;
                    try{
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("moneySplit").child("users").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("userContact").setValue(userData.getUserContact());
                                dataSnapshot.getRef().child("dataflag").setValue(userData.getDataflag());
                                Intent intent = new Intent(ContactInfo.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG,"Could not update database");
                            }
                        });



                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
