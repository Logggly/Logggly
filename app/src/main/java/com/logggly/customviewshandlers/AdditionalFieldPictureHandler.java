package com.logggly.customviewshandlers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.managers.PictureManager;
import com.logggly.ui.fragments.PictureCaptureFragment;

import java.io.IOException;

/**
 * Created by Hafiz Waleed Hussain on 2/9/2015.
 */
public class AdditionalFieldPictureHandler extends AbstractAdditionalField{
    private ImageView mImageView;
    private PictureManager mPictureManager;
    private String mPath ;

    public AdditionalFieldPictureHandler(FragmentManager fragmentManager,
                                         TextView headerTextView,
                                         ImageView imageView,
                                         String path) {
        super(fragmentManager, headerTextView);
        mImageView = imageView;
        mPath = path;
        mPictureManager = new PictureManager(fragmentManager,mCallback);
        mImageView.setOnClickListener(mPictureManager.getImageViewOnClickListener());
        setImageView();
    }

    private void setImageView() {
        if(mPath!=null && !mPath.isEmpty()){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mImageView.getContext().getContentResolver(), Uri.parse(mPath));
                mImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,48,48,false));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getFieldName(){
        return mImageView.getTag().toString();
    }

    public String getFieldType(){
        return mImageView.getContext().getResources().getString(R.string.picture);
    }

    public String getFieldData(){
        return mPath;
    }

    private PictureCaptureFragment.Callback mCallback = new PictureCaptureFragment.Callback() {

        @Override
        public void imageUri(Uri uri) {
            mPath = uri.toString();
            setImageView();
        }
    };
}
