package com.proj.balance.money;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ContactInfo extends AppCompatActivity {

    CoordinatorLayout rel;
    EditText cont;
    Button bnext;

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
            }
        });

    }
}
