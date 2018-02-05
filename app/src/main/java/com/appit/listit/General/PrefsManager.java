package com.appit.listit.General;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by itay feldman on 17/01/2018.
 */

public class PrefsManager {



    private static String CURRENT_LIST_KEY = "current_list";

    public static void setCurrentList(String ListId){
        Prefs.putString(CURRENT_LIST_KEY , ListId);
    }

    public static String getCurrentList(){
        return Prefs.getString(CURRENT_LIST_KEY , "");
    }
}
