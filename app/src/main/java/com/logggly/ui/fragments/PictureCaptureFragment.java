package com.logggly.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.logggly.R;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Hafiz Waleed Hussain on 2/10/2015.
 */
public class PictureCaptureFragment extends AbstractLoggglyFragment{


    public static PictureCaptureFragment newInstance(){
        PictureCaptureFragment pictureCaptureFragment = new PictureCaptureFragment();
        return pictureCaptureFragment;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private Uri mHighQualityImageUri = null;
    private final int REQUEST_CODE_HIGH_QUALITY_IMAGE = 2;
    private Callback mCallback;

    public static interface Callback extends Serializable {
        void imageUri(Uri uri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHighQualityImageUri = generateTimeStampPhotoFileUri(getActivity());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CODE_HIGH_QUALITY_IMAGE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_HIGH_QUALITY_IMAGE:
                    mCallback.imageUri(mHighQualityImageUri);
                    break;
                default:
                    break;
            }
        }
        getActivity().onBackPressed();
    }





    private Uri generateTimeStampPhotoFileUri(Context context) {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory(context);
        if (outputDir != null) {
            Time t = new Time();
            t.setToNow();
            File photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }

    private File getPhotoDirectory(Context context) {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, context.getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            context,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }


}
