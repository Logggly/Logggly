package com.logggly.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.logggly.R;
import com.logggly.ui.fragments.MainDataFragment;


public class MainActivity extends AbstractLoggglyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.MainActivity_container_framelayout
                    , MainDataFragment.newInstance())
                    .commit();
        }
    }


}
