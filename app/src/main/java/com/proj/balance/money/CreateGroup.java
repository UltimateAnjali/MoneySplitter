package com.proj.balance.money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.DialogInterface;
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
    public String groupName, groupType;
    private static final String TAG = "--Create Group--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.create_group);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        grpData = new GroupData();

        til = (TextInputLayout)findViewById(R.id.grpNameLay);
        grpNmEdit = (EditText)findViewById(R.id.grpNameEdit);
        //selectBtn = (Button)findViewById(R.id.select_grp_type);
        gridView = (GridView) findViewById(R.id.mygrid);

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

            HashMap<String,Boolean> members = new HashMap<>();
            members.put(UserData.firebaseUid,true);
            grpData.setMembers(members);
            myRef.setValue(grpData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

            /*HashMap<String,Boolean> userGroups = new HashMap<>();
            userGroups.put(grpData.getGrpKey(),true);
            UserData.groups = userGroups;*/

            DatabaseReference myref2 = dbref.child("moneySplit").child("users").child(UserData.firebaseUid);
            myref2.child("groups").child(grpData.getGrpKey()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
            //grpData.setGrpAdmin(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //myref2.setValue(grpData.getGrpAdmin());


            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Group Created"+grpData.getGrpKey(),Snackbar.LENGTH_LONG);
            snackbar.show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_LONG).show();
        }
        return true;
    }

   /* public void checkContactPermission(){
        if(ActivityCompat.checkSelfPermission(this, permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            requestContactPermission();
        }
        else{
            setData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CONTACT){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setData();
            }
            else{
                Toast.makeText(this,"You need to grant permission to access contacts",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestContactPermission(){
        ActivityCompat.requestPermissions(this,new String[]{permission.READ_CONTACTS},PERMISSION_REQUEST_CONTACT);
    }

    public void setData(){
        mMemberName = (AutoCompleteTextView) findViewById(R.id.memberATV);
        final ArrayAdapter<String> myAdp = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.single_contact, R.id.tv_ContactName, getAllContactNames());
        mMemberName.setAdapter(myAdp);

        mMemberName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"You chose: "+ parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                moji.add(parent.getItemAtPosition(position).toString());
                setList();
            }
        });
    }

    public void setList(){
        memberList = (ListView)findViewById(R.id.myList);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.single_contact,R.id.tv_ContactName,moji);
        memberList.setAdapter(adp);
        mMemberName.setText(null);
    }

    private List<String> getAllContactNames() {
        List<String> lContactNamesList = new ArrayList<String>();
        try {
            Cursor lPeople = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (lPeople != null) {
                while (lPeople.moveToNext()) {
                    lContactNamesList.add(lPeople.getString(lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            }
        } catch (NullPointerException e) {
            Log.e("getAllContactNames()", e.getMessage());
        }
        return lContactNamesList;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectBtn.setText(options[i].toString());
                Toast.makeText(getApplicationContext(),"Selected: "+options[i],Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();*/

}

