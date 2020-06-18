package com.pm.attandancemanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pm.attandancemanager.R;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.Constants;
import com.pm.attandancemanager.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentReportActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private PieChart pieChart;
    private Students student;
    List<Students> list = new ArrayList<>();
    private ArrayList<PieEntry> entries = new ArrayList<>();
    public final int[] PIE_COLORS = {Color.rgb(0, 128, 0), Color.rgb(255, 0, 0), Color.rgb(128, 128, 0), Color.rgb(128, 0, 128)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report);
        student = (Students) getIntent().getSerializableExtra(Constants.STUDENT_NAME);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(student.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pieChart = (PieChart) findViewById(R.id.pie_chart);
        PieDataSet dataset = new PieDataSet(getChartValues(), "");
        if (list.size() > 0) {

            PieData data = new PieData(dataset);
            dataset.setColors(PIE_COLORS);
            // data.setValueTextColor(Color.RED);
        /*dataset.setValueLinePart1OffsetPercentage(90.f);
        dataset.setValueLinePart1Length(1f);
        dataset.setValueLinePart2Length(.2f);*/
            dataset.setValueTextColor(Color.WHITE);
            dataset.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
            //dataset.setColors(ColorTemplate.MATERIAL_COLORS);
            dataset.setDrawValues(true);
            dataset.setHighlightEnabled(true);
            dataset.setValueTextSize(14f);
            dataset.setSelectionShift(5f);  // selected portion will zoom out according to selected shift value
            pieChart.setOnChartValueSelectedListener(this);
            pieChart.setRotationEnabled(false);
            pieChart.setRotationAngle(45f);
            pieChart.setCenterText("Report");
            pieChart.setCenterTextColor(Color.parseColor("#162750"));
            pieChart.setDrawCenterText(true);
            pieChart.setEnabled(true);
            pieChart.setHighlightPerTapEnabled(false);
            pieChart.setSelected(true);
            pieChart.setFocusable(true);
            pieChart.setUsePercentValues(false);
            pieChart.setDrawEntryLabels(false);
            //pieChart.setBackgroundColor(Color.TRANSPARENT);
            //pieChart.setDrawSliceText(true);// index values
            Description description = new Description();
            description.setText("");
            description.setEnabled(false);
            pieChart.setDescription(description);
            pieChart.setHoleColor(Color.WHITE); // center hole color
            pieChart.setData(data);// set the data and list of labels into chart
            pieChart.notifyDataSetChanged();
        } else {


            DialogUtils.appToast(this, "No Records found");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    // graph data for current day
    //Entry class is the base class for all chart data types in MPAndroidChart library
    private ArrayList<PieEntry> getChartValues() {

        try {
            DatabaseHandler db = new DatabaseHandler(this);
            list = db.getAttendance(student.getId(),"NA");
            if (list.size() > 0) {
                if (list.get(0).getTotalPresents() == 0 && list.get(0).getTotalAbsents() == 0 && list.get(0).getTotalLeaves() == 0 && list.get(0).getTotalLate() == 0) {
                    findViewById(R.id.tv_colors).setVisibility(View.GONE);
                    findViewById(R.id.pie_chart).setVisibility(View.GONE);
                    findViewById(R.id.tv_alert).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.tv_colors).setVisibility(View.VISIBLE);
                    findViewById(R.id.pie_chart).setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_alert).setVisibility(View.GONE);
                    float presents = (float) list.get(0).getTotalPresents();
                    float absents = (float) list.get(0).getTotalAbsents();
                    float leaves = (float) list.get(0).getTotalLeaves();
                    float late = (float) list.get(0).getTotalLate();
                    entries.add(new PieEntry(presents, 0));
                    entries.add(new PieEntry(absents, 1));
                    entries.add(new PieEntry(leaves, 2));
                    entries.add(new PieEntry(late, 3));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entries;
    }

    // this method will call if user will click any value on chart
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(StudentReportActivity.this, "Selected value : " + e.getData() + " at X(" + e.getX() + ")", Toast.LENGTH_SHORT).show();
    }

    // this method will call if user will click anywhere instead of chart value on chart
    @Override
    public void onNothingSelected() {
        Toast.makeText(StudentReportActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
    }


}
