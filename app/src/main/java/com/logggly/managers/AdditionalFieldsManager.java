package com.logggly.managers;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.customviewsmodels.AdditionalFieldAlphaNumericModel;
import com.logggly.customviewsmodels.AdditionalFieldDurationModel;
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

    private HashMap<String,AdditionalFieldDurationModel> mAdditionalFieldDurationModelHashMap = new HashMap<>();
    private HashMap<String,AdditionalFieldAlphaNumericModel> mAdditionalFieldAlphaNumericModelHashMap = new HashMap<>();

    public AdditionalFieldsManager(Context context, TableLayout parentLayout, FragmentManager fragmentManager) {
        mContext = context;
        mParentLayout = parentLayout;
        mFragmentManager = fragmentManager;
    }

    public void init(JSONArray additionalFieldJSONArray) {
        for (int i = 0 ; i < additionalFieldJSONArray.length();i++){
            JSONObject jsonObject = additionalFieldJSONArray.optJSONObject(i);
            String field = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE);
            String fieldName = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME);

            if(field.equals(mContext.getString(R.string.alphanumeric))){
                EditText editText = (EditText) mParentLayout.findViewWithTag(fieldName);
                String text = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                editText.setText(text);
                mAdditionalFieldAlphaNumericModelHashMap.put(fieldName,
                        new AdditionalFieldAlphaNumericModel(mFragmentManager,editText));
            }
            else if(field.equals(mContext.getString(R.string.url))){

            }
            else if(field.equals(mContext.getString(R.string.duration))){
                TextView textView = (TextView) mParentLayout.findViewWithTag(fieldName);
                String calendarAsString = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
                Calendar calendar = Calendar.getInstance();
                if(calendarAsString != null && !calendarAsString.isEmpty()){
                    calendar.setTimeInMillis(Long.parseLong(calendarAsString));
                }
                mAdditionalFieldDurationModelHashMap.put(fieldName,
                        new AdditionalFieldDurationModel(mFragmentManager,
                                textView, calendar));

            }
        }
    }

    public String getJSONArrayWithDataForSaveAsString() {
        JSONArray jsonArray = new JSONArray();
        parseDurationHashMaptoJSONArray(jsonArray);
        parseAlphaNumericHashMaptoJSONArray(jsonArray);



        return jsonArray.toString();
    }

    private void parseDurationHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldDurationModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldDurationModel> entry = (Map.Entry<String, AdditionalFieldDurationModel>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldDurationModel additionalFieldDurationModel = entry.getValue();
            try {
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME,
                        additionalFieldDurationModel.getFieldName());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE,
                        additionalFieldDurationModel.getFieldType());
                jsonObject.put(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA,
                        additionalFieldDurationModel.getFieldData());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parseAlphaNumericHashMaptoJSONArray(JSONArray jsonArray) {
        Iterator iterator = mAdditionalFieldAlphaNumericModelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AdditionalFieldAlphaNumericModel> entry = (Map.Entry<String, AdditionalFieldAlphaNumericModel>) iterator.next();
            JSONObject jsonObject = new JSONObject();
            AdditionalFieldAlphaNumericModel additionalFieldAlphaNumericModel= entry.getValue();
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
    }

}
