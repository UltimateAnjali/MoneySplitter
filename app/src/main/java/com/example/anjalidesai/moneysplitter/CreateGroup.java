package com.example.anjalidesai.moneysplitter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class CreateGroup extends Activity{

    private AutoCompleteTextView mMemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mMemberName = (AutoCompleteTextView) findViewById(R.id.memberATV);
        mMemberName.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.single_contact,R.id.tv_ContactName,getAllContactNames()));
    }

    private List<String> getAllContactNames(){
        List<String> lContactNamesList = new ArrayList<String>();
        try{
            Cursor lPeople = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if(lPeople != null){
                while (lPeople.moveToNext()){
                    lContactNamesList.add(lPeople.getString(lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            }
        }catch (NullPointerException e){
            Log.e("getAllContactNames()",e.getMessage());
        }
        return lContactNamesList;
    }
}

