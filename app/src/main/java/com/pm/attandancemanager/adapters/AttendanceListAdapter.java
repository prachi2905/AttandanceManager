package com.pm.attandancemanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.attandancemanager.R;
import com.pm.attandancemanager.activities.TakeAttendanceActivity;
import com.pm.attandancemanager.models.Classes;
import com.pm.attandancemanager.utils.Constants;

import java.util.List;


public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ViewHolder> {

    private List<Classes> studentsList;
    private Activity mContext;

    public AttendanceListAdapter(Activity context, List<Classes> studentsList) {
        this.mContext = context;
        this.studentsList = studentsList;
    }

    public AttendanceListAdapter() {}

    public void updateAdapter(Activity context, List<Classes> studentsList) {
        this.mContext = context;
        this.studentsList = studentsList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_classes, parent, false);

        return new AttendanceListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.menuIcon.setVisibility(View.GONE);
        holder.mTvSubjectName.setText("" + studentsList.get(position).getSubjectName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TakeAttendanceActivity.class);
                intent.putExtra(Constants.SUBJECT_NAME, studentsList.get(position).getSubjectName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSubjectName, mTvTotalLectures;
        ImageView menuIcon;

        ViewHolder(View itemView) {
            super(itemView);

            mTvSubjectName = (TextView) itemView.findViewById(R.id.tv_subject_name);
            mTvTotalLectures = (TextView) itemView.findViewById(R.id.tv_total_lecture);
            menuIcon = (ImageView) itemView.findViewById(R.id.menu_bar);
        }

    }
}
