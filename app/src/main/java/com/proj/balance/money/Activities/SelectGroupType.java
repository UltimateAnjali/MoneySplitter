package com.proj.balance.money.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.proj.balance.money.Adapters.GridAdapter;
import com.proj.balance.money.R;

public class SelectGroupType extends AppCompatActivity {

    private GridView gridView;
    String groupName, groupType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group_type);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        gridView = (GridView) findViewById(R.id.mygrid);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            groupName = bundle.getString("grpName");
        }

        gridView.setAdapter(new GridAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                groupType = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(getApplicationContext(),AddMembers.class);
                Bundle bundle = new Bundle();
                bundle.putString("grpName",groupName);
                bundle.putString("grpType",groupType);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
