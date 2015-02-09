package com.logggly.ui.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TableLayout;

import com.logggly.factories.CustomViewFactory;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hafiz Waleed Hussain on 2/8/2015.
 */
public class CustomViewCreator {
// [
//      {"fieldName":"tet","fieldType":"Alphanumeric"},
//      {"fieldName":"gs","fieldType":"Documents"},
//      {"fieldName":"yrj","fieldType":"Audio"}
// ]

    public static final void createViewForJSONArray(Context context,TableLayout parent,
                                                    JSONArray jsonArray,
                                                    int compulsoryViewsCount){
        removeAdditionalViews(parent, compulsoryViewsCount);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            CustomViewFactory.createAndAttachCustomView(jsonObject, parent);
        }

    }

    public static void removeAdditionalViews(TableLayout parent, int compulsoryViewsCount) {
        if(compulsoryViewsCount != parent.getChildCount()){
            for (int i = parent.getChildCount()-1; i>=compulsoryViewsCount ;i--){
                parent.removeViewAt(i);
            }
        }
    }
}
