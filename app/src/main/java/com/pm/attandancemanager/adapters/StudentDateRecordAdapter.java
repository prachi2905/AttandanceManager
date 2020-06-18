package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.ViewAttendanceActivity;
import com.pm.attandancemanager.database.DatabaseHandler;
import com.pm.attandancemanager.models.DateModel;
import com.pm.attandancemanager.models.Students;
import com.pm.attandancemanager.utils.AndroidVersionUtility;
import com.pm.attandancemanager.utils.Constants;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ghanshyamnayma on 28/10/17.
 */

public class StudentDateRecordAdapter extends RecyclerView.Adapter<StudentDateRecordAdapter.ViewHolder> {

    private List<DateModel> datesList;
    private Activity mContext;

    public StudentDateRecordAdapter(Activity context, List<DateModel> datesList) {
        this.mContext = context;
        this.datesList = datesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_attendance, parent, false);
        return new StudentDateRecordAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mTvSubjectName.setText("" + datesList.get(position).getDateTime());
        try {
            DatabaseHandler db = new DatabaseHandler(mContext);
            List<DateModel> dateModel = db.getAllDates(datesList.get(position).getDateTime());
            List<Students> attendance = db.getAttendance(-1, datesList.get(position).getDateTime());

            if (dateModel.get(0).getTotalLectures() != 0) {
                float num = (float) (100 * (float) dateModel.get(0).getTotalPresents() / (float) attendance.size());
                holder.mTvTotalLectures.setText("" + new DecimalFormat("##.#").format(num) + " %");
                if (num < 50.0)
                    holder.mTvTotalLectures.setTextColor(AndroidVersionUtility.getColor(mContext,R.color.color_absent));
                else
                    holder.mTvTotalLectures.setTextColor(AndroidVersionUtility.getColor(mContext,R.color.color_present));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewAttendanceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DATE_TIME, datesList.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSubjectName, mTvTotalLectures;

        ViewHolder(View itemView) {
            super(itemView);

            mTvSubjectName = (TextView) itemView.findViewById(R.id.tv_subject_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_lecture);
        }

    }

}
