package com.proj.balance.money;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AddMembers extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AddMembersAdapter membersAdapter;
    private List<AddMembersData> membersDataList;
    private List<AddMembersData> checkDataList = new ArrayList<>();
    private List<String> mSelectedMembers = new ArrayList<>();
    private static final String TAG = "--Add Members--";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    AddMembersData data;
    String temp;
    int xy;
    int size;
    int loopCount=0;
    private String groupName, groupType;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    UserData mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Bundle myBundle = getIntent().getExtras();
        if(myBundle!=null){
            groupName = myBundle.getString("grpName");
            groupType = myBundle.getString("grpType");
        }
        else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        membersDataList = new ArrayList<>();
        membersAdapter = new AddMembersAdapter(this, membersDataList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(membersAdapter);

        checkpermission();
    }

    public void checkpermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            prepareData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                checkpermission();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareData() {

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = getApplicationContext()
                    .getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {

                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        String newNumber = updatedNumber(displayNumber);
                        Boolean isAdded = false;

                        data = new AddMembersData(displayName,newNumber,isAdded);
                        //membersDataList.add(data);
                        checkDataList.add(data);

                    } else {
                    }
                }
                size = checkDataList.size();
                if(size>=1) {
                    CheckExist();
                    System.out.println("------------>size>=1");
                }

            } finally {
                cursor.close();

            }
        }
        membersAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private String updatedNumber(String contact) {
        String cont = contact.replaceAll("\\D+","");
        if(cont.length()>10){
            cont = cont.substring(cont.length()-10);
        }
        temp = "(";
        for(int i = 0; i < cont.length(); i++){
            if (i==3){
                temp = temp + cont.substring(0,3)+") ";
            }
            if(i==6){
                temp = temp + cont.substring(3,6) + "-" + cont.substring(6);
            }
        }
        return temp;
    }

    private void CheckExist() {

        Query query = dbref.child("moneySplit").child("users")
                .orderByChild("userContact").equalTo(checkDataList.get(loopCount).getContactNum());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot contact:dataSnapshot.getChildren()){

                        UserData userData = contact.getValue(UserData.class);

                        data = new AddMembersData(checkDataList.get(loopCount).getPersonName(),
                                userData.getUserContact(),
                                checkDataList.get(loopCount).getAdded());
                        data.setKey(userData.getFirebaseUid());
                        membersDataList.add(data);
                        membersAdapter.notifyDataSetChanged();
                        if(LoopHandling())
                        {
                            break;
                        }
                        Toast.makeText(getApplicationContext(),"aai gayu"+membersDataList.get(0).getPersonName(),Toast.LENGTH_LONG).show();
                        System.out.println("------------>membersDataList "+membersDataList.get(0).getPersonName());
                    }
                }
                else {
                   LoopHandling();
//                    data = new AddMembersData("Anjali","xxx");
//                    membersDataList.add(data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean LoopHandling() {
        if(loopCount < size - 1) {
            loopCount++;
            CheckExist();
            return false;
        }else{
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmembers_menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final GroupData grpData = new GroupData();
        mSelectedMembers = membersAdapter.selectedMembers;
        HashMap<String,Boolean> members = new HashMap<>();

        for(int cnt=0;cnt<mSelectedMembers.size();cnt++){
            members.put(mSelectedMembers.get(cnt),true);
        }

        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mUserData = dataSnapshot.getValue(UserData.class);
                    grpData.setGrpAdmin(mUserData.getFirebaseUid());
                }
                else {
                    Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        DatabaseReference mQuery = dbref.child("moneySplit").child("groups").push();

        grpData.setGrpKey(mQuery.getKey());
        grpData.setGrpType(groupType);
        grpData.setGrpName(groupName);

        grpData.setMembers(members);

        mQuery.setValue(grpData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        for(int memberCnt = 0; memberCnt < mSelectedMembers.size(); memberCnt++){
            addGrpKeyInUserData(mQuery.getKey().toString(),mSelectedMembers.get(memberCnt));
        }

        return true;
    }

    private void addGrpKeyInUserData(String grpKey,String userKey) {

        final DatabaseReference ref = dbref.child("moneySplit").child("users").child(userKey).child("groups");
        ref.child(grpKey).setValue(true);
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(),CreateGroup.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("grpkey",grpKey);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
}
