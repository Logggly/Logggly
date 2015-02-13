package com.logggly.customviewshandlers;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hafiz Waleed Hussain on 2/13/2015.
 */
public abstract class AbstractAdditionalField {

    public abstract String getFieldName();
    public abstract String getFieldType();
    public abstract String getFieldData();

    private TextView mHeadingTextView;

    protected FragmentManager mFragmentManager;

    protected AbstractAdditionalField(FragmentManager fragmentManager, TextView headingTextView) {
        mFragmentManager = fragmentManager;
        mHeadingTextView = headingTextView;
        mHeadingTextView.setOnClickListener(mHeaderOnClickListener);
    }

    private View.OnClickListener mHeaderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mHeadingTextView.getContext(),
                    getFieldType(),
                    Toast.LENGTH_SHORT).show();
        }
    };

}
