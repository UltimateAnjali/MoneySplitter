package capital.handout.paisa.moneysplitter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anjali desai on 05-10-2017.
 */

public class MyPrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "moneysplitter_pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public MyPrefManager(Context mContext){
        this._context = mContext;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        boolean myValue = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        System.out.println("--------------------->>>>>>>>>>"+myValue);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
