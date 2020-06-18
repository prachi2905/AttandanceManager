package com.pm.attandancemanager.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.adapters.TakeAttendanceListAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TakeAttendanceActivity extends AppCompatActivity {

    private static final String TAG = TakeAttendanceActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private Button submit_button;
    private DatabaseHandler db;
    private String subName;
    private TextView mTextAlert, mDateTime, mTvDate;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private TakeAttendanceListAdapter adapter;
    private List<Students> listOfStudent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        String subjectName = getIntent().getStringExtra(Constants.SUBJECT_NAME);
        mTextAlert = (TextView) findViewById(R.id.text_alert);
        mDateTime = (TextView) findViewById(R.id.date_time);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subjectName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        subName = getIntent().getStringExtra(Constants.SUBJECT_NAME);
        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_take);
        submit_button = (Button) findViewById(R.id.btn_submit);
        mTvDate = (TextView) findViewById(R.id.date_time);
        // Inflate the layout for this fragment

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        String currentDate = dateFormat.format(date);
        mDateTime.setText("Time: " + dateFormat.format(date));
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        mDateTime.setText(day + "-" + month + "-" + year);


        mDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });
        listOfStudent.clear();
        listOfStudent = db.getAllStudentList(subName);
        if (listOfStudent.size() > 0) {
            mTextAlert.setVisibility(View.GONE);
            mTvDate.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.VISIBLE);
        } else {
            mTvDate.setVisibility(View.GONE);
            mTextAlert.setVisibility(View.VISIBLE);
            mTextAlert.setText("Add Student Name");
            submit_button.setVisibility(View.GONE);
        }
        Log.e(TAG, "Students " + listOfStudent);
        for (int n = 0; n < listOfStudent.size(); n++) {
            Students model = listOfStudent.get(n);
            model.setStatus("Present");
            model.setDateTime(currentDate);
            listOfStudent.set(n, model);
        }
        populateViews(listOfStudent);
    }

    public void populateViews(List<Students> listOfSubjects) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new TakeAttendanceListAdapter(this, listOfSubjects, submit_button);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void DateDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDateTime.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                String selectedDate = dateFormat.format(calendar.getTime());
                Log.e(TAG, "selected date " + selectedDate);
                for (int x = 0; x < listOfStudent.size(); x++) {
                    Students model = listOfStudent.get(x);
                    model.setDateTime(selectedDate);
                    listOfStudent.set(x, model);
                }
                adapter.setAttendanceDate(selectedDate, listOfStudent);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, R.style.DialogTheme, listener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(new Date().getTime());
        dpDialog.show();

    }
}
