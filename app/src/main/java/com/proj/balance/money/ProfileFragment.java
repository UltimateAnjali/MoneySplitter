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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView imageView;
    TextView username;
    EditText useremail;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (ImageView)view.findViewById(R.id.profile_image);
        username = (TextView)view.findViewById(R.id.user_name);
        useremail = (EditText) view.findViewById(R.id.user_email);

        username.setText(UserData.userGivenName+" "+UserData.userFamilyName);
        useremail.setText(UserData.userEmail);

        /*Glide.with(getContext()).load(Uri.parse(UserData.userPhoto))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);*/

        Picasso.with(getContext())
                .load(Uri.parse(UserData.userPhoto))
                .noFade()
                .transform(new CircleTransform())
                .into(imageView);
        return view;
    }

}
