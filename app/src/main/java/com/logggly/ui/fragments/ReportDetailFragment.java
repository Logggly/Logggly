package com.logggly.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logggly.R;
import com.logggly.databases.DatabaseContract;
import com.logggly.managers.AdditionalFieldsManager;
import com.logggly.managers.DateTimeManager;
import com.logggly.models.TaskModel;
import com.logggly.ui.customviews.CustomViewCreator;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 1/31/2015.
 */
public class ReportDetailFragment extends Fragment implements DateFragment.Callback,
TimeFragment.Callback{

    private TaskModel taskModel;
    private TextView dateTextView;
    private TextView timeTextView;
    private EditText notesEditText;
    private TableLayout mParentLayoutForAdditionalFields;
    private AdditionalFieldsManager mAdditionalFieldsManager;

    public static ReportDetailFragment newInstance(TaskModel taskModel){
        ReportDetailFragment reportDetailFragment = new ReportDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", taskModel);
        reportDetailFragment.setArguments(bundle);
        return reportDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskModel = (TaskModel) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragent_report_detail, null);
        mParentLayoutForAdditionalFields = (TableLayout) view.findViewById(R.id.FragmentReportDetail_parent_tablelayout);
        mAdditionalFieldsManager = new AdditionalFieldsManager(getActivity(),mParentLayoutForAdditionalFields,
                getActivity().getSupportFragmentManager());
        DateTimeManager dateTimeManager = new DateTimeManager(taskModel.getCalendar(),getActivity().getSupportFragmentManager(),this,this);
        TextView idTextView = (TextView) view.findViewById(R.id.FragmentReportDetail_id_textview);
        TextView tagTextView= (TextView) view.findViewById(R.id.FragmentReportDetail_tag_textview);
        dateTextView= (TextView) view.findViewById(R.id.FragmentReportDetail_set_date_textview);
        dateTextView.setOnClickListener(dateTimeManager.getSetDateButtonOnClickListener());
        timeTextView= (TextView) view.findViewById(R.id.FragmentReportDetail_set_time_textview);
        timeTextView.setOnClickListener(dateTimeManager.getSetTimeButtonOnClickListener());
        timeTextView.setOnClickListener(dateTimeManager.getSetTimeButtonOnClickListener());
        TextView addressTextView= (TextView) view.findViewById(R.id.FragmentReportDetail_address_textview);
        notesEditText = (EditText) view.findViewById(R.id.FragmentReportDetail_notes_edittext);

        final Button deleteButton = (Button) view.findViewById(R.id.FragmentReportDetail_delete_button);
        final Button updateButton = (Button) view.findViewById(R.id.FragmentReportDetail_update_button);

        idTextView.setText(taskModel.getId());
        tagTextView.setText(taskModel.getTag());
        dateTextView.setText(taskModel.getDate());
        timeTextView.setText(taskModel.getTime());
        addressTextView.setText(taskModel.getLocation());
        notesEditText.setText(taskModel.getNotes());

        if(taskModel.getAdditionalFields() != null){
            CustomViewCreator.createViewForJSONArray(getActivity(),
                    mParentLayoutForAdditionalFields,
                    taskModel.getAdditionalFields(),
                    mParentLayoutForAdditionalFields.getChildCount());
            mAdditionalFieldsManager.init(taskModel.getAdditionalFields());
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(getString(R.string.event_detail));
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog dialog = builder.create();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getActivity()
                        .getContentResolver()
                        .delete(DatabaseContract.Tasks.buildUriAgainstDeleteId(taskModel.getId())
                                ,null
                                ,null);
                dialog.dismiss();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskModel.setNotes(notesEditText.getText().toString());
                taskModel.setAdditionalFields(mAdditionalFieldsManager.getJSONArrayWithDataForSaveAsString());
                int id = getActivity()
                        .getContentResolver()
                        .update(DatabaseContract.Tasks.CONTENT_URI,taskModel.getContentValues(),null,null);

                if( id == 1){
                    Toast.makeText(getActivity(),"Event updated successfully",Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });
        return view;
    }


    @Override
    public void updateDateInstance(Calendar mCalendar) {
        taskModel.setCalendar(mCalendar);
        dateTextView.setText(taskModel.getDate());
    }

    @Override
    public void updateTimeInstance(Calendar mCalendar) {
        taskModel.setCalendar(mCalendar);
        timeTextView.setText(taskModel.getTime());
    }
}
