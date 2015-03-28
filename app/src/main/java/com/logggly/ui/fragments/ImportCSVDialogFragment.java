package com.logggly.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.logggly.utilities.CSVUtility;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Hafiz Waleed Hussain on 3/15/2015.
 */
public class ImportCSVDialogFragment extends DialogFragment {

    public static ImportCSVDialogFragment newInstance(){
        return new ImportCSVDialogFragment();
    }

    private ListView mListView;
    private CSVUtility mCSVUtility;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCSVUtility = new CSVUtility();
        mListView = new ListView(getActivity());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,android.R.id.text1, getCSVFiles());

        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = CSVUtility.getDirectory()+"/"+(String) parent.getAdapter().getItem(position);
                mCSVUtility.importCSV(fileName, getActivity());
            }
        });

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(mListView);
        dialog.setTitle("CSV Files");
        return dialog;
    }


    private String[] getCSVFiles(){
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.contains(".csv");
            }
        };
        File file = new File(CSVUtility.getDirectory());
        return file.list(filenameFilter);
    }
}
