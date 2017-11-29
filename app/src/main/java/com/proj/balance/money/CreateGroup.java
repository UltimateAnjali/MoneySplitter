package com.proj.balance.money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.*;

public class CreateGroup extends AppCompatActivity{

    private AutoCompleteTextView mMemberName;
    //private ListView memberList;
    private Button selectBtn;
    private TextInputLayout til;
    private EditText grpNmEdit;
    public static int PERMISSION_REQUEST_CONTACT = 0;
    //private static ArrayList<String> moji = new ArrayList<String>();
    private CoordinatorLayout coordinatorLayout;
    private GridView gridView;
    public CharSequence options[] = new CharSequence[]{"Apartment","Trip","Party","Social Gathering"};
    public GroupData grpData;
    public String groupName, groupType, key;
    private static final String TAG = "--Create Group--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.create_group);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        til = (TextInputLayout)findViewById(R.id.grpNameLay);
        grpNmEdit = (EditText)findViewById(R.id.grpNameEdit);
        //selectBtn = (Button)findViewById(R.id.select_grp_type);
        gridView = (GridView) findViewById(R.id.mygrid);

//        Bundle myBundle = getIntent().getExtras();
//        if(myBundle!=null){
//            key = myBundle.getString("grpkey");
//        }
//        else{
//            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
//        }

        grpData = new GroupData();

        gridView.setAdapter(new GridAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                groupType = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(),"selected: "+adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
            }
        });

        //selectBtn.setOnClickListener(this);

        //checkContactPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creategroup_menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        groupName = grpNmEdit.getText().toString();
        //String groupType = selectBtn.getText().toString();
        //int numberOfMembers = moji.size();
        try{

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference myRef = dbref.child("moneySplit").child("groups").push();

            grpData.setGrpKey(myRef.getKey());
            grpData.setGrpName(groupName);
            grpData.setGrpAdmin(UserData.firebaseUid);
            grpData.setGrpType(groupType);

            /*HashMap<String,Boolean> members = new HashMap<>();
            members.put(UserData.firebaseUid,true);
            grpData.setMembers(members);*/
            myRef.setValue(grpData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(getApplicationContext(),AddMembers.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("key",grpData.getGrpKey());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"Group Created",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });

            /*DatabaseReference myref2 = dbref.child("moneySplit").child("users").child(UserData.firebaseUid);
            myref2.child("groups").child(grpData.getGrpKey()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });*/

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_LONG).show();
        }
        return true;
    }
}

