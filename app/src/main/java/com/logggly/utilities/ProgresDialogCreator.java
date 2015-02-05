package com.logggly.utilities;

import android.app.ProgressDialog;

import com.logggly.ui.activities.AbstractLoggglyActivity;

/**
 * Created by waleed on 06/09/2014.
 */
public class ProgresDialogCreator {


    public static ProgressDialog create(){
        return create("","",false);
    }

    public static ProgressDialog create(int title, int message, boolean isCancelable){
        String stringTitle = AbstractLoggglyActivity.BASE_ACTIVITY_CONTEXT.getString(title);
        String stringMessage = AbstractLoggglyActivity.BASE_ACTIVITY_CONTEXT.getString(message);
        return create(stringTitle,stringMessage,isCancelable);
    }

    public static ProgressDialog create(String title, String message, boolean isCancelable){
        ProgressDialog progressDialog = new ProgressDialog(AbstractLoggglyActivity.BASE_ACTIVITY_CONTEXT);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(isCancelable);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }
}
