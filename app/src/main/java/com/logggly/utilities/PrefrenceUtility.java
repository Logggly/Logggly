package com.logggly.utilities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.logggly.LoggglyApplication;

/**
 * Created by Hafiz Waleed Hussain on 12/29/2014.
 */
public class PrefrenceUtility {

    public static final String AVAILABLE_CONTACT_TONE_MESSAGE = "available_contact_tone_message";
    public static final String TASK_CURRENT_ID = "task_current_id";

    public static final void setTaskCurrentId(long id){
        getSharedPreferenceEditor().putLong(TASK_CURRENT_ID,id).commit();
    }

    public static final long getTaskCurrentId(){
        return getSharedPreference().getLong(TASK_CURRENT_ID,0);
    }





    /**
     * This method is only access able in class and use to create a
     * Application level shared preference object.
     * @return SharedPreference object.
     */
    private static final SharedPreferences getSharedPreference(){
        return PreferenceManager.getDefaultSharedPreferences(LoggglyApplication.CONTEXT);
    }

    /**
     * This method is only access able in class and use to create a
     * Application level shared preference editor object.
     * @return SharedPreference.Editor object.
     */
    private static final SharedPreferences.Editor getSharedPreferenceEditor(){
        return getSharedPreference().edit();
    }


}
