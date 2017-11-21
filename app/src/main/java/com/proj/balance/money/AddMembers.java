package com.proj.balance.money;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AddMembers extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private AddMembersAdapter membersAdapter;
    private List<AddMembersData> membersDataList;
    private List<AddMembersData> checkDataList = new ArrayList<>();
    private static final String TAG = "--Add Members--";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    AddMembersData data;
    String temp;
    int xy;
    int size;
    int loopCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

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
            prepareAlbums();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                checkpermission();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareAlbums() {

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
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);

                        data = new AddMembersData(displayName,displayNumber);
                        checkDataList.add(data);
                        /*DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                        Query query = dbref.child("moneySplit").child("users")
                                .orderByChild("userContact").equalTo(displayNumber);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot contact:dataSnapshot.getChildren()){
                                        if(contact.getValue(UserData.class).getUserContact().equals(displayNumber)){
                                            data = new AddMembersData(displayName,displayNumber);
                                            membersDataList.add(data);
                                            Toast.makeText(getApplicationContext(),"aai gayu",Toast.LENGTH_LONG).show();
                                            //Log.i(TAG,contact.toString());
                                        }
                                    }
                                }
                                else {
                                    data = new AddMembersData("Anjali","xxx");
                                    membersDataList.add(data);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                        //Log.i(TAG, "Name: " + displayName);
                        //Log.i(TAG, "Phone Number: " + displayNumber);

                        //haven't seen this number yet: do something with this contact!
                    } else {
                        //don't do anything with this contact because we've already found this number
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
        /*ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if((cursor!=null ? cursor.getCount() : 0) > 0){
            while (cursor != null && cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    Cursor pCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (pCursor.moveToNext()){
                        String phoneNum = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int cntlength = phoneNum.length();
                        if(cntlength > 10){
                            int lel = cntlength-10;
                            temp = phoneNum.substring(lel);
                        }
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNum);
                        Log.i(TAG, "Temp: "+temp);
                        data = new AddMembersData(name,phoneNum);
                        membersDataList.add(data);
                    }
                    pCursor.close();
                }
            }
        }
        /*data = new AddMembersData("Anjali","0123456789");
        membersDataList.add(a);
        a = new AddMembersData("Anjali","0123456789");
        membersDataList.add(a);
        a = new AddMembersData("Anjali","0123456789");
        membersDataList.add(a);
        a = new AddMembersData("Anjali","0123456789");
        membersDataList.add(a);*/

        membersAdapter.notifyDataSetChanged();
    }

    private void CheckExist() {


        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Query query = dbref.child("moneySplit").child("users")
                .orderByChild("userContact").equalTo(checkDataList.get(loopCount).getContactNum());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot contact:dataSnapshot.getChildren()){

                        UserData userData = contact.getValue(UserData.class);

                            data = new AddMembersData(userData.getUserName(),userData.getUserContact());
                            membersDataList.add(data);
                            membersAdapter.notifyDataSetChanged();
                           if(LoopHandling())
                            {
                                break;
                            }
                            Toast.makeText(getApplicationContext(),"aai gayu"+membersDataList.get(0).getPersonName(),Toast.LENGTH_LONG).show();
                        System.out.println("------------>membersDataList "+membersDataList.get(0).getPersonName());
                            //Log.i(TAG,contact.toString());

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
        if(loopCount<size-1)
        {
            loopCount++;
            CheckExist();
            return false;
        }else{
            return true;
        }

    }

    /*public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmembers_menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(),"Lel",Toast.LENGTH_SHORT).show();
        return true;
    }
}
