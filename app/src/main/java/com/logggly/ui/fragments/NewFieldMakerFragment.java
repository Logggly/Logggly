package com.logggly.ui.fragments;

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
import com.logggly.models.NewFieldMakerModel;

import java.util.ArrayList;

/**
 * Created by Hafiz Waleed Hussain on 2/7/2015.
 */
public class NewFieldMakerFragment extends AbstractLoggglyFragment{


    public static final NewFieldMakerFragment newInstance(){
        return new NewFieldMakerFragment();
    }

    private EditText mFieldNameEditText;
    private Spinner mFieldTypeSpinner;
    private Button mAddButton;
    private ListView mFieldsListView;
    private Button mDoneButton;
    private NewFieldMakerAdapter mListAdapter;

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

            Toast.makeText(getActivity(), mListAdapter.getCount()+" : count",Toast.LENGTH_SHORT).show();
            ArrayList<NewFieldMakerModel> data = new ArrayList<>(mListAdapter.getCount());
            for (int i = 0; i < data.size();i++){
                data.add(i,mListAdapter.getItem(i));
                Toast.makeText(getActivity(), data.get(i).toString(),Toast.LENGTH_SHORT).show();
            }

        }
    };


}
