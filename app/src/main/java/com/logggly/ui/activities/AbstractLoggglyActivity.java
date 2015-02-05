package com.logggly.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public abstract class AbstractLoggglyActivity extends FragmentActivity{

    public static Context BASE_ACTIVITY_CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ACTIVITY_CONTEXT = this;
    }
}
