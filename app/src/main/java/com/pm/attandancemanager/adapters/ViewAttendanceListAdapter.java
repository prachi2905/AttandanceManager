package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.StudentReportActivity;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.AndroidVersionUtility;
import com.pm.attandancemanager.utils.Constants;

import java.util.List;


/**
 * Created by ghanshyamnayma on 12/07/17 on 23/5/17.
 */

public class ViewAttendanceListAdapter extends RecyclerView.Adapter<ViewAttendanceListAdapter.ViewHolder> {

    private List<Students> studentsList;
    private Activity mContext;

    public ViewAttendanceListAdapter(Activity context, List<Students> studentsList) {
        this.mContext = context;
        this.studentsList = studentsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_attendance, parent, false);

        return new ViewAttendanceListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mTvStudentName.setText("" + studentsList.get(position).getName());
        holder.mTvTotalLectures.setText("" + studentsList.get(position).getTotalPresents() + "/" + studentsList.get(position).getTotalLectures());
        holder.mTvStatus.setText("" + studentsList.get(position).getStatus());
        if (studentsList.get(position).getStatus().equalsIgnoreCase("Present"))
            holder.mTvStatus.setBackground(AndroidVersionUtility.getDrawable(mContext, R.drawable.round_bg_green));
        else if (studentsList.get(position).getStatus().equalsIgnoreCase("Absent"))
            holder.mTvStatus.setBackground(AndroidVersionUtility.getDrawable(mContext, R.drawable.round_bg_red));
        else if (studentsList.get(position).getStatus().equalsIgnoreCase("Leave"))
            holder.mTvStatus.setBackground(AndroidVersionUtility.getDrawable(mContext, R.drawable.round_bg_yellow));
        else
            holder.mTvStatus.setBackground(AndroidVersionUtility.getDrawable(mContext, R.drawable.round_bg_pink));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STUDENT_NAME, studentsList.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RadioGroup mRadioGroup;
        TextView mTvStudentName, mTvStatus, mTvTotalLectures;

        ViewHolder(View itemView) {
            super(itemView);

            mTvStudentName = (TextView) itemView.findViewById(R.id.tv_student_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_presents);
            mTvStatus = (TextView) itemView.findViewById(R.id.tv_status);


        }

    }
}
