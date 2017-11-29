package com.proj.balance.money;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

   //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference();

    MyPrefManager prefManager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button loginBtn;
    private TextView appname;
    private int[] layouts;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private MyFonts fontType;

    GoogleApiClient mGoogleApiClient;
    static final int RC_SIGN_IN = 9001;
    private static final String TAG = "--Login Activity--";
    FirebaseAuth mAuth;
    //FirebaseAuth.AuthStateListener mAuthStateListener;
    public UserData userData;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*-------------------------------------------Design Part-------------------------------------------*/
        //Intro Slider
        prefManager = new MyPrefManager(LoginActivity.this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        /*if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }*/

        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        appname = (TextView)findViewById(R.id.appName);

        //Setting typeface
        fontType = new MyFonts(getApplicationContext());
        appname.setTypeface(fontType.getMerri());

        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3};

        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        /*-------------------------------------End of Design Part------------------------------------------*/

        /*-----------------------------------Google Sign In Part-------------------------------------------*/
        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance();

        //Configure database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        checkSignIn();

        loginBtn.setOnClickListener(this);
        /*---------------------------------End of Google Sign In Part--------------------------------------*/
        /*----------------------------------End of OnCreate method-----------------------------------------*/
    }

    public void checkSignIn(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null && FirebaseAuth.getInstance().getCurrentUser().getUid()!=null){
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference readRef = dbref.child("moneySplit")
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        if(userData.getDataflag() == null){
                            Intent intent = new Intent(getApplicationContext(),ContactInfo.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }else{
                        Log.d(TAG,"User doesn't exist");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG,"Error occured");
                    Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                //Snackbar snackbar = Snackbar.make(coord)
                Toast.makeText(this,"Sign In Failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"signInWithCredential:onComplete: "+task.isSuccessful());
                        if(!task.isSuccessful()){
                            Log.w(TAG,"signInWithCredential",task.getException());
                            Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Authentication Success", Toast.LENGTH_LONG).show();
                            final Query query = mDatabase.child("moneySplit").child("users");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                       // Toast.makeText(getApplicationContext(),"Has",Toast.LENGTH_SHORT).show();
                                        userData = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(UserData.class);
                                        if(userData.getDataflag() == null){
                                            Intent intent = new Intent(getApplicationContext(),ContactInfo.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else {
                                       // Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();
                                        userData = new UserData(acct.getDisplayName(),
                                                acct.getGivenName(),
                                                acct.getFamilyName(),
                                                acct.getEmail(),
                                                acct.getId(),
                                                String.valueOf(acct.getPhotoUrl()));
                                        userData.setFirebaseUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mDatabase.child("moneySplit")
                                                .child("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userData);
                                        Intent intent = new Intent(getApplicationContext(),ContactInfo.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void AddUser(final String googleId, final Context context){

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactiveDot));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.activeDot));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /*----------------------------------Implementing ViewPager-----------------------------------------*/
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /*----------------------------------Adapter for ViewPager-----------------------------------------*/
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
