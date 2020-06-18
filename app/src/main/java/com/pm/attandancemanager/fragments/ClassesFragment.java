package com.pm.attandancemanager.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.adapters.ClassesListAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.database.RefreshDatabaseListener;
import com.pm.attandancemanager.models.Classes;
import com.pm.attandancemanager.utils.AndroidVersionUtility;
import com.pm.attandancemanager.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;


public class ClassesFragment extends Fragment {

    public Context mContext;
    private RecyclerView recyclerView;
    private List<Classes> listOfSubjects;
    private ClassesListAdapter mAdapter;
    private FloatingActionButton fab;
    private DatabaseHandler db;
    public static RefreshDatabaseListener mListener;
    private TextView mTextAlert;

    @SuppressLint("ValidFragment")
    public ClassesFragment(RefreshDatabaseListener mListener) {
        this.mListener = mListener;
    }

    public ClassesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("classes frag", "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_classes, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_classes);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_classes);
        mTextAlert = (TextView) rootView.findViewById(R.id.text_alert);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSubjectDialog();
            }
        });
        listOfSubjects = new ArrayList<>();
        listOfSubjects.clear();

        // mAdapter = new ClassesListAdapter(getActivity(), listOfSubjects, mTextAlert, mListener);
        db = new DatabaseHandler(getActivity());
        listOfSubjects = db.getAllClassList(); //new Select().from(Classes.class).queryList();
        if (listOfSubjects.size() > 0) {
            mTextAlert.setVisibility(View.GONE);
            populateViews(listOfSubjects);
        } else {
            mTextAlert.setVisibility(View.VISIBLE);
            //DialogUtils.appToast(getActivity(), "No Records found");

        }
        return rootView;

    }


    public void populateViews(List<Classes> listOfSubjects) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter = new ClassesListAdapter(getActivity(), listOfSubjects, mTextAlert, mListener);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        this.mContext = mContext;
    }

    public void addNewSubjectDialog() {
        final Dialog dialog;
        if (AndroidVersionUtility.isLollipopOrGreater()) {
            dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(mContext);
        }

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.custom_dialog_add_students);
        final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.add_new_class));
        dialog.setCancelable(true);

        final EditText edtName = (EditText) dialog.findViewById(R.id.et_dialog_name);
        edtName.setHint("Enter subject/class name");
        final EditText edtRollNumber = (EditText) dialog.findViewById(R.id.et_dialog_roll_number);
        edtRollNumber.setVisibility(View.GONE);
        final EditText edtSection = (EditText) dialog.findViewById(R.id.et_dialog_section);
        edtSection.setVisibility(View.VISIBLE);

        final TextView txtSave = (TextView) dialog.findViewById(R.id.tv_save);
        final TextView txtCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = false;
                String name = edtName.getText().toString();
                if (!name.isEmpty()) {
                    edtName.setError(null);
                    String section = edtSection.getText().toString();
                    if (!section.isEmpty())
                        name = name + "-" + section;

                    List<Classes> subjects = new ArrayList<Classes>();
                    subjects.clear();
                    subjects = db.getAllClassList();

                    Classes classes = new Classes();
                    name = name.toUpperCase();
                    classes.setSubjectName(name);
                    if (subjects.size() == 0) {
                        db.addClass(classes);
                        mListener.dataRefreshed();
                        listOfSubjects.add(classes);
                        if (listOfSubjects.size() == 1) {
                            mTextAlert.setVisibility(View.GONE);
                            populateViews(listOfSubjects);
                        } else {
                            mAdapter.notifyDataSetChanged();
                            if (listOfSubjects.size() == 0)
                                mTextAlert.setVisibility(View.VISIBLE);
                        }
                    } else {
                        for (int x = 0; x < subjects.size(); x++) {
                            if (subjects.get(x).getSubjectName().equalsIgnoreCase(name)) {
                                isExist = true;
                                break;
                            } else {
                                isExist = false;
                            }
                        }

                        if (!isExist) {
                            db.addClass(classes);
                            mListener.dataRefreshed();
                            listOfSubjects.add(classes);
                            if (listOfSubjects.size() == 1) {
                                mTextAlert.setVisibility(View.GONE);
                                populateViews(listOfSubjects);
                            } else {
                                mAdapter.notifyDataSetChanged();
                                if (listOfSubjects.size() == 0)
                                    mTextAlert.setVisibility(View.VISIBLE);
                            }
                        } else {
                            DialogUtils.appToast(mContext, "Class/Subject Already exist");
                        }

                    }
                    dialog.cancel();

                } else {
                    edtName.setError("Required field");
                }

            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG","CLASSES FREGMENT");
    }
}