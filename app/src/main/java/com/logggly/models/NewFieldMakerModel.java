package com.logggly.models;

/**
 * Created by Hafiz Waleed Hussain on 2/7/2015.
 */
public class NewFieldMakerModel {

    private String mFieldName;
    private String mFieldType;

    public NewFieldMakerModel(String fieldName, String fieldType) {
        mFieldName = fieldName;
        mFieldType = fieldType;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public String getFieldType() {
        return mFieldType;
    }

    @Override
    public String toString() {
        return "NewFieldMakerModel{" +
                "mFieldName='" + mFieldName + '\'' +
                ", mFieldType='" + mFieldType + '\'' +
                '}';
    }
}
