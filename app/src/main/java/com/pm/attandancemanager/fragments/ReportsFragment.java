package com.pm.attandancemanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.AddStudentsActivity;
import com.pm.attandancemanager.adapters.StudentDateRecordAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.database.RefreshDatabaseListener;
import com.pm.attandancemanager.models.DateModel;

import java.util.ArrayList;
import java.util.List;


public class ReportsFragment extends Fragment implements RefreshDatabaseListener{

    public Context mContext;
    private RecyclerView recyclerView;
    private List<DateModel> listOfDates;
    private StudentDateRecordAdapter mAdapter;
    private FloatingActionButton fab;
    private DatabaseHandler db;
    private TextView mAlertText;
    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_reports);
        mAlertText = (TextView) rootView.findViewById(R.id.text_alert);
        new AddStudentsActivity(this);
        showAllRecords();
        return rootView;
    }

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        this.mContext = mContext;
    }

    public void populateViews(List<DateModel> listOfDates) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter = new StudentDateRecordAdapter(getActivity(), listOfDates);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void dataRefreshed() {
        Log.e("TAG","methodCalled");
        showAllRecords();

    }
    public void showAllRecords()
    {
        listOfDates = new ArrayList<>();
        listOfDates.clear();

        db = new DatabaseHandler(getActivity());
        listOfDates = db.getAllDates("ALL");
        if (listOfDates.size() > 0)
        {
            mAlertText.setVisibility(View.GONE);
            populateViews(listOfDates);
        }
        else
        {
            mAlertText.setVisibility(View.VISIBLE);
        }

    }
}