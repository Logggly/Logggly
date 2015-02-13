package com.logggly.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.logggly.R;
import com.logggly.databases.DatabaseContract;

/**
 * Created by Hafiz Waleed Hussain on 2/13/2015.
 */
public class TagsListDialogFragment extends DialogFragment{

    public static TagsListDialogFragment newInstance(){
        TagsListDialogFragment tagsListDialogFragment = new TagsListDialogFragment();
        return tagsListDialogFragment;
    }

    public static interface Callback{
        void selectedTagName(String name);
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Cursor cursor = getActivity().
                getContentResolver().
                query(DatabaseContract.Tags.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        builder.setTitle(R.string.tag);
        builder.setSingleChoiceItems(cursor,-1, DatabaseContract.Tags.COLUMN_NAME,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mCallback == null){
                    throw new RuntimeException("Set Callback");
                }
                cursor.moveToPosition(which);
                mCallback.selectedTagName(cursor.getString(cursor.getColumnIndex(DatabaseContract.Tags.COLUMN_NAME)));
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
