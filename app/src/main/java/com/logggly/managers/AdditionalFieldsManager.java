package com.logggly.managers;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.customviewshandlers.AdditionalFieldAlphaNumericHandler;
import com.logggly.customviewshandlers.AdditionalFieldDurationHandler;
import com.logggly.customviewshandlers.AdditionalFieldNumericHandler;
import com.logggly.customviewshandlers.AdditionalFieldPictureHandler;
import com.logggly.databases.DatabaseContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hafiz Waleed Hussain on 2/9/2015.
 */
public class AdditionalFieldsManager {

    private Context mContext;
    private TableLayout mParentLayout;
    private FragmentManager mFragmentManager;

    private HashMap<String,AdditionalFieldDurationHandler> mAdditionalFieldDurationModelHashMap = new HashMap<>();
    private HashMap<String,AdditionalFieldAlphaNumericHandler> mAdditionalFieldAlphaNumericModelHashMap = new HashMap<>();
    private HashMap<String,AdditionalFieldNumericHandler> mAdditionalFieldNumericModelHashMap = new HashMap<>();
    private HashMap<String,AdditionalFieldPictureHandler> mAdditionalFieldPictureModelHashMap = new HashMap<>();

    public AdditionalFieldsManager(Context context, TableLayout parentLayout,
                                   FragmentManager fragmentManager) {
        mContext = context;
        mParentLayout = parentLayout;
        mFragmentManager = fragmentManager;
    }

    public void init(JSONArray additionalFieldJSONArray) {
        for (int i = 0 ; i < additionalFieldJSONArray.length();i++){
            JSONObject jsonObject = additionalFieldJSONArray.optJSONObject(i);
            String field = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE);
            String fieldName = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME);
            TextView headerTextView = (TextView) mParentLayout.findViewWithTag(fieldName+":");

            if(field.equals(mContext.getString(R.string.alphanumeric))){
                EditText editText = (EditText) mParentLayout.findViewWithTag(fieldName);
                String text = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                editText.setText(text);
                mAdditionalFieldAlphaNumericModelHashMap.put(fieldName,
                        new AdditionalFieldAlphaNumericHandler(mFragmentManager,
                                headerTextView,
                                editText));
            }
            else if(field.equals(mContext.getString(R.string.numeric))){
                EditText editText = (EditText) mParentLayout.findViewWithTag(fieldName);
                String text = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                editText.setText(text);
                mAdditionalFieldAlphaNumericModelHashMap.put(fieldName,
                        new AdditionalFieldAlphaNumericHandler(mFragmentManager,
                                headerTextView,
                                editText));
            }

            else if(field.equals(mContext.getString(R.string.url))){

            }
            else if(field.equals(mContext.getString(R.string.duration))){
                TextView textView = (TextView) mParentLayout.findViewWithTag(fieldName);
                String startAndEndDate = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();

                if(startAndEndDate != null && !startAndEndDate.isEmpty()) {
                    String[] startEndDates = startAndEndDate.split(" ");
                    startDate.setTimeInMillis(Long.parseLong(startEndDates[0]));
                    endDate.setTimeInMillis(Long.parseLong(startEndDates[1]));
                }else{
                    startDate = Calendar.getInstance();
                    endDate = (Calendar) startDate.clone();
                }
                mAdditionalFieldDurationModelHashMap.put(fieldName,
                        new AdditionalFieldDurationHandler(mFragmentManager,
                                headerTextView,
                                textView,
                                startDate,
                                endDate));

            }
            else if(field.equals(mContext.getString(R.string.picture))){
                ImageView imageView = (ImageView) mParentLayout.findViewWithTag(fieldName);
                String text = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                mAdditionalFieldPictureModelHashMap.put(fieldName,
                        new AdditionalFieldPictureHandler(mFragmentManager,
                                headerTextView,
                                imageView,
                                text));

            }

        }
    }

    public String getJSONArrayWithDataForSaveAsString() {
        JSONArray jsonArray = new JSONArray();
        parseDurationHashMaptoJSONArray(jsonArray);
        parseAlphaNumericHashMaptoJSONArray(jsonArray);
        parseNumericHashMaptoJSONArray(jsonArray);
        parsePictureHashMaptoJSONArray(jsonArray);



        return jsonArray.toString();
    }

    private void parseDurationHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldDurationModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldDurationHandler> entry = (Map.Entry<String, AdditionalFieldDurationHandler>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldDurationHandler additionalFieldDurationHandler = entry.getValue();
            try {
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME,
                        additionalFieldDurationHandler.getFieldName());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE,
                        additionalFieldDurationHandler.getFieldType());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA,
                        additionalFieldDurationHandler.getFieldData());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parseAlphaNumericHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldAlphaNumericModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldAlphaNumericHandler> entry = (Map.Entry<String, AdditionalFieldAlphaNumericHandler>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldAlphaNumericHandler additionalFieldAlphaNumericHandler = entry.getValue();
            try {
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME,
                        additionalFieldAlphaNumericHandler.getFieldName());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE,
                        additionalFieldAlphaNumericHandler.getFieldType());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA,
                        additionalFieldAlphaNumericHandler.getFieldData());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parseNumericHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldNumericModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldNumericHandler> entry = (Map.Entry<String, AdditionalFieldNumericHandler>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldNumericHandler additionalFieldAlphaNumericModel= entry.getValue();
            try {
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME,
                        additionalFieldAlphaNumericModel.getFieldName());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE,
                        additionalFieldAlphaNumericModel.getFieldType());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA,
                        additionalFieldAlphaNumericModel.getFieldData());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parsePictureHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldPictureModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldPictureHandler> entry = (Map.Entry<String, AdditionalFieldPictureHandler>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldPictureHandler additionalFieldAlphaNumericModel= entry.getValue();
            try {
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME,
                        additionalFieldAlphaNumericModel.getFieldName());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE,
                        additionalFieldAlphaNumericModel.getFieldType());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA,
                        additionalFieldAlphaNumericModel.getFieldData());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void clear(){
        mAdditionalFieldDurationModelHashMap.clear();
        mAdditionalFieldAlphaNumericModelHashMap.clear();
        mAdditionalFieldNumericModelHashMap.clear();
        mAdditionalFieldPictureModelHashMap.clear();
    }

}
