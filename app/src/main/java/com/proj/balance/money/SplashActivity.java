package com.proj.balance.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final String TAG = "--Splash Activity--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null && FirebaseAuth.getInstance().getCurrentUser().getUid()!=null){
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference readRef = dbref.child("moneySplit").child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        if(userData.dataflag.equals(false)){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(SplashActivity.this,ContactInfo.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }
                            },SPLASH_DISPLAY_LENGTH);
                        }
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(SplashActivity.this,AddMembers.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }
                            },SPLASH_DISPLAY_LENGTH);
                        }

                    }else{
                        FirebaseAuth.getInstance().signOut();
                        Log.d(TAG,"User doesn't exist");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                            }
                        },SPLASH_DISPLAY_LENGTH);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
        }
    }
}
