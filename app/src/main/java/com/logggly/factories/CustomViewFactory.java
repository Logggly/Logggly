package com.logggly.factories;

import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.databases.DatabaseContract;

import org.json.JSONObject;

/**
 * Created by Hafiz Waleed Hussain on 2/8/2015.
 */
public class CustomViewFactory {


    public static void createAndAttachCustomView(JSONObject jsonObject,TableLayout parent){
        String field = jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE);
        Context context = parent.getContext();

        if(field.equals(context.getString(R.string.alphanumeric))){
            createAlphanumericView(jsonObject, parent);
        }
        else if(field.equals(context.getString(R.string.numeric))){
            createNumericView(jsonObject, parent);
        }
        else if(field.equals(context.getString(R.string.url))){
            createUrlView(jsonObject, parent);
        }
        else if(field.equals(context.getString(R.string.duration))){
            createDurationView(jsonObject, parent);
        }
        else if(field.equals(context.getString(R.string.picture))){
            createPictureView(jsonObject, parent);
        }
    }

    private static void createDurationView(JSONObject jsonObject, TableLayout parent) {
        ViewGroup viewGroup = getViewGroupWithInitialization(jsonObject,
                R.layout.custom_view_table_row_duration,
                parent);

        TextView startDateTextView = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_start_time);
        TextView endDateTextView = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_end_time);

        TextView editText = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_chronometer_textview);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(getFieldName(jsonObject));

    }


    private static void createAlphanumericView(JSONObject jsonObject, TableLayout parent) {
        ViewGroup viewGroup = getViewGroupWithInitialization(jsonObject,
                R.layout.custom_view_table_row_alphanumeric,
                parent);

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(getFieldName(jsonObject));
    }


    private static void createNumericView(JSONObject jsonObject, TableLayout parent) {
        ViewGroup viewGroup = getViewGroupWithInitialization(jsonObject,
                R.layout.custom_view_table_row_alphanumeric,
                parent);

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,-"));
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(getFieldName(jsonObject));
    }


    private static void createUrlView(JSONObject jsonObject, TableLayout parent) {
        ViewGroup viewGroup = getViewGroupWithInitialization(jsonObject,
                R.layout.custom_view_table_row_url,
                parent);

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(getFieldName(jsonObject));

    }

    private static void createPictureView(JSONObject jsonObject, TableLayout parent) {
        ViewGroup viewGroup = getViewGroupWithInitialization(jsonObject,
                R.layout.custom_view_table_row_picture,
                parent);

        ImageView editText = (ImageView) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(getFieldName(jsonObject));

    }


    // Helper methods

    private static ViewGroup getViewGroupWithInitialization(JSONObject jsonObject,int layoutId, TableLayout parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(layoutId,parent,true);

        String fieldName = getFieldName(jsonObject);
        setHeadingTextView(viewGroup, fieldName);
        return viewGroup;
    }

    private static void setHeadingTextView(ViewGroup viewGroup, String fieldName) {
        String headingText = fieldName+":";
        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(headingText);
        textView.setTag(headingText);
    }

    private static String getFieldName(JSONObject jsonObject) {
        return jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME);
    }
}
