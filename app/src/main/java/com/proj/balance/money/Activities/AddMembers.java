package com.proj.balance.money.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.Adapters.AddMembersAdapter;
import com.proj.balance.money.DataModels.AddMembersData;
import com.proj.balance.money.DataModels.GroupData;
import com.proj.balance.money.R;
import com.proj.balance.money.DataModels.UserData;

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
    int size;
    int loopCount=0;
    int userMemberCount = 0;
    private String groupName, groupType;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    public UserData mUserData;
    public UserData currentUserData;
    public GroupData groupData;
    String currentUserContactNumber;
    HashMap<String,String> connectedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Get the group name and group type
        Bundle myBundle = getIntent().getExtras();
        if(myBundle!=null){
            groupName = myBundle.getString("grpName");
            groupType = myBundle.getString("grpType");
        }
        else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }

        Query query = dbref
                .child(getString(R.string.db_name))
                .child(getString(R.string.user_table))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserData = dataSnapshot.getValue(UserData.class);
                    currentUserContactNumber = currentUserData.getUserContact();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        membersDataList = new ArrayList<>();
        membersAdapter = new AddMembersAdapter(this, membersDataList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
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
            Toast.makeText(getApplicationContext(),"Permissions needed",Toast.LENGTH_SHORT).show();
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
                        if(userData.getUserContact().equals(currentUserContactNumber)){

                        }
                        else {
                            data = new AddMembersData(checkDataList.get(loopCount).getPersonName(),
                                    userData.getUserContact(),
                                    checkDataList.get(loopCount).getAdded());
                            data.setKey(userData.getFirebaseUid());
                            membersDataList.add(data);
                            membersAdapter.notifyDataSetChanged();
                        }

                        if(LoopHandling())
                        {
                            break;
                        }
                        //System.out.println("------------>membersDataList "+membersDataList.get(0).getPersonName());
                    }
                }
                else {
                   LoopHandling();
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

        Query query = dbref
                .child(getString(R.string.db_name))
                .child(getString(R.string.user_table))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mUserData = dataSnapshot.getValue(UserData.class);
                    putAllGroupData();
                }
                else {
                    Toast.makeText(getApplicationContext(),"An error occured",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    private void putAllGroupData() {
        groupData = new GroupData();

        final DatabaseReference mQuery = dbref.child("moneySplit").child("groups").push();

        mSelectedMembers = membersAdapter.selectedMembers;
        mSelectedMembers.add(currentUserData.getFirebaseUid());

        final HashMap<String,String> members = new HashMap<>();

        for(int cnt = 0; cnt < mSelectedMembers.size(); cnt ++){
            members.put(mSelectedMembers.get(cnt),"0.00");
        }
       // members.put(currentUserData.getFirebaseUid(),"0.00");

        for(int memberCnt = 0; memberCnt < mSelectedMembers.size(); memberCnt ++){
            addGrpKeyInUserData(mQuery.getKey().toString(),mSelectedMembers.get(memberCnt));
        }
        //addGrpKeyInUserData(mQuery.getKey().toString(),currentUserData.getFirebaseUid());

        //Toast.makeText(getApplicationContext(),"an"+mUserData.getUserGivenName(),Toast.LENGTH_SHORT).show();
        groupData.setGrpAdmin(mUserData.getFirebaseUid());
        groupData.setGrpKey(mQuery.getKey());
        groupData.setGrpType(groupType);
        groupData.setGrpName(groupName);
        groupData.setMembers(members);

        mQuery.setValue(groupData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //mQuery.child("members").child(groupData.getGrpAdmin()).setValue("0.00");
                if (members!=null){
//                    DatabaseReference refg = dbref.child(getString(R.string.db_name))
//                            .child(getString(R.string.user_members));
//                    refg.push();
//
//                    String userConnectionKey = refg.getKey();
                    putUserMembers(members);
                }
            }
        });
    }

    private void addGrpKeyInUserData(String grpKey,String userKey) {
        final DatabaseReference ref = dbref
                .child(getString(R.string.db_name))
                .child(getString(R.string.user_table))
                .child(userKey)
                .child("groups");
        ref.child(grpKey).setValue("0.00");
    }

    private void putUserMembers(final HashMap<String,String> members) {

        final HashMap<String,String> newMemberSet = new HashMap<>();
        if(mSelectedMembers.get(userMemberCount)!=null){
            try{
                connectedUsers = new HashMap<>();
                final DatabaseReference refh = dbref.child(getString(R.string.db_name))
                        .child(getString(R.string.user_members))
                        .child(mSelectedMembers.get(userMemberCount));
                refh.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                connectedUsers.put(snapshot.getKey(), (String) snapshot.getValue());
                            }
                        }

                        else if(!dataSnapshot.exists()){
                            for (String key: members.keySet()){
                                if(key.equals(mSelectedMembers.get(userMemberCount))){

                                }
                                else{
                                    newMemberSet.put(key,members.get(key));
                                }
                            }
                            refh.setValue(newMemberSet);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(connectedUsers!=null){
                    for(int conn = 0; conn < mSelectedMembers.size() ; conn++){
                        if(connectedUsers.containsKey(mSelectedMembers.get(conn))){
                        }

                        else if(mSelectedMembers.get(conn).equals(mSelectedMembers.get(userMemberCount))){
                        }

                        else {
                            connectedUsers.put(mSelectedMembers.get(conn),"0.00");
                            refh.child(mSelectedMembers.get(conn)).setValue("0.00");
                        }
                    }
                }


            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
            }
        }

        if(userMemberCount < mSelectedMembers.size() - 1){
            userMemberCount++;
            putUserMembers(members);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

//        final ArrayList<String> databaseMembers = new ArrayList<>();
//        final DatabaseReference myref = dbref
//                .child(getString(R.string.db_name))
//                .child(getString(R.string.user_members))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        Query query2 = myref;
//        query2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot memberSnapshot : dataSnapshot.getChildren()) {
//                        databaseMembers.add(memberSnapshot.getKey());
//                    }
//                    //Toast.makeText(getApplicationContext(),"Exists",Toast.LENGTH_SHORT).show();
//                } else {
//                    myref.setValue(members);
//                    //Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if (databaseMembers != null) {
//            for (int cnt = 0; cnt < mSelectedMembers.size(); cnt++) {
//                if (!databaseMembers.contains(mSelectedMembers.get(cnt))) {
//                    databaseMembers.add(mSelectedMembers.get(cnt));
//                    DatabaseReference dbref1 = dbref.child(getString(R.string.db_name))
//                            .child(getString(R.string.user_members))
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    dbref1.child(mSelectedMembers.get(cnt)).setValue("0.00");
//                }
//            }
//        }




    }
}
