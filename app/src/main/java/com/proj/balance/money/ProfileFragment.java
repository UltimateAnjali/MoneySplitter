package com.proj.balance.money;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView imageView;
    TextView username;
    EditText useremail;
    //object
    DatabaseReference dbref;
    UserData userData;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (ImageView)view.findViewById(R.id.profile_image);
        username = (TextView)view.findViewById(R.id.user_name);
        useremail = (EditText) view.findViewById(R.id.user_email);
//
        Query query = dbref.child("moneySplit").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userData = dataSnapshot.getValue(UserData.class);
                    username.setText(UserData.userGivenName+" "+UserData.userFamilyName);
                    useremail.setText(UserData.userEmail);
                    //user object for contact
                    Picasso.with(getContext())
                            .load(Uri.parse(UserData.userPhoto))
                            .noFade()
                            .transform(new CircleTransform())
                            .into(imageView);
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
