package com.logggly.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.logggly.databases.DatabaseContract;
import com.logggly.models.TaskModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hafiz Waleed Hussain on 3/14/2015.
 */
public class CSVUtility {

    private static String[] HEADER = {
            DatabaseContract.Tasks._ID,
            DatabaseContract.Tasks.COLUMN_TAG,
            DatabaseContract.Tasks.COLUMN_DATE_TIME,
            DatabaseContract.Tasks.COLUMN_LOCATION_NAME,
            DatabaseContract.Tasks.COLUMN_NOTES
//            DatabaseContract.Tasks.COLUMN_ADDITIONAL_FIELDS,
    };

    private static CellProcessor[] CELL_PROCESSORS = {
            new NotNull(),
            new NotNull(),
            new NotNull(),
            new NotNull(),
            new Optional()
//            new Optional()
    };

    private HashMap<String,Object> data = new HashMap<>();

    public void importCSV(String fileName, Context context){
        try {
            ICsvListReader iCsvListReader =
                    new CsvListReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
            iCsvListReader.getHeader(true);
            List<Object> objects;
            Calendar mCalendar = Calendar.getInstance();
            while ((objects = iCsvListReader.read(CELL_PROCESSORS)) != null){
                ContentValues contentValues = new ContentValues();
//                contentValues.put(DatabaseContract.Tasks._ID,Integer.parseInt((String) objects.get(0)));
                contentValues.put(DatabaseContract.Tasks.COLUMN_TAG, (String) objects.get(1));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(mCalendar.getTimeInMillis());
                contentValues.put(DatabaseContract.Tasks.COLUMN_DATE_TIME, dateFormat.format(date));
                contentValues.put(DatabaseContract.Tasks.COLUMN_LOCATION_NAME, (String) objects.get(3));
                contentValues.put(DatabaseContract.Tasks.COLUMN_NOTES, (String) objects.get(4));
                contentValues.put(DatabaseContract.Tasks.COLUMN_ADDITIONAL_FIELDS, (String) objects.get(5));
                Uri id = context.getContentResolver().insert(DatabaseContract.Tasks.CONTENT_URI, contentValues);
                Toast.makeText(context, ContentUris.parseId(id)+"",Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exportCSV(String tagName, Cursor cursor){
        File csv = getFile(tagName);
        try {
            ICsvMapWriter mapWriter = new CsvMapWriter(new FileWriter(csv.getAbsoluteFile(),true),
                    CsvPreference.STANDARD_PREFERENCE);

            List<String> header = new LinkedList<>(Arrays.asList(HEADER));
            List<CellProcessor> cellProcessor = new LinkedList<>(Arrays.asList(CELL_PROCESSORS));
            String[] headerArray = null;
            CellProcessor[] cellProcessorsArray = null;
            while(cursor.moveToNext()){
                TaskModel taskModel = new TaskModel(cursor);

                if(cursor.isFirst()){
                    setUpHeader(header, cellProcessor, taskModel);
                    headerArray = new String[header.size()];
                    header.toArray(headerArray);

                    mapWriter.writeHeader(headerArray);

                    cellProcessorsArray = new CellProcessor[cellProcessor.size()];
                    cellProcessor.toArray(cellProcessorsArray);
                }
                data.put(HEADER[0], taskModel.getId());
                data.put(HEADER[1], taskModel.getTag());
                data.put(HEADER[2],taskModel.getDate()+" "+taskModel.getTime());
                data.put(HEADER[3],taskModel.getLocation());
                data.put(HEADER[4],taskModel.getNotes());

                JSONArray jsonArray = taskModel.getAdditionalFields();
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    String fieldName =
                            jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME);
                    String fieldType =
                            jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE);
                    String fieldData =
                            jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_DATA);
//                    data.put(fieldName+"("+fieldType+")",fieldData);
                    data.put(fieldName,fieldData);
                }
                mapWriter.write(data,headerArray,cellProcessorsArray);
            }
            data.clear();
            mapWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setUpHeader(List<String> header, List<CellProcessor> cellProcessor, TaskModel taskModel) {
        JSONArray jsonArray = taskModel.getAdditionalFields();
        for (int i=0; i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String fieldName =
                    jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_NAME);
            String fieldType =
                    jsonObject.optString(DatabaseContract.AdditionalFieldsJSONManager.FIELD_TYPE);
//            header.add(fieldName+"("+fieldType+")");
            header.add(fieldName);
            cellProcessor.add(new Optional());
        }
    }

    public static String getDirectory(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"Logggly");
        return file.getAbsolutePath();
    }

    private File getFile(String tagName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"Logggly");
        file.mkdirs();

        String fileName = getFileName(tagName);
        File csv = new File(file.getAbsolutePath(),fileName);
        if(csv.exists()) {
            csv.delete();
            csv = new File(file.getAbsolutePath(), fileName);
        }
        return csv;
    }

    private String getFileName(String tagName){
        return tagName+".csv";
    }

}
