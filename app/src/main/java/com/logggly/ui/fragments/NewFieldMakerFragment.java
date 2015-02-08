package com.logggly.ui.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.logggly.R;
import com.logggly.adapters.NewFieldMakerAdapter;
import com.logggly.databases.DatabaseContract;
import com.logggly.models.NewFieldMakerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.logggly.databases.DatabaseContract.AdditionalFieldsJSONManager;

/**
 * Created by Hafiz Waleed Hussain on 2/7/2015.
 */
public class NewFieldMakerFragment extends AbstractLoggglyFragment{

    private static final String TAG_NAME = "tagName";

    public static final NewFieldMakerFragment newInstance(String tagName){
        NewFieldMakerFragment newFieldMakerFragment = new NewFieldMakerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_NAME,tagName);
        newFieldMakerFragment.setArguments(bundle);
        return newFieldMakerFragment;
    }

    private EditText mFieldNameEditText;
    private Spinner mFieldTypeSpinner;
    private Button mAddButton;
    private ListView mFieldsListView;
    private Button mDoneButton;
    private NewFieldMakerAdapter mListAdapter;

    private String mTagName;

    private ArrayAdapter mSpinnerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] data = getResources().getStringArray(R.array.field_types);
        mSpinnerAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                data);
        mListAdapter = new NewFieldMakerAdapter(getActivity());

        mTagName = getArguments().getString(TAG_NAME);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_field_maker,container,false);
        mFieldNameEditText = (EditText) view.findViewById(R.id.FragmentNewFieldCreator_field_name_edittext);
        mFieldTypeSpinner = (Spinner) view.findViewById(R.id.FragmentNewFieldCreator_field_type_spinner);
        mFieldTypeSpinner.setAdapter(mSpinnerAdapter);
        mAddButton = (Button) view.findViewById(R.id.FragmentNewFieldCreator_add_button);
        mAddButton.setOnClickListener(mAddButtonOnClickListener);
        mDoneButton = (Button) view.findViewById(R.id.FragmentNewFieldCreator_done_button);
        mDoneButton.setOnClickListener(mDoneButtonClick);
        mFieldsListView = (ListView) view.findViewById(R.id.FragmentNewFieldCreator_listview);
        mFieldsListView.setAdapter(mListAdapter);
        mFieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewFieldMakerModel newFieldMakerModel = mListAdapter.getItem(position);
                mListAdapter.remove(newFieldMakerModel);
                mListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private View.OnClickListener mAddButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mFieldName = mFieldNameEditText.getText().toString().trim();
            String mFieldType = mFieldTypeSpinner.getSelectedItem().toString();
            if(mFieldName.isEmpty()){
                Toast.makeText(getActivity(),getString(R.string.please_enter_field_name),Toast.LENGTH_SHORT).show();
                return;
            }

            mListAdapter.add(new NewFieldMakerModel(mFieldName,mFieldType));
            mFieldNameEditText.setText("");

        }
    };

    private View.OnClickListener mDoneButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < mListAdapter.getCount();i++){
                NewFieldMakerModel newFieldMakerModel = mListAdapter.getItem(i);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(AdditionalFieldsJSONManager.FIELD_NAME,
                        newFieldMakerModel.getFieldName());
                jsonObject.put(AdditionalFieldsJSONManager.FIELD_TYPE,
                        newFieldMakerModel.getFieldType());
                jsonArray.put(jsonObject);
            }

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContract.Tags.COLUMN_NAME, mTagName);
                contentValues.put(DatabaseContract.Tags.COLUMN_ADDITIONAL_FIELDS, jsonArray.toString());
                Uri uri = getActivity().getContentResolver().insert(DatabaseContract.Tags.CONTENT_URI,contentValues);
                if(ContentUris.parseId(uri) > 0){
                    Toast.makeText(getActivity(),R.string.new_tag_created_successfully,Toast.LENGTH_SHORT).show();
                }
                getActivity().onBackPressed();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


}
