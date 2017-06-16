package com.example.anjalidesai.moneysplitter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGroup extends AppCompatActivity{

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    AutoCompleteTextView mMemberName = null;

    private ArrayList<Map<String, String>> arrayListMap;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mMemberName = (AutoCompleteTextView)findViewById(R.id.memberATV);

        arrayListMap = new ArrayList<Map<String, String>>();

        PopulatePeopleList();

        simpleAdapter = new SimpleAdapter(this, arrayListMap, R.layout.cust_contact_view,
                new String[] { "Name", "Phone", "Type" }, new int[] {
                R.id.ccontName, R.id.ccontNo, R.id.ccontType });
        mMemberName.setAdapter(simpleAdapter);
        mMemberName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index,
                                    long arg3) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                String name  = map.get("Name");
                String number = map.get("Phone");
                mMemberName.setText(""+name+"<"+number+">");
            }
        });
    }

    public  void PopulatePeopleList(){
        arrayListMap.clear();
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (people.moveToNext()) {
                String contactName = people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
                String hasPhone = people.getString(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if ((Integer.parseInt(hasPhone) > 0)) {
                    // You know have the number so now query it like this
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        //store numbers and display a dialog letting the user select which.
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String numberType = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        Map<String, String> NamePhoneType = new HashMap<String, String>();
                        NamePhoneType.put("Name", contactName);
                        NamePhoneType.put("Phone", phoneNumber);
                        if (numberType.equals("0"))
                            NamePhoneType.put("Type", "Work");
                        else if (numberType.equals("1"))
                            NamePhoneType.put("Type", "Home");
                        else if (numberType.equals("2"))
                            NamePhoneType.put("Type", "Mobile");
                        else
                            NamePhoneType.put("Type", "Other");
                        //Then add this map to the list.
                        arrayListMap.add(NamePhoneType);
                    }
                    phones.close();
                }
            }
            people.close();
            startManagingCursor(people);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                PopulatePeopleList();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

