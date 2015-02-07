package com.logggly.ui.fragments;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.logggly.R;
import com.logggly.background.services.FetchAddressIntentService;
import com.logggly.databases.DatabaseContract;
import com.logggly.managers.DateTimeManager;
import com.logggly.managers.TagAdapterManager;
import com.logggly.utilities.DateTimeFormatter;
import com.logggly.utilities.PrefrenceUtility;
import com.logggly.utilities.TimeOfDayUtility;
import com.logggly.utilities.VoiceUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public class MainDataFragment extends AbstractLoggglyFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        DateFragment.Callback,
        TimeFragment.Callback{

    public static final String TAG = MainDataFragment.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private TextView mVoiceStatusTextView;

    public static final MainDataFragment newInstance(){
        MainDataFragment mainDataFragment = new MainDataFragment();
        return mainDataFragment;
    }

    private TextView mAddressTextView;
    private ProgressBar mAddressProgressBar;
    private AutoCompleteTextView mTagEditText;
    private EditText mNotesEditText;
    private Button mSaveButton;
    private Button mReportButton;
    private TextView mSetDateTextView;
    private TextView mSetTimeTextView;
    private TextView mIdTextView;
    private TableRow mMetaDateTableRow;
    private TextView mMetaDateTextView;
    private TableRow mMetaTimeTableRow;
    private TextView mMetaTimeTextView;
    private Button mExpandButton;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private AddressResultReceiver mAddressResultReceiver;
    private Calendar mCalendar;
    private DateTimeManager mDateTimeManager;
    private TagAdapterManager mTagAdapterManager;
//    private VoiceUtility mVoiceUtility;

    public MainDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mAddressResultReceiver = new AddressResultReceiver(new Handler());
        mCalendar = Calendar.getInstance();
        mDateTimeManager = new DateTimeManager(mCalendar,getActivity(),this);
        mTagAdapterManager = new TagAdapterManager(getActivity());
//        mVoiceUtility = VoiceUtility.getInstance(getActivity());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        mGoogleApiClient.disconnect();
        mTagAdapterManager.destroyLoader();
//        mVoiceUtility.setCallback(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_data,container,false);
        mGoogleApiClient.connect();
//        mVoiceUtility.setCallback(mVoiceUtilityCallback);
        mVoiceStatusTextView = (TextView) view.findViewById(R.id.FragmentMainData_voice_textview);
        mAddressTextView = (TextView) view.findViewById(R.id.FragmentMainData_address_textview);
        mAddressProgressBar = (ProgressBar) view.findViewById(R.id.FragmentMainData_address_progress_bar);
     
        mIdTextView = (TextView) view.findViewById(R.id.FragmentMainData_id_textview);
        mIdTextView.setText((PrefrenceUtility.getTaskCurrentId() + 1) + "");
        mTagEditText = (AutoCompleteTextView) view.findViewById(R.id.FragmentMainData_tag_edittext);
        autoCompleteTagInit();
//        mTagEditText.setOnFocusChangeListener(mOnFocusChangeListener);
        mNotesEditText = (EditText) view.findViewById(R.id.FragmentMainData_notes_edittext);
        mSaveButton = (Button) view.findViewById(R.id.FragmentMainData_save_button);
        mSaveButton.setOnClickListener(mSaveButtonClickListener);
        mReportButton = (Button) view.findViewById(R.id.FragmentMainData_report_button);
        mReportButton.setOnClickListener(mReportClickListener);
        mExpandButton = (Button) view.findViewById(R.id.FragmentMainData_expand_button);
        mExpandButton.setOnClickListener(mExpandButtonClickListener);

        mMetaDateTableRow = (TableRow)view.findViewById(R.id.MainDataFragment_meta_date_tablerow);
        mSetDateTextView = (TextView) view.findViewById(R.id.FragmentMainData_set_date_textview);
        mMetaDateTextView = (TextView) view.findViewById(R.id.FragmentMainData_meta_date_textview);
        mSetDateTextView.setOnClickListener(mDateTimeManager.getSetDateButtonOnClickListener());
        setDateTextView();

        mMetaTimeTableRow = (TableRow)view.findViewById(R.id.MainDataFragment_meta_time_tablerow);
        mSetTimeTextView = (TextView) view.findViewById(R.id.FragmentMainData_set_time_textview);
        mMetaTimeTextView = (TextView) view.findViewById(R.id.FragmentMainData_meta_time_textview);
        mSetTimeTextView.setOnClickListener(mDateTimeManager.getSetTimeButtonOnClickListener());
        setTimeTextView();

        return view;
    }

    private void autoCompleteTagInit() {
        mTagEditText.setAdapter(mTagAdapterManager.getAdapter());
        mTagAdapterManager.initLoader();
        mTagAdapterManager.getAdapter().setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(DatabaseContract.Tags.COLUMN_NAME));
            }
        });
        mTagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTagAdapterManager.restartLoaderWithFilter(s.toString());
            }
        });
    }

    public void setDateTextView() {
        mSetDateTextView.setText(DateTimeFormatter.dateFormatter(mCalendar));
        mMetaDateTextView.setText("Week Of Month"+ mCalendar.get(Calendar.WEEK_OF_MONTH));
    }

    public void setTimeTextView() {
        mSetTimeTextView.setText(DateTimeFormatter.timeFormatter(mCalendar));
        mMetaTimeTextView.setText(TimeOfDayUtility.timeOfDay(mCalendar.get(Calendar.HOUR),
                mCalendar.get(Calendar.MINUTE),mCalendar.get(Calendar.AM_PM)));
    }


    private AlertDialog mNewTagCreatorAlertDialog;
    private boolean newFieldsIsChecked = false;
    private View.OnClickListener mSaveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tagText = mTagEditText.getText().toString().trim();
            if(tagText.isEmpty()){
                tagText = getString(R.string.Null);
            }

                Cursor cursor = getActivity().getContentResolver().query(DatabaseContract.Tags.buildUriForSearchTag(tagText), null, null, null, null);
                if (cursor.getCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.new_tag));
//                    builder.setMessage(getString(R.string.new_tag));
                    builder.setMultiChoiceItems(R.array.additional_fields,new boolean[]{newFieldsIsChecked},new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            newFieldsIsChecked = isChecked;
                        }
                    });
                    final String finalTagText = tagText;
                    builder.setPositiveButton(getString(R.string.yes), newTagCreatorDialogYesClickListener);
                    builder.setNegativeButton(getString(R.string.no), newTagCreatorDialogNoClickListener);
                    mNewTagCreatorAlertDialog = builder.create();
                    mNewTagCreatorAlertDialog.show();
                }else{
                    saveTask(tagText);
                }



        }
    };

    private DialogInterface.OnClickListener newTagCreatorDialogNoClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                            mTagEditText.setText("");
                dialog.dismiss();
                newFieldsIsChecked = false;
            }
        };


    private DialogInterface.OnClickListener newTagCreatorDialogYesClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(newFieldsIsChecked){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.MainActivity_container_framelayout
                            , NewFieldMakerFragment.newInstance())
                            .addToBackStack(null)
                            .commit();

                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContract.Tags.COLUMN_NAME, mTagEditText.getText().toString().trim().toLowerCase());
                    Uri newUri = getActivity().getContentResolver().insert(DatabaseContract.Tags.CONTENT_URI, contentValues);
                    if (ContentUris.parseId(newUri) > 0) {
                        saveTask(mTagEditText.getText().toString().trim().toLowerCase());
                    }
                }
                dialog.dismiss();
                newFieldsIsChecked = false;
            }
        };


    private void saveTask(String tagText) {
        String notesText = mNotesEditText.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Tasks.COLUMN_TAG, tagText);
        contentValues.put(DatabaseContract.Tasks.COLUMN_LATITUDE,mLastLocation.getLatitude());
        contentValues.put(DatabaseContract.Tasks.COLUMN_LONGITUDE,mLastLocation.getLongitude());
        contentValues.put(DatabaseContract.Tasks.COLUMN_NOTES, notesText);
        contentValues.put(DatabaseContract.Tasks.COLUMN_LOCATION_NAME, mAddressTextView.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(mCalendar.getTimeInMillis());
        contentValues.put(DatabaseContract.Tasks.COLUMN_DATE_TIME, dateFormat.format(date));

        Uri newUri = getActivity().getContentResolver().insert(DatabaseContract.Tasks.CONTENT_URI, contentValues);
        long rowId = ContentUris.parseId(newUri);
        if( rowId > 0 ){
            Toast.makeText(getActivity(), "Task successfully saved", Toast.LENGTH_SHORT).show();
            mTagEditText.setText("");
            mTagEditText.requestFocus();
            mNotesEditText.setText("");
            mIdTextView.setText((rowId+1)+"");
            PrefrenceUtility.setTaskCurrentId(rowId);
        }else{
            Toast.makeText(getActivity(),"Task not saved",Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener mReportClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.MainActivity_container_framelayout
                    , ReportFragment.newInstance())
                    .addToBackStack(null)
                    .commit();

            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mNotesEditText.getWindowToken(), 0);
        }
    };

    private boolean isNotesWriting = false;


    private VoiceUtility.Callback mVoiceUtilityCallback = new VoiceUtility.Callback() {
        @Override
        public void voiceResult(ArrayList<String> data) {
            String[] strings = data.get(0).split(" ");

            if(mNewTagCreatorAlertDialog != null && mNewTagCreatorAlertDialog.isShowing()){
                if(strings[0].equalsIgnoreCase("yes")){
                    newTagCreatorDialogYesClickListener.onClick(mNewTagCreatorAlertDialog,0);
                }else if(strings[0].equalsIgnoreCase("no")){
                    newTagCreatorDialogNoClickListener.onClick(mNewTagCreatorAlertDialog,0);
                }
                return;
            }

            if(isNotesWriting){
                if(data.get(0).equalsIgnoreCase("endnotes") || data.get(0).equalsIgnoreCase("end notes")){
                    isNotesWriting = false;
                    Log.i(TAG, "command: "+strings[0]);
                }else{
                    Log.i(TAG, "notes: "+strings[0]);
                    StringBuilder stringBuilder = new StringBuilder(mNotesEditText.
                            getText().toString());
                    stringBuilder.append(data.get(0)).append(" ");
                    mNotesEditText.setText(stringBuilder.toString());
                    mNotesEditText.requestFocus();
                }
            }


            int sizeOfData = strings.length;
            if( sizeOfData > 0 && isNotesWriting == false){
                Log.i(TAG, "command: "+strings[0]);
                if( strings[0].equalsIgnoreCase("log")){
                    if(sizeOfData > 1){
                        Log.i(TAG,"Tag: "+strings[1]);
                        mTagEditText.requestFocus();
                        mTagEditText.setText(strings[1]);
                    }
                }else if( strings[0].equalsIgnoreCase("notes")){
                    isNotesWriting = true;
                    mTagEditText.requestFocus();
                    Log.i(TAG, "command: "+strings[0]);

                }else if( strings[0].equalsIgnoreCase("save")){
                    Log.i(TAG, "command: "+strings[0]);
                    mSaveButtonClickListener.onClick(null);
                }
            }

        }


        @Override
        public void error(String errorMessage) {
            Log.e(TAG, errorMessage);
            mVoiceStatusTextView.setText(errorMessage);
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if( mLastLocation == null){
            setAddressTextView(getString(R.string.location_not_available));
        }else{
            startFetchAddressService();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void startFetchAddressService(){
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mAddressResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if( mLastLocation == null){
            setAddressTextView(getString(R.string.location_not_available));
        }else{
            startFetchAddressService();
        }
    }

    @Override
    public void updateDateInstance(Calendar mCalendar) {
        this.mCalendar = mCalendar;
        setDateTextView();
    }

    @Override
    public void updateTimeInstance(Calendar mCalendar) {
        this.mCalendar = mCalendar;
        setTimeTextView();
    }

    class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            mAddressProgressBar.setVisibility(View.GONE);
            if(resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT){
//                mSaveButton.setEnabled(true);
            }
            String resultString = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            setAddressTextView(resultString);
            mSaveButton.setEnabled(true);

        }
    }

    private View.OnClickListener mExpandButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mMetaTimeTableRow.isShown()){
                mMetaTimeTableRow.setVisibility(View.GONE);
                mMetaDateTableRow.setVisibility(View.GONE);
            }else{
                mMetaTimeTableRow.setVisibility(View.VISIBLE);
                mMetaDateTableRow.setVisibility(View.VISIBLE);
            }
        }
    };

    private void setAddressTextView(String resultString) {
        mAddressTextView.setVisibility(View.VISIBLE);
        mAddressTextView.setText(resultString);
    }

}
