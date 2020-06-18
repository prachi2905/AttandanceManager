package com.pm.attandancemanager.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.adapters.StudentsListAdapter;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.database.RefreshDatabaseListener;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.AndroidVersionUtility;
import com.pm.attandancemanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class AddStudentsActivity extends AppCompatActivity {

    private static final String TAG = AddStudentsActivity.class.getSimpleName();
    private List<Students> listOfStudents = new ArrayList<>();
    private RecyclerView recyclerView;
    private StudentsListAdapter mAdapter;
    private DatabaseHandler db;
    private String subjectName;
    private static RefreshDatabaseListener mListener;
    private TextView mTextAlert;
    boolean isExist = false;


    public AddStudentsActivity(RefreshDatabaseListener mListener) {
        this.mListener = mListener;
    }

    public AddStudentsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);

        subjectName = getIntent().getStringExtra(Constants.SUBJECT_NAME);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_class_students);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTextAlert = (TextView) findViewById(R.id.text_alert);

        db = new DatabaseHandler(this);
        listOfStudents.clear();
        try {
            listOfStudents = db.getAllStudentList(subjectName); //new Select().from(Classes.class).queryList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //listOfStudents = new Select().from(Students.class).queryList();
        if (listOfStudents.size() > 0)
            populateViews(listOfStudents);
        else {
            mTextAlert.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_students);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentDialog();
            }
        });
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(subjectName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /*
*  add new students and enter details like name , roll number and section of the class if any
* */


    public void addStudentDialog() {
        final Dialog dialog;
        if (AndroidVersionUtility.isLollipopOrGreater()) {
            dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(this);
        }

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.custom_dialog_add_students);
        dialog.setCancelable(true);

        final EditText edtName = (EditText) dialog.findViewById(R.id.et_dialog_name);
        final EditText edtRollNumber = (EditText) dialog.findViewById(R.id.et_dialog_roll_number);
        edtRollNumber.setVisibility(View.VISIBLE);
        final EditText edtSection = (EditText) dialog.findViewById(R.id.et_dialog_section);
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
                mTextAlert.setVisibility(View.GONE);
                String name = edtName.getText().toString();
                String rollNumber = edtRollNumber.getText().toString();
                if (name.isEmpty())
                    edtName.setError("required field");
                    if (rollNumber.isEmpty())
                        edtRollNumber.setError("required field");

                    if (!name.isEmpty() && !rollNumber.isEmpty()) {
                        for (int x = 0; x < listOfStudents.size(); x++) {
                            if (listOfStudents.get(x).getRollNumber().equalsIgnoreCase(rollNumber)) {
                                isExist = true;
                                break;
                            } else {
                                isExist = false;
                            }
                        }
                        if (!isExist) {
                            Log.e(TAG, "before added " + db.getAllStudentList(subjectName));

                            Students students = new Students();
                            students.setName(name);
                            students.setRollNumber(rollNumber);
                            students.setClassName(subjectName);
                            students.setTotalLectures(0);
                            db.addStudentRecord(students);
                            listOfStudents.add(students);
                            populateViews(listOfStudents);

                            Log.e(TAG, "after added " + db.getAllStudentList(subjectName));
                            //mListener.dataRefreshed();
                        } else {
                            makeText(AddStudentsActivity.this, "Roll number already exist", Toast.LENGTH_SHORT).show();
                        }

                        dialog.cancel();
                    }

                }
        });
        dialog.show();
    }

    public void populateViews(List<Students> listOfStudents) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter = new StudentsListAdapter(this, listOfStudents,true);
        recyclerView.setAdapter(mAdapter);

    }

}
