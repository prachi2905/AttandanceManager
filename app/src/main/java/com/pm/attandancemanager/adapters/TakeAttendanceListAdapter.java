package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.Students;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by ghanshyamnayma on 12/07/17 on 23/5/17.
 */

public class TakeAttendanceListAdapter extends RecyclerView.Adapter<TakeAttendanceListAdapter.ViewHolder> {

    private DatabaseHandler db;
    private List<Students> studentsList;
    private Activity mContext;
    private Button submit_button;
    private String dateTime;
    int presents = 0, absents = 0, late = 0, leaves = 0;

    public TakeAttendanceListAdapter(Activity context, List<Students> studentsList, Button submit_button) {
        this.mContext = context;
        this.studentsList = studentsList;
        this.submit_button = submit_button;

        db = new DatabaseHandler(mContext);
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        dateTime = dateFormat.format(date);
    }

    public void setAttendanceDate(String dateTime, List<Students> studentsList) {
        this.dateTime = dateTime;
        this.studentsList = studentsList;
        Log.e("dateTime ", "" + dateTime);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student_attendance, parent, false);

        return new TakeAttendanceListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mTvStudentName.setText("" + studentsList.get(position).getName());
        holder.mTvRollNumber.setText("Roll no. " + studentsList.get(position).getRollNumber());
        holder.mTvTotalLectures.setText("" + studentsList.get(position).getTotalPresents() + "/" + studentsList.get(position).getTotalLectures());

        holder.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int childCount = radioGroup.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(x);
                    if (btn.isChecked()) {
                        Students model = studentsList.get(position);
                        model.setStatus(btn.getText().toString());
                        studentsList.set(position, model);
                    }
                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAlreadyTaken = false;
                boolean isFirstTime = true;
                presents = 0;
                absents = 0;
                leaves = 0;
                late = 0;
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                DateFormat sf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                Date date = new Date(Calendar.getInstance().getTimeInMillis());
                if (dateTime.contains(dateFormat.format(date)))
                    dateTime = sf.format(date);

               /* List<DateModel> listDate = db.getAllDates("All");
                if (listDate.size() > 0)
                {
                    isFirstTime = false;
                    for (int r = 0; r < listDate.size(); r++) {
                        if (dateTime.equalsIgnoreCase(listDate.get(r).getDateTime())) {
                            isAlreadyTaken = true;
                            break;
                        }
                    }
                }*/
                    for (int x = 0; x < studentsList.size(); x++) {
                        studentsList.get(x).setDateTime(dateTime);
                        String status = studentsList.get(x).getStatus();
                        if (status.equalsIgnoreCase("Present"))
                            presents++;
                        if (status.equalsIgnoreCase("Absent"))
                            absents++;
                        if (status.equalsIgnoreCase("Late"))
                            late++;
                        if (status.equalsIgnoreCase("Leave"))
                            leaves++;
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setTitle(mContext.getString(R.string.app_name));
                    builder1.setMessage("Total Strength " + studentsList.size() + ",\nPresents " + presents + ", \nAbsent " + absents + ", \nLate " + late + ", \nLeaves " + leaves);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    for (int x = 0; x < studentsList.size(); x++) {
                                        String status = studentsList.get(x).getStatus();
                                        if (status.equalsIgnoreCase("Present"))
                                            studentsList.get(x).setTotalPresents(studentsList.get(x).getTotalPresents() + 1);
                                        else if (status.equalsIgnoreCase("Absent"))
                                            studentsList.get(x).setTotalAbsents(studentsList.get(x).getTotalAbsents() + 1);
                                        else if (status.equalsIgnoreCase("Late"))
                                            studentsList.get(x).setTotalLate(studentsList.get(x).getTotalLate() + 1);
                                        else if (status.equalsIgnoreCase("Leave"))
                                            studentsList.get(x).setTotalLeaves(studentsList.get(x).getTotalLeaves() + 1);

                                        studentsList.get(x).setTotalLectures(studentsList.get(x).getTotalLectures() + 1);
                                        db.addAttendance(studentsList.get(x));
                                    }
                                    try {
                                        int totalLectures = studentsList.get(0).getTotalLectures();
                                        db.addDateTime(totalLectures, presents, absents, leaves, late, dateTime);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.cancel();
                                    Toast.makeText(mContext, "Attendance completed successfully", Toast.LENGTH_SHORT).show();
                                    mContext.finish();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    //Toast.makeText(mContext, "SuccesFully applied", Toast.LENGTH_SHORT).show();
                    Log.e("Class Status", "Total Students " + studentsList.size() + ", totalPresents " + presents + ", totalAbsent " + absents + ", totalLate " + late + ", totalLeaves " + leaves);
            }
        });
    }


    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RadioGroup mRadioGroup;
        TextView mTvStudentName, mTvRollNumber, mTvTotalLectures;

        ViewHolder(View itemView) {
            super(itemView);

            mTvStudentName = (TextView) itemView.findViewById(R.id.tv_student_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_presents);
            mTvRollNumber = (TextView) itemView.findViewById(R.id.tv_roll_number);
            mRadioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
        }

    }
}
