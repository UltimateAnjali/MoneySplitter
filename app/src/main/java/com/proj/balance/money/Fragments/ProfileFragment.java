package com.proj.balance.money.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.proj.balance.money.CircleTransform;
import com.proj.balance.money.DataModels.UserData;
import com.proj.balance.money.MyFonts;
import com.proj.balance.money.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView imageView;
    TextView username, userEmailText, userContactText;
    EditText useremail, userContact;
    Button edit, save;
    DatabaseReference dbref;
    UserData userData;
    MyFonts fontFace;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Profile");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (ImageView)view.findViewById(R.id.profile_image);
        username = (TextView)view.findViewById(R.id.user_name);
        userEmailText = (TextView)view.findViewById(R.id.user_email_tv);
        userContactText = (TextView)view.findViewById(R.id.user_contact_tv);
        useremail = (EditText) view.findViewById(R.id.user_email);
        userContact = (EditText)view.findViewById(R.id.user_contact);
        edit = (Button)view.findViewById(R.id.edit_user);
        save = (Button)view.findViewById(R.id.save_changes);

        fontFace = new MyFonts(getContext());
        username.setTypeface(fontFace.getMerri());
        userEmailText.setTypeface(fontFace.getVolk());
        useremail.setTypeface(fontFace.getMont());
        userContactText.setTypeface(fontFace.getVolk());
        userContact.setTypeface(fontFace.getMont());

        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userData = dataSnapshot.getValue(UserData.class);
                    username.setText(userData.getUserGivenName()+" "+userData.getUserFamilyName());
                    useremail.setText(userData.getUserEmail());
                    userContact.setText(userData.getUserContact());
                    //user object for contact
                    Picasso.with(getContext())
                            .load(Uri.parse(userData.getUserPhoto()+"?sz=500"))
                            .noFade()
                            .transform(new CircleTransform())
                            .into(imageView);
                    Log.e("Image","---->"+userData.getUserPhoto().toString());
                }
                else {
                    Toast.makeText(getContext(),"An error occured",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*Glide.with(getContext()).load(Uri.parse(UserData.userPhoto))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);*/


        return view;
    }

}
