package com.pm.attandancemanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.adapters.ViewAttendanceListAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.DateModel;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolBar;
    TextView tvPresents, tvAbsents, tvLeaves, tvLate;
    private ViewAttendanceListAdapter mAdapter;
    private DatabaseHandler db;
    private DateModel dateTime;
    private List<Students> listOfStudents = new ArrayList<>();
    private List<DateModel> listOfDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_view_attendance);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        tvPresents = (TextView) findViewById(R.id.textView2);
        tvAbsents = (TextView) findViewById(R.id.textView3);
        tvLeaves = (TextView) findViewById(R.id.textView4);
        tvLate = (TextView) findViewById(R.id.textView5);

        dateTime = (DateModel) getIntent().getSerializableExtra(Constants.DATE_TIME);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(dateTime.getDateTime());
        }

        db = new DatabaseHandler(this);
        listOfStudents.clear();

        try {
           // listOfStudents = db.getAllStudentAttendance(-1, dateTime.getDateTime());
            listOfStudents = db.getAttendance(0,dateTime.getDateTime());
            listOfDates = db.getAllDates(dateTime.getDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listOfStudents.size() > 0) {
            tvPresents.setText("Total Presents " + listOfDates.get(0).getTotalPresents());
            tvAbsents.setText("Total Absents " + listOfDates.get(0).getTotalAbsents());
            tvLeaves.setText("Total Leaves " + listOfDates.get(0).getTotalLeaves());
            tvLate.setText("Total Late " + listOfDates.get(0).getTotalLate());
            populateViews(listOfStudents);
        }
    }

    public void populateViews(List<Students> listOfStudents) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter = new ViewAttendanceListAdapter(this, listOfStudents);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
