package com.pm.attandancemanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.adapters.AttendanceListAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.database.RefreshDatabaseListener;
import com.pm.attandancemanager.models.Classes;
import com.pm.attandancemanager.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class AttendanceFragment extends Fragment implements RefreshDatabaseListener {

    public Context mContext;
    private RecyclerView recyclerView;
    private List<Classes> listOfSubjects;
    private DatabaseHandler db;
    private TextView mAlertText;
    private RecyclerView.Adapter adapter;
    public AttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_attendance);
        mAlertText = (TextView) rootView.findViewById(R.id.text_alert);
        // Inflate the layout for this fragment


        new ClassesFragment(this);
        showAllRecords();
        Log.e("attendance frag","onCreateView");
        return rootView;
    }

    public void showAllRecords() {
        listOfSubjects = new ArrayList<>();
        listOfSubjects.clear();

        db = new DatabaseHandler(getActivity());
        listOfSubjects =db.getAllClassList(); //new Select().from(Classes.class).queryList();
        if (listOfSubjects.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            mAlertText.setVisibility(View.GONE);
            populateViews(listOfSubjects);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            //new AttendanceListAdapter().updateAdapter(getActivity(),listOfSubjects);
            DialogUtils.appToast(getActivity(), "No Records found");
            mAlertText.setVisibility(View.VISIBLE);
        }
    }

    // setting recycler view with list of specialization
    public void populateViews(List<Classes> listOfSubjects) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AttendanceListAdapter(getActivity(), listOfSubjects);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        this.mContext = mContext;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG","Attendance Fragment");
    }

    @Override
    public void dataRefreshed() {
        showAllRecords();
    }

}