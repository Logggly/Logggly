package com.logggly.customviewsmodels;

import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.logggly.R;

/**
 * Created by Hafiz Waleed Hussain on 2/9/2015.
 */
public class AdditionalFieldAlphaNumericModel {
    private EditText mEditText;

    public AdditionalFieldAlphaNumericModel (FragmentManager fragmentManager, EditText editText) {
        mEditText = editText;
    }

    public String getFieldName(){
        return mEditText.getTag().toString();
    }

    public String getFieldType(){
        return mEditText.getContext().getResources().getString(R.string.alphanumeric);
    }

    public String getFieldData(){
        return mEditText.getText().toString();
    }
}
