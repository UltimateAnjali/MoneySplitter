package com.example.anjalidesai.moneysplitter;

import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import static android.Manifest.*;

public class CreateGroup extends Activity {

    private AutoCompleteTextView mMemberName;
    public static int PERMISSION_REQUEST_CONTACT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        checkContactPermission();
    }

    void checkContactPermission(){
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

    public void setData(){
        mMemberName = (AutoCompleteTextView) findViewById(R.id.memberATV);
        mMemberName.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.single_contact, R.id.tv_ContactName, getAllContactNames()));
    }

    private void requestContactPermission(){
        ActivityCompat.requestPermissions(this,new String[]{permission.READ_CONTACTS},PERMISSION_REQUEST_CONTACT);
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
}

