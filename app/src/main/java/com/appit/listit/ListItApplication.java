package com.appit.listit;

import android.content.Context;
import android.content.ContextWrapper;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.orm.SugarApp;
import com.pixplicity.easyprefs.library.Prefs;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
//import com.google.firebase.storage.FirebaseStorage;


/**
 * Created by itay feldman on 29/012/2017.
 */

public class ListItApplication extends SugarApp {

    // private static GoogleAnalytics sAnalytics;
    // private static Tracker sTracker;

    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseAnalytics mFirebaseAnalytics;
    private static GoogleSignInOptions mGso;
    //private static FirebaseStorage mFirebaseStorage;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

//        sAnalytics = GoogleAnalytics.getInstance(this);

        // Obtain the FirebaseAnalytics instance.
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();*/
        mContext = this;
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    public static Context getContext(){
        return mContext;
    }

    synchronized public FirebaseAnalytics getDefaultFirebaseAnalytics() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }

        return mFirebaseAnalytics;
    }

    synchronized public FirebaseAuth getDefaultFirebaseAuth() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (mFirebaseAuth == null) {
            mFirebaseAuth = FirebaseAuth.getInstance();
        }

        return mFirebaseAuth;
    }

    /*synchronized public GoogleSignInOptions getDefaultGoogleSignInOptions() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (mGso == null) {
            mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.google_web_client_id))
                    .requestEmail()
                    .build();
        }

        return mGso;
    }*/

}