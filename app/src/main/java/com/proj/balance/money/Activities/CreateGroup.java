package com.proj.balance.money.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.proj.balance.money.Adapters.GridAdapter;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;

public class CreateGroup extends AppCompatActivity{

    private EditText grpNmEdit;
    public String groupName;
    FloatingActionButton goToNext;
    private static final String TAG = "--Create Group--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        grpNmEdit = (EditText)findViewById(R.id.grpNameEdit);
        goToNext = (FloatingActionButton)findViewById(R.id.fab);

        goToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupName = grpNmEdit.getText().toString();
                if (groupName == null || groupName.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter a group name",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),SelectGroupType.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("grpName",groupName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.creategroup_menu_items,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        groupName = grpNmEdit.getText().toString();
//        Intent intent = new Intent(getApplicationContext(),AddMembers.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("grpName",groupName);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        finish();
////        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Group Created",Snackbar.LENGTH_LONG);
////        snackbar.show();
//
//        return true;
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}

