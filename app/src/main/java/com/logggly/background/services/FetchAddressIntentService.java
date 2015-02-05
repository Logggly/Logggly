package com.logggly.background.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.logggly.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

    public static final String TAG = FetchAddressIntentService.class.getSimpleName();

    protected ResultReceiver mResultReceiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        List<Address> addresses = null;

        mResultReceiver = (ResultReceiver) intent.getParcelableExtra(Constants.RECEIVER);
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException e) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG,errorMessage,e);
        } catch (IllegalArgumentException e){
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), e);
        }

        if( addresses == null || addresses.size() == 0){
            if( errorMessage.isEmpty()){
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG,errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }else{
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mResultReceiver.send(resultCode,bundle);
    }

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.logggly";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }
}
