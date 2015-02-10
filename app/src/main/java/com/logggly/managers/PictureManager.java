package com.logggly.managers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.logggly.R;
import com.logggly.ui.fragments.PictureCaptureFragment;

/**
 * Created by Hafiz Waleed Hussain on 2/4/2015.
 */
public class PictureManager {

    private FragmentManager mFragmentManager;
    private PictureCaptureFragment.Callback mCallback;

    public PictureManager(FragmentManager fragmentManager, PictureCaptureFragment.Callback callback) {
        mFragmentManager = fragmentManager;
        mCallback = callback;
    }

    public View.OnClickListener getImageViewOnClickListener() {
        return mSetImageViewOnClickListener;
    }


    private View.OnClickListener mSetImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            PictureCaptureFragment fragment = PictureCaptureFragment.newInstance();
            fragment.setCallback(mCallback);
            fragmentTransaction.add(R.id.MainActivity_container_framelayout
                    , fragment)
                    .addToBackStack(null)
                    .commit();
        }
    };


}
