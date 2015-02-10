package com.logggly.factories;

import android.content.Context;
import android.text.InputType;
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_view_table_row_duration,parent,true);

        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME) + ":");

        TextView startDateTextView = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_start_time);
        TextView endDateTextView = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_end_time);

        TextView editText = (TextView) viewGroup.findViewById(R.id.CustomViewDuration_chronometer_textview);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME));

    }

    private static void createAlphanumericView(JSONObject jsonObject, TableLayout parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_view_table_row_alphanumeric,parent,true);

        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME) + ":");

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME));
    }

    private static void createNumericView(JSONObject jsonObject, TableLayout parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_view_table_row_alphanumeric,parent,true);

        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME) + ":");

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME));
    }

    private static void createUrlView(JSONObject jsonObject, TableLayout parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_view_table_row_url,parent,true);

        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME) + ":");

        EditText editText = (EditText) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME));

    }

    private static void createPictureView(JSONObject jsonObject, TableLayout parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.custom_view_table_row_picture,parent,true);

        TextView textView = (TextView) viewGroup.findViewById(R.id.CustomView_heading_textview);
        textView.setId(0);
        textView.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME) + ":");

        ImageView editText = (ImageView) viewGroup.findViewById(R.id.CustomView_field);
//        editText.setText(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE));
        editText.setId(0);
        editText.setTag(jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME));

    }

}
