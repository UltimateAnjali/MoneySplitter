package capital.handout.paisa.moneysplitter;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;
import static android.Manifest.*;

public class CreateGroup extends Activity {

    private AutoCompleteTextView mMemberName;
    private ListView memberList;
    public static int PERMISSION_REQUEST_CONTACT = 0;
    private static ArrayList<String> moji = new ArrayList<String>();

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
}

