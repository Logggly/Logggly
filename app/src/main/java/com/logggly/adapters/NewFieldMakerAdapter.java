package com.logggly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.models.NewFieldMakerModel;

import java.util.ArrayList;

/**
 * Created by Hafiz Waleed Hussain on 2/7/2015.
 */
public class NewFieldMakerAdapter extends ArrayAdapter<NewFieldMakerModel>{

    private LayoutInflater mLayoutInflater;

    private static int layoutId = R.layout.row_fragment_new_field_maker;

    public NewFieldMakerAdapter(Context context) {
        super(context, layoutId,  new ArrayList());
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if( convertView == null){
            convertView = mLayoutInflater.inflate(layoutId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mFieldNameTextView = (TextView)convertView
                    .findViewById(R.id.RowFragmentNewFieldMaker_field_name_textview);
            viewHolder.mFieldTypeTextView = (TextView)convertView
                    .findViewById(R.id.RowFragmentNewFieldMaker_field_type_textview);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewFieldMakerModel newFieldMakerModel = getItem(position);
        viewHolder.mFieldNameTextView.setText(newFieldMakerModel.getFieldName());
        viewHolder.mFieldTypeTextView.setText(newFieldMakerModel.getFieldType());

        return convertView;
    }

    private static class ViewHolder{
        TextView mFieldNameTextView;
        TextView mFieldTypeTextView;
    }
}
