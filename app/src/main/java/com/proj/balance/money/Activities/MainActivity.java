package com.proj.balance.money.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.proj.balance.money.Fragments.GroupFragment;
import com.proj.balance.money.Fragments.PersonOwingsFragment;
import com.proj.balance.money.Fragments.ProfileFragment;
import com.proj.balance.money.R;

public class MainActivity extends AppCompatActivity  {

    private GoogleApiClient mGoogleApiClient;
    private ActionBarDrawerToggle myDrawerToggle;
    private NavigationView mynavigationview;
    private DrawerLayout myDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
//        DrawerLayout myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
//        myDrawerLayout.addDrawerListener(myDrawerToggle);
//        myDrawerToggle.syncState();
//        mynavigationview=(NavigationView) findViewById(R.id.nav_view);
//        mynavigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//
//                    case R.id.action_create_group: {
//                        Intent intent = new Intent(getApplicationContext(), CreateGroup.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    }
//                    case R.id.action_sign_out: {
//                        signOut();
//                        break;
//                    }
//                }
//
//                return false;
//            }
//        });



//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.groups:
                        selectedFragment = GroupFragment.newInstance();
                        break;
                    case R.id.owings:
                        selectedFragment = PersonOwingsFragment.newInstance();
                        break;
                    case R.id.profile:
                        selectedFragment = ProfileFragment.newInstance();
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, GroupFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_create_group: {
                Intent intent = new Intent(getApplicationContext(), CreateGroup.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.action_sign_out: {
                signOut();
                break;
            }
        }
        return false;
//        if (myDrawerToggle.onOptionsItemSelected(item)) {
//
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
