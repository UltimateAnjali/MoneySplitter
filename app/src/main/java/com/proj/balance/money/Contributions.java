package com.proj.balance.money;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;



public class Contributions extends AppCompatActivity {

    private TextView myUserText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributions);

        //userData = new UserData();
        myUserText = (TextView)findViewById(R.id.welcomeTitle);
        myUserText.setText("Welcome: "+UserData.userName);
    }
}
